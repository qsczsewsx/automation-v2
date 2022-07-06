package com.tcbs.automation.bondfeemanagement.entity.bondgroup;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import com.tcbs.automation.bondfeemanagement.entity.bond.BondBaseInfo;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "BOND_GROUP_MAPPING")
@Getter
@Setter
public class BondGroupMapping {
  private static String paramBondId = "bondId";
  private static String paramGroupId = "groupId";

  @EmbeddedId
  private BondGroupMappingId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("BOND_ID")
  @JoinColumn(name = "BOND_ID")
  private BondBaseInfo bondBaseInfo;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("GROUP_ID")
  @JoinColumn(name = "GROUP_ID")
  private BondGroup bondGroup;

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table BondGroupMapping");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static BondGroupMapping getById(BondGroupMapping.BondGroupMappingId id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondGroupMapping> query = session.createQuery("select btgm from BondGroupMapping btgm where btgm.id =:id",
      BondGroupMapping.class);
    query.setParameter("id", id);
    return query.uniqueResult();
  }

  @Step
  public static List<BondGroupMapping> getByBondIdsAndGroupId(List<Integer> bondIds, Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondGroupMapping> query = session.createNativeQuery("select * from BOND_GROUP_MAPPING btgm where BOND_ID in :bondIds AND GROUP_ID = :groupId",
      BondGroupMapping.class);
    query.setParameter("bondIds", bondIds);
    query.setParameter(paramGroupId, groupId);
    return query.getResultList();
  }

  @Step
  public static List<BondGroupMapping> getByGroupId(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondGroupMapping> query = session.createNativeQuery("select * from BOND_GROUP_MAPPING btgm where GROUP_ID = :groupId",
      BondGroupMapping.class);
    query.setParameter(paramGroupId, groupId);
    return query.getResultList();
  }

  @Step
  public static Integer getBondGroupId(Integer bondId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<?> query = session.createNativeQuery("select GROUP_ID from BOND_GROUP_MAPPING where BOND_ID = :bondId");
    query.setParameter(paramBondId, bondId);
    Object result = query.uniqueResult();
    if (result == null) {
      return null;
    }

    return ((BigDecimal) result).intValue();
  }

  @Step
  public static void insert(BondGroupMapping bondGroupMapping) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondGroupMapping);
    trans.commit();
  }

  @Step
  public static void deleteByBondId(Integer bondId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_GROUP_MAPPING where BOND_ID =:bondId");
    query.setParameter(paramBondId, bondId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByBondIdAndGroupId(Integer bondId, Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_GROUP_MAPPING where BOND_ID = :bondId and GROUP_ID = :groupId");
    query.setParameter(paramBondId, bondId);
    query.setParameter(paramGroupId, groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByGroupId(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_GROUP_MAPPING where GROUP_ID = :groupId");
    query.setParameter(paramGroupId, groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Embeddable
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  @Builder
  public static class BondGroupMappingId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "BOND_ID")
    private Integer bondId;

    @Column(name = "GROUP_ID")
    private Integer groupId;
  }
}
