package com.project.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.Staff} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StaffDTO implements Serializable {

    private Long id;

    @NotNull
    private String userName;

    @NotNull
    private String fullName;

    private String mobile;

    private String email;

    private String note;

    private LocalDate createDate;

    private LocalDate modifiedDate;

    private Integer isLead;

    private String imageURL;

    private StaffDTO staffLead;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Integer getIsLead() {
        return isLead;
    }

    public void setIsLead(Integer isLead) {
        this.isLead = isLead;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public StaffDTO getStaffLead() {
        return staffLead;
    }

    public void setStaffLead(StaffDTO staffLead) {
        this.staffLead = staffLead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffDTO)) {
            return false;
        }

        StaffDTO staffDTO = (StaffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", note='" + getNote() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", isLead=" + getIsLead() +
            ", imageURL='" + getImageURL() + "'" +
            ", staffLead=" + getStaffLead() +
            "}";
    }
}
