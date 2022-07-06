package com.tcbs.automation.dwh.dwhservice;

import com.tcbs.automation.dwh.Dwh;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Smy_dwh_flx_StockEvent")
public class SmyDwhFlxStockEventEntity {
  @Id
  private long autoid;
  private String catype;
  private Date reportdate;
  private Date exdate;
  private String symbol;
  private String devidentshares;
  private String devidentrate;
  private String exrate;
  private BigDecimal exprice;
  private String rightoffrate;
  private String event;
  private String description;
  private String purposedesc;
  private String advdesc;
  private String icalStockRatio;
  private int etlCurDate;
  private Timestamp etlRunDatetime;

  @Step("insert data")
  public static void insertData(SmyDwhFlxStockEventEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("insert into Smy_dwh_flx_StockEvent " +
      "(AUTOID, CATYPE, REPORTDATE, EXDATE, SYMBOL, DEVIDENTSHARES, DEVIDENTRATE, EXRATE, EXPRICE, RIGHTOFFRATE, EVENT, DESCRIPTION, PURPOSEDESC, " +
      "ADVDESC, ICAL_STOCK_RATIO, EtlCurDate, EtlRunDatetime) " +
      "values ((select MAX(AUTOID) FROM Smy_dwh_flx_StockEvent), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
      "(select MAX(EtlCurDate) FROM Smy_dwh_flx_StockEvent), (select MAX(EtlRunDatetime) FROM Smy_dwh_flx_StockEvent))");
    query.setParameter(1, entity.getCatype());
    query.setParameter(2, entity.getReportdate());
    query.setParameter(3, entity.getExdate());
    query.setParameter(4, entity.getSymbol());
    query.setParameter(5, entity.getDevidentshares());
    query.setParameter(6, entity.getDevidentrate());
    query.setParameter(7, entity.getExrate());
    query.setParameter(8, entity.getExprice());
    query.setParameter(9, entity.getRightoffrate());
    query.setParameter(10, entity.getEvent());
    query.setParameter(11, entity.getDescription());
    query.setParameter(12, entity.getPurposedesc());
    query.setParameter(13, entity.getAdvdesc());
    query.setParameter(14, entity.getIcalStockRatio());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by key")
  public static void deleteByTickerAndDate(SmyDwhFlxStockEventEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<SmyDwhFlxStockEventEntity> query = session.createQuery(
      "DELETE FROM SmyDwhFlxStockEventEntity i WHERE i.symbol=:ticker and i.exdate=:exDate"
    );
    query.setParameter("ticker", entity.getSymbol());
    query.setParameter("exDate", entity.getExdate());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("Get data by key")
  public static List<HashMap<String, Object>> getByExDate(String fromDate, String toDate, String isAll) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select sdscir.ticker as symbol, ex.EXDATE as exdividendDate, sdscir.exchangeName, Stock_Rating ,  ");
    queryBuilder.append(" sdscir.NameLv2, sdscir.NameEnLv2, ex.EVENT as event, ICAL_STOCK_RATIO as ratio, EXPRICE as exPrice  ");
    queryBuilder.append(" from Smy_dwh_stox_CompanyIndustryRate sdscir  ");
    queryBuilder.append(" left join ( SELECT EXDATE, SYMBOL, CATYPE as EVENT, ICAL_STOCK_RATIO, EXPRICE FROM Smy_dwh_flx_StockEvent ");
    queryBuilder.append("             WHERE EXDATE BETWEEN :fromDate AND :toDate ");
    queryBuilder.append("             AND EtlCurDate = (SELECT MAX(EtlCurDate) FROM Smy_dwh_flx_StockEvent) ");
    queryBuilder.append("              ) ex on ex.SYMBOL = sdscir.ticker");
    queryBuilder.append(" where sdscir.exchangeId in (0,1,3) ");
    if (isAll.equalsIgnoreCase("N")) {
      queryBuilder.append(" and ex.EXDATE is not null  ");
    }
    queryBuilder.append(" order by sdscir.ticker ");


    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Column(name = "AUTOID")
  public long getAutoid() {
    return autoid;
  }

  public void setAutoid(long autoid) {
    this.autoid = autoid;
  }

  @Basic
  @Column(name = "CATYPE")
  public String getCatype() {
    return catype;
  }

  public void setCatype(String catype) {
    this.catype = catype;
  }

  @Basic
  @Column(name = "REPORTDATE")
  public Date getReportdate() {
    return reportdate;
  }

  public void setReportdate(Date reportdate) {
    this.reportdate = reportdate;
  }

  @Basic
  @Column(name = "EXDATE")
  public Date getExdate() {
    return exdate;
  }

  public void setExdate(Date exdate) {
    this.exdate = exdate;
  }

  @Basic
  @Column(name = "SYMBOL")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "DEVIDENTSHARES")
  public String getDevidentshares() {
    return devidentshares;
  }

  public void setDevidentshares(String devidentshares) {
    this.devidentshares = devidentshares;
  }

  @Basic
  @Column(name = "DEVIDENTRATE")
  public String getDevidentrate() {
    return devidentrate;
  }

  public void setDevidentrate(String devidentrate) {
    this.devidentrate = devidentrate;
  }

  @Basic
  @Column(name = "EXRATE")
  public String getExrate() {
    return exrate;
  }

  public void setExrate(String exrate) {
    this.exrate = exrate;
  }

  @Basic
  @Column(name = "EXPRICE")
  public BigDecimal getExprice() {
    return exprice;
  }

  public void setExprice(BigDecimal exprice) {
    this.exprice = exprice;
  }

  @Basic
  @Column(name = "RIGHTOFFRATE")
  public String getRightoffrate() {
    return rightoffrate;
  }

  public void setRightoffrate(String rightoffrate) {
    this.rightoffrate = rightoffrate;
  }

  @Basic
  @Column(name = "EVENT")
  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  @Basic
  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "PURPOSEDESC")
  public String getPurposedesc() {
    return purposedesc;
  }

  public void setPurposedesc(String purposedesc) {
    this.purposedesc = purposedesc;
  }

  @Basic
  @Column(name = "ADVDESC")
  public String getAdvdesc() {
    return advdesc;
  }

  public void setAdvdesc(String advdesc) {
    this.advdesc = advdesc;
  }

  @Basic
  @Column(name = "ICAL_STOCK_RATIO")
  public String getIcalStockRatio() {
    return icalStockRatio;
  }

  public void setIcalStockRatio(String icalStockRatio) {
    this.icalStockRatio = icalStockRatio;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public int getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(int etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDatetime")
  public Timestamp getEtlRunDatetime() {
    return etlRunDatetime;
  }

  public void setEtlRunDatetime(Timestamp etlRunDatetime) {
    this.etlRunDatetime = etlRunDatetime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyDwhFlxStockEventEntity that = (SmyDwhFlxStockEventEntity) o;
    return autoid == that.autoid &&
      etlCurDate == that.etlCurDate &&
      Objects.equals(catype, that.catype) &&
      Objects.equals(reportdate, that.reportdate) &&
      Objects.equals(exdate, that.exdate) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(devidentshares, that.devidentshares) &&
      Objects.equals(devidentrate, that.devidentrate) &&
      Objects.equals(exrate, that.exrate) &&
      Objects.equals(exprice, that.exprice) &&
      Objects.equals(rightoffrate, that.rightoffrate) &&
      Objects.equals(event, that.event) &&
      Objects.equals(description, that.description) &&
      Objects.equals(purposedesc, that.purposedesc) &&
      Objects.equals(advdesc, that.advdesc) &&
      Objects.equals(icalStockRatio, that.icalStockRatio) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autoid, catype, reportdate, exdate, symbol, devidentshares, devidentrate, exrate, exprice, rightoffrate, event, description, purposedesc, advdesc, icalStockRatio, etlCurDate,
      etlRunDatetime);
  }
}
