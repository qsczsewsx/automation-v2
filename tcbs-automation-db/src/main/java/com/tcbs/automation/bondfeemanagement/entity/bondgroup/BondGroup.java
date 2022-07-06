package com.tcbs.automation.bondfeemanagement.entity.bondgroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcbs.automation.bondfeemanagement.ApprovalEntity;
import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOND_GROUP")
@Getter
@Setter
@SqlResultSetMapping(
  name = "BondGroupIdMapping",
  classes = {
    @ConstructorResult(
      targetClass = BondGroupMapping.class,
      columns = {
        @ColumnResult(name = "GROUP_ID", type = Integer.class),
      })
  })
public class BondGroup extends ApprovalEntity {
  private static String SQL_UNION = "UNION ";

  @Id
  @Column(name = "GROUP_ID", updatable = false, nullable = false)
  private Integer groupId;

  @Column(name = "GROUP_NAME", nullable = false)
  private String groupName;

  @Column(name = "BONDTEMPLATE_CODE", nullable = false)
  private String bondTemplateCode;

  @Column(name = "BONDTEMPLATE_INDEX", updatable = false, nullable = false)
  private Integer bondTemplateIndex;

  @Column(name = "GROUP_STATUS")
  private String groupStatus;

  @OneToMany(
    mappedBy = "bondGroup",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @JsonIgnore
  private List<BondGroupMapping> bondGroupMappings = new ArrayList<>();

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table BOND_GROUP");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_GROUP where GROUP_ID = :groupId");
    query.setParameter("groupId", groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondGroup bondTimelineGroup) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondTimelineGroup);
    trans.commit();
  }

  @Step
  public static BondGroup getById(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondGroup> query = session.createNativeQuery("select * from BOND_GROUP where GROUP_ID = :groupId", BondGroup.class);
    query.setParameter("groupId", groupId);
    List<BondGroup> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BondGroup> findAllById(List<Integer> groupIds) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondGroup> query = session.createQuery("select btg from BondGroup btg where btg.groupId in :groupIds", BondGroup.class);
    query.setParameter("groupIds", groupIds);
    return query.getResultList();
  }


  @Step
  public static Integer getGroupHasMaxCountFees() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondGroup> query = session.createNativeQuery(
      "SELECT * FROM BOND_GROUP WHERE group_id IN (\n" +
        "    SELECT GROUP_ID FROM (SELECT GROUP_ID FROM (\n" +
        "        select GROUP_ID, count(*) as TL_COUNT from BOND_FEE \n" +
        "        where GROUP_ID in (select group_id from BOND_GROUP where status = 'ACTIVE')\n" +
        "        group by GROUP_ID\n" +
        "        ) ORDER BY TL_COUNT desc\n" +
        "    ) WHERE rownum = 1\n" +
        ") \n", BondGroup.class);
    List<BondGroup> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0).groupId;
  }

  public static List<BondGroup> searchBondGroupBy(String keyword, int pageIndex, int pageSize) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    StringBuilder stringBuilder = buildSearchClause(keyword, pageIndex, pageSize);
    org.hibernate.query.Query<BondGroup> query = session.createNativeQuery(stringBuilder.toString(), BondGroup.class);
    return query.getResultList();
  }

  public static long getTotalCount(String keyword) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    StringBuilder stringBuilder = buildCountClause(keyword);
    Query query = session.createNativeQuery(stringBuilder.toString());
    return ((BigDecimal) query.getSingleResult()).longValue();
  }

  private static StringBuilder buildSearchClause(String keyword, int pageIndex, int pageSize) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT btg2.* \n");
    stringBuilder.append("FROM (\n");
    stringBuilder.append(buildSearchInnerClause(keyword));
    stringBuilder.append(") so \n");
    stringBuilder.append("INNER JOIN BOND_GROUP btg2 ON so.GROUP_ID = btg2.GROUP_ID \n");
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
    stringBuilder.append("INNER JOIN BOND_GROUP btg2 ON so.GROUP_ID = btg2.GROUP_ID \n");

    return stringBuilder;
  }

  private static StringBuilder buildSearchInnerClause(String keyword) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT DISTINCT s.GROUP_ID FROM \n");
    stringBuilder.append("( \n");
    stringBuilder.append(buildSearchBondGroupNameClause(keyword));
    stringBuilder.append(SQL_UNION);
    stringBuilder.append(buildSearchBondTemplateCodeClause(keyword));
    stringBuilder.append(SQL_UNION);
    stringBuilder.append(buildSearchGroupCreatedByClause(keyword));
    stringBuilder.append(SQL_UNION);
    stringBuilder.append(buildSearchBondCodeInnerClause(keyword));
    stringBuilder.append(SQL_UNION);
    stringBuilder.append(buildSearchBondIssuerNameClause(keyword));
    stringBuilder.append(") s \n");

    return stringBuilder;
  }

  private static StringBuilder buildStringSelectBondGroup() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT btg.GROUP_ID \n");
    stringBuilder.append("FROM BOND_GROUP btg \n");

    return stringBuilder;
  }

  private static StringBuilder buildSearchBondGroupNameClause(String keyword) {
    StringBuilder stringBuilder = buildStringSelectBondGroup();
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' AND (UPPER(btg.GROUP_NAME) LIKE '%" + keyword + "%')  \n");

    return stringBuilder;
  }


  private static StringBuilder buildSearchBondTemplateCodeClause(String keyword) {
    StringBuilder stringBuilder = buildStringSelectBondGroup();
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' AND (UPPER(btg.BONDTEMPLATE_CODE) LIKE '%" + keyword + "%')   \n");

    return stringBuilder;
  }


  private static StringBuilder buildSearchGroupCreatedByClause(String keyword) {
    StringBuilder stringBuilder = buildStringSelectBondGroup();
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' AND (UPPER(btg.CREATED_BY) LIKE '%" + keyword + "%')   \n");

    return stringBuilder;
  }

  private static StringBuilder buildSearchBondCodeInnerClause(String keyword) {
    StringBuilder stringBuilder = buildStringSelectBondGroup();
    stringBuilder.append("INNER JOIN BOND_GROUP_MAPPING btgm ON btg.GROUP_ID = btgm.GROUP_ID \n");
    stringBuilder.append("INNER JOIN BOND_BASE_INFO bbi ON btgm.BOND_ID = bbi.BOND_ID \n");
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' \n");
    stringBuilder.append("GROUP BY btg.GROUP_ID \n");
    stringBuilder.append("HAVING count(CASE WHEN UPPER(bbi.BOND_CODE) LIKE '%" + keyword + "%' THEN bbi.BOND_CODE END) > 0 \n");

    return stringBuilder;
  }

  private static StringBuilder buildSearchBondIssuerNameClause(String keyword) {
    StringBuilder stringBuilder = buildStringSelectBondGroup();
    stringBuilder.append("INNER JOIN BOND_GROUP_MAPPING btgm ON btg.GROUP_ID = btgm.GROUP_ID \n");
    stringBuilder.append("INNER JOIN BOND_BASE_INFO bbi ON btgm.BOND_ID = bbi.BOND_ID \n");
    stringBuilder.append("WHERE btg.STATUS <> 'INACTIVE' \n");
    stringBuilder.append("GROUP BY btg.GROUP_ID \n");
    stringBuilder.append("HAVING count(CASE WHEN UPPER(bbi.ISSUER_NAME_NO_ACCENT) LIKE '%" + keyword + "%' THEN bbi.BOND_CODE END) > 0 \n");

    return stringBuilder;
  }
}
