package com.project.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.CusNote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CusNoteDTO implements Serializable {

    private Long id;

    @NotNull
    private String createBy;

    @NotNull
    private String content;

    private LocalDate createDate;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CusNoteDTO)) {
            return false;
        }

        CusNoteDTO cusNoteDTO = (CusNoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cusNoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CusNoteDTO{" +
            "id=" + getId() +
            ", createBy='" + getCreateBy() + "'" +
            ", content='" + getContent() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", customer=" + getCustomer() +
            "}";
    }
}
