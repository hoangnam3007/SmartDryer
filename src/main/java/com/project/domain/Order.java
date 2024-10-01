package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.domain.enumeration.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "sale_note")
    private String saleNote;

    @Column(name = "tech_note")
    private String techNote;

    @Column(name = "note")
    private String note;

    @Column(name = "material_source")
    private Integer materialSource;

    @Column(name = "cus_name")
    private String cusName;

    @Column(name = "cus_address")
    private String cusAddress;

    @Column(name = "cus_mobile")
    private String cusMobile;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(name = "activation")
    private Integer activation;

    @Column(name = "assign_by")
    private String assignBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<HistoryOrder> historyOrders = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customerEquipments", "orders", "cusNotes", "province", "district", "ward" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders", "staffLead" }, allowSetters = true)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private SourceOrder sourceOrder;

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

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Order code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Order createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public Order createDate(LocalDate createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getFinishDate() {
        return this.finishDate;
    }

    public Order finishDate(LocalDate finishDate) {
        this.setFinishDate(finishDate);
        return this;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Order amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getSaleNote() {
        return this.saleNote;
    }

    public Order saleNote(String saleNote) {
        this.setSaleNote(saleNote);
        return this;
    }

    public void setSaleNote(String saleNote) {
        this.saleNote = saleNote;
    }

    public String getTechNote() {
        return this.techNote;
    }

    public Order techNote(String techNote) {
        this.setTechNote(techNote);
        return this;
    }

    public void setTechNote(String techNote) {
        this.techNote = techNote;
    }

    public String getNote() {
        return this.note;
    }

    public Order note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getMaterialSource() {
        return this.materialSource;
    }

    public Order materialSource(Integer materialSource) {
        this.setMaterialSource(materialSource);
        return this;
    }

    public void setMaterialSource(Integer materialSource) {
        this.materialSource = materialSource;
    }

    public String getCusName() {
        return this.cusName;
    }

    public Order cusName(String cusName) {
        this.setCusName(cusName);
        return this;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return this.cusAddress;
    }

    public Order cusAddress(String cusAddress) {
        this.setCusAddress(cusAddress);
        return this;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusMobile() {
        return this.cusMobile;
    }

    public Order cusMobile(String cusMobile) {
        this.setCusMobile(cusMobile);
        return this;
    }

    public void setCusMobile(String cusMobile) {
        this.cusMobile = cusMobile;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public Order imageURL(String imageURL) {
        this.setImageURL(imageURL);
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public LocalDate getAppointmentDate() {
        return this.appointmentDate;
    }

    public Order appointmentDate(LocalDate appointmentDate) {
        this.setAppointmentDate(appointmentDate);
        return this;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getActivation() {
        return this.activation;
    }

    public Order activation(Integer activation) {
        this.setActivation(activation);
        return this;
    }

    public void setActivation(Integer activation) {
        this.activation = activation;
    }

    public String getAssignBy() {
        return this.assignBy;
    }

    public Order assignBy(String assignBy) {
        this.setAssignBy(assignBy);
        return this;
    }

    public void setAssignBy(String assignBy) {
        this.assignBy = assignBy;
    }

    public Set<HistoryOrder> getHistoryOrders() {
        return this.historyOrders;
    }

    public void setHistoryOrders(Set<HistoryOrder> historyOrders) {
        if (this.historyOrders != null) {
            this.historyOrders.forEach(i -> i.setOrder(null));
        }
        if (historyOrders != null) {
            historyOrders.forEach(i -> i.setOrder(this));
        }
        this.historyOrders = historyOrders;
    }

    public Order historyOrders(Set<HistoryOrder> historyOrders) {
        this.setHistoryOrders(historyOrders);
        return this;
    }

    public Order addHistoryOrder(HistoryOrder historyOrder) {
        this.historyOrders.add(historyOrder);
        historyOrder.setOrder(this);
        return this;
    }

    public Order removeHistoryOrder(HistoryOrder historyOrder) {
        this.historyOrders.remove(historyOrder);
        historyOrder.setOrder(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Sale getSale() {
        return this.sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Order sale(Sale sale) {
        this.setSale(sale);
        return this;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Order staff(Staff staff) {
        this.setStaff(staff);
        return this;
    }

    public SourceOrder getSourceOrder() {
        return this.sourceOrder;
    }

    public void setSourceOrder(SourceOrder sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    public Order sourceOrder(SourceOrder sourceOrder) {
        this.setSourceOrder(sourceOrder);
        return this;
    }

    public Province getProvince() {
        return this.province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Order province(Province province) {
        this.setProvince(province);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Order district(District district) {
        this.setDistrict(district);
        return this;
    }

    public Ward getWard() {
        return this.ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Order ward(Ward ward) {
        this.setWard(ward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", finishDate='" + getFinishDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", amount=" + getAmount() +
            ", saleNote='" + getSaleNote() + "'" +
            ", techNote='" + getTechNote() + "'" +
            ", note='" + getNote() + "'" +
            ", materialSource=" + getMaterialSource() +
            ", cusName='" + getCusName() + "'" +
            ", cusAddress='" + getCusAddress() + "'" +
            ", cusMobile='" + getCusMobile() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", appointmentDate='" + getAppointmentDate() + "'" +
            ", activation=" + getActivation() +
            ", assignBy='" + getAssignBy() + "'" +
            "}";
    }
}
