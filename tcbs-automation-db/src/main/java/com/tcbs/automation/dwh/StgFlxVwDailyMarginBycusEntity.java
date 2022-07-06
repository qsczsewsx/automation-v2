package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Stg_flx_VW_DAILY_MARGIN_BYCUS")
public class StgFlxVwDailyMarginBycusEntity {
  @Id
  private String tcbsid;
  private String custodycd;
  private BigDecimal totalMargin;
  private Date reportDate;

  @Step("select data")
  public static List<StgFlxVwDailyMarginBycusEntity> getMarginInfo(String date) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM (   \r\n");
    queryStringBuilder.append("               SELECT c.tcbsid, m.*   \r\n");
    queryStringBuilder.append("               FROM (  \r\n");
    queryStringBuilder.append("                     SELECT custodycd, sum(intbal) totalMargin, to_date as reportDate \r\n");
    queryStringBuilder.append("                     FROM Stg_flx_VW_DAILY_MARGIN_BYCUS  \r\n");
    queryStringBuilder.append("                     WHERE EtlCurDate = (SELECT MAX(EtlCurDate) FROM Stg_flx_VW_DAILY_MARGIN_BYCUS)  \r\n");
    queryStringBuilder.append("                     AND to_date=:toDate  \r\n");
    queryStringBuilder.append("                     GROUP BY custodycd, to_date  \r\n");
    queryStringBuilder.append("                     ) m \r\n");
    queryStringBuilder.append("               LEFT JOIN stg_tcb_customer c on c.custodycode = m.custodycd  \r\n");
    queryStringBuilder.append("               WHERE 1=1  \r\n");
    queryStringBuilder.append("               AND c.EtlCurDate = (SELECT MAX(EtlCurDate) FROM stg_tcb_customer)  \r\n");
    queryStringBuilder.append("               GROUP BY c.tcbsid, custodycd, totalMargin, reportDate ) a \r\n");
    queryStringBuilder.append("ORDER BY a.tcbsid ASC  \r\n");

    List<StgFlxVwDailyMarginBycusEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("toDate", date)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            StgFlxVwDailyMarginBycusEntity info = StgFlxVwDailyMarginBycusEntity.builder()
              .tcbsid((String) object[0])
              .custodycd((String) object[1])
              .totalMargin((BigDecimal) object[2])
              .reportDate((Date) object[3])
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

  @Step("select data")
  public static Boolean checkEltDateTime() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT case when max(EtlCurDate) - convert(varchar(8), getdate() - 1,112) < 0 then 'false' else 'true' end as latElt  from Stg_flx_VW_DAILY_MARGIN_BYCUS ");
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).getResultList();
      return Boolean.valueOf(String.valueOf(result.get(0)));
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return Boolean.FALSE;
  }
}
