package com.cloudtravel.consumer.common.model;

public class BaseAccount {
    private Long accountId;

    private String username;

    private String mobile;

    private String password;

    private String nickname;

    private Short sex;

    private Long headPicFileId;

    private Long registerDate;

    private Long version;

    private Boolean delFlg;

    private String addAction;

    private Long addAccountId;

    private String addTermIp;

    private Long addDt;

    private String updAction;

    private Long updAccountId;

    private String updTermIp;

    private Long updDt;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public Long getHeadPicFileId() {
        return headPicFileId;
    }

    public void setHeadPicFileId(Long headPicFileId) {
        this.headPicFileId = headPicFileId;
    }

    public Long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Long registerDate) {
        this.registerDate = registerDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public String getAddAction() {
        return addAction;
    }

    public void setAddAction(String addAction) {
        this.addAction = addAction == null ? null : addAction.trim();
    }

    public Long getAddAccountId() {
        return addAccountId;
    }

    public void setAddAccountId(Long addAccountId) {
        this.addAccountId = addAccountId;
    }

    public String getAddTermIp() {
        return addTermIp;
    }

    public void setAddTermIp(String addTermIp) {
        this.addTermIp = addTermIp == null ? null : addTermIp.trim();
    }

    public Long getAddDt() {
        return addDt;
    }

    public void setAddDt(Long addDt) {
        this.addDt = addDt;
    }

    public String getUpdAction() {
        return updAction;
    }

    public void setUpdAction(String updAction) {
        this.updAction = updAction == null ? null : updAction.trim();
    }

    public Long getUpdAccountId() {
        return updAccountId;
    }

    public void setUpdAccountId(Long updAccountId) {
        this.updAccountId = updAccountId;
    }

    public String getUpdTermIp() {
        return updTermIp;
    }

    public void setUpdTermIp(String updTermIp) {
        this.updTermIp = updTermIp == null ? null : updTermIp.trim();
    }

    public Long getUpdDt() {
        return updDt;
    }

    public void setUpdDt(Long updDt) {
        this.updDt = updDt;
    }
}