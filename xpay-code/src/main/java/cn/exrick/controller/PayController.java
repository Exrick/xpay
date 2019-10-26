package cn.exrick.controller;

import cn.exrick.bean.Pay;
import cn.exrick.bean.dto.DataTablesResult;
import cn.exrick.bean.dto.PageVo;
import cn.exrick.bean.dto.Result;
import cn.exrick.common.utils.*;
import cn.exrick.dao.PayDao;
import cn.exrick.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Exrickx
 */
@Controller
@Api(tags = "开放接口",description = "支付捐赠管理")
@CacheConfig(cacheNames = "xpay")
public class PayController {

    private static final Logger log= LoggerFactory.getLogger(PayController.class);

    @Autowired
    private PayService payService;

    @Autowired
    private PayDao payDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${ip.expire}")
    private Long IP_EXPIRE;

    @Value("${my.token}")
    private String MY_TOKEN;

    @Value("${email.sender}")
    private String EMAIL_SENDER;

    @Value("${email.receiver}")
    private String EMAIL_RECEIVER;

    @Value("${token.admin.expire}")
    private Long ADMIN_EXPIRE;

    @Value("${token.fake.expire}")
    private Long FAKE_EXPIRE;

    @Value("${fake.pre}")
    private String FAKE_PRE;

    @Value("${server.url}")
    private String SERVER_URL;

    @Value("${qrnum}")
    private Integer QRNUM;

    private static final String CLOSE_KEY="XPAY_CLOSE_KEY";

    @RequestMapping(value = "/thanks/list",method = RequestMethod.GET)
    @ResponseBody
    public DataTablesResult getThanksList(int draw, int start, int length, @RequestParam("search[value]") String search,
                                          @RequestParam("order[0][column]") int orderCol, @RequestParam("order[0][dir]") String orderDir){

        DataTablesResult result = new DataTablesResult();
        //获取客户端需要排序的列
        String[] cols = {"nickName","payType", "money", "info", "state", "createTime"};
        String orderColumn = cols[orderCol];
        if(orderColumn == null) {
            orderColumn = "createTime";
        }
        //获取排序方式 默认为desc(asc)
        if(orderDir == null) {
            orderDir = "desc";
        }
        if(length>50){
            result.setDraw(draw);
            result.setSuccess(false);
            result.setError("看我那么多数据干嘛");
            return result;
        }
        PageVo pageVo = new PageVo();
        int page = start/length + 1;
        pageVo.setPageNumber(page);
        pageVo.setPageSize(length);
        pageVo.setSort(orderColumn);
        pageVo.setOrder(orderDir);
        Pageable pageable = PageUtil.initPage(pageVo);


        Page<Pay> payPage;
        try {
            payPage = payService.getPayListByPage(1,search,pageable);
        }catch (Exception e){
            log.error(e.toString());
            result.setSuccess(false);
            result.setDraw(draw);
            result.setError("获取捐赠列表失败");
            return result;
        }
        for(Pay p : payPage.getContent()){
            p.setId("");
            p.setEmail(null);
            p.setTestEmail(null);
            p.setMobile(null);
            p.setCustom(null);
            p.setPayNum(null);
            p.setDevice(null);
        }
        result.setRecordsFiltered(Math.toIntExact(payPage.getTotalElements()));
        result.setRecordsTotal(Math.toIntExact(payPage.getTotalElements()));
        result.setData(payPage.getContent());
        result.setDraw(draw);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/pay/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取未支付数据")
    @ResponseBody
    public DataTablesResult getPayList(){

        DataTablesResult result=new DataTablesResult();
        List<Pay> list=new ArrayList<>();
        try {
            list=payService.getPayList(2);
            list.addAll(payService.getPayList(3));
        }catch (Exception e){
            result.setSuccess(false);
            result.setError("获取未支付数据失败");
            return result;
        }
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/pay/check/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取支付审核列表")
    @ResponseBody
    public DataTablesResult getCheckList(){

        DataTablesResult result=new DataTablesResult();
        List<Pay> list = new ArrayList<>();
        try {
            list=payService.getPayList(0);
            list.addAll(payService.getPayList(4));
        }catch (Exception e){
            result.setSuccess(false);
            result.setError("获取支付审核列表失败");
            return result;
        }
        result.setData(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/pay/state/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取支付状态")
    @ResponseBody
    @Cacheable(key = "#id")
    public Result<Object> getPayState(@PathVariable String id){


        Pay pay=null;
        try {
            pay=payService.getPay(getPayId(id));
        }catch (Exception e){
            return new ResultUtil<Object>().setErrorMsg("获取支付数据失败");
        }
        return new ResultUtil<Object>().setData(pay.getState());
    }

    @RequestMapping(value = "/pay/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取支付数据")
    @ResponseBody
    public Result<Object> getPayData(@PathVariable String id,
                                     @RequestParam String token){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        Pay pay=null;
        try {
            pay=payService.getPay(getPayId(id));
        }catch (Exception e){
            return new ResultUtil<Object>().setErrorMsg("获取支付数据失败");
        }
        return new ResultUtil<Object>().setData(pay);
    }

    @RequestMapping(value = "/pay/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加支付订单")
    @ResponseBody
    public Result<Object> addPay(@ModelAttribute Pay pay, HttpServletRequest request){

        if(StringUtils.isBlank(pay.getNickName())||StringUtils.isBlank(String.valueOf(pay.getMoney()))
                ||pay.getMoney().compareTo(new BigDecimal("5.00"))==-1
                ||StringUtils.isBlank(pay.getEmail())||!EmailUtils.checkEmail(pay.getEmail())){
            return new ResultUtil<Object>().setErrorMsg("请填写完整信息和正确的通知邮箱和金额");
        }
        if(pay.getCustom()==null){
            return new ResultUtil<Object>().setErrorMsg("缺少自定义金额参数");
        }
        // 判断是否开启支付
        String isOpen = redisTemplate.opsForValue().get(CLOSE_KEY);
        Long expireOpen = redisTemplate.getExpire(CLOSE_KEY, TimeUnit.HOURS);
        if(StringUtils.isNotBlank(isOpen)){
            String msg = "";
            if(expireOpen<0){
                msg = "系统暂时关闭，如有疑问请进行反馈";
            }else{
                msg = "暂停支付测试，剩余"+expireOpen+"小时后开放，早点休息吧";
            }
            return new ResultUtil<Object>().setErrorMsg(msg);
        }
        //防炸库验证
        String ip= IpInfoUtils.getIpAddr(request);
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip="127.0.0.1";
        }
        String temp=redisTemplate.opsForValue().get(ip);
        Long expire = redisTemplate.getExpire(ip,TimeUnit.SECONDS);
        if(StringUtils.isNotBlank(temp)){
            return new ResultUtil<Object>().setErrorMsg("您提交的太频繁啦，作者的学生服务器要炸啦！请"+expire+"秒后再试");
        }

        try {
            if(pay.getCustom()!=null&&pay.getCustom()&&!"UnionPay".equals(pay.getPayType())&&!"Diandan".equals(pay.getPayType())){
                //自定义金额生成四位数随机标识
                pay.setPayNum(StringUtils.getRandomNum());
            }else{
                // 从redis中取出num
                String key = "XPAY_NUM_"+pay.getPayType();
                String value=redisTemplate.opsForValue().get(key);
                // 初始化
                if(StringUtils.isBlank(value)){
                    redisTemplate.opsForValue().set(key,"0");
                }
                // 取出num
                String num  = String.valueOf(Integer.parseInt(redisTemplate.opsForValue().get(key))+1);
                if(QRNUM.equals(Integer.valueOf(num))){
                    redisTemplate.opsForValue().set(key, "0");
                }else{
                    // 更新记录num
                    redisTemplate.opsForValue().set(key, String.valueOf(num));
                }
                pay.setPayNum(num);
            }
            payService.addPay(pay);
            pay.setTime(StringUtils.getTimeStamp(new Date()));
        }catch (Exception e){
            log.error(e.toString());
            return new ResultUtil<Object>().setErrorMsg("添加捐赠支付订单失败");
        }
        //记录缓存
        redisTemplate.opsForValue().set(ip,"added",IP_EXPIRE, TimeUnit.MINUTES);

        //给管理员发送审核邮件
        String tokenAdmin= UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(pay.getId(),tokenAdmin,ADMIN_EXPIRE,TimeUnit.DAYS);
        pay=getAdminUrl(pay,pay.getId(),tokenAdmin,MY_TOKEN);
        emailUtils.sendTemplateMail(EMAIL_SENDER,EMAIL_RECEIVER,"【XPay个人收款支付系统】待审核处理","email-admin",pay);

        //给假管理员发送审核邮件
        if(StringUtils.isNotBlank(pay.getTestEmail())&&EmailUtils.checkEmail(pay.getTestEmail())){
            Pay pay2=payService.getPay(pay.getId());
            String tokenFake=UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(FAKE_PRE+pay.getId(),tokenFake,FAKE_EXPIRE,TimeUnit.HOURS);
            pay2=getAdminUrl(pay2,FAKE_PRE+pay.getId(),tokenFake,MY_TOKEN);
            emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getTestEmail(),"【XPay个人收款支付系统】待审核处理","email-fake",pay2);
        }

        Pay p = new Pay();
        p.setId(pay.getId());
        p.setPayNum(pay.getPayNum());
        return new ResultUtil<Object>().setData(p);
    }

    @RequestMapping(value = "/pay/edit",method = RequestMethod.POST)
    @ApiOperation(value = "编辑支付订单")
    @ResponseBody
    @CacheEvict(key = "#id")
    public Result<Object> editPay(@ModelAttribute Pay pay,
                                  @RequestParam String id,
                                  @RequestParam String token){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        try {
            pay.setId(getPayId(pay.getId()));
            Pay p=payService.getPay(getPayId(pay.getId()));
            pay.setState(p.getState());
            if(!pay.getId().contains(FAKE_PRE)){
                pay.setCreateTime(StringUtils.getDate(pay.getTime()));
            }else{
                //假管理
                pay.setMoney(p.getMoney());
                pay.setPayType(p.getPayType());
            }
            payService.updatePay(pay);
        }catch (Exception e){
            return new ResultUtil<Object>().setErrorMsg("编辑支付订单失败");
        }
        if(id.contains(FAKE_PRE)){
            redisTemplate.opsForValue().set(id,"",1L,TimeUnit.SECONDS);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/pay/pass",method = RequestMethod.GET)
    @ApiOperation(value = "审核通过支付订单")
    @CacheEvict(key = "#id")
    public String addPay(@RequestParam String id,
                         @RequestParam String token,
                         @RequestParam String myToken,
                         @RequestParam String sendType,
                         Model model){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            model.addAttribute("errorMsg","无效的Token或链接");
            return "500";
        }
        if(!myToken.equals(MY_TOKEN)){
            model.addAttribute("errorMsg","您未通过二次验证，当我傻吗");
            return "500";
        }
        try {
            payService.changePayState(getPayId(id),1);
            //通知回调
            Pay pay=payService.getPay(getPayId(id));
            if(StringUtils.isNotBlank(pay.getEmail())&&EmailUtils.checkEmail(pay.getEmail())){
                if("0".equals(sendType)){
                    emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getEmail(),"【XPay个人收款支付系统】支付成功通知","pay-success",pay);
                }else if("1".equals(sendType)){
                    emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getEmail(),"【XPay个人收款支付系统】支付成功通知","sendwxcode",pay);
                }else if("2".equals(sendType)){
                    emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getEmail(),"【XPay个人收款支付系统】支付成功通知","sendxboot",pay);
                }
            }
        }catch (Exception e){
            model.addAttribute("errorMsg","处理数据出错");
            return "500";
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/pay/passNotShow",method = RequestMethod.GET)
    @ApiOperation(value = "审核通过但不显示加入捐赠表")
    @CacheEvict(key = "#id")
    public String passNotShowPay(@RequestParam String id,
                                 @RequestParam String token,
                                 Model model){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            model.addAttribute("errorMsg","无效的Token或链接");
            return "500";
        }
        try {
            Pay pay=payService.getPay(getPayId(id));
            if(id.contains(FAKE_PRE)&&pay.getState()==1){
                model.addAttribute("errorMsg","对于已成功支付的订单您无权进行该操作");
                return "500";
            }
            payService.changePayState(getPayId(id),3);
            //通知回调
            if(StringUtils.isNotBlank(pay.getEmail())&&EmailUtils.checkEmail(pay.getEmail())){
                emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getEmail(),"【XPay个人收款支付系统】支付成功通知","pay-notshow",pay);
            }
        }catch (Exception e){
            model.addAttribute("errorMsg","处理数据出错");
            return "500";
        }
        if(id.contains(FAKE_PRE)){
            redisTemplate.opsForValue().set(id,"",1L,TimeUnit.SECONDS);
        }
        return "redirect:/success";
    }


    @RequestMapping(value = "/pay/back",method = RequestMethod.GET)
    @ApiOperation(value = "审核驳回支付订单")
    @CacheEvict(key = "#id")
    public String backPay(@RequestParam String id,
                          @RequestParam String token,
                          @RequestParam String myToken,
                          Model model){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            model.addAttribute("errorMsg","无效的Token或链接");
            return "500";
        }
        if(!myToken.equals(MY_TOKEN)){
            model.addAttribute("errorMsg","您未通过二次验证，当我傻吗");
            return "500";
        }
        try {
            payService.changePayState(getPayId(id),2);
            //通知回调
            Pay pay=payService.getPay(getPayId(id));
            if(StringUtils.isNotBlank(pay.getEmail())&&EmailUtils.checkEmail(pay.getEmail())){
                emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getEmail(),"【XPay个人收款支付系统】支付失败通知","pay-fail",pay);
            }
        }catch (Exception e){
            model.addAttribute("errorMsg","处理数据出错");
            return "500";
        }
        if(id.contains(FAKE_PRE)){
            redisTemplate.opsForValue().set(id,"",1L,TimeUnit.SECONDS);
        }
        return "redirect:/success";
    }

    @RequestMapping(value = "/pay/del",method = RequestMethod.GET)
    @ApiOperation(value = "删除支付订单")
    @ResponseBody
    @CacheEvict(key = "#id")
    public Result<Object> delPay(@RequestParam String id,
                                 @RequestParam String token){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        try {
            //通知回调
            Pay pay=payService.getPay(getPayId(id));
            if(id.contains(FAKE_PRE)&&pay.getState()==1){
                return new ResultUtil<Object>().setErrorMsg("您无权删除已成功支付的订单");
            }
            if(StringUtils.isNotBlank(pay.getEmail())&&EmailUtils.checkEmail(pay.getEmail())){
                emailUtils.sendTemplateMail(EMAIL_SENDER,pay.getEmail(),"【XPay个人收款支付系统】支付失败通知","pay-fail",pay);
            }
            payService.delPay(getPayId(id));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResultUtil<Object>().setErrorMsg("删除支付订单失败");
        }
        if(id.contains(FAKE_PRE)){
            redisTemplate.opsForValue().set(id,"",1L,TimeUnit.SECONDS);
        }
        return new ResultUtil<Object>().setData(null);
    }

    /**
     * 关闭系统
     * @param id
     * @param token
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay/close",method = RequestMethod.GET)
    public String closeForSixHour(@RequestParam String id,
                                  @RequestParam String token,
                                  Model model){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            model.addAttribute("errorMsg","无效的Token或链接");
            return "500";
        }
        try {
            redisTemplate.opsForValue().set(CLOSE_KEY, "CLOSED");
        }catch (Exception e){
            model.addAttribute("errorMsg","处理数据出错");
            return "500";
        }
        return "redirect:/success";
    }

    /**
     * 开启
     * @param id
     * @param token
     * @param model
     * @return
     */
    @RequestMapping(value = "/pay/open",method = RequestMethod.GET)
    public String open(@RequestParam String id,
                       @RequestParam String token,
                       Model model){

        String temp=redisTemplate.opsForValue().get(id);
        if(!token.equals(temp)){
            model.addAttribute("errorMsg","无效的Token或链接");
            return "500";
        }
        try {
            redisTemplate.delete(CLOSE_KEY);
        }catch (Exception e){
            model.addAttribute("errorMsg","处理数据出错");
            return "500";
        }
        return "redirect:/success";
    }

    /**
     * 数据统计
     * @return
     */
    @RequestMapping(value = "/pay/statistic",method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> statistic(@RequestParam Integer type,
                                    @RequestParam(required = false) String start,
                                    @RequestParam(required = false) String end,
                                    @RequestParam String myToken){

        if(!MY_TOKEN.equals(myToken)){
            return new ResultUtil<Object>().setErrorMsg("二次密码验证不正确");
        }
        return new ResultUtil<Object>().setData(payService.statistic(type, start, end));
    }

    /**
     * 拼接管理员链接
     */
    public Pay getAdminUrl(Pay pay,String id,String token,String myToken){

        String pass=SERVER_URL+"/pay/pass?sendType=0&id="+id+"&token="+token+"&myToken="+myToken;
        pay.setPassUrl(pass);

        String pass2=SERVER_URL+"/pay/pass?sendType=1&id="+id+"&token="+token+"&myToken="+myToken;
        pay.setPassUrl2(pass2);

        String pass3=SERVER_URL+"/pay/pass?sendType=2&id="+id+"&token="+token+"&myToken="+myToken;
        pay.setPassUrl3(pass3);

        String back=SERVER_URL+"/pay/back?id="+id+"&token="+token+"&myToken="+myToken;
        pay.setBackUrl(back);

        String passNotShow=SERVER_URL+"/pay/passNotShow?id="+id+"&token="+token;
        pay.setPassNotShowUrl(passNotShow);

        String edit=SERVER_URL+"/pay-edit?id="+id+"&token="+token;
        pay.setEditUrl(edit);

        String del=SERVER_URL+"/pay-del?id="+id+"&token="+token;
        pay.setDelUrl(del);

        String close=SERVER_URL+"/pay/close?id="+id+"&token="+token;
        pay.setCloseUrl(close);

        String open=SERVER_URL+"/pay/open?id="+id+"&token="+token;
        pay.setOpenUrl(open);

        String statistic=SERVER_URL+"/statistic?myToken="+myToken;
        pay.setStatistic(statistic);
        return pay;
    }

    /**
     * 获得假管理ID
     * @param id
     * @return
     */
    public String getPayId(String id){
        if(id.contains(FAKE_PRE)){
            String realId=id.substring(id.indexOf("-",0)+1,id.length());
            return realId;
        }
        return id;
    }
}
