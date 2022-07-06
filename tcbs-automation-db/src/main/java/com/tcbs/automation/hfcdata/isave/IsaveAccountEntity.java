package com.tcbs.automation.hfcdata.isave;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IsaveAccountEntity {
  @Step("Get list customer isave with tip money")
  public static List<HashMap<String, Object>> getListCustomer(String date, int page, int size) {
    int voffRows = page * size;
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select " +
      " tcbs_id as tcbsId" +
      " , u.fullname as fullName" +
      " , custodycd as custodyCd" +
      " , a.account_no as accNo" +
      " , a.status as status" +
      " , a.created_at as createdAt" +
      " from staging.stg_isave_account a" +
      " left join dwh.smy_dwh_cas_alluserview u " +
      " on a.tcbs_id = u.tcbsid" +
      " where to_char(a.created_at, 'YYYY-MM-DD') = :p_date" +
      " order by tcbs_id asc" +
      " limit :p_size offset :v_offRows");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_date", date)
        .setParameter("p_size", size)
        .setParameter("v_offRows", voffRows)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
