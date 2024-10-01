package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.domain.enumeration.CusStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "address")
    private String address;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "source")
    private String source;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CusStatus status;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "equipment" }, allowSetters = true)
    private Set<CustomerEquipment> customerEquipments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "historyOrders", "customer", "sale", "staff", "sourceOrder", "province", "district", "ward" },
        allowSetters = true
    )
    private Set<Order> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer" }, allowSetters = true)
    private Set<CusNote> cusNotes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customers", "orders", "districts" }, allowSetters = true)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customers", "orders", "wards", "province" }, allowSetters = true)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customers", "orders", "district" }, allowSetters = true)
    private Ward ward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public Customer userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return this.code;
    }

    public Customer code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Customer displayName(String displayName) {
        this.setDisplayName(displayName);
        return this;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAddress() {
        return this.address;
    }

    public Customer address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Customer createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Customer mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return this.source;
    }

    public Customer source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return this.note;
    }

    public Customer note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CusStatus getStatus() {
        return this.status;
    }

    public Customer status(CusStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CusStatus status) {
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Customer createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public Customer modifiedDate(LocalDate modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Set<CustomerEquipment> getCustomerEquipments() {
        return this.customerEquipments;
    }

    public void setCustomerEquipments(Set<CustomerEquipment> customerEquipments) {
        if (this.customerEquipments != null) {
            this.customerEquipments.forEach(i -> i.setCustomer(null));
        }
        if (customerEquipments != null) {
            customerEquipments.forEach(i -> i.setCustomer(this));
        }
        this.customerEquipments = customerEquipments;
    }

    public Customer customerEquipments(Set<CustomerEquipment> customerEquipments) {
        this.setCustomerEquipments(customerEquipments);
        return this;
    }

    public Customer addCustomerEquipment(CustomerEquipment customerEquipment) {
        this.customerEquipments.add(customerEquipment);
        customerEquipment.setCustomer(this);
        return this;
    }

    public Customer removeCustomerEquipment(CustomerEquipment customerEquipment) {
        this.customerEquipments.remove(customerEquipment);
        customerEquipment.setCustomer(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCustomer(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setCustomer(this));
        }
        this.orders = orders;
    }

    public Customer orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Customer addOrder(Order order) {
        this.orders.add(order);
        order.setCustomer(this);
        return this;
    }

    public Customer removeOrder(Order order) {
        this.orders.remove(order);
        order.setCustomer(null);
        return this;
    }

    public Set<CusNote> getCusNotes() {
        return this.cusNotes;
    }

    public void setCusNotes(Set<CusNote> cusNotes) {
        if (this.cusNotes != null) {
            this.cusNotes.forEach(i -> i.setCustomer(null));
        }
        if (cusNotes != null) {
            cusNotes.forEach(i -> i.setCustomer(this));
        }
        this.cusNotes = cusNotes;
    }

    public Customer cusNotes(Set<CusNote> cusNotes) {
        this.setCusNotes(cusNotes);
        return this;
    }

    public Customer addCusNote(CusNote cusNote) {
        this.cusNotes.add(cusNote);
        cusNote.setCustomer(this);
        return this;
    }

    public Customer removeCusNote(CusNote cusNote) {
        this.cusNotes.remove(cusNote);
        cusNote.setCustomer(null);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Customer province(Province province) {
        this.setProvince(province);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Customer district(District district) {
        this.setDistrict(district);
        return this;
    }

    public Ward getWard() {
        return this.ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Customer ward(Ward ward) {
        this.setWard(ward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", code='" + getCode() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", address='" + getAddress() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", source='" + getSource() + "'" +
            ", note='" + getNote() + "'" +
            ", status='" + getStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
