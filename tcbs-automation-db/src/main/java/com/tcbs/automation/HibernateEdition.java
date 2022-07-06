package com.tcbs.automation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateEdition implements AutoCloseable {

  private SessionFactory sessionFactory;
  private Session session;
  private Configuration configuration;
  private String fileConfig;

  public HibernateEdition(String fileConfig) {
    this.fileConfig = fileConfig;
    sessionFactory = buildSessionFactory(fileConfig);
  }

  public HibernateEdition(String fileConfig, String username, String password, String url) {
    this.configuration = new Configuration();
    this.configuration.setProperty("hibernate.connection.url", url);
    this.configuration.setProperty("hibernate.connection.username", username);
    this.configuration.setProperty("hibernate.connection.password", password);
    this.configuration.configure(fileConfig);
    sessionFactory = buildSessionFactory(this.configuration);
  }

  public HibernateEdition(Configuration configuration) {
    this.configuration = configuration;
    sessionFactory = buildSessionFactory(configuration);
  }

  private SessionFactory buildSessionFactory(String fileConfig) {
    if (fileConfig != null && !fileConfig.equals("")) {
      ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().configure(fileConfig).build();
      Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
      return metadata.getSessionFactoryBuilder().build();
    }
    return null;
  }

  private SessionFactory buildSessionFactory(Configuration configuration) {
    try {
      if (configuration != null) {
        return configuration.buildSessionFactory();
      }
    } catch (Throwable ex) {
      throw new ExceptionInInitializerError(ex);
    }
    return null;
  }

  public void shutdown() {
    // Close caches and connection pools
    getSessionFactory().close();
  }

  public void openSession() {
    if (sessionFactory != null) {
      session = sessionFactory.openSession();
    }
  }

  public void closeSession() {
    if (session != null) {
      session.close();
      session = null;
    }
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public Session getSession() {
    if (session == null) {
      openSession();
    }
    return session;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public String getFileConfig() {
    return fileConfig;
  }

  public void setFileConfig(String fileConfig) {
    this.fileConfig = fileConfig;
  }

  @Override
  public void close() throws Exception {
    closeSession();
  }
}