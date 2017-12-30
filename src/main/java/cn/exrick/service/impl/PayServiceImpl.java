package cn.exrick.service.impl;

import cn.exrick.bean.Pay;
import cn.exrick.common.utils.StringUtils;
import cn.exrick.dao.PayDao;
import cn.exrick.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Exrickx
 */
@Service
public class PayServiceImpl implements PayService {

    private static final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private PayDao payDao;

    @Override
    public List<Pay> getPayList(Integer state) {

        List<Pay> list=payDao.getByStateIs(state);
        for(Pay pay:list){
            //屏蔽隐私数据
            pay.setId("");
            pay.setEmail("");
            pay.setTestEmail("");
            pay.setUsername("");
        }
        return list;
    }

    @Override
    public List<Pay> getNotPayList() {

        List<Pay> list=payDao.getByStateIsNotAndStateIsNot(0,1);
        for(Pay pay:list){
            //屏蔽隐私数据
            pay.setId("");
            pay.setEmail("");
            pay.setTestEmail("");
            pay.setUsername("");
        }
        return list;
    }

    @Override
    public Pay getPay(String id) {

        Pay pay=payDao.findOne(id);
        pay.setTime(StringUtils.getTimeStamp(pay.getCreateTime()));
        return pay;
    }

    @Override
    public int addPay(Pay pay) {

        pay.setId(UUID.randomUUID().toString());
        pay.setCreateTime(new Date());
        pay.setState(0);
        payDao.save(pay);
        return 1;
    }

    @Override
    public int updatePay(Pay pay) {

        pay.setUpdateTime(new Date());
        payDao.saveAndFlush(pay);
        return 1;
    }

    @Override
    public int changePayState(String id, Integer state) {

        Pay pay=getPay(id);
        pay.setState(state);
        pay.setUpdateTime(new Date());
        payDao.saveAndFlush(pay);
        return 1;
    }

    @Override
    public int delPay(String id) {

        payDao.delete(id);
        return 1;
    }
}
