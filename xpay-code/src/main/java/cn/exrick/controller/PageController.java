package cn.exrick.controller;

import cn.exrick.common.utils.StringUtils;
import cn.exrick.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


/**
 * @author Exrickx
 */
@Controller
public class PageController {

    private static final Logger log= LoggerFactory.getLogger(PageController.class);

    @Autowired
    private PayService payService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/")
    public String index(){

        return "index";
    }

    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page,
                           HttpServletRequest request){

        String id = request.getParameter("id");
        if("openAlipay".equals(page)&&StringUtils.isNotBlank(id)){
            // 已扫码状态
            try{
                payService.changePayState(id,4);
                Set<String> keys = redisTemplate.keys("xpay:*");
                redisTemplate.delete(keys);
            }catch (Exception e){

            }
        }
        return page;
    }
}
