package com.cloudtravel.common.consumer.model;

public class RoleModel {
    private Long roleId;

    private Long spId;

    private String roleName;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getSpId() {
        return spId;
    }

    public void setSpId(Long spId) {
        this.spId = spId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
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