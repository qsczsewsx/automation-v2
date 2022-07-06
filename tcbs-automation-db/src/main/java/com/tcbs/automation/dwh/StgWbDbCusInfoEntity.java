package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Stg_wb_DB_CUS_INFO")
public class StgWbDbCusInfoEntity {
  private Date rptDate;
  private String cusId;
  private String cusName;
  private String region;
  private String cusGroup;
  private String cusGroup1;
  private String idGroup;
  private String segment;
  private String subSegment;
  private String subSegment1;
  private String subSegment2;
  private String rm;
  private String grm;
  private String sh;
  private String industry;
  private String enterpriseType;
  private String badDebt;
  private String sectorId;
  private String wbBb;
  private String gsoId;
  private String sector;
  private String subSector;
  private String industryGso;
  private String subIndustryGso;
  private String sectorEn;
  private String subSectorEn;
  private String groupSector;
  private String custype;
  private Date cusContactDate;
  private String cusTaxLegal;
  private String cusRegistration;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get wb info")
  public static List<HashMap<String, Object>> getWBInfo(String id) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT cusName, cusGroup, cusGroup_1, sector, sector_EN, segment, rm, stc, mst FROM ( ");
    queryStringBuilder.append(" 	select CusName as cusName, CusGroup  as cusGroup, CusGroup_1  as cusGroup_1,   ");
    queryStringBuilder.append(" 	 Sector as sector, Sector_EN as sector_EN , Segment as segment , RM as rm,CusTaxLegal as stc, CusRegistration as mst, RptDate , ");
    queryStringBuilder.append(" 	 row_number() over(partition by CusID order by RptDate desc) as rn ");
    queryStringBuilder.append(" 	from Stg_wb_DB_CUS_INFO  ");
    queryStringBuilder.append(" 	WHERE EtlCurDate = (select max(EtlCurDate) from Stg_wb_DB_CUS_INFO)  ");
    queryStringBuilder.append(" 	and ( CusTaxLegal in (select RTRIM(LTRIM(value)) from string_split(:id,','))   ");
    queryStringBuilder.append(" 	or CusRegistration in (select RTRIM(LTRIM(value)) from string_split(:id,',')) )  ");
    queryStringBuilder.append(" ) t1 ");
    queryStringBuilder.append(" where rn = 1 ");
    queryStringBuilder.append(" group by cusName, cusGroup, cusGroup_1, sector, sector_EN, segment, rm, stc, mst ");
    queryStringBuilder.append(" order by cusName, cusGroup, cusGroup_1, sector, sector_EN, segment, rm, stc, mst asc; ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("id", id)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("insert data")
  public void saveWBInfo(StgWbDbCusInfoEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
  }

  @Step("delete data")
  public void deleteWBInfo(StgWbDbCusInfoEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);

    Query<StgWbDbCusInfoEntity> query = session.createQuery(
      "DELETE FROM StgWbDbCusInfoEntity i WHERE i.rptDate = FORMAT(:rptDate, 'yyyy-MM-dd') AND i.cusTaxLegal =:tax AND i.cusRegistration =:cusReg");
    query.setParameter("rptDate", entity.getRptDate());
    query.setParameter("tax", entity.getCusTaxLegal());
    query.setParameter("cusReg", entity.getCusRegistration());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("update etl date")
  public void updateEtlDate(Integer etlDate, StgWbDbCusInfoEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);

    Query<StgWbDbCusInfoEntity> query = session.createQuery(
      "UPDATE StgWbDbCusInfoEntity i SET i.etlCurDate =:etlDate WHERE i.cusTaxLegal =:tax AND i.cusRegistration =:cusReg");
    query.setParameter("etlDate", etlDate);
    query.setParameter("tax", entity.getCusTaxLegal());
    query.setParameter("cusReg", entity.getCusRegistration());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Basic
  @Id
  @Column(name = "RptDate")
  public Date getRptDate() {
    return rptDate;
  }

  public void setRptDate(Date rptDate) {
    this.rptDate = rptDate;
  }

  @Basic
  @Column(name = "CusID")
  public String getCusId() {
    return cusId;
  }

  public void setCusId(String cusId) {
    this.cusId = cusId;
  }

  @Basic
  @Column(name = "CusName")
  public String getCusName() {
    return cusName;
  }

  public void setCusName(String cusName) {
    this.cusName = cusName;
  }

  @Basic
  @Column(name = "Region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  @Basic
  @Column(name = "CusGroup")
  public String getCusGroup() {
    return cusGroup;
  }

  public void setCusGroup(String cusGroup) {
    this.cusGroup = cusGroup;
  }

  @Basic
  @Column(name = "CusGroup_1")
  public String getCusGroup1() {
    return cusGroup1;
  }

  public void setCusGroup1(String cusGroup1) {
    this.cusGroup1 = cusGroup1;
  }

  @Basic
  @Column(name = "ID_GROUP")
  public String getIdGroup() {
    return idGroup;
  }

  public void setIdGroup(String idGroup) {
    this.idGroup = idGroup;
  }

  @Basic
  @Column(name = "Segment")
  public String getSegment() {
    return segment;
  }

  public void setSegment(String segment) {
    this.segment = segment;
  }

  @Basic
  @Column(name = "Sub_Segment")
  public String getSubSegment() {
    return subSegment;
  }

  public void setSubSegment(String subSegment) {
    this.subSegment = subSegment;
  }

  @Basic
  @Column(name = "Sub_Segment1")
  public String getSubSegment1() {
    return subSegment1;
  }

  public void setSubSegment1(String subSegment1) {
    this.subSegment1 = subSegment1;
  }

  @Basic
  @Column(name = "Sub_Segment2")
  public String getSubSegment2() {
    return subSegment2;
  }

  public void setSubSegment2(String subSegment2) {
    this.subSegment2 = subSegment2;
  }

  @Basic
  @Column(name = "RM")
  public String getRm() {
    return rm;
  }

  public void setRm(String rm) {
    this.rm = rm;
  }

  @Basic
  @Column(name = "GRM")
  public String getGrm() {
    return grm;
  }

  public void setGrm(String grm) {
    this.grm = grm;
  }

  @Basic
  @Column(name = "SH")
  public String getSh() {
    return sh;
  }

  public void setSh(String sh) {
    this.sh = sh;
  }

  @Basic
  @Column(name = "Industry")
  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  @Basic
  @Column(name = "Enterprise_Type")
  public String getEnterpriseType() {
    return enterpriseType;
  }

  public void setEnterpriseType(String enterpriseType) {
    this.enterpriseType = enterpriseType;
  }

  @Basic
  @Column(name = "Bad_Debt")
  public String getBadDebt() {
    return badDebt;
  }

  public void setBadDebt(String badDebt) {
    this.badDebt = badDebt;
  }

  @Basic
  @Column(name = "SectorID")
  public String getSectorId() {
    return sectorId;
  }

  public void setSectorId(String sectorId) {
    this.sectorId = sectorId;
  }

  @Basic
  @Column(name = "WB_BB")
  public String getWbBb() {
    return wbBb;
  }

  public void setWbBb(String wbBb) {
    this.wbBb = wbBb;
  }

  @Basic
  @Column(name = "GSO_ID")
  public String getGsoId() {
    return gsoId;
  }

  public void setGsoId(String gsoId) {
    this.gsoId = gsoId;
  }

  @Basic
  @Column(name = "Sector")
  public String getSector() {
    return sector;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }

  @Basic
  @Column(name = "Sub_Sector")
  public String getSubSector() {
    return subSector;
  }

  public void setSubSector(String subSector) {
    this.subSector = subSector;
  }

  @Basic
  @Column(name = "Industry_GSO")
  public String getIndustryGso() {
    return industryGso;
  }

  public void setIndustryGso(String industryGso) {
    this.industryGso = industryGso;
  }

  @Basic
  @Column(name = "SubIndustry_GSO")
  public String getSubIndustryGso() {
    return subIndustryGso;
  }

  public void setSubIndustryGso(String subIndustryGso) {
    this.subIndustryGso = subIndustryGso;
  }

  @Basic
  @Column(name = "Sector_EN")
  public String getSectorEn() {
    return sectorEn;
  }

  public void setSectorEn(String sectorEn) {
    this.sectorEn = sectorEn;
  }

  @Basic
  @Column(name = "SubSector_EN")
  public String getSubSectorEn() {
    return subSectorEn;
  }

  public void setSubSectorEn(String subSectorEn) {
    this.subSectorEn = subSectorEn;
  }

  @Basic
  @Column(name = "Group_Sector")
  public String getGroupSector() {
    return groupSector;
  }

  public void setGroupSector(String groupSector) {
    this.groupSector = groupSector;
  }

  @Basic
  @Column(name = "CUSTYPE")
  public String getCustype() {
    return custype;
  }

  public void setCustype(String custype) {
    this.custype = custype;
  }

  @Basic
  @Column(name = "CusContact_Date")
  public Date getCusContactDate() {
    return cusContactDate;
  }

  public void setCusContactDate(Date cusContactDate) {
    this.cusContactDate = cusContactDate;
  }

  @Basic
  @Column(name = "CusTaxLegal")
  public String getCusTaxLegal() {
    return cusTaxLegal;
  }

  public void setCusTaxLegal(String cusTaxLegal) {
    this.cusTaxLegal = cusTaxLegal;
  }

  @Basic
  @Column(name = "CusRegistration")
  public String getCusRegistration() {
    return cusRegistration;
  }

  public void setCusRegistration(String cusRegistration) {
    this.cusRegistration = cusRegistration;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StgWbDbCusInfoEntity that = (StgWbDbCusInfoEntity) o;
    return Objects.equals(rptDate, that.rptDate) && Objects.equals(cusId, that.cusId) && Objects.equals(cusName, that.cusName)
      && Objects.equals(region, that.region) && Objects.equals(cusGroup, that.cusGroup) && Objects.equals(cusGroup1, that.cusGroup1)
      && Objects.equals(idGroup, that.idGroup) && Objects.equals(segment, that.segment) && Objects.equals(subSegment, that.subSegment)
      && Objects.equals(subSegment1, that.subSegment1) && Objects.equals(subSegment2, that.subSegment2) && Objects.equals(rm, that.rm)
      && Objects.equals(grm, that.grm) && Objects.equals(sh, that.sh) && Objects.equals(industry, that.industry)
      && Objects.equals(enterpriseType, that.enterpriseType) && Objects.equals(badDebt, that.badDebt) && Objects.equals(sectorId, that.sectorId)
      && Objects.equals(wbBb, that.wbBb) && Objects.equals(gsoId, that.gsoId) && Objects.equals(sector, that.sector) && Objects.equals(subSector, that.subSector)
      && Objects.equals(industryGso, that.industryGso) && Objects.equals(subIndustryGso, that.subIndustryGso) && Objects.equals(sectorEn, that.sectorEn)
      && Objects.equals(subSectorEn, that.subSectorEn) && Objects.equals(groupSector, that.groupSector) && Objects.equals(custype, that.custype)
      && Objects.equals(cusContactDate, that.cusContactDate) && Objects.equals(cusTaxLegal, that.cusTaxLegal) && Objects.equals(cusRegistration, that.cusRegistration)
      && Objects.equals(etlCurDate, that.etlCurDate) && Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rptDate, cusId, cusName, region, cusGroup, cusGroup1, idGroup, segment, subSegment, subSegment1, subSegment2,
      rm, grm, sh, industry, enterpriseType, badDebt, sectorId, wbBb, gsoId, sector, subSector, industryGso, subIndustryGso, sectorEn,
      subSectorEn, groupSector, custype, cusContactDate, cusTaxLegal, cusRegistration, etlCurDate, etlRunDateTime);
  }
}
