package com.project.domain;

import static com.project.domain.CusNoteTestSamples.*;
import static com.project.domain.CustomerEquipmentTestSamples.*;
import static com.project.domain.CustomerTestSamples.*;
import static com.project.domain.DistrictTestSamples.*;
import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.ProvinceTestSamples.*;
import static com.project.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void customerEquipmentTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        CustomerEquipment customerEquipmentBack = getCustomerEquipmentRandomSampleGenerator();

        customer.addCustomerEquipment(customerEquipmentBack);
        assertThat(customer.getCustomerEquipments()).containsOnly(customerEquipmentBack);
        assertThat(customerEquipmentBack.getCustomer()).isEqualTo(customer);

        customer.removeCustomerEquipment(customerEquipmentBack);
        assertThat(customer.getCustomerEquipments()).doesNotContain(customerEquipmentBack);
        assertThat(customerEquipmentBack.getCustomer()).isNull();

        customer.customerEquipments(new HashSet<>(Set.of(customerEquipmentBack)));
        assertThat(customer.getCustomerEquipments()).containsOnly(customerEquipmentBack);
        assertThat(customerEquipmentBack.getCustomer()).isEqualTo(customer);

        customer.setCustomerEquipments(new HashSet<>());
        assertThat(customer.getCustomerEquipments()).doesNotContain(customerEquipmentBack);
        assertThat(customerEquipmentBack.getCustomer()).isNull();
    }

    @Test
    void orderTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        customer.addOrder(orderBack);
        assertThat(customer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.removeOrder(orderBack);
        assertThat(customer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();

        customer.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(customer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.setOrders(new HashSet<>());
        assertThat(customer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();
    }

    @Test
    void cusNoteTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        CusNote cusNoteBack = getCusNoteRandomSampleGenerator();

        customer.addCusNote(cusNoteBack);
        assertThat(customer.getCusNotes()).containsOnly(cusNoteBack);
        assertThat(cusNoteBack.getCustomer()).isEqualTo(customer);

        customer.removeCusNote(cusNoteBack);
        assertThat(customer.getCusNotes()).doesNotContain(cusNoteBack);
        assertThat(cusNoteBack.getCustomer()).isNull();

        customer.cusNotes(new HashSet<>(Set.of(cusNoteBack)));
        assertThat(customer.getCusNotes()).containsOnly(cusNoteBack);
        assertThat(cusNoteBack.getCustomer()).isEqualTo(customer);

        customer.setCusNotes(new HashSet<>());
        assertThat(customer.getCusNotes()).doesNotContain(cusNoteBack);
        assertThat(cusNoteBack.getCustomer()).isNull();
    }

    @Test
    void provinceTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Province provinceBack = getProvinceRandomSampleGenerator();

        customer.setProvince(provinceBack);
        assertThat(customer.getProvince()).isEqualTo(provinceBack);

        customer.province(null);
        assertThat(customer.getProvince()).isNull();
    }

    @Test
    void districtTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        customer.setDistrict(districtBack);
        assertThat(customer.getDistrict()).isEqualTo(districtBack);

        customer.district(null);
        assertThat(customer.getDistrict()).isNull();
    }

    @Test
    void wardTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Ward wardBack = getWardRandomSampleGenerator();

        customer.setWard(wardBack);
        assertThat(customer.getWard()).isEqualTo(wardBack);

        customer.ward(null);
        assertThat(customer.getWard()).isNull();
    }
}
