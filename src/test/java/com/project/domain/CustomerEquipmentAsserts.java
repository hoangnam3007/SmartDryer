package com.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerEquipmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerEquipmentAllPropertiesEquals(CustomerEquipment expected, CustomerEquipment actual) {
        assertCustomerEquipmentAutoGeneratedPropertiesEquals(expected, actual);
        assertCustomerEquipmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerEquipmentAllUpdatablePropertiesEquals(CustomerEquipment expected, CustomerEquipment actual) {
        assertCustomerEquipmentUpdatableFieldsEquals(expected, actual);
        assertCustomerEquipmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerEquipmentAutoGeneratedPropertiesEquals(CustomerEquipment expected, CustomerEquipment actual) {
        assertThat(expected)
            .as("Verify CustomerEquipment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerEquipmentUpdatableFieldsEquals(CustomerEquipment expected, CustomerEquipment actual) {
        assertThat(expected)
            .as("Verify CustomerEquipment relevant properties")
            .satisfies(e -> assertThat(e.getQuantily()).as("check quantily").isEqualTo(actual.getQuantily()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerEquipmentUpdatableRelationshipsEquals(CustomerEquipment expected, CustomerEquipment actual) {
        assertThat(expected)
            .as("Verify CustomerEquipment relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getEquipment()).as("check equipment").isEqualTo(actual.getEquipment()));
    }
}
