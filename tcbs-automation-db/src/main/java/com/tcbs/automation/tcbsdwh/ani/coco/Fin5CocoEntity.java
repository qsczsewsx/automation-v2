package com.tcbs.automation.tcbsdwh.ani.coco;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fin5CocoEntity {

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getCocoTraderFromSql(String tcbsid) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select * from (");
    queryBuilder.append(" select ");
    queryBuilder.append(" T3.TCBSID ");
    queryBuilder.append(" ,t1.*");
    queryBuilder.append(" ,t2.tcbsid as trader_tcbsid");
    queryBuilder.append(" ,row_number () over (partition by trader order by distance ) as rownum");
    queryBuilder.append(" from(select");
    queryBuilder.append(" recommendations");
    queryBuilder.append(" ,distance");
    queryBuilder.append(" ,TRADER");
    queryBuilder.append(" from dwh.trader_recommend )T1");
    queryBuilder.append(" left join (select custodycd,tcbsid from dwh.smy_dwh_cas_alluserview) t2");
    queryBuilder.append(" on t1.trader = t2.custodycd ");
    queryBuilder.append(" left join (select custodycd,tcbsid from dwh.smy_dwh_cas_alluserview) t3");
    queryBuilder.append(" on t1.recommendations  = t3.custodycd ");
    queryBuilder.append(" where 1=1");
    queryBuilder.append(" and t3.TCBSID is not null");
    queryBuilder.append(" and t2.tcbsid = :tcbsid ");
    queryBuilder.append(" )where rownum <=20");
    queryBuilder.append(" order by trader,rownum ");


    try {

      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsid", tcbsid)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
