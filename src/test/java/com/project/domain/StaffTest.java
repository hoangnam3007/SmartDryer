package com.project.domain;

import static com.project.domain.OrderTestSamples.*;
import static com.project.domain.StaffTestSamples.*;
import static com.project.domain.StaffTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StaffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Staff.class);
        Staff staff1 = getStaffSample1();
        Staff staff2 = new Staff();
        assertThat(staff1).isNotEqualTo(staff2);

        staff2.setId(staff1.getId());
        assertThat(staff1).isEqualTo(staff2);

        staff2 = getStaffSample2();
        assertThat(staff1).isNotEqualTo(staff2);
    }

    @Test
    void orderTest() {
        Staff staff = getStaffRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        staff.addOrder(orderBack);
        assertThat(staff.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getStaff()).isEqualTo(staff);

        staff.removeOrder(orderBack);
        assertThat(staff.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getStaff()).isNull();

        staff.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(staff.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getStaff()).isEqualTo(staff);

        staff.setOrders(new HashSet<>());
        assertThat(staff.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getStaff()).isNull();
    }

    @Test
    void staffLeadTest() {
        Staff staff = getStaffRandomSampleGenerator();
        Staff staffBack = getStaffRandomSampleGenerator();

        staff.setStaffLead(staffBack);
        assertThat(staff.getStaffLead()).isEqualTo(staffBack);

        staff.staffLead(null);
        assertThat(staff.getStaffLead()).isNull();
    }
}
