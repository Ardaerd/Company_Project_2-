package org.ozyegin.cs.repository;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * This is a common class for database container creation
 * in order to reuse the same database container in multiple tests
 */
public class HomeworkPostgresqlContainer extends PostgreSQLContainer<HomeworkPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres";
    private static HomeworkPostgresqlContainer container;

    private HomeworkPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static synchronized HomeworkPostgresqlContainer getInstance() {
        if (container == null) {
            container = new HomeworkPostgresqlContainer().withInitScript("db-schema.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
