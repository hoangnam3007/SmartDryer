package com.project.service.dto;

import com.project.domain.enumeration.CusStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CreateCustomerDTO {

    @NotNull
    private String userName;

    private String code;

    private String displayName;

    private String address;

    private String createBy;

    private String mobile;

    private String email;

    private String source;

    private String note;

    private CusStatus status;

    private LocalDate createDate;

    private LocalDate modifiedDate;

    private ProvinceDTO province;

    private DistrictDTO district;

    private WardDTO ward;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CusStatus getStatus() {
        return status;
    }

    public void setStatus(CusStatus status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    public WardDTO getWard() {
        return ward;
    }

    public void setWard(WardDTO ward) {
        this.ward = ward;
    }

    @Override
    public String toString() {
        return (
            "CustomerDTO{" +
            "userName='" +
            getUserName() +
            "'" +
            ", code='" +
            getCode() +
            "'" +
            ", displayName='" +
            getDisplayName() +
            "'" +
            ", address='" +
            getAddress() +
            "'" +
            ", createBy='" +
            getCreateBy() +
            "'" +
            ", mobile='" +
            getMobile() +
            "'" +
            ", email='" +
            getEmail() +
            "'" +
            ", source='" +
            getSource() +
            "'" +
            ", note='" +
            getNote() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", createDate='" +
            getCreateDate() +
            "'" +
            ", modifiedDate='" +
            getModifiedDate() +
            "'" +
            ", province=" +
            getProvince() +
            ", district=" +
            getDistrict() +
            ", ward=" +
            getWard() +
            "}"
        );
    }
}
