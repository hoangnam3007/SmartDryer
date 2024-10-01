package com.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.CustomerEquipment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerEquipmentDTO implements Serializable {

    private Long id;

    private Integer quantily;

    private CustomerDTO customer;

    private EquipmentDTO equipment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantily() {
        return quantily;
    }

    public void setQuantily(Integer quantily) {
        this.quantily = quantily;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public EquipmentDTO getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerEquipmentDTO)) {
            return false;
        }

        CustomerEquipmentDTO customerEquipmentDTO = (CustomerEquipmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customerEquipmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerEquipmentDTO{" +
            "id=" + getId() +
            ", quantily=" + getQuantily() +
            ", customer=" + getCustomer() +
            ", equipment=" + getEquipment() +
            "}";
    }
}
