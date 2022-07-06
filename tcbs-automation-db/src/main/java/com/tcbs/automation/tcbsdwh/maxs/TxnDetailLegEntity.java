package com.tcbs.automation.tcbsdwh.maxs;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TxnDetailLegEntity {
  @Step("Get data from db")
  public static List<HashMap<String, Object>> getData(List<String> custody, List<String> cptyCustody,
                                                      List<String> bondCode, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select * ");
    queryBuilder.append("   from (	select w.changeid ");
    queryBuilder.append("     , w.ID as warehouseID ");
    queryBuilder.append("     , leg1_instance_id, leg2_instance_id, product_code, w.process_status ");
    queryBuilder.append("     , '0001011809' as custcbsid, '105P011809' as cuscustodycd ");
    queryBuilder.append("     , l2.COUNTER_PARTY_ACCOUNT_ID as cptycustodycd ");
    queryBuilder.append("     , c.TCBSID as cptytcbsid ");
    queryBuilder.append("     , case when w.process_status = 'DONE' then l2.dirty_price else l1.dirty_price end as leg2_dirty_price ");
    queryBuilder.append("     , cast(w.leg2_deal_date as date) as leg2_deal_date ");
    queryBuilder.append("     , w.leg2_expected_volume ");
    queryBuilder.append("   , w.leg2_actual_volume ");
    queryBuilder.append("   , w.leg2_actual_volume * (case when w.process_status = 'done' then l2.dirty_price else l1.dirty_price end) as actual_dirty_value ");
    queryBuilder.append("     , w.leg2_actual_volume * b.price as actual_par_value ");
    queryBuilder.append("     , w.leg2_outright_volume ");
    queryBuilder.append("   , w.leg2_outright_volume * l1.dirty_price as outright_dirty_value ");
    queryBuilder.append("     , w.leg2_outright_volume * b.price as outright_par_value ");
    queryBuilder.append("     , case when w.leg2_outright_volume is null then null else cast(w.updated_at as date) end as date_outright ");
    queryBuilder.append(" , case  when type = 'SELL' and w.process_status not in ('CANCELED','CANCELLED') then 'R' ");
    queryBuilder.append(" when type = 'BUY' and w.process_status not in ('CANCELED','CANCELLED') then 'RR' ");
    queryBuilder.append(" when type in ('SELL','BUY') and w.process_status is null then '3rd' ");
    queryBuilder.append(" end as warehousetype ");
    queryBuilder.append(" from staging.Stg_venus_TBL_WAREHOUSE_Changed w ");
    queryBuilder.append(" left join staging.Stg_venus_TBL_TCS_TBT l1 on w.leg1_instance_id = l1.instance_id ");
    queryBuilder.append(" left join staging.Stg_venus_TBL_TCS_TBT l2 on w.leg2_instance_id = l2.instance_id ");
    queryBuilder.append(
      " left join (select TCBSID, CUSTODYCD  from dwh.Smy_dwh_cas_AllUserView where etlcurdate = (select max(etlcurdate) from dwh.Smy_dwh_cas_AllUserView)) c on l2.COUNTER_PARTY_ACCOUNT_ID = c.CUSTODYCD ");
    queryBuilder.append(" left join staging.Stg_tcb_Bond b on w.PRODUCT_CODE = b.Code ");
    queryBuilder.append(" where w.PRODUCT_TYPE = 'CORP_BOND') leg ");
    if (!bondCode.get(0).equalsIgnoreCase("")) {
      queryBuilder.append(" where product_code in :bondCode ");
    } else {
      queryBuilder.append(" where :bondCode is not null  ");
    }
    if (!custody.get(0).equalsIgnoreCase("")) {
      queryBuilder.append(" and cuscustodycd in :custody ");
    } else {
      queryBuilder.append(" and :custody is not null  ");
    }
    if (!cptyCustody.get(0).equalsIgnoreCase("")) {
      queryBuilder.append(" and cptycustodycd in :cptyCustody ");
    } else {
      queryBuilder.append(" and :cptyCustody is not null  ");
    }

    queryBuilder.append(" and leg2_deal_date >= :fromDate and leg2_deal_date <= :toDate ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("bondCode", bondCode)
        .setParameter("custody", custody)
        .setParameter("cptyCustody", cptyCustody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
