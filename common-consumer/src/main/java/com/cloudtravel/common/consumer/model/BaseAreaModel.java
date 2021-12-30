package com.cloudtravel.common.consumer.model;

import java.math.BigDecimal;

public class BaseAreaModel {
    private Long areaId;

    private Long parentAreaId;

    private String areaName;

    private String fullAreaName;

    private String shortName;

    private String fullShortName;

    private Short areaLevel;

    private String cityCode;

    private String zipCode;

    private String pinyin;

    private String jianpin;

    private String firstChar;

    private BigDecimal lng;

    private BigDecimal lat;

    private Long hotPiror;

    private String remark;

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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getParentAreaId() {
        return parentAreaId;
    }

    public void setParentAreaId(Long parentAreaId) {
        this.parentAreaId = parentAreaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getFullAreaName() {
        return fullAreaName;
    }

    public void setFullAreaName(String fullAreaName) {
        this.fullAreaName = fullAreaName == null ? null : fullAreaName.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getFullShortName() {
        return fullShortName;
    }

    public void setFullShortName(String fullShortName) {
        this.fullShortName = fullShortName == null ? null : fullShortName.trim();
    }

    public Short getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Short areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }

    public String getJianpin() {
        return jianpin;
    }

    public void setJianpin(String jianpin) {
        this.jianpin = jianpin == null ? null : jianpin.trim();
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Long getHotPiror() {
        return hotPiror;
    }

    public void setHotPiror(Long hotPiror) {
        this.hotPiror = hotPiror;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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