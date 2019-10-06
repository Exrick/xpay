package cn.exrick.common.task;

import cn.exrick.bean.Pay;
import cn.exrick.common.utils.EmailUtils;
import cn.exrick.common.utils.StringUtils;
import cn.exrick.dao.PayDao;
import cn.exrick.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Exrickx
 */
@Component
public class Jobs {

    final static Logger log= LoggerFactory.getLogger(Jobs.class);

    @Autowired
    private PayService payService;

    @Autowired
    private PayDao payDao;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${email.sender}")
    private String EMAIL_SENDER;

    private static final String CLOSE_KEY="XPAY_CLOSE_KEY";

    /**
     * 每日凌晨清空除捐赠和审核中以外的数据
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void cronJob(){

        List<Pay> list=payDao.getByStateIs(2);
        list.addAll(payDao.getByStateIs(3));
        for(Pay p:list){
            try {
                payService.delPay(p.getId());
            }catch (Exception e){
                log.error("定时删除数据"+p.getId()+"失败");
                e.printStackTrace();
            }
        }

        log.info("定时执行清空驳回和通过不展示的数据完毕");

        //设置未审核或已扫码数据为支付失败
        List<Pay> list1=payDao.getByStateIs(0);
        list1.addAll(payDao.getByStateIs(4));
        for(Pay p:list1){
            p.setState(2);
            p.setUpdateTime(new Date());
            try {
                payService.updatePay(p);
            }catch (Exception e){
                log.error("设置未审核数据"+p.getId()+"为支付失败出错");
                e.printStackTrace();
            }
        }

        log.info("定时执行设置未审核数据为支付失败完毕");
    }

    /**
     * 每日10am统一发送支付失败邮件
     */
    @Scheduled(cron="0 0 10 * * ?")
    public void sendEmailJob(){

        List<Pay> list=payDao.getByStateIs(2);
        for(Pay p:list){
            p.setTime(StringUtils.getTimeStamp(p.getCreateTime()));
            if(StringUtils.isNotBlank(p.getEmail())&&EmailUtils.checkEmail(p.getEmail())) {
                emailUtils.sendTemplateMail(EMAIL_SENDER, p.getEmail(), "【XPay个人收款支付系统】支付失败通知", "pay-fail", p);
            }
        }

        log.info("定时执行统一发送支付失败邮件完毕");
    }

    /**
     * 每日1am关闭系统7小时
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void close(){

        redisTemplate.opsForValue().set(CLOSE_KEY, "CLOSED", 7L, TimeUnit.HOURS);
        log.info("定时关闭系统成功");
    }
}
