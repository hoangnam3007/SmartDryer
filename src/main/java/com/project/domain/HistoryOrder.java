package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.domain.enumeration.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HistoryOrder.
 */
@Entity
@Table(name = "history_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoryOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_new")
    private OrderStatus statusNew;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_old")
    private OrderStatus statusOld;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "historyOrders", "customer", "sale", "staff", "sourceOrder", "province", "district", "ward" },
        allowSetters = true
    )
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoryOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public HistoryOrder modifiedBy(String modifiedBy) {
        this.setModifiedBy(modifiedBy);
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public OrderStatus getStatusNew() {
        return this.statusNew;
    }

    public HistoryOrder statusNew(OrderStatus statusNew) {
        this.setStatusNew(statusNew);
        return this;
    }

    public void setStatusNew(OrderStatus statusNew) {
        this.statusNew = statusNew;
    }

    public OrderStatus getStatusOld() {
        return this.statusOld;
    }

    public HistoryOrder statusOld(OrderStatus statusOld) {
        this.setStatusOld(statusOld);
        return this;
    }

    public void setStatusOld(OrderStatus statusOld) {
        this.statusOld = statusOld;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public HistoryOrder modifiedDate(LocalDate modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public HistoryOrder order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoryOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryOrder{" +
            "id=" + getId() +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", statusNew='" + getStatusNew() + "'" +
            ", statusOld='" + getStatusOld() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
