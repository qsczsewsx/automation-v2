package com.automation;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DataBaseUtils {
  public static Transaction beginTransaction(Session session) {
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    return session.getTransaction();
  }

  public static String containsLowerCase(String field) {
    return "%" + field.toLowerCase() + "%";
  }
}
