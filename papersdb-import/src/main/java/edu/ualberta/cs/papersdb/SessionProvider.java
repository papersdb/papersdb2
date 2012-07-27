package edu.ualberta.cs.papersdb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class SessionProvider {
    public enum Mode {
        DEBUG,
        RUN;
    }

    private final SessionFactory sessionFactory;
    private final ServiceRegistry serviceRegistry;

    public SessionProvider(Mode mode) {
        // configure() configures settings from hibernate.cfg.xml found in the
        // resources directory
        Configuration configuration = new Configuration().configure();

        if (mode == Mode.RUN) {
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.use_sql_comments", "true");
        }
        serviceRegistry = new ServiceRegistryBuilder().applySettings(
            configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session openSession() {
        return sessionFactory.openSession();
    }
}
