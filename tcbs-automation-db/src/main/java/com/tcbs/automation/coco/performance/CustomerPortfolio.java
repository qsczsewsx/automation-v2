package com.tcbs.automation.coco.performance;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPortfolio {
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @ColumnIndex(name = "componentType", index = 0, ignoreIfNull = true)
  private String componentType;

  @ColumnIndex(name = "USER_ID", index = 1, ignoreIfNull = true)
  private String userId;

  @ColumnIndex(name = "REPORT_DATE", index = 2, ignoreIfNull = true)
  private Date reportDate;

  @ColumnIndex(name = "TICKER", index = 3, ignoreIfNull = true)
  private String component;

  @ColumnIndex(name = "CLOSE_VALUE", index = 4, ignoreIfNull = true)
  private Double componentValue;

  @ColumnIndex(name = "CLOSE_VOLUME", index = 5, ignoreIfNull = true)
  private Double componentVolume;

  private Double componentWeight = 0.0;

  @ColumnIndex(name = "COGS_PER_UNIT_ADJ", index = 6, ignoreIfNull = true)
  private Double costPrice;

  private List<PortfolioIndustryItem> componentDetail;

  public static List<CustomerPortfolio> getPortfolio(String userId, Date reportDate, Constants.Ctype cType) throws Exception {
    List<CustomerPortfolio> res;
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall(Constants.Procedure.PROC_PE_GET_PORTFOLIO_PERCENT_BY_USER);

    LocalDate currentDate;
    if (reportDate == null) {
      currentDate = LocalDate.now();
    } else {
      currentDate = reportDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(userId);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate));
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(cType.getValue());
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    res = ReflexData.convertResultSetToObj(resultSets, CustomerPortfolio.class);

    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CustomerPortfolio)) {
      return false;
    }

    CustomerPortfolio cP = (CustomerPortfolio) obj;
    return cP.userId.equals(userId) &&
      cP.reportDate.equals(reportDate) &&
      cP.componentType.equals(componentType);
  }

  @Override
  public int hashCode() {
    return hashCode();
  }
}
