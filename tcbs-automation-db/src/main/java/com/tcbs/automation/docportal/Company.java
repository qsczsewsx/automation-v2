package com.tcbs.automation.docportal;

import com.tcbs.automation.VNCharacterUtils;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "COMPANY")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(9)")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "GA_ID", columnDefinition = "NUMBER(19)")
  private Long gaId;

  @Column(name = "ORIGINAL_ID", columnDefinition = "NUMBER(9)", nullable = false)
  private Integer originalId;

  @Column(name = "COMPANY_GROUP_TYPE", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String groupType;

  @Column(name = "COMPANY_GROUP_GA_ID", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private Long groupGaId;

  @Column(name = "SEGMENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String segment;

  @Column(name = "SECTORL1", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String sectorL1;

  @Column(name = "SECTORL2", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String sectorL2;

  @Column(name = "REGION", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String region;

  @Column(name = "NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String name;

  @Column(name = "TICKER", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String ticker;

  // Migrate info from GA
  @Column(name = "BRC_NO_A101", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String brcNoA101;

  @Column(name = "TAX_CODE", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String taxCode;

  @Column(name = "BRC_NO_A102", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String brcNoA102;

  @Column(name = "STC_CODE", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String stcCode;

  @Column(name = "STATUS", columnDefinition = "VARCHAR(100)", length = 200, nullable = false)
  private String status;
  /* No Accent fields */
  @Column(name = "COMPANY_GROUP_TYPE_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String groupTypeNoAccent;
  @Column(name = "SEGMENT_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String segmentNoAccent;
  @Column(name = "SECTORL1_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String sectorL1NoAccent;
  @Column(name = "SECTORL2_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String sectorL2NoAccent;
  @Column(name = "REGION_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String regionNoAccent;
  @Column(name = "NAME_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  @Setter(AccessLevel.NONE)
  private String nameNoAccent;
  @Column(name = "TICKER_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String tickerNoAccent;
  @Column(name = "BRC_NO_A101_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String brcNoA101NoAccent;
  @Column(name = "BRC_NO_A102_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String brcNoA102NoAccent;
  @Column(name = "STC_CODE_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String stcCodeNoAccent;
  @Column(name = "TAX_CODE_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String taxCodeNoAccent;

  public static Company findById(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    Company company = session.find(Company.class, fileId);

    return company;
  }

  public static List<Company> findByIdIn(List<Integer> ids) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<Company> query = session.createQuery("select c from Company c where c.id in :ids",
      Company.class);
    query.setParameter("ids", ids);
    List<Company> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static Integer save(Company company) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    Integer generatedId = (Integer) session.save(company);
    trans.commit();

    return generatedId;
  }

  public static void deleteById(Integer id) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM Company c WHERE c.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
    if (this.groupType != null) {
      this.groupTypeNoAccent = VNCharacterUtils.removeAccent(this.groupType).toLowerCase();
    } else {
      this.groupTypeNoAccent = null;
    }
  }

  public void setSegment(String segment) {
    this.segment = segment;
    if (this.segment != null) {
      this.segmentNoAccent = VNCharacterUtils.removeAccent(this.segment).toLowerCase();
    } else {
      this.segmentNoAccent = null;
    }
  }

  public void setSectorL1(String sectorL1) {
    this.sectorL1 = sectorL1;
    if (this.sectorL1 != null) {
      this.sectorL1NoAccent = VNCharacterUtils.removeAccent(this.sectorL1).toLowerCase();
    } else {
      this.sectorL1NoAccent = null;
    }
  }

  public void setSectorL2(String sectorL2) {
    this.sectorL2 = sectorL2;
    if (this.sectorL2 != null) {
      this.sectorL2NoAccent = VNCharacterUtils.removeAccent(this.sectorL2).toLowerCase();
    } else {
      this.sectorL2NoAccent = null;
    }
  }

  public void setRegion(String region) {
    this.region = region;
    if (this.region != null) {
      this.regionNoAccent = VNCharacterUtils.removeAccent(this.region).toLowerCase();
    } else {
      this.regionNoAccent = null;
    }
  }

  public void setName(String name) {
    this.name = name;
    if (this.name != null) {
      this.nameNoAccent = VNCharacterUtils.removeAccent(this.name).toLowerCase();
    } else {
      this.nameNoAccent = null;
    }
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
    if (this.ticker != null) {
      this.tickerNoAccent = VNCharacterUtils.removeAccent(this.ticker).toLowerCase();
    } else {
      this.tickerNoAccent = null;
    }
  }

  public void setBrcNoA101(String brcNoA101) {
    this.brcNoA101 = brcNoA101;
    if (this.brcNoA101 != null) {
      this.brcNoA101NoAccent = VNCharacterUtils.removeAccent(this.brcNoA101).toLowerCase();
    } else {
      this.brcNoA101NoAccent = null;
    }
  }

  public void setBrcNoA102(String brcNoA102) {
    this.brcNoA102 = brcNoA102;
    if (this.brcNoA102 != null) {
      this.brcNoA102NoAccent = VNCharacterUtils.removeAccent(this.brcNoA102).toLowerCase();
    } else {
      this.brcNoA102NoAccent = null;
    }
  }

  public void setStcCode(String stcCode) {
    this.stcCode = stcCode;
    if (this.stcCode != null) {
      this.stcCodeNoAccent = VNCharacterUtils.removeAccent(this.stcCode).toLowerCase();
    } else {
      this.stcCodeNoAccent = null;
    }
  }

  public void setTaxCode(String taxCode) {
    this.taxCode = taxCode;
    if (this.taxCode != null) {
      this.taxCodeNoAccent = VNCharacterUtils.removeAccent(this.taxCode).toLowerCase();
    } else {
      this.taxCodeNoAccent = null;
    }
  }

}
