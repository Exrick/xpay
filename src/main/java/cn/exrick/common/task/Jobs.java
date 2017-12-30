package cn.exrick.common.task;

import cn.exrick.bean.Pay;
import cn.exrick.dao.PayDao;
import cn.exrick.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                log.info("定时删除数据"+p.getId()+"失败");
                e.printStackTrace();
            }
        }

        log.info("执行清空除捐赠和审核中的数据完毕");
    }
}
