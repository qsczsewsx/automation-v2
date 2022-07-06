package com.tcbs.automation.cas;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class CAS {
  public static HibernateEdition casConnection = Database.CAS.getConnection();

  public static Query createNativeQuery(String sql) {
    return casConnection.getSession().createNativeQuery(sql).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
  }

}
