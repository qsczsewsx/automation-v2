package com.tcbs.automation.portfolio;

import com.tcbs.automation.HibernateEdition;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class CleanData {
  public static void byProductCode(String underlyingCode, HibernateEdition portfolioSit) throws Exception {
    Session session = portfolioSit.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query1 = session.createNativeQuery(String.format("DELETE \n" +
      "FROM INV_POR_ACCOUNTING_ITEM_ATTR\n" +
      "WHERE ACCOUNTING_ITEM_ID IN (\n" +
      "    SELECT ai.ID\n" +
      "    FROM INV_POR_ACCOUNTING_ITEM ai\n" +
      "    INNER JOIN INV_POR_PRODUCT_ITEM prod ON prod.ID = ai.PRODUCT_ITEM_ID\n" +
      "    WHERE prod.UNDERLYING_CODE = '%s' )\n" +
      "    ", underlyingCode));
    int res1 = query1.executeUpdate();

    Query<?> query2 = session.createNativeQuery(
      String.format("DELETE FROM \n" +
        " INV_POR_ACCOUNTING_ITEM\n" +
        "WHERE PRODUCT_ITEM_ID IN (\n" +
        "    SELECT ID \n" +
        "    FROM INV_POR_PRODUCT_ITEM\n" +
        "    WHERE UNDERLYING_CODE = '%s')\n", underlyingCode));
    int res2 = query2.executeUpdate();

    Query<?> query4 = session.createNativeQuery(
      String.format("DELETE FROM \n" +
        "INV_POR_RETAIL\n" +
        "WHERE PRODUCT_ITEM_ID IN (\n" +
        "    SELECT ID \n" +
        "    FROM INV_POR_PRODUCT_ITEM\n" +
        "    WHERE UNDERLYING_CODE = '%s'\n )", underlyingCode));
    int res4 = query4.executeUpdate();

    Query<?> query3 = session.createNativeQuery(
      String.format("DELETE \n" +
        "FROM INV_POR_PRODUCT_ITEM\n" +
        "WHERE UNDERLYING_CODE = '%s'\n", underlyingCode));
    int res3 = query3.executeUpdate();
    trans.commit();
  }
}
