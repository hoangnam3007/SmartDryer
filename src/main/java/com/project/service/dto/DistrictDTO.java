package com.project.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.project.domain.District} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DistrictDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String name;

    private ProvinceDTO province;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProvinceDTO getProvince() {
        return province;
    }

    public void setProvince(ProvinceDTO province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DistrictDTO)) {
            return false;
        }

        DistrictDTO districtDTO = (DistrictDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, districtDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DistrictDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", province=" + getProvince() +
            "}";
    }
}
