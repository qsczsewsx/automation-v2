package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CwInfoEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getCwInfo(String lang) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT cw.CW_SYMBOL , cw.CK_COSO , cw.THOIHAN , cw.NGAYPHATHANH , cw.NGAYNIEMYET , ");
    queryBuilder.append("        cw.NGAYDAOHAN , cw.GIAPHATHANH , cw.KL_NIEMYET , ci.NAMEENLV2, ci.NAMELV2, TOCHUCPHATHANH_CW ");
    queryBuilder.append(" FROM ");

    if (lang.equalsIgnoreCase("vi")) {
      queryBuilder.append(" VIETSTOCK_CW_INFO cw  ");
    } else if (lang.equalsIgnoreCase("en")) {
      queryBuilder.append(" VIETSTOCK_CW_INFO_ENG cw  ");
    }

    queryBuilder.append(" LEFT JOIN SMY_DWH_STOX_COMPANYINDUSTRYRATE ci ON cw.CK_COSO = ci.TICKER ");
    queryBuilder.append(" WHERE cw.NGAYPHATHANH IS NOT NULL ");
    queryBuilder.append(" AND TO_CHAR(cw.NGAYDAOHAN, 'YYYY-MM-DD') >= TO_CHAR(SYSDATE, 'YYYY-MM-DD') ");
    queryBuilder.append(" ORDER BY cw.CK_COSO asc ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
