package com.tcbs.automation.tcbsdwh.stock;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllSecuritiesEntity {

  public static List<HashMap<String, Object>> getAllSecurities(String ticker, String lang) {
    StringBuilder query = new StringBuilder();

    query.append(" select ticker, outstandingshare, ");
    query.append(" CASE WHEN :lang = 'EN' THEN en_organname ELSE organname END AS issuerFullName ");
    query.append(" from staging.stg_tcs_stx_cpf_organization ");
    query.append(" where length(ticker) > 0 and status = 1 and etlcurdate = (select max(etlcurdate) from staging.stg_tcs_stx_cpf_organization) ");
    query.append(" and comgroupcode in ('UpcomIndex', 'VNINDEX', 'HNXIndex') ");
    query.append(" and OutstandingShare is not null");
    if (!ticker.equalsIgnoreCase("") && !ticker.equalsIgnoreCase("NULL")) {
      query.append(" and ticker = :ticker ");
    }
    query.append(" order by ticker asc ; ");

    try {
      if (!ticker.equalsIgnoreCase("") && !ticker.equalsIgnoreCase("NULL")) {
        return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(query.toString())
          .setParameter("lang", lang)
          .setParameter("ticker", ticker)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } else {
        return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(query.toString())
          .setParameter("lang", lang)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
