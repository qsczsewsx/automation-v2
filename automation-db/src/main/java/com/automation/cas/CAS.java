package com.automation.cas;

import com.automation.Database;
import com.automation.HibernateEdition;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class CAS {
  public static HibernateEdition casConnection = Database.CAS.getConnection();

  public static Query createNativeQuery(String sql) {
    return casConnection.getSession().createNativeQuery(sql).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
  }

}
