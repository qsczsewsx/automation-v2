package com.tcbs.automation.hfcdata.msvip;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBlockAgeInfoEntity {

  @Step("Get stockDetail Info from db")
  public static List<HashMap<String, Object>> getListStockDetail(String from, String to, String custody) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select custodycd, fullname, s.contractno , TO_CHAR(s.contractdate, 'DD/MM/YYYY') as contractdate, s.vendor, s.subno, s.subdate, s.symbol  ");
    queryBuilder.append(" , s.sectype, s.org_qtty, s.emkvsd, s.caemkvsd, s.emktcbs, s.caemkqtty, s.unemkvsd, s.uncavsd, s.unemktcbs, s.uncaqtty  ");
    queryBuilder.append(" , (s.emkvsd - s.unemkvsd) as slcl_vsd, (s.caemkvsd - s.uncavsd) as slcl_cavsd, (s.emktcbs - s.unemktcbs) as slcl_tcbs  ");
    queryBuilder.append(" , (s.caemkqtty - s.uncaqtty) as slcl_catcbs, s.asbalance, s.dealid, s.datereport  ");
    queryBuilder.append("  from api.iwealth_monthlyport_as0008 s ");
    queryBuilder.append("  where custodycd = :custody and datereport between :from  and :to ");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setParameter("custody", custody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get right detail Info from db")
  public static List<HashMap<String, Object>> getListRightDetail(String from, String to, String custody) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select custodycd, fullname, r.contractno, TO_CHAR(r.contractdate, 'DD/MM/YYYY') as contractdate, r.vendor, r.subno, r.subdate, r.symbol, r.caqtty  ");
    queryBuilder.append(" , r.rqtty, r.caamt, r.uqtty, r.urqtty, r.uamt, r.caqtty_remain, r.rqtty_remain, r.caamt_remain, r.description  ");
    queryBuilder.append("  , r.status, r.dealid, r.datereport ");
    queryBuilder.append("  from api.iwealth_monthlyport_as00081 r ");
    queryBuilder.append("  where custodycd = :custody and datereport between :from  and :to ");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from.concat(" 00:00:00"))
        .setParameter("to", to.concat(" 00:00:00"))
        .setParameter("custody", custody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
