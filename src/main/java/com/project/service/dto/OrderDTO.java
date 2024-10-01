package com.project.service.dto;

import com.project.domain.enumeration.OrderStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.Order} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private String createBy;

    private LocalDate createDate;

    private LocalDate finishDate;

    private OrderStatus status;

    private Long amount;

    private String saleNote;

    private String techNote;

    private String note;

    private Integer materialSource;

    private String cusName;

    private String cusAddress;

    private String cusMobile;

    private String imageURL;

    private LocalDate appointmentDate;

    private Integer activation;

    private String assignBy;

    private CustomerDTO customer;

    private SaleDTO sale;

    private StaffDTO staff;

    private SourceOrderDTO sourceOrder;

    private ProvinceDTO province;

    private DistrictDTO district;

    private WardDTO ward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getSaleNote() {
        return saleNote;
    }

    public void setSaleNote(String saleNote) {
        this.saleNote = saleNote;
    }

    public String getTechNote() {
        return techNote;
    }

    public void setTechNote(String techNote) {
        this.techNote = techNote;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getMaterialSource() {
        return materialSource;
    }

    public void setMaterialSource(Integer materialSource) {
        this.materialSource = materialSource;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusMobile() {
        return cusMobile;
    }

    public void setCusMobile(String cusMobile) {
        this.cusMobile = cusMobile;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getActivation() {
        return activation;
    }

    public void setActivation(Integer activation) {
        this.activation = activation;
    }

    public String getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(String assignBy) {
        this.assignBy = assignBy;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public SaleDTO getSale() {
        return sale;
    }

    public void setSale(SaleDTO sale) {
        this.sale = sale;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff = staff;
    }

    public SourceOrderDTO getSourceOrder() {
        return sourceOrder;
    }

    public void setSourceOrder(SourceOrderDTO sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    public WardDTO getWard() {
        return ward;
    }

    public void setWard(WardDTO ward) {
        this.ward = ward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDTO{" +
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
            ", customer=" + getCustomer() +
            ", sale=" + getSale() +
            ", staff=" + getStaff() +
            ", sourceOrder=" + getSourceOrder() +
            ", province=" + getProvince() +
            ", district=" + getDistrict() +
            ", ward=" + getWard() +
            "}";
    }
}
