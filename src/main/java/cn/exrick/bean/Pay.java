package cn.exrick.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Exrickx
 */
@Entity
@Table(name = "t_pay")
public class Pay implements Serializable{

    /**
     * 唯一标识
     */
    @Id
    @Column
    private String id;

    @Column
    private String nickName;

    @Column
    private BigDecimal money;

    @Column
    private String info;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    /**
     * 用户通知邮箱
     */
    @Column
    private String email;

    @Column
    private String testEmail;

    /**
     * 显示状态 0待审核 1确认显示 2驳回 3通过不展示
     */
    @Column
    private Integer state=0;

    @Column
    private String payType;

    @Column
    private String username;

    @Transient
    private String time;

    @Transient
    private String passUrl;

    @Transient
    private String backUrl;

    @Transient
    private String passNotShowUrl;

    @Transient
    private String editUrl;

    @Transient
    private String delUrl;

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getPassUrl() {
        return passUrl;
    }

    public void setPassUrl(String passUrl) {
        this.passUrl = passUrl;
    }

    public String getPassNotShowUrl() {
        return passNotShowUrl;
    }

    public void setPassNotShowUrl(String passNotShowUrl) {
        this.passNotShowUrl = passNotShowUrl;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public String getDelUrl() {
        return delUrl;
    }

    public void setDelUrl(String delUrl) {
        this.delUrl = delUrl;
    }

    public String getTestEmail() {
        return testEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTestEmail(String testEmail) {
        this.testEmail = testEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
