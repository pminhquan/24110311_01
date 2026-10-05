package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaConfig {

    private static final EntityManagerFactory FACTORY;

    static {
        try {
            java.util.Map<String, Object> overrides = new java.util.HashMap<>();

            String dbUrl = System.getProperty("db.url");
            if (dbUrl == null || dbUrl.isBlank()) dbUrl = System.getenv("DB_URL");
            if (dbUrl != null && !dbUrl.isBlank()) overrides.put("jakarta.persistence.jdbc.url", dbUrl);

            String dbUser = System.getProperty("db.user");
            if (dbUser == null || dbUser.isBlank()) dbUser = System.getenv("DB_USER");
            if (dbUser != null && !dbUser.isBlank()) overrides.put("jakarta.persistence.jdbc.user", dbUser);

            String dbPassword = System.getProperty("db.password");
            if (dbPassword == null) dbPassword = System.getProperty("jakarta.persistence.jdbc.password");
            if (dbPassword == null) dbPassword = System.getenv("DB_PASSWORD");
            if (dbPassword != null) overrides.put("jakarta.persistence.jdbc.password", dbPassword);

            FACTORY = Persistence.createEntityManagerFactory("ExamDB", overrides);
        } catch (Throwable ex) {
            System.err.println("Initial EntityManagerFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private JpaConfig() {
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void shutdown() {
        if (FACTORY != null && FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}
