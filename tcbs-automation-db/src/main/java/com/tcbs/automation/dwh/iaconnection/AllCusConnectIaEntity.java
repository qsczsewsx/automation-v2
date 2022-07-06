package com.tcbs.automation.dwh.iaconnection;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllCusConnectIaEntity {
  public static List<HashMap<String, Object>> getByCondition() {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  select * from (  ");
    queryStringBuilder.append("  Select Cf.custodyCd , af.bankAcctNo  ");
    queryStringBuilder.append("  From stg_flx_cfmast cf, stg_flx_afmast af ");
    queryStringBuilder.append("  where cf.custid = af.custid ");
    queryStringBuilder.append("  and cf.status <>'C' and af.alternateacct = 'Y'  ");
    queryStringBuilder.append("  group by Cf.custodycd , af.bankAcctNo) a ");
    queryStringBuilder.append("  order by custodyCd ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
