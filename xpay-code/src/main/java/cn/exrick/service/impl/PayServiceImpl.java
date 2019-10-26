package cn.exrick.service.impl;

import cn.exrick.bean.Pay;
import cn.exrick.bean.dto.Count;
import cn.exrick.common.utils.DateUtils;
import cn.exrick.common.utils.StringUtils;
import cn.exrick.dao.PayDao;
import cn.exrick.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    public Page<Pay> getPayListByPage(Integer state, String key, Pageable pageable) {
        return payDao.findAll(new Specification<Pay>() {
            @Override
            public Predicate toPredicate(Root<Pay> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<String> nickNameField = root.get("nickName");
                Path<String> infoField = root.get("info");
                Path<String> payTypeField=root.get("payType");
                Path<Integer> stateField=root.get("state");

                List<Predicate> list = new ArrayList<Predicate>();

                //模糊搜素
                if(StringUtils.isNotBlank(key)){
                    Predicate p1 = cb.like(nickNameField,'%'+key+'%');
                    Predicate p3 = cb.like(infoField,'%'+key+'%');
                    Predicate p4 = cb.like(payTypeField,'%'+key+'%');
                    list.add(cb.or(p1,p3,p4));
                }

                //状态
                if(state!=null){
                    list.add(cb.equal(stateField, state));
                }

                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<Pay> getPayList(Integer state) {

        List<Pay> list=payDao.getByStateIs(state);
        for(Pay pay:list){
            //屏蔽隐私数据
            pay.setId("");
            pay.setEmail("");
            pay.setTestEmail("");
            pay.setPayNum(null);
            pay.setMobile(null);
            pay.setCustom(null);
            pay.setDevice(null);
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

    @Override
    public Count statistic(Integer type, String start, String end) {

        Count count=new Count();
        if(type==-1){
            // 总
            count.setAmount(payDao.countAllMoney());
            count.setAlipay(payDao.countAllMoneyByType("Alipay"));
            count.setWechat(payDao.countAllMoneyByType("Wechat"));
            count.setQq(payDao.countAllMoneyByType("QQ"));
            count.setUnion(payDao.countAllMoneyByType("UnionPay"));
            count.setDiandan(payDao.countAllMoneyByType("Diandan"));
            return count;
        }
        Date startDate=null,endDate=null;
        if(type==0){
            // 今天
            startDate = DateUtils.getDayBegin();
            endDate = DateUtils.getDayEnd();
        }if(type==6){
            // 昨天
            startDate = DateUtils.getBeginDayOfYesterday();
            endDate = DateUtils.getEndDayOfYesterDay();
        }else if(type==1){
            // 本周
            startDate = DateUtils.getBeginDayOfWeek();
            endDate = DateUtils.getEndDayOfWeek();
        }else if(type==2){
            // 本月
            startDate = DateUtils.getBeginDayOfMonth();
            endDate = DateUtils.getEndDayOfMonth();
        }else if(type==3){
            // 本年
            startDate = DateUtils.getBeginDayOfYear();
            endDate = DateUtils.getEndDayOfYear();
        }else if(type==4){
            // 上周
            startDate = DateUtils.getBeginDayOfLastWeek();
            endDate = DateUtils.getEndDayOfLastWeek();
        }else if(type==5){
            // 上个月
            startDate = DateUtils.getBeginDayOfLastMonth();
            endDate = DateUtils.getEndDayOfLastMonth();
        }else if(type==-2){
            // 自定义
            startDate = DateUtils.parseStartDate(start);
            endDate = DateUtils.parseEndDate(end);
        }
        count.setAmount(payDao.countMoney(startDate, endDate));
        count.setAlipay(payDao.countMoneyByType("Alipay", startDate, endDate));
        count.setWechat(payDao.countMoneyByType("Wechat", startDate, endDate));
        count.setQq(payDao.countMoneyByType("QQ", startDate, endDate));
        count.setUnion(payDao.countMoneyByType("UnionPay", startDate, endDate));
        count.setDiandan(payDao.countMoneyByType("Diandan", startDate, endDate));
        return count;
    }
}
