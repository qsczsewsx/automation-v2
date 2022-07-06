package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "iCalendar_BondExpiryDate")
public class ICalendarBondExpiryDateEntity {
  private Integer id;
  private Integer tradingId;
  private String tradingCode;
  private Timestamp pDate;
  private BigDecimal pQuantity;
  private BigDecimal pPrincipal;
  private BigDecimal oBalQ;
  private Timestamp accDate;
  private String customerTcbsid;
  private String customerCustodyCode;
  private String rmtcbsid;
  private String bondProductCode;
  private String bondCategoryCode;
  private String bondCode;
  private Timestamp endDateAdj;
  private Timestamp matchingDate;
  private BigDecimal donGiaTatToan;
  private BigDecimal tienTatToanSeNhan;
  private BigDecimal couponDaNhan;
  private BigDecimal loiNhuanNeuTatToan;
  private BigDecimal loiTucNeuTatToan;
  private BigDecimal gocDauTuNeuRollTiep;
  private Timestamp ngayDenHanRollTiep;
  private BigDecimal tienRollTiepSeNhan;
  private BigDecimal couponSeNhanRollTiep;
  private BigDecimal loiNhuanNeuRollTiep;
  private BigDecimal taiDauTuRollTiep;
  private BigDecimal loiTucNeuRollTiep;
  private Timestamp ngayDaoHan;
  private BigDecimal tienDaoHanSeNhan;
  private BigDecimal couponSeNhanDaoHan;
  private BigDecimal loiNhuanNeuGiuDaoHan;
  private BigDecimal taiDauTuGiuDaoHan;
  private BigDecimal loiTucNeuGiuDaoHan;
  private Integer soLanDaRoll;
  private String duocRollTiepKhong;
  private Timestamp createdDate;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "TradingID")
  public Integer getTradingId() {
    return tradingId;
  }

  public void setTradingId(Integer tradingId) {
    this.tradingId = tradingId;
  }

  @Basic
  @Column(name = "TradingCode")
  public String getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
  }

  @Basic
  @Column(name = "PDate")
  public Timestamp getpDate() {
    return pDate;
  }

  public void setpDate(Timestamp pDate) {
    this.pDate = pDate;
  }

  @Basic
  @Column(name = "PQuantity")
  public BigDecimal getpQuantity() {
    return pQuantity;
  }

  public void setpQuantity(BigDecimal pQuantity) {
    this.pQuantity = pQuantity;
  }

  @Basic
  @Column(name = "PPrincipal")
  public BigDecimal getpPrincipal() {
    return pPrincipal;
  }

  public void setpPrincipal(BigDecimal pPrincipal) {
    this.pPrincipal = pPrincipal;
  }

  @Basic
  @Column(name = "OBalQ")
  public BigDecimal getoBalQ() {
    return oBalQ;
  }

  public void setoBalQ(BigDecimal oBalQ) {
    this.oBalQ = oBalQ;
  }

  @Basic
  @Column(name = "AccDate")
  public Timestamp getAccDate() {
    return accDate;
  }

  public void setAccDate(Timestamp accDate) {
    this.accDate = accDate;
  }

  @Basic
  @Column(name = "CustomerTCBSID")
  public String getCustomerTcbsid() {
    return customerTcbsid;
  }

  public void setCustomerTcbsid(String customerTcbsid) {
    this.customerTcbsid = customerTcbsid;
  }

  @Basic
  @Column(name = "CustomerCustodyCode")
  public String getCustomerCustodyCode() {
    return customerCustodyCode;
  }

  public void setCustomerCustodyCode(String customerCustodyCode) {
    this.customerCustodyCode = customerCustodyCode;
  }

  @Basic
  @Column(name = "RMTCBSID")
  public String getRmtcbsid() {
    return rmtcbsid;
  }

  public void setRmtcbsid(String rmtcbsid) {
    this.rmtcbsid = rmtcbsid;
  }

  @Basic
  @Column(name = "BondProductCode")
  public String getBondProductCode() {
    return bondProductCode;
  }

  public void setBondProductCode(String bondProductCode) {
    this.bondProductCode = bondProductCode;
  }

  @Basic
  @Column(name = "BondCategoryCode")
  public String getBondCategoryCode() {
    return bondCategoryCode;
  }

  public void setBondCategoryCode(String bondCategoryCode) {
    this.bondCategoryCode = bondCategoryCode;
  }

  @Basic
  @Column(name = "BondCode")
  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  @Basic
  @Column(name = "EndDate_Adj")
  public Timestamp getEndDateAdj() {
    return endDateAdj;
  }

  public void setEndDateAdj(Timestamp endDateAdj) {
    this.endDateAdj = endDateAdj;
  }

  @Basic
  @Column(name = "MatchingDate")
  public Timestamp getMatchingDate() {
    return matchingDate;
  }

  public void setMatchingDate(Timestamp matchingDate) {
    this.matchingDate = matchingDate;
  }

  @Basic
  @Column(name = "DonGiaTatToan")
  public BigDecimal getDonGiaTatToan() {
    return donGiaTatToan;
  }

  public void setDonGiaTatToan(BigDecimal donGiaTatToan) {
    this.donGiaTatToan = donGiaTatToan;
  }

  @Basic
  @Column(name = "TienTatToanSeNhan")
  public BigDecimal getTienTatToanSeNhan() {
    return tienTatToanSeNhan;
  }

  public void setTienTatToanSeNhan(BigDecimal tienTatToanSeNhan) {
    this.tienTatToanSeNhan = tienTatToanSeNhan;
  }

  @Basic
  @Column(name = "CouponDaNhan")
  public BigDecimal getCouponDaNhan() {
    return couponDaNhan;
  }

  public void setCouponDaNhan(BigDecimal couponDaNhan) {
    this.couponDaNhan = couponDaNhan;
  }

  @Basic
  @Column(name = "LoiNhuanNeuTatToan")
  public BigDecimal getLoiNhuanNeuTatToan() {
    return loiNhuanNeuTatToan;
  }

  public void setLoiNhuanNeuTatToan(BigDecimal loiNhuanNeuTatToan) {
    this.loiNhuanNeuTatToan = loiNhuanNeuTatToan;
  }

  @Basic
  @Column(name = "LoiTucNeuTatToan")
  public BigDecimal getLoiTucNeuTatToan() {
    return loiTucNeuTatToan;
  }

  public void setLoiTucNeuTatToan(BigDecimal loiTucNeuTatToan) {
    this.loiTucNeuTatToan = loiTucNeuTatToan;
  }

  @Basic
  @Column(name = "GocDauTuNeuRollTiep")
  public BigDecimal getGocDauTuNeuRollTiep() {
    return gocDauTuNeuRollTiep;
  }

  public void setGocDauTuNeuRollTiep(BigDecimal gocDauTuNeuRollTiep) {
    this.gocDauTuNeuRollTiep = gocDauTuNeuRollTiep;
  }

  @Basic
  @Column(name = "NgayDenHanRollTiep")
  public Timestamp getNgayDenHanRollTiep() {
    return ngayDenHanRollTiep;
  }

  public void setNgayDenHanRollTiep(Timestamp ngayDenHanRollTiep) {
    this.ngayDenHanRollTiep = ngayDenHanRollTiep;
  }

  @Basic
  @Column(name = "TienRollTiepSeNhan")
  public BigDecimal getTienRollTiepSeNhan() {
    return tienRollTiepSeNhan;
  }

  public void setTienRollTiepSeNhan(BigDecimal tienRollTiepSeNhan) {
    this.tienRollTiepSeNhan = tienRollTiepSeNhan;
  }

  @Basic
  @Column(name = "CouponSeNhanRollTiep")
  public BigDecimal getCouponSeNhanRollTiep() {
    return couponSeNhanRollTiep;
  }

  public void setCouponSeNhanRollTiep(BigDecimal couponSeNhanRollTiep) {
    this.couponSeNhanRollTiep = couponSeNhanRollTiep;
  }

  @Basic
  @Column(name = "LoiNhuanNeuRollTiep")
  public BigDecimal getLoiNhuanNeuRollTiep() {
    return loiNhuanNeuRollTiep;
  }

  public void setLoiNhuanNeuRollTiep(BigDecimal loiNhuanNeuRollTiep) {
    this.loiNhuanNeuRollTiep = loiNhuanNeuRollTiep;
  }

  @Basic
  @Column(name = "TaiDauTuRollTiep")
  public BigDecimal getTaiDauTuRollTiep() {
    return taiDauTuRollTiep;
  }

  public void setTaiDauTuRollTiep(BigDecimal taiDauTuRollTiep) {
    this.taiDauTuRollTiep = taiDauTuRollTiep;
  }

  @Basic
  @Column(name = "LoiTucNeuRollTiep")
  public BigDecimal getLoiTucNeuRollTiep() {
    return loiTucNeuRollTiep;
  }

  public void setLoiTucNeuRollTiep(BigDecimal loiTucNeuRollTiep) {
    this.loiTucNeuRollTiep = loiTucNeuRollTiep;
  }

  @Basic
  @Column(name = "NgayDaoHan")
  public Timestamp getNgayDaoHan() {
    return ngayDaoHan;
  }

  public void setNgayDaoHan(Timestamp ngayDaoHan) {
    this.ngayDaoHan = ngayDaoHan;
  }

  @Basic
  @Column(name = "TienDaoHanSeNhan")
  public BigDecimal getTienDaoHanSeNhan() {
    return tienDaoHanSeNhan;
  }

  public void setTienDaoHanSeNhan(BigDecimal tienDaoHanSeNhan) {
    this.tienDaoHanSeNhan = tienDaoHanSeNhan;
  }

  @Basic
  @Column(name = "CouponSeNhanDaoHan")
  public BigDecimal getCouponSeNhanDaoHan() {
    return couponSeNhanDaoHan;
  }

  public void setCouponSeNhanDaoHan(BigDecimal couponSeNhanDaoHan) {
    this.couponSeNhanDaoHan = couponSeNhanDaoHan;
  }

  @Basic
  @Column(name = "LoiNhuanNeuGiuDaoHan")
  public BigDecimal getLoiNhuanNeuGiuDaoHan() {
    return loiNhuanNeuGiuDaoHan;
  }

  public void setLoiNhuanNeuGiuDaoHan(BigDecimal loiNhuanNeuGiuDaoHan) {
    this.loiNhuanNeuGiuDaoHan = loiNhuanNeuGiuDaoHan;
  }

  @Basic
  @Column(name = "TaiDauTuGiuDaoHan")
  public BigDecimal getTaiDauTuGiuDaoHan() {
    return taiDauTuGiuDaoHan;
  }

  public void setTaiDauTuGiuDaoHan(BigDecimal taiDauTuGiuDaoHan) {
    this.taiDauTuGiuDaoHan = taiDauTuGiuDaoHan;
  }

  @Basic
  @Column(name = "LoiTucNeuGiuDaoHan")
  public BigDecimal getLoiTucNeuGiuDaoHan() {
    return loiTucNeuGiuDaoHan;
  }

  public void setLoiTucNeuGiuDaoHan(BigDecimal loiTucNeuGiuDaoHan) {
    this.loiTucNeuGiuDaoHan = loiTucNeuGiuDaoHan;
  }

  @Basic
  @Column(name = "SoLanDaRoll")
  public Integer getSoLanDaRoll() {
    return soLanDaRoll;
  }

  public void setSoLanDaRoll(Integer soLanDaRoll) {
    this.soLanDaRoll = soLanDaRoll;
  }

  @Basic
  @Column(name = "DuocRollTiepKhong")
  public String getDuocRollTiepKhong() {
    return duocRollTiepKhong;
  }

  public void setDuocRollTiepKhong(String duocRollTiepKhong) {
    this.duocRollTiepKhong = duocRollTiepKhong;
  }

  @Basic
  @Column(name = "CreatedDate")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ICalendarBondExpiryDateEntity that = (ICalendarBondExpiryDateEntity) o;
    return id == that.id &&
      Objects.equals(tradingId, that.tradingId) &&
      Objects.equals(tradingCode, that.tradingCode) &&
      Objects.equals(pDate, that.pDate) &&
      Objects.equals(pQuantity, that.pQuantity) &&
      Objects.equals(pPrincipal, that.pPrincipal) &&
      Objects.equals(oBalQ, that.oBalQ) &&
      Objects.equals(accDate, that.accDate) &&
      Objects.equals(customerTcbsid, that.customerTcbsid) &&
      Objects.equals(customerCustodyCode, that.customerCustodyCode) &&
      Objects.equals(rmtcbsid, that.rmtcbsid) &&
      Objects.equals(bondProductCode, that.bondProductCode) &&
      Objects.equals(bondCategoryCode, that.bondCategoryCode) &&
      Objects.equals(bondCode, that.bondCode) &&
      Objects.equals(endDateAdj, that.endDateAdj) &&
      Objects.equals(matchingDate, that.matchingDate) &&
      Objects.equals(donGiaTatToan, that.donGiaTatToan) &&
      Objects.equals(tienTatToanSeNhan, that.tienTatToanSeNhan) &&
      Objects.equals(couponDaNhan, that.couponDaNhan) &&
      Objects.equals(loiNhuanNeuTatToan, that.loiNhuanNeuTatToan) &&
      Objects.equals(loiTucNeuTatToan, that.loiTucNeuTatToan) &&
      Objects.equals(gocDauTuNeuRollTiep, that.gocDauTuNeuRollTiep) &&
      Objects.equals(ngayDenHanRollTiep, that.ngayDenHanRollTiep) &&
      Objects.equals(tienRollTiepSeNhan, that.tienRollTiepSeNhan) &&
      Objects.equals(couponSeNhanRollTiep, that.couponSeNhanRollTiep) &&
      Objects.equals(loiNhuanNeuRollTiep, that.loiNhuanNeuRollTiep) &&
      Objects.equals(taiDauTuRollTiep, that.taiDauTuRollTiep) &&
      Objects.equals(loiTucNeuRollTiep, that.loiTucNeuRollTiep) &&
      Objects.equals(ngayDaoHan, that.ngayDaoHan) &&
      Objects.equals(tienDaoHanSeNhan, that.tienDaoHanSeNhan) &&
      Objects.equals(couponSeNhanDaoHan, that.couponSeNhanDaoHan) &&
      Objects.equals(loiNhuanNeuGiuDaoHan, that.loiNhuanNeuGiuDaoHan) &&
      Objects.equals(taiDauTuGiuDaoHan, that.taiDauTuGiuDaoHan) &&
      Objects.equals(loiTucNeuGiuDaoHan, that.loiTucNeuGiuDaoHan) &&
      Objects.equals(soLanDaRoll, that.soLanDaRoll) &&
      Objects.equals(duocRollTiepKhong, that.duocRollTiepKhong) &&
      Objects.equals(createdDate, that.createdDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tradingId, tradingCode, pDate, pQuantity, pPrincipal, oBalQ, accDate, customerTcbsid, customerCustodyCode, rmtcbsid, bondProductCode, bondCategoryCode, bondCode,
      endDateAdj,
      matchingDate, donGiaTatToan, tienTatToanSeNhan, couponDaNhan, loiNhuanNeuTatToan, loiTucNeuTatToan, gocDauTuNeuRollTiep, ngayDenHanRollTiep, tienRollTiepSeNhan, couponSeNhanRollTiep,
      loiNhuanNeuRollTiep, taiDauTuRollTiep, loiTucNeuRollTiep, ngayDaoHan, tienDaoHanSeNhan, couponSeNhanDaoHan, loiNhuanNeuGiuDaoHan, taiDauTuGiuDaoHan, loiTucNeuGiuDaoHan, soLanDaRoll,
      duocRollTiepKhong, createdDate);
  }

  @Step("insert data")
  public boolean saveBondExpiry(ICalendarBondExpiryDateEntity bondExpiryEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.save(bondExpiryEntity);
    session.getTransaction().commit();
    return true;
  }

  @Step("update data")
  public boolean updateBondExpiry(ICalendarBondExpiryDateEntity bondExpiryEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.update(bondExpiryEntity);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by key")
  public void deleteByBondCode(String bondCode) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<ICalendarBondExpiryDateEntity> query = session.createQuery(
      "DELETE FROM ICalendarBondExpiryDateEntity i WHERE i.bondCode=:bondCode"
    );
    query.setParameter("bondCode", bondCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
