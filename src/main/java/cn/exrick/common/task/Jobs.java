package cn.exrick.common.task;

import cn.exrick.bean.Pay;
import cn.exrick.dao.PayDao;
import cn.exrick.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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

    /**
     * 每日凌晨清空除捐赠和审核中以外的数据
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void cronJob(){

        List<Pay> list=payDao.getByStateIsNotAndStateIsNot(0,1);
        for(Pay p:list){
            try {
                payService.delPay(p.getId());
            }catch (Exception e){
                log.error("定时删除数据"+p.getId()+"失败");
                e.printStackTrace();
            }
        }

        log.info("定时执行清空除捐赠和审核中的数据完毕");

        //设置未审核数据为支付失败
        List<Pay> list1=payDao.getByStateIs(0);
        for(Pay p:list1){
            p.setState(2);
            p.setUpdateTime(new Date());
            try {
                payService.updatePay(p);
            }catch (Exception e){
                log.error("设置未审核数据"+p.getId()+"为支付失败");
                e.printStackTrace();
            }
        }

        log.info("定时执行设置未审核数据为支付失败完毕");
    }
}
