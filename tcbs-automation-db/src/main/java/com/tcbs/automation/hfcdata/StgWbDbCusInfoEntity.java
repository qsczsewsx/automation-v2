package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StgWbDbCusInfoEntity {
  @Step("Get wb info")
  public static List<HashMap<String, Object>> getWBInfo(String id) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT CUSNAME, CUSGROUP , CUSGROUP_1, SECTOR, SECTOR_EN , SEGMENT , RM , stc, mst FROM ( ");
    queryStringBuilder.append(" 	select CUSNAME, CUSGROUP , CUSGROUP_1,  ");
    queryStringBuilder.append("     SECTOR, SECTOR_EN , SEGMENT , RM , CUSTAXLEGAL as stc, CUSREGISTRATION as mst, RPTDATE , ");
    queryStringBuilder.append("     row_number() over(partition by CusName order by RptDate desc) as rn ");
    queryStringBuilder.append("    from STG_WB_DB_CUS_INFO  ");
    queryStringBuilder.append("    WHERE RPTDATE = (select max(RPTDATE) from STG_WB_DB_CUS_INFO)  ");
    queryStringBuilder.append("    and ( CUSTAXLEGAL in ( :id )  ");
    queryStringBuilder.append("    or CUSREGISTRATION in ( :id ) )  ");
    queryStringBuilder.append("    order by RPTDATE desc  ");
    queryStringBuilder.append(" )t1 ");
    queryStringBuilder.append(" WHERE rn = 1 ");
    queryStringBuilder.append(" GROUP BY CUSNAME, CUSGROUP , CUSGROUP_1, SECTOR, SECTOR_EN , SEGMENT , RM , stc, mst ");
    queryStringBuilder.append(" ORDER BY CUSNAME, CUSGROUP , CUSGROUP_1, SECTOR, SECTOR_EN , SEGMENT , RM , stc, mst ASC  ");

    try {
      return HfcData.hfcDataDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("id", Arrays.asList(StringUtils.split(id, ",")))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get ref list")
  public static List<HashMap<String, Object>> getWBAllRefList() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select distinct CusGroup as cusGroup , CusGroup_1 as cusGroup1, Segment as segment, Sector_EN as sectorEn ");
    queryStringBuilder.append("from STG_WB_DB_CUS_INFO ");

    try {
      return HfcData.hfcDataDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
