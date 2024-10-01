package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Staff.
 */
@Entity
@Table(name = "staff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "is_lead")
    private Integer isLead;

    @Column(name = "image_url")
    private String imageURL;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "staff")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "historyOrders", "customer", "sale", "staff", "sourceOrder", "province", "district", "ward" },
        allowSetters = true
    )
    private Set<Order> orders = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "staffLead" }, allowSetters = true)
    private Staff staffLead;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("staff")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public Staff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public Staff userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Staff fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public Staff mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public Staff email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return this.note;
    }

    public Staff note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Staff createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getModifiedDate() {
        return this.modifiedDate;
    }

    public Staff modifiedDate(LocalDate modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getIsLead() {
        return this.isLead;
    }

    public Staff isLead(Integer isLead) {
        this.setIsLead(isLead);
        return this;
    }

    public void setIsLead(Integer isLead) {
        this.isLead = isLead;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Staff imageURL(String imageURL) {
        this.setImageURL(imageURL);
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setStaff(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setStaff(this));
        }
        this.orders = orders;
    }

    public Staff orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Staff addOrder(Order order) {
        this.orders.add(order);
        order.setStaff(this);
        return this;
    }

    public Staff removeOrder(Order order) {
        this.orders.remove(order);
        order.setStaff(null);
        return this;
    }

    public Staff getStaffLead() {
        return this.staffLead;
    }

    public void setStaffLead(Staff staff) {
        this.staffLead = staff;
    }

    public Staff staffLead(Staff staff) {
        this.setStaffLead(staff);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Staff)) {
            return false;
        }
        return getId() != null && getId().equals(((Staff) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Staff{" +
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
            "}";
    }
}
