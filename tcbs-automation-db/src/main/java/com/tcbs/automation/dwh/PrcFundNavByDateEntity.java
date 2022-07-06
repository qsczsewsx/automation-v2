package com.tcbs.automation.dwh;

import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.time.DateUtils;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrcFundNavByDateEntity {
  @Id
  private Date matchedDate;
  private String product;
  private Double navCurrent;
  private Double total;

  @SneakyThrows
  @Step("Get Fund NAV data")
  public List<PrcFundNavByDateEntity> byCondition(String product, String month, String year, String fromDate, String toDate) {
    // validation
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date inputFromDate;
    Date inputToDate = new Date();
    if (!month.equals("")) {
      inputFromDate = sdf.parse(sdf.format(DateUtils.addMonths(new Date(), -Integer.valueOf(month))));
    } else if (month.equals("") && !year.equals("")) {
      inputFromDate = sdf.parse(sdf.format(DateUtils.addYears(new Date(), -Integer.valueOf(year))));
    } else {
      inputFromDate = sdf.parse(fromDate);
      inputToDate = sdf.parse(toDate);
    }
    // process
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT CAST(nv.[Date] as date) as matchedDate, TotalNAV as total, NAVperCCQ as navCurrent , Product as fundCode ");
    queryStringBuilder.append("FROM Prc_Fund_NAV_byDate nv   ");
    queryStringBuilder.append("LEFT JOIN Dtm_dwh_Date_Dim dd on nv.[Date] = dd.[Date]    ");
    queryStringBuilder.append("LEFT JOIN INV_HOLIDAY ih on nv.[Date] = ih.[timestamp]    ");
    queryStringBuilder.append("WHERE Product=:product ");
    queryStringBuilder.append("AND CAST(nv.[Date] as date) between :fromDate and :toDate ");
    queryStringBuilder.append("and dd.[DayOfWeek] not in (6,7)    ");
    queryStringBuilder.append("and ih.[timestamp] is null    ");
    queryStringBuilder.append("order by nv.[date] asc    ");

    List<PrcFundNavByDateEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("product", product)
        .setParameter("fromDate", inputFromDate)
        .setParameter("toDate", inputToDate)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            PrcFundNavByDateEntity info = PrcFundNavByDateEntity.builder()
              .matchedDate((Date) object[0])
              .total((Double) object[1])
              .navCurrent((Double) object[2])
              .product((String) object[3])
              .build();
            listResult.add(info);
          }
        );
        return listResult;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
