package com.tcbs.automation.hfcdata.msvip;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDebitMarginInfoEntity {

  @Step("Get debit margin info ms vip from db")
  public static List<HashMap<String, Object>> getDebitMarginInfo(String custody, String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("Select custodycd, afacctno, fullname, mnemonic, TO_CHAR(rlsdate, 'YYYY-MM-DD') as rlsdate, TO_CHAR(duedate, 'YYYY-MM-DD') as duedate, lnschdid, rlsprin, paid, lnprin, intamt, feeintamt, feerate ");
    queryBuilder.append(", nvl(lnprin,0) + nvl(intamt,0) + nvl(feeintamt,0) as totalln ");
    queryBuilder.append(" from api.iwealth_monthlyport_mr0013 WHERE custodycd = :custody AND datereport BETWEEN :from AND :to ");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
