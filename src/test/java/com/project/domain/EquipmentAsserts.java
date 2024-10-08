package com.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EquipmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEquipmentAllPropertiesEquals(Equipment expected, Equipment actual) {
        assertEquipmentAutoGeneratedPropertiesEquals(expected, actual);
        assertEquipmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEquipmentAllUpdatablePropertiesEquals(Equipment expected, Equipment actual) {
        assertEquipmentUpdatableFieldsEquals(expected, actual);
        assertEquipmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEquipmentAutoGeneratedPropertiesEquals(Equipment expected, Equipment actual) {
        assertThat(expected)
            .as("Verify Equipment auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEquipmentUpdatableFieldsEquals(Equipment expected, Equipment actual) {
        assertThat(expected)
            .as("Verify Equipment relevant properties")
            .satisfies(e -> assertThat(e.getEquipmentCode()).as("check equipmentCode").isEqualTo(actual.getEquipmentCode()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEquipmentUpdatableRelationshipsEquals(Equipment expected, Equipment actual) {
        // empty method
    }
}
