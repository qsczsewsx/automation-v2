package com.tcbs.automation.dwh.ixu;

import com.tcbs.automation.dwh.Dwh;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Smy_dwh_flx_AllStockTxn")
public class SmyDwhFlxAllStockTxnEntity {
  private int id;
  private Date busDate;
  private String custodycd;
  private String afacctno;
  private String mnemonic;
  private String symbol;
  private String field;
  private Long quantity;
  private BigDecimal buyPrice;
  private BigDecimal buyAmt;
  private BigDecimal sellPrice;
  private BigDecimal sellAmt;
  private Integer pnL;
  private Integer pcPnL;
  private String secType;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;
  private String tcbsId;
  private BigDecimal totalAmt;
  private BigDecimal totalFee;

  public static List<HashMap<String, Object>> getByCus(String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT * FROM    ");
    queryStringBuilder.append("   (select st.CustodyCD as custodycd, st.BusDate, st.totalAmt, st.totalFee, au.TCBSID as tcbsid from ");
    queryStringBuilder.append("    (select t.*, f.totalFee from");
    queryStringBuilder.append("     ( select st.CustodyCD, st.BusDate, sum(iif(Field = N'Bán', sellamt, buyamt)) totalAmt ");
    queryStringBuilder.append("       from DailyPort_StockTxn st ");
    queryStringBuilder.append("       left join Prc_flx_SecuritiesType sect on sect.symbol = st.SYMBOL ");
    queryStringBuilder.append("       where Field in ('Mua', N'Bán') and (sect.sectype = 'Stock' OR sect.sectype = 'Fund') ");
    queryStringBuilder.append("       and busDate >= :fromDate ");
    queryStringBuilder.append("       and busDate < :toDate ");
    queryStringBuilder.append("       group by st.CustodyCD, st.BusDate) t ");
    queryStringBuilder.append("     left join ");
    queryStringBuilder.append("     ( select CustodyCD, cast(txDate as date) txDate, sum(FEE_AMT_Detail) totalFee ");
    queryStringBuilder.append("     from Prc_Flx_Stock_TradingFee ");
    queryStringBuilder.append("     where sectype_name in (N'Cổ phiếu thường', N'Chứng quyền', N'Chứng chỉ quỹ') ");
    queryStringBuilder.append("     and txdate = :fromDate ");
    queryStringBuilder.append("   group by CustodyCD, txDate) f on t.CustodyCD = f.CustodyCD and t.BusDate = f.txDate ) st ");
    queryStringBuilder.append("   LEFT JOIN smy_dwh_cas_alluserview au   ");
    queryStringBuilder.append("   ON st.custodycd = au.custodycd AND au.etlcurdate = (SELECT MAX(EtlCurDate) FROM smy_dwh_cas_alluserview)  ");
    queryStringBuilder.append("   GROUP BY au.tcbsid, st.custodycd, st.totalAmt, st.totalFee, st.BusDate ) a");
    queryStringBuilder.append("   ORDER BY a.custodyCd ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get vip tcbsid")
  public static List<HashMap<String, Object>> getListVip(List<String> tcbsId){
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT stc.Tcbsid, v.AUM_VIP FROM Stg_tcb_Customer stc ");
    queryStringBuilder.append(" left join [Stg_tcbs-share_R_TBL_CU_0015_IB_CST_FOR_TCB] v on stc.CustomerId = v.CUSTOMERID ");
    queryStringBuilder.append(" WHERE Tcbsid in ( :tcbsId ) ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("Get latest etlDate")
  public static Integer getLatestEtlDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT MAX(etlCurDate) ");
    queryStringBuilder.append(" FROM Smy_dwh_flx_AllStockTxn   ");

    try {
      List<Integer> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();
      Integer latestDate = result.get(0);
      return latestDate;
    } catch (Exception ex) {

      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("update ETL data")
  public static void updateEtlVipDate(Date reportDate) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("UPDATE [Stg_tcbs-share_R_TBL_CU_0015_IB_CST_FOR_TCB] " +
      " SET ReportDate=?  " +
      " WHERE ReportDate = (SELECT MAX(ReportDate) FROM [Stg_tcbs-share_R_TBL_CU_0015_IB_CST_FOR_TCB]) ");
    query.setParameter(1, reportDate);
    query.executeUpdate();
    trans.commit();
  }


  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "BusDate")
  public Date getBusDate() {
    return busDate;
  }

  public void setBusDate(Date busDate) {
    this.busDate = busDate;
  }

  @Basic
  @Column(name = "CustodyCD")
  public String getCustodycd() {
    return custodycd;
  }

  public void setCustodycd(String custodyCd) {
    this.custodycd = custodyCd;
  }

  @Basic
  @Column(name = "AFACCTNO")
  public String getAfacctno() {
    return afacctno;
  }

  public void setAfacctno(String afacctno) {
    this.afacctno = afacctno;
  }

  @Basic
  @Column(name = "MNEMONIC")
  public String getMnemonic() {
    return mnemonic;
  }

  public void setMnemonic(String mnemonic) {
    this.mnemonic = mnemonic;
  }

  @Basic
  @Column(name = "Symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "Field")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Basic
  @Column(name = "Quantity")
  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  @Basic
  @Column(name = "BuyPrice")
  public BigDecimal getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(BigDecimal buyPrice) {
    this.buyPrice = buyPrice;
  }

  @Basic
  @Column(name = "BuyAmt")
  public BigDecimal getBuyAmt() {
    return buyAmt;
  }

  public void setBuyAmt(BigDecimal buyAmt) {
    this.buyAmt = buyAmt;
  }

  @Basic
  @Column(name = "SellPrice")
  public BigDecimal getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(BigDecimal sellPrice) {
    this.sellPrice = sellPrice;
  }

  @Basic
  @Column(name = "SellAmt")
  public BigDecimal getSellAmt() {
    return sellAmt;
  }

  public void setSellAmt(BigDecimal sellAmt) {
    this.sellAmt = sellAmt;
  }

  @Basic
  @Column(name = "PnL")
  public Integer getPnL() {
    return pnL;
  }

  public void setPnL(Integer pnL) {
    this.pnL = pnL;
  }

  @Basic
  @Column(name = "PCPnL")
  public Integer getPcPnL() {
    return pcPnL;
  }

  public void setPcPnL(Integer pcPnL) {
    this.pcPnL = pcPnL;
  }

  @Basic
  @Column(name = "SecType")
  public String getSecType() {
    return secType;
  }

  public void setSecType(String secType) {
    this.secType = secType;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDateTime")
  public Timestamp getEtlRunDateTime() {
    return etlRunDateTime;
  }

  public void setEtlRunDateTime(Timestamp etlRunDateTime) {
    this.etlRunDateTime = etlRunDateTime;
  }

  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }

  public BigDecimal getTotalAmt() {
    return totalAmt;
  }

  public void setTotalAmt(BigDecimal totalAmt) {
    this.totalAmt = totalAmt;
  }

  public BigDecimal getTotalFee() {
    return totalFee;
  }

  public void setTotalFee(BigDecimal totalFee) {
    this.totalFee = totalFee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyDwhFlxAllStockTxnEntity that = (SmyDwhFlxAllStockTxnEntity) o;
    return id == that.id &&
      Objects.equals(busDate, that.busDate) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(afacctno, that.afacctno) &&
      Objects.equals(mnemonic, that.mnemonic) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(field, that.field) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(buyPrice, that.buyPrice) &&
      Objects.equals(buyAmt, that.buyAmt) &&
      Objects.equals(sellPrice, that.sellPrice) &&
      Objects.equals(sellAmt, that.sellAmt) &&
      Objects.equals(pnL, that.pnL) &&
      Objects.equals(pcPnL, that.pcPnL) &&
      Objects.equals(secType, that.secType) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, busDate, custodycd, afacctno, mnemonic, symbol, field, quantity, buyPrice, buyAmt, sellPrice, sellAmt, pnL, pcPnL, secType, etlCurDate, etlRunDateTime);
  }
}
