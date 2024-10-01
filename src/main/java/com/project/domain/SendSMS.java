package com.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SendSMS.
 */
@Entity
@Table(name = "send_sms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SendSMS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "mobile", nullable = false)
    private String mobile;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "sended_date")
    private LocalDate sendedDate;

    @Column(name = "type")
    private Integer type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SendSMS id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return this.mobile;
    }

    public SendSMS mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return this.content;
    }

    public SendSMS content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return this.status;
    }

    public SendSMS status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SendSMS createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getSendedDate() {
        return this.sendedDate;
    }

    public SendSMS sendedDate(LocalDate sendedDate) {
        this.setSendedDate(sendedDate);
        return this;
    }

    public void setSendedDate(LocalDate sendedDate) {
        this.sendedDate = sendedDate;
    }

    public Integer getType() {
        return this.type;
    }

    public SendSMS type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendSMS)) {
            return false;
        }
        return getId() != null && getId().equals(((SendSMS) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SendSMS{" +
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
