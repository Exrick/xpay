package cn.exrick.service;

import cn.exrick.bean.Pay;
import cn.exrick.bean.dto.Count;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author Exrickx
 */
public interface PayService {

    /**
     * 分页获取支付列表
     * @param state
     * @param key
     * @param pageable
     * @return
     */
    Page<Pay> getPayListByPage(Integer state, String key, Pageable pageable);

    /**
     * 获得支付表
     * @param state
     * @return
     */
    List<Pay> getPayList(Integer state);

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

    /**
     * 统计数据
     * @param type
     * @param start
     * @param end
     * @return
     */
    Count statistic(Integer type, String start, String end);
}
