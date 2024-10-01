package com.project.service.dto;

import com.project.domain.enumeration.OrderStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.HistoryOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoryOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private String modifiedBy;

    private OrderStatus statusNew;

    private OrderStatus statusOld;

    private LocalDate modifiedDate;

    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public OrderStatus getStatusNew() {
        return statusNew;
    }

    public void setStatusNew(OrderStatus statusNew) {
        this.statusNew = statusNew;
    }

    public OrderStatus getStatusOld() {
        return statusOld;
    }

    public void setStatusOld(OrderStatus statusOld) {
        this.statusOld = statusOld;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryOrderDTO)) {
            return false;
        }

        HistoryOrderDTO historyOrderDTO = (HistoryOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historyOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryOrderDTO{" +
            "id=" + getId() +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", statusNew='" + getStatusNew() + "'" +
            ", statusOld='" + getStatusOld() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
