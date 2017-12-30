package cn.exrick.service;

import cn.exrick.bean.Pay;

import java.util.List;

/**
 * @author Exrickx
 */
public interface PayService {

    /**
     * 获得支付表
     * @param state
     * @return
     */
    List<Pay> getPayList(Integer state);

    /**
     * 获得未支付表
     * @return
     */
    List<Pay> getNotPayList();

    /**
     * 获得支付
     * @param id
     * @return
     */
    Pay getPay(String id);

    /**
     * 添加支付
     * @param pay
     * @return
     */
    int addPay(Pay pay);

    /**
     * 编辑支付
     * @param pay
     * @return
     */
    int updatePay(Pay pay);

    /**
     * 状态改变
     * @param id
     * @param state
     * @return
     */
    int changePayState(String id,Integer state);

    /**
     * 删除除捐赠和审核中以外的数据支付
     * @param id
     * @return
     */
    int delPay(String id);
}
