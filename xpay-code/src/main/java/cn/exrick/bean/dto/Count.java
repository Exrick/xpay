package cn.exrick.bean.dto;

import java.math.BigDecimal;

public class Count {

    private BigDecimal amount = new BigDecimal("0.00");

    private BigDecimal alipay = new BigDecimal("0.00");

    private BigDecimal wechat = new BigDecimal("0.00");

    private BigDecimal qq = new BigDecimal("0.00");

    private BigDecimal union = new BigDecimal("0.00");

    private BigDecimal diandan = new BigDecimal("0.00");

    public BigDecimal getDiandan() {
        return diandan;
    }

    public void setDiandan(BigDecimal diandan) {
        this.diandan = diandan;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAlipay() {
        return alipay;
    }

    public void setAlipay(BigDecimal alipay) {
        this.alipay = alipay;
    }

    public BigDecimal getWechat() {
        return wechat;
    }

    public void setWechat(BigDecimal wechat) {
        this.wechat = wechat;
    }

    public BigDecimal getQq() {
        return qq;
    }

    public void setQq(BigDecimal qq) {
        this.qq = qq;
    }

    public BigDecimal getUnion() {
        return union;
    }

    public void setUnion(BigDecimal union) {
        this.union = union;
    }
}
