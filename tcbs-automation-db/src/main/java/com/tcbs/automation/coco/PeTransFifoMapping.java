package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "PE_TRANS_FIFO_MAPPING")
public class PeTransFifoMapping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "B_DATE")
  private Date bDate;
  @Column(name = "B_VOLUME")
  private Double bVolume;
  @Column(name = "REF_PRICE")
  private Double refPrice;
  @Column(name = "OPEN_VALUE")
  private String openValue;
  @Column(name = "BUYING_VOLUME")
  private Double buyingVolume;
  @Column(name = "BUYING_VALUE")
  private String buyingValue;
  @Column(name = "SELLING_VOLUME")
  private Double sellingVolume;
  @Column(name = "SELLING_VALUE")
  private String sellingValue;
  @Column(name = "CLOSE_VOLUME")
  private Double closeVolume;
  @Column(name = "CLOSE_PRICE")
  private Double closePrice;
  @Column(name = "CLOSE_VALUE")
  private String closeValue;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "COGS_PER_UNIT")
  private Double cogsPerUnit;
  @Column(name = "COGS_PER_UNIT_ADJ")
  private Double cogsPerUnitAdj;

  public static List<TransSummaryReportByUser> getUserPortfolioPresent(String userId, String fromDate, String toDate, String type) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<TransSummaryReportByUser> transSummaryReportByUsers = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRANS_SUMMARY_REPORT_BY_USER");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(userId);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, String.class, ParameterMode.IN).bindValue(type);
    call.registerParameter(5, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    transSummaryReportByUsers = ReflexData.convertResultSetToObj(resultSets, TransSummaryReportByUser.class);


    return transSummaryReportByUsers;
  }
}
