package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A District.
 */
@Entity
@Table(name = "district")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customerEquipments", "orders", "cusNotes", "province", "district", "ward" }, allowSetters = true)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "historyOrders", "customer", "sale", "staff", "sourceOrder", "province", "district", "ward" },
        allowSetters = true
    )
    private Set<Order> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customers", "orders", "district" }, allowSetters = true)
    private Set<Ward> wards = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customers", "orders", "districts" }, allowSetters = true)
    private Province province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public District id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public District code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public District name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(Set<Customer> customers) {
        if (this.customers != null) {
            this.customers.forEach(i -> i.setDistrict(null));
        }
        if (customers != null) {
            customers.forEach(i -> i.setDistrict(this));
        }
        this.customers = customers;
    }

    public District customers(Set<Customer> customers) {
        this.setCustomers(customers);
        return this;
    }

    public District addCustomer(Customer customer) {
        this.customers.add(customer);
        customer.setDistrict(this);
        return this;
    }

    public District removeCustomer(Customer customer) {
        this.customers.remove(customer);
        customer.setDistrict(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setDistrict(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setDistrict(this));
        }
        this.orders = orders;
    }

    public District orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public District addOrder(Order order) {
        this.orders.add(order);
        order.setDistrict(this);
        return this;
    }

    public District removeOrder(Order order) {
        this.orders.remove(order);
        order.setDistrict(null);
        return this;
    }

    public Set<Ward> getWards() {
        return this.wards;
    }

    public void setWards(Set<Ward> wards) {
        if (this.wards != null) {
            this.wards.forEach(i -> i.setDistrict(null));
        }
        if (wards != null) {
            wards.forEach(i -> i.setDistrict(this));
        }
        this.wards = wards;
    }

    public District wards(Set<Ward> wards) {
        this.setWards(wards);
        return this;
    }

    public District addWard(Ward ward) {
        this.wards.add(ward);
        ward.setDistrict(this);
        return this;
    }

    public District removeWard(Ward ward) {
        this.wards.remove(ward);
        ward.setDistrict(null);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District province(Province province) {
        this.setProvince(province);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }
        return getId() != null && getId().equals(((District) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
