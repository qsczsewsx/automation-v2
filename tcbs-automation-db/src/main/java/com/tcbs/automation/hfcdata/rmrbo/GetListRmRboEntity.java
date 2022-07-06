package com.tcbs.automation.hfcdata.rmrbo;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetListRmRboEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getListRmRbo(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select Cus_TCBSID, cus_custodycode as Cus_CustodyCD ");
    queryBuilder.append(" , case when aum_vip is not null then 'VIP' else 'NonVIP' end as Viptype ");
    queryBuilder.append(" , case when RmType in ('Identified','BB') then 'RM' else RmType end as Identity_Type ");
    queryBuilder.append(" , RM_CustodyCode as RM_CustodyCD ");
    queryBuilder.append(" , etlrundatetime  as UpdateDate ");
    queryBuilder.append(" from dwh.Smy_dwh_cas_ParentUser ");
    queryBuilder.append(" where ETLCurDate = (select max(ETLCurDate) from dwh.Smy_dwh_cas_ParentUser) ");
    queryBuilder.append(" and  RmType in ('Identified', 'RBO', 'BB'); ");

    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
