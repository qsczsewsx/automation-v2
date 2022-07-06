package com.tcbs.automation.hfcdata.msvip;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetStatementAccountInfoEntity {
  @Step("Get statement account info ms vip from db")
  public static List<HashMap<String, Object>> getStatementAccountInfo(String custody, String afAcctNo, String from, String to) {
    final String custodyCd = "custodycd";
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select sb.txdate, sb.custodycd, sb.afacctno , sb.txdesc, sb.ticker ");
    queryBuilder.append(", sb.se_credit_amt, sb.se_debit_amt, sb.ci_credit_amt, sb.ci_debit_amt, sb.ci_end_amt, c.customername, c.idnumber, TO_CHAR(c.iddate, 'YYYY-MM-DD') as  iddate ");
    queryBuilder.append(", c.idplace, c.phone, c.address, cb.ci_begin_amt, cb.ci_total_credit_amt, cb.ci_total_debit_amt, cb.ci_end_amt as ci_end_period_amt ");
    queryBuilder.append(", cb.cash_avail, cb.CI_EMKAMT_BAL, cb.OD_BUY_SECU, cb.TMTRACHAM, cb.CUR_HOLDBALANCE, cb.CI_BFBALANCE_BAL, cb.CI_BOND_BAL ");
    queryBuilder.append(", cb.OD_SECURE_PO, cb.CI_BOND_BAL_AVAIL, cb.CI_FUND_BAL from api.iwealth_monthlyport_cf1000 sb ");
    queryBuilder.append("left join api.iwealth_monthlyport_customer c on sb.custodycd = c.custodycd ");
    queryBuilder.append(" left join api.iwealth_monthlyport_cashbal cb on sb.afacctno = cb.afacctno ");
    queryBuilder.append(" WHERE sb.custodycd = :custody AND sb.afAcctNo = :afAcctNo AND txdate BETWEEN :from AND :to ");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setParameter("afAcctNo", afAcctNo)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
