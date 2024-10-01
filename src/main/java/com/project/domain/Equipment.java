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
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "equipment_code", nullable = false)
    private String equipmentCode;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "customer", "equipment" }, allowSetters = true)
    private Set<CustomerEquipment> customerEquipments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Equipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return this.equipmentCode;
    }

    public Equipment equipmentCode(String equipmentCode) {
        this.setEquipmentCode(equipmentCode);
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getDescription() {
        return this.description;
    }

    public Equipment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CustomerEquipment> getCustomerEquipments() {
        return this.customerEquipments;
    }

    public void setCustomerEquipments(Set<CustomerEquipment> customerEquipments) {
        if (this.customerEquipments != null) {
            this.customerEquipments.forEach(i -> i.setEquipment(null));
        }
        if (customerEquipments != null) {
            customerEquipments.forEach(i -> i.setEquipment(this));
        }
        this.customerEquipments = customerEquipments;
    }

    public Equipment customerEquipments(Set<CustomerEquipment> customerEquipments) {
        this.setCustomerEquipments(customerEquipments);
        return this;
    }

    public Equipment addCustomerEquipment(CustomerEquipment customerEquipment) {
        this.customerEquipments.add(customerEquipment);
        customerEquipment.setEquipment(this);
        return this;
    }

    public Equipment removeCustomerEquipment(CustomerEquipment customerEquipment) {
        this.customerEquipments.remove(customerEquipment);
        customerEquipment.setEquipment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipment)) {
            return false;
        }
        return getId() != null && getId().equals(((Equipment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + getId() +
            ", equipmentCode='" + getEquipmentCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
