package com.tcbs.automation.dwh;


import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class TopRMEntity {

  @Step("Get top RM from DWH")
  public static List<HashMap<String, Object>> byCondition(String periodType, String productType,
                                                          String region, String agencyCode, String zone) {

    String bondFactor;
    String fundFactor;
    if (productType.equals("Fund")) {
      bondFactor = "0";
      fundFactor = "1";
    } else if (productType.equals("Bond")) {
      bondFactor = "1";
      fundFactor = "0";
    } else {
      bondFactor = "1";
      fundFactor = "1";
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    List<HashMap<String, Object>> resultList;

    if (periodType.equals("DTD")) {
      queryStringBuilder.append("select Top 10 RmName, ROUND(Bond_BuyAmt / 1000000000.0, 3) AS Bond_BuyAmt, ROUND(Fund_BuyAmt / 1000000000.0, 3) AS Fund_BuyAmt  ");
      queryStringBuilder.append("from ( select RmCustodyCode, RmName, sum([bondBuyAmt]) as Bond_BuyAmt, sum([fundBuyAmt]) as Fund_BuyAmt,   ");
      queryStringBuilder.append("     sum([bondBuyAmt]*:bondFactor + [fundBuyAmt]*:fundFactor) as Total_BuyAmt   ");
      queryStringBuilder.append("     from Prc_Rm_Performance_byDate   ");
      queryStringBuilder.append("     where :periodType = 'DTD'   ");
      queryStringBuilder.append("     and EtlCurDate = (select max(EtlCurDate) from Prc_Rm_Performance_byDate)   ");
      queryStringBuilder.append("     and toDate = cast(getdate() - 1 as date)   ");
      queryStringBuilder.append("     and (:region = 'All' and 1=1   ");
      queryStringBuilder.append("     or (:region = 'Zone' and Zone = :zone)   ");
      queryStringBuilder.append("     or (:region = 'Agency' and AgencyCode = :agencyCode))   ");
      queryStringBuilder.append("     group by RmCustodyCode, RmName) a  ");
      queryStringBuilder.append("order by a.Total_BuyAmt desc   ");
    } else {
      queryStringBuilder.append("select Top 10 RmName, ROUND(Bond_BuyAmt / 1000000000.0, 3) AS Bond_BuyAmt, ROUND(Fund_BuyAmt / 1000000000.0, 3) AS Fund_BuyAmt  ");
      queryStringBuilder.append("from (select RmCustodyCode, RmName, sum([bondBuyAmt]) as Bond_BuyAmt , sum([fundBuyAmt]) as Fund_BuyAmt,   ");
      queryStringBuilder.append("     sum([bondBuyAmt]*:bondFactor + [fundBuyAmt]*:fundFactor) as Total_BuyAmt   ");
      queryStringBuilder.append("     from Prc_Rm_Performance_UpToDate   ");
      queryStringBuilder.append("     where EtlCurDate = (select max(EtlCurDate) from Prc_Rm_Performance_UpToDate)   ");
      queryStringBuilder.append("     and PeriodType = :periodType   ");
      queryStringBuilder.append("     and (:region = 'All' and 1=1   ");
      queryStringBuilder.append("     or (:region = 'Zone' and Zone = :zone)   ");
      queryStringBuilder.append("     or (:region = 'Agency' and AgencyCode = :agencyCode))   ");
      queryStringBuilder.append("     group by RmCustodyCode, RmName ) a  ");
      queryStringBuilder.append("order by a.Total_BuyAmt desc   ");
    }

    resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("bondFactor", bondFactor)
      .setParameter("fundFactor", fundFactor)
      .setParameter("periodType", periodType)
      .setParameter("zone", zone)
      .setParameter("agencyCode", agencyCode)
      .setParameter("region", region)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    Dwh.dwhDbConnection.closeSession();
    return resultList;
  }
}
