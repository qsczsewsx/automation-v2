package com.tcbs.automation.ixu;

import com.google.common.collect.Maps;
import com.tcbs.automation.SqlUtils;
import joptsimple.internal.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.apache.commons.codec.binary.Hex;
import org.assertj.core.util.Lists;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IXU_GENERAL_LEDGER")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuGeneralLedgerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBSID")
  private String tcbsId;

  @Column(name = "RANKING_POINT")
  private Long rankingPoint;

  @Column(name = "REDEEMABLE_POINT")
  private Long redeemablePoint;

  @Column(name = "BLOCK_POINT")
  private Long blockPoint;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Column(name = "CHECKSUM")
  private String checksum;

  @Column(name = "MEMBERSHIP_TYPE_ID")
  private Long membershipTypeId;

  @Column(name = "CODE_105C")
  private String code105C;

  @Column(name = "CUSTOMER_FULL_NAME")
  private String customerFullName;

  @Step
  public static List<IxuGeneralLedgerEntity> findIxuGeneralLedgerByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<IxuGeneralLedgerEntity> query = ixuDbConnection.getSession().createQuery(
      "from IxuGeneralLedgerEntity a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<IxuGeneralLedgerEntity> ixuGeneralLedgerEntityList = query.getResultList();
    ixuDbConnection.closeSession();
    return ixuGeneralLedgerEntityList;
  }

  @Step
  public static void saveOrUpdate(IxuGeneralLedgerEntity ixuGeneralLedgerEntity) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    IxuGeneralLedgerEntity entity = session.get(IxuGeneralLedgerEntity.class, ixuGeneralLedgerEntity.getId());
    entity.setRankingPoint(ixuGeneralLedgerEntity.getRankingPoint());
    entity.setRedeemablePoint(ixuGeneralLedgerEntity.getRedeemablePoint());
    entity.setBlockPoint(ixuGeneralLedgerEntity.getBlockPoint());
    entity.setMembershipTypeId(ixuGeneralLedgerEntity.getMembershipTypeId());
    entity.setChecksum(ixuGeneralLedgerEntity.getChecksum());
    session.saveOrUpdate(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateByTcbsId(Long redeemablePoint, String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "update IxuGeneralLedgerEntity i set i.redeemablePoint=:redeemablePoint where i.tcbsId=:tcbsId"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("redeemablePoint", redeemablePoint);
    int numberRowDeleted = query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateByTcbsIdWithCorrectChecksum(Long redeemablePoint, String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query<IxuGeneralLedgerEntity> query = ixuDbConnection.getSession().createQuery(
      "from IxuGeneralLedgerEntity a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<IxuGeneralLedgerEntity> r = query.getResultList();
    IxuGeneralLedgerEntity entity = null;
    if (r.isEmpty()) {
      entity = new IxuGeneralLedgerEntity();
      entity.setTcbsId(tcbsId);
      entity.setRedeemablePoint(0l);
      entity.setBlockPoint(0l);
      entity.setRankingPoint(0l);
      entity.setMembershipTypeId(1l);
    } else {
      entity = r.get(0);
    }
    entity.setRedeemablePoint(redeemablePoint);

    String checksum = createCheckSum(entity);

    Query<ManualTransactionEntity> queryUpdate = ixuDbConnection.getSession().createQuery(
      "update IxuGeneralLedgerEntity i set i.redeemablePoint=:redeemablePoint, i.checksum=:checksum where i.tcbsId=:tcbsId"
    );
    queryUpdate.setParameter("tcbsId", tcbsId);
    queryUpdate.setParameter("redeemablePoint", redeemablePoint);
    queryUpdate.setParameter("checksum", checksum);
    int numberRowDeleted = queryUpdate.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  private static String createCheckSum(IxuGeneralLedgerEntity entity) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      String s = String.format("%s:%s:%.2f:%.2f:%.2f:%d", "Tcbs@20022020", entity.getTcbsId(),
        entity.getRankingPoint().doubleValue(), entity.getRedeemablePoint().doubleValue(),
        entity.getBlockPoint().doubleValue(), entity.getMembershipTypeId());
      byte[] encodedhash = digest.digest(s.getBytes());
      return Hex.encodeHexString(encodedhash);
    } catch (Exception ex) {
      return "";
    }
  }

  @Step
  public static void updateMembershipTypeByTcbsId(int type, String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "update IxuGeneralLedgerEntity i set i.membershipTypeId =:memberId where i.tcbsId=:tcbsId"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("memberId", type);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IxuGeneralLedgerEntity> findIxuGeneralLedgerByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("select * from IXU_GENERAL_LEDGER a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(tcbsId, "TCBSID"));
    Query<IxuGeneralLedgerEntity> query = session.createNativeQuery(stringBuilder.toString(), IxuGeneralLedgerEntity.class);
    List<IxuGeneralLedgerEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static void clearGL(String tcbsId) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }

    Query query = ixuDbConnection.getSession().createQuery("delete from IxuGeneralLedgerEntity gl where gl.tcbsId=:tcbsid");
    query.setParameter("tcbsid", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static int removeIxuGeneralLedger(String tcbsid) {
    Transaction transaction = ixuDbConnection.getSession().beginTransaction();
    Query<IxuGeneralLedgerEntity> query = ixuDbConnection.getSession().createQuery(
      "delete from IxuGeneralLedgerEntity a where a.tcbsId=:id");
    query.setParameter("id", tcbsid);
    int rs = query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
    return rs;
  }

  @Step
  public static void insertNewTcbsId(String tcbsId, Double rankingPoint, Double redeemablePoint, String checksum, int membershipTypeId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    javax.persistence.Query query = ixuDbConnection.getSession().createNativeQuery(String.format(
      "insert into IXU_GENERAL_LEDGER (TCBSID, RANKING_POINT, REDEEMABLE_POINT, CREATED_DATE, UPDATED_DATE,CHECKSUM, MEMBERSHIP_TYPE_ID)  values('%s', %.2f, %.2f, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '%s',%d) ",
      tcbsId, rankingPoint, redeemablePoint, checksum, membershipTypeId));
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static void clearGeneralLedgerHistory(List<String> referralLocation, List<String> referenceId, String action, String source) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createNativeQuery("delete from GENERALLEDGER a where " +
      "a.REFERENCELOCATION in :referralLocation and " +
      "a.ACTION = :action and a.REFERENCEID in :referenceId and a.SOURCE = :source");
    query.setParameter("referralLocation", referralLocation);
    query.setParameter("action", action);
    query.setParameter("referenceId", referenceId);
    query.setParameter("source", source);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public List<HashMap<String, String>> searchIxuCustomer(Map<String, Object> request) {
    String sqlQuery = " select v.TCBSID, v.RANKING_POINT, v.REDEEMABLE_POINT, i.NAME , trunc(v.UPDATED_DATE), "
      + " count(*) over () as TOTAL_RECORDS" +
      " FROM IXU_GENERAL_LEDGER v " +
      " LEFT JOIN IXU_MEMBERSHIP_TYPE i ON i.ID =  v.MEMBERSHIP_TYPE_ID ";

    StringBuilder condition = new StringBuilder();

    condition = buildConditionQuery(request, condition);

    if (condition.length() != 0) {
      sqlQuery += " WHERE " + condition.toString();
    }
    sqlQuery += " order by v.CREATED_DATE desc ";

    int page = 1;
    int size = 20;
    if (request.get("page") != null && Integer.valueOf(request.get("page").toString()) > 0) {
      page = Integer.valueOf(request.get("page").toString());
    }
    if (request.get("size") != null && Integer.valueOf(request.get("size").toString()) > 0) {
      size = Integer.valueOf(request.get("size").toString());
    }
    int offset = (page - 1) * size;
    sqlQuery += " offset :zOffset rows fetch next :zSize rows only ";

    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    NativeQuery query = session.createSQLQuery(sqlQuery);

    query.setParameter("zOffset", offset);
    query.setParameter("zSize", size);

    List<Object[]> results = query.getResultList();
    String[] responseField = {"tcbs_id", "ranking_amount", "redeem_amount", "rank", "changed_time", "total"};

    List<HashMap<String, String>> response = Lists.newArrayList();

    if (!results.isEmpty()) {
      results.forEach(result -> {
        HashMap<String, String> map = Maps.newHashMap();
        for (int i = 0; i < responseField.length; i++) {
          map.put(responseField[i], result[i].toString());
        }
        response.add(map);
      });
    }
    ixuDbConnection.closeSession();
    return response;
  }

  @Step
  public Long countIxuCustomer(Map<String, Object> request) {
    String sqlQuery = " select count(*) " +
      " FROM IXU_GENERAL_LEDGER v " +
      " LEFT JOIN IXU_MEMBERSHIP_TYPE i ON i.ID =  v.MEMBERSHIP_TYPE_ID ";

    StringBuilder condition = new StringBuilder();

    condition = buildConditionQuery(request, condition);

    if (condition.length() != 0) {
      sqlQuery += " WHERE " + condition.toString();
    }
    sqlQuery += " order by v.CREATED_DATE desc ";
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();

    NativeQuery query = session.createSQLQuery(sqlQuery);
    BigDecimal result = (BigDecimal) query.getSingleResult();
    if (result != null) {
      ixuDbConnection.closeSession();
      return result.longValue();
    } else {
      ixuDbConnection.closeSession();
      return BigDecimal.ZERO.longValue();
    }
  }

  private StringBuilder buildConditionQuery(Map<String, Object> request, StringBuilder condition) {

    condition = getQueryListRequest(request, condition);

    if (request.get("ranking_amount_min") != null && request.get("ranking_amount_min") != Strings.EMPTY) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.RANKING_POINT >= %s", request.get("ranking_amount_min")));
    }

    if (request.get("ranking_amount_max") != null && request.get("ranking_amount_max") != Strings.EMPTY) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.RANKING_POINT <= %s", request.get("ranking_amount_max")));
    }

    if (request.get("redeem_amount_min") != null && request.get("redeem_amount_min") != Strings.EMPTY) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.REDEEMABLE_POINT >= %s", request.get("redeem_amount_min")));
    }

    if (request.get("redeem_amount_max") != null && request.get("redeem_amount_max") != Strings.EMPTY) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.REDEEMABLE_POINT <= %s", request.get("redeem_amount_max")));
    }

    if (request.get("changed_time_from") != null) {
      buildQueryWithCondition(condition);

      condition.append(String.format(" trunc(v.UPDATED_DATE) >= to_date('%s', 'dd/MM/yyyy') ",
        request.get("changed_time_from").toString()));
    }

    if (request.get("changed_time_to") != null) {
      buildQueryWithCondition(condition);

      condition.append(String.format(" trunc(v.UPDATED_DATE) <= to_date('%s', 'dd/MM/yyyy') ",
        request.get("changed_time_to").toString()));
    }
    return condition;
  }

  private StringBuilder getQueryListRequest(Map<String, Object> request, StringBuilder condition) {
    if (request.get("tcbs_id") != null) {
      String[] tcbsIds = (String[]) request.get("tcbs_id");
      if (tcbsIds.length > 0) {
        buildQueryWithCondition(condition);
        condition.append(" LOWER(v.TCBSID) IN (?)");
        condition = getConditionListString(tcbsIds, condition, true);
      }
    }

    if (request.get("rank") != null) {
      String[] ranks = (String[]) request.get("rank");

      if (ranks.length > 0) {
        buildQueryWithCondition(condition);
        condition.append(" LOWER(i.NAME) IN (?)");
        condition = getConditionListString(ranks, condition, true);
      }
    }
    return condition;
  }

  private void buildQueryWithCondition(StringBuilder condition) {
    if (condition.length() != 0) {
      condition.append(" AND  ");
    }
  }

  private StringBuilder getConditionListString(String[] lists, StringBuilder condition, boolean toLower) {
    String[] arr = new String[lists.length];
    for (int i = 0; i < lists.length; i++) {
      String list = lists[i];
      if (toLower) {
        arr[i] = "'" + list.toLowerCase() + "'";
      } else {
        arr[i] = "'" + list + "'";
      }
    }
    String result = condition.toString().replace("?", Strings.join(arr, ","));
    return new StringBuilder(result);
  }

  public static void insertNewTcbsId(String tcbsId, Double rankingPoint, Double redeemablePoint, String checksum, int membershipTypeId, Double blockPoint) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    javax.persistence.Query query = ixuDbConnection.getSession().createNativeQuery(String.format(
      "insert into IXU_GENERAL_LEDGER (TCBSID, RANKING_POINT, REDEEMABLE_POINT, CREATED_DATE, UPDATED_DATE,CHECKSUM, MEMBERSHIP_TYPE_ID, BLOCK_POINT)  values('%s', %.2f, %.2f, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '%s', %d, %.2f) ",
      tcbsId, rankingPoint, redeemablePoint, checksum, membershipTypeId, blockPoint));
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
