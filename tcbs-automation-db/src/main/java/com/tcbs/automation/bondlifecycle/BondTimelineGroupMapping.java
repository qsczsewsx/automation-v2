package com.tcbs.automation.bondlifecycle;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "BOND_TIMELINE_GROUP_MAPPING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondTimelineGroupMapping extends BaseEntity {

  @EmbeddedId
  private BondTimelineGroupMappingId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("BOND_ID")
  @JoinColumn(name = "BOND_ID")
  private BondBaseInfo bondBaseInfo;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("BOND_TIMELINE_GROUP_ID")
  @JoinColumn(name = "BOND_TIMELINE_GROUP_ID")
  private BondTimelineGroup bondTimelineGroup;

  @Step
  public static BondTimelineGroupMapping getById(BondTimelineGroupMappingId id) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineGroupMapping> query = session.createQuery("select btgm from BondTimelineGroupMapping btgm where id =:id",
      BondTimelineGroupMapping.class);
    query.setParameter("id", id);
    return query.uniqueResult();
  }

  @Step
  public static List<BondTimelineGroupMapping> getByBondIdsAndGroupId(List<Integer> bondIds, Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineGroupMapping> query = session.createNativeQuery("select * from BOND_TIMELINE_GROUP_MAPPING btgm where BOND_ID in :bondIds AND BOND_TIMELINE_GROUP_ID = :groupId",
      BondTimelineGroupMapping.class);
    query.setParameter("bondIds", bondIds);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

  @Step
  public static Integer getBondTimelimeGroupId(Integer bondId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<?> query = session.createNativeQuery("select BOND_TIMELINE_GROUP_ID from BOND_TIMELINE_GROUP_MAPPING where BOND_ID = :bondId");
    query.setParameter("bondId", bondId);
    Object result = query.uniqueResult();
    if (result == null) {
      return null;
    }

    return ((BigDecimal) result).intValue();
  }

  @Step
  public static void insert(BondTimelineGroupMapping bondTimelineGroupMapping) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondTimelineGroupMapping);
    trans.commit();
  }

  @Step
  public static void deleteByBondId(Integer bondId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_GROUP_MAPPING where BOND_ID =:bondId");
    query.setParameter("bondId", bondId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByBondIdAndGroupId(Integer bondId, Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_GROUP_MAPPING where BOND_ID = :bondId and BOND_TIMELINE_GROUP_ID = :groupId");
    query.setParameter("bondId", bondId);
    query.setParameter("groupId", groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByGroupId(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_GROUP_MAPPING where BOND_TIMELINE_GROUP_ID = :groupId");
    query.setParameter("groupId", groupId);
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
  public static class BondTimelineGroupMappingId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "BOND_ID")
    private Integer bondId;

    @Column(name = "BOND_TIMELINE_GROUP_ID")
    @JsonProperty("groupId")
    private Integer bondTimelineGroupId;
  }
}
