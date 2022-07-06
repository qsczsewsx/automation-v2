package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayEntity {
  private String timeStamp;
  private Integer active;
  private String name;

  public static List<HolidayEntity> getListHoliday(String toDate) {
    String query = "SELECT convert(varchar, [TIMESTAMP], 23) AS timestamp,\n" +
      "ACTIVE AS active,\n" +
      "NAME AS name\n" +
      "FROM INV_HOLIDAY ih \n" +
      "WHERE [TIMESTAMP] <= :toDate\n" +
      "ORDER BY [TIMESTAMP] ASC";

    try {
      List<Map<String, Object>> listHoliday = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return buildObj(listHoliday);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<HolidayEntity> buildObj(List<Map<String, Object>> results) {
    List<HolidayEntity> newResults = new ArrayList<>();
    for (int i = 0; i < results.size(); ++i) {
      HolidayEntity holidayEntity = HolidayEntity.builder()
        .timeStamp((String) results.get(i).get("timestamp"))
        .active((Integer) results.get(i).get("active"))
        .name((String) results.get(i).get("name"))
        .build();
      newResults.add(holidayEntity);
    }
    return newResults;
  }

  public static Map<String, Object> getBusinessDaysAdd(Integer numberDays, String fromDate) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT CAST(dbo.businessDaysAdd(:numberDays, :fromDate) AS Date) AS Date \n");
    try {
      List<Map<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("numberDays", numberDays)
        .setParameter("fromDate", fromDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    Stoxplus.stoxDbConnection.closeSession();

    return null;
  }
}
