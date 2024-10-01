package com.project.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.SendSMS} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SendSMSDTO implements Serializable {

    private Long id;

    @NotNull
    private String mobile;

    private String content;

    private Integer status;

    private LocalDate createDate;

    private LocalDate sendedDate;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getSendedDate() {
        return sendedDate;
    }

    public void setSendedDate(LocalDate sendedDate) {
        this.sendedDate = sendedDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendSMSDTO)) {
            return false;
        }

        SendSMSDTO sendSMSDTO = (SendSMSDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sendSMSDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SendSMSDTO{" +
            "id=" + getId() +
            ", mobile='" + getMobile() + "'" +
            ", content='" + getContent() + "'" +
            ", status=" + getStatus() +
            ", createDate='" + getCreateDate() + "'" +
            ", sendedDate='" + getSendedDate() + "'" +
            ", type=" + getType() +
            "}";
    }
}
