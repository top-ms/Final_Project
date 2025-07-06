package com.epam.rd.autocode.assessment.appliances.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceImplStructureTest {

    private static List<Method> allMethods;
    private static List<Constructor<?>> allConstructors;
    private static List<Field> allFields;

    @BeforeAll
    static void setup() throws ClassNotFoundException {
        final Class<?> clazz = AdminServiceImpl.class;
        allMethods = Arrays.asList(clazz.getDeclaredMethods());
        allConstructors = Arrays.asList(clazz.getConstructors());
        allFields = Arrays.asList(clazz.getDeclaredFields());
    }

    @Test
    @DisplayName("Check method count")
    void checkMethodCount() {
        assertTrue(allMethods.size() >= 5); // мінімально очікувана кількість методів
    }

    @Test
    @DisplayName("Check constructor count and visibility")
    void checkConstructors() {
        assertEquals(1, allConstructors.size());
        assertTrue(Modifier.isPublic(allConstructors.get(0).getModifiers()));
    }

    @Test
    @DisplayName("Check required method names")
    void checkMethodNames() {
        List<String> expected = List.of(
                "getAllAdminsAsDto",
                "getAdminByEmail",
                "deleteAdminById",
                "register",
                "existsByEmail",
                "findByIdForEdit",
                "updateAdmin",
                "findById"
        );

        for (String name : expected) {
            boolean exists = allMethods.stream().anyMatch(m -> m.getName().equals(name));
            assertTrue(exists, "Method " + name + " should exist");
        }
    }

    @Test
    @DisplayName("Fields must be private")
    void checkFieldModifiers() {
        for (Field field : allFields) {
            assertTrue(Modifier.isPrivate(field.getModifiers()));
        }
    }

    @Test
    @DisplayName("Check expected field names and types")
    void checkFieldNamesAndTypes() {
        assertTrue(allFields.stream().anyMatch(f -> f.getName().equals("adminRepository") && f.getType().getSimpleName().equals("AdminRepository")));
        assertTrue(allFields.stream().anyMatch(f -> f.getName().equals("modelMapper") && f.getType().getSimpleName().equals("ModelMapper")));
        assertTrue(allFields.stream().anyMatch(f -> f.getName().equals("passwordEncoder") && f.getType().getSimpleName().equals("BCryptPasswordEncoder")));
    }
}
