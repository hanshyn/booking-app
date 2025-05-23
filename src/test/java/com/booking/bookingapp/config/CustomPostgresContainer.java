package com.booking.bookingapp.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgresContainer extends PostgreSQLContainer<CustomPostgresContainer> {
    private static final String DB_IMAGE_NAME = "postgres:latest";

    private static CustomPostgresContainer container;

    private CustomPostgresContainer() {
        super(DB_IMAGE_NAME);
    }

    public static synchronized CustomPostgresContainer getInstance() {
        if (container == null) {
            container = new CustomPostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", container.getJdbcUrl());
        System.setProperty("TEST_DB_USER", container.getUsername());
        System.setProperty("TEST_DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }
}
