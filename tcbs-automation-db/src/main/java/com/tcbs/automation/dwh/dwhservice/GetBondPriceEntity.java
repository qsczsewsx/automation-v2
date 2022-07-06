package com.tcbs.automation.dwh.dwhservice;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBondPriceEntity {
  @Step("Get data by key")
  public static List<HashMap<String, Object>> getBondPrice(List<String> bondCodes) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select bs.CODE, bs.ISSUE_DATE as PublicDate, bs.PAR as Par_Value,  ");
    queryBuilder.append(" ROW_NUMBER() over (partition by bs.BOND_STATIC_ID order by Coupon_Fix_Date) as rn,  ");
    queryBuilder.append(" bc.* ");
    queryBuilder.append(" from Stg_tci_INV_BOND_COUPON bc ");
    queryBuilder.append(" left join Stg_tci_INV_BOND_STATIC bs on bc.Bond_static_id = bs.BOND_STATIC_ID ");
    queryBuilder.append(" where bs.CODE in :bondCodes   ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("bondCodes", bondCodes)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
