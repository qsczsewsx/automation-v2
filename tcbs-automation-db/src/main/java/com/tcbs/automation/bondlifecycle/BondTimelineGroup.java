package com.tcbs.automation.bondlifecycle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcbs.automation.bondlifecycle.dto.BondTimelineGroupIdMapping;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOND_TIMELINE_GROUP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SqlResultSetMapping(
  name = "BondTimelineGroupIdMapping",
  classes = {
    @ConstructorResult(
      targetClass = BondTimelineGroupIdMapping.class,
      columns = {
        @ColumnResult(name = "GROUP_ID", type = Integer.class),
      })
  })
public class BondTimelineGroup extends ApprovalEntity {

  @Id
  @Column(name = "GROUP_ID")
  private Integer groupId;

  @OneToMany(
    mappedBy = "bondTimelineGroup",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @JsonIgnore
  private List<BondTimelineGroupMapping> bondTimelineGroupMappings = new ArrayList<>();

  @Column(name = "RULE_STATUS")
  private String ruleStatus;

  public BondTimelineGroup(Integer groupId) {
    this.groupId = groupId;
  }

  @Step
  public static void deleteById(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_GROUP where GROUP_ID = :groupId");
    query.setParameter("groupId", groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondTimelineGroup bondTimelineGroup) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondTimelineGroup);
    trans.commit();
  }

  @Step
  public static BondTimelineGroup getById(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineGroup> query = session.createNativeQuery("select * from BOND_TIMELINE_GROUP where GROUP_ID = :groupId", BondTimelineGroup.class);
    query.setParameter("groupId", groupId);
    List<BondTimelineGroup> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BondTimelineGroup> findAllById(List<Integer> groupIds) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineGroup> query = session.createQuery("select btg from BondTimelineGroup btg where btg.groupId in :groupIds", BondTimelineGroup.class);
    query.setParameter("groupIds", groupIds);
    return query.getResultList();
  }


  @Step
  public static Integer getGroupHasMaxCountTimelines() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineGroup> query = session.createNativeQuery(
      "SELECT * FROM BOND_TIMELINE_GROUP WHERE group_id IN (\n" +
        "    SELECT bond_timeline_group_id FROM (SELECT bond_timeline_group_id FROM (\n" +
        "        select bond_timeline_group_id, count(*) as TL_COUNT from bond_timeline \n" +
        "        where bond_timeline_group_id in (select group_id from bond_timeline_group where status = 'ACTIVE')\n" +
        "        group by bond_timeline_group_id\n" +
        "        ) ORDER BY TL_COUNT desc\n" +
        "    ) WHERE rownum = 1\n" +
        ") \n", BondTimelineGroup.class);
    List<BondTimelineGroup> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0).groupId;
  }

  public static List<BondTimelineGroupIdMapping> searchBondTimelineGroupBy(String keyword, int pageIndex, int pageSize) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    StringBuilder stringBuilder = buildSearchClause(keyword, pageIndex, pageSize);
    Query query = session.createNativeQuery(stringBuilder.toString(), "BondTimelineGroupIdMapping");
    @SuppressWarnings("unchecked")
    List<BondTimelineGroupIdMapping> results = query.getResultList();

    return results;
  }

  public static long getTotalCount(String keyword) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    StringBuilder stringBuilder = buildCountClause(keyword);
    Query query = session.createNativeQuery(stringBuilder.toString());
    return ((BigDecimal) query.getSingleResult()).longValue();
  }

  private static StringBuilder buildSearchClause(String keyword, int pageIndex, int pageSize) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT btg2.GROUP_ID \n");
    stringBuilder.append("FROM (\n");
    stringBuilder.append(buildSearchInnerClause(keyword));
    stringBuilder.append(") so \n");
    stringBuilder.append("INNER JOIN BOND_TIMELINE_GROUP btg2 ON so.GROUP_ID = btg2.GROUP_ID \n");
    stringBuilder.append("ORDER BY btg2.GROUP_ID DESC \n");

    int rowBegin = (pageIndex - 1) * pageSize;
    stringBuilder.append("OFFSET " + rowBegin + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY \n");

    return stringBuilder;
  }

  private static StringBuilder buildCountClause(String keyword) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT count(*) \n");
    stringBuilder.append("FROM (\n");
    stringBuilder.append(buildSearchInnerClause(keyword));
    stringBuilder.append(") so \n");
    stringBuilder.append("INNER JOIN BOND_TIMELINE_GROUP btg2 ON so.GROUP_ID = btg2.GROUP_ID \n");

    return stringBuilder;
  }

  private static StringBuilder buildSearchInnerClause(String keyword) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT DISTINCT s.GROUP_ID FROM \n");
    stringBuilder.append("( \n");
    stringBuilder.append(buildSearchBondCodeInnerClause(keyword));
    stringBuilder.append("UNION \n");
    stringBuilder.append(buildSearchCreatedByAndApprovalStatusInnerClause(keyword));
    stringBuilder.append(") s \n");

    return stringBuilder;

  }

  private static StringBuilder buildSearchBondCodeInnerClause(String keyword) {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("SELECT btg.GROUP_ID \n");
    stringBuilder.append("FROM BOND_TIMELINE_GROUP btg \n");
    stringBuilder.append("INNER JOIN BOND_TIMELINE_GROUP_MAPPING btgm ON btg.GROUP_ID = btgm.BOND_TIMELINE_GROUP_ID \n" +
      "INNER JOIN BOND_BASE_INFO bbi ON btgm.BOND_ID = bbi.BOND_ID \n");
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' \n");
    stringBuilder.append("GROUP BY btg.GROUP_ID \n");
    stringBuilder.append("HAVING count(CASE WHEN UPPER(bbi.BOND_CODE) LIKE \n"
      + "'%" + keyword + "%'"
      + "THEN bbi.BOND_CODE END) > 0 \n");

    return stringBuilder;

  }

  private static StringBuilder buildSearchCreatedByAndApprovalStatusInnerClause(String keyword) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT btg.GROUP_ID \n");
    stringBuilder.append("FROM BOND_TIMELINE_GROUP btg \n");
    stringBuilder.append("INNER JOIN REFERENCE_DATA rd ON btg.APPROVAL_STATUS = rd.CODE \n");
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' AND (UPPER(btg.CREATED_BY) LIKE '%" + keyword + "%') \n");

    return stringBuilder;
  }
}
