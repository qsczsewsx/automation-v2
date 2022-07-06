package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetIxuCashbackAndCommissionEntity {

  @Step("Get margin product info")
  public static List<HashMap<String, Object>> getMarginProductInfo(String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " select 'Margin' as product, 'Dynamic' as feeName,g.CAMPAIGN_ID as CampaignID,CAST(ISSUE_DATE as date) as issueDate, sum(Preferential_Interest) as totalValue, sum(g.Point) as totalXu , sum(g.Point)/sum(Preferential_Interest) as rate ");
    queryBuilder.append(" from stg_ixu_general_transaction g left join Stg_ixu_DYNAMIC_MARGIN_TRANSACTION m on g.REFERENCE_ID = m.ID where g.CAMPAIGN_ID = 45 ");
    queryBuilder.append(" and STATUS = 'SUCCESS' and CAST(ISSUE_DATE as date) between :from and :to group by Issue_Date, CAMPAIGN_ID ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get stock product info")
  public static List<HashMap<String, Object>> getStockProductInfo() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select 'Stock' as product, c.CampaignName as feeName,CampaignID, CAST(g.Issue_Date as date) as issueDate, sum(s.AMOUNT) as totalValue, sum(g.Point) as totalXu,  sum(g.Point)/ sum(s.AMOUNT) as rate ");
    queryBuilder.append(" from Stg_ixu_general_transaction g left join Stg_IXU_RB_CAMPAIGN c on g.CAMPAIGN_ID = c.campaignID left join stg_ixu_STOCK_TRANSACTION s on g.REFERENCE_ID = s.ID ");
    queryBuilder.append(" where (c.category = 'STOCK' or g.CAMPAIGN_ID = 55) and g.AWARD_TYPE = 'Redeemable' AND g.STATUS IN ('PENDING', 'SUCCESS') and action in ('Credit', 'Debit') ");
    queryBuilder.append(" and g.point > 0 group by CampaignName, g.Issue_Date, CampaignID ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
