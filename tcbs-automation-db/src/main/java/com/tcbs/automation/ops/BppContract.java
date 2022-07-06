package com.tcbs.automation.ops;

import com.tcbs.automation.enumerable.orion.hercules.*;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BPP_CONTRACT")
public class BppContract {

  private static final long serialVersionUID = 1L;
  private static String TRADING_CODE = "tradingCode";
  private static String STATUS = "status";
  private static String PLEDGE_ID = "pledgeID";
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPP_CONTRACT_SEQ")
  @SequenceGenerator(sequenceName = "BPP_CONTRACT_SEQ", allocationSize = 1, name = "BPP_CONTRACT_SEQ")
  private Integer id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "BLOCK_ID", referencedColumnName = "ID")
  private BppPledge block;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "UNBLOCK_ID", referencedColumnName = "ID")
  private BppPledge unblock;

  @Column(name = "TRADING_CODE")
  private String tradingCode;

  @Column(name = "LISTED_CODE")
  private String listedCode;

  @Column(name = "BOND_NAME")
  private String bondName;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "BOND_TYPE")
  @Convert(converter = BondTypeConverter.class)
  private BondType bondType;

  @Column(name = "ISSUER")
  private String issuer;

  @Column(name = "ISSUE_DATE")
  @Temporal(TemporalType.DATE)
  private Date issueDate;

  @Column(name = "MATURITY_DATE")
  @Temporal(TemporalType.DATE)
  private Date maturityDate;

  @Column(name = "PRINCIPAL")
  private Integer principal;

  @Column(name = "QUANTITY")
  private Integer quantity;

  @Column(name = "PAR_ACCOUNT_NAME")
  private String parAccountName;

  @Column(name = "PAR_ACCOUNT_NUM")
  private String parAccountNum;

  @Column(name = "PAR_BANK")
  private String parBank;

  @Column(name = "PAR_BANK_CODE")
  private String parBankCode;

  @Column(name = "PAR_BRANCH")
  private String parBranch;

  @Column(name = "PAR_CITAD_CODE")
  private String parCitadCode;

  @Column(name = "PAR_PROVINCE")
  private String parProvince;

  @Column(name = "COUPON_ACCOUNT_NAME")
  private String couponAccountName;

  @Column(name = "COUPON_ACCOUNT_NUM")
  private String couponAccountNum;

  @Column(name = "COUPON_BANK")
  private String couponBank;

  @Column(name = "COUPON_BANK_CODE")
  private String couponBankCode;

  @Column(name = "COUPON_BRANCH")
  private String couponBranch;

  @Column(name = "COUPON_CITAD_CODE")
  private String couponCitadCode;

  @Column(name = "COUPON_PROVINCE")
  private String couponProvince;

  @Column(name = "SELL_ACCOUNT_NAME")
  private String sellAccountName;

  @Column(name = "SELL_ACCOUNT_NUM")
  private String sellAccountNum;

  @Column(name = "SELL_BANK")
  private String sellBank;

  @Column(name = "SELL_BANK_CODE")
  private String sellBankCode;

  @Column(name = "SELL_BRANCH")
  private String sellBranch;

  @Column(name = "SELL_CITAD_CODE")
  private String sellCitadCode;

  @Column(name = "SELL_PROVINCE")
  private String sellProvince;

  @Column(name = "BLOCK_NOTE")
  private String blockNote;

  @Column(name = "UNBLOCK_NOTE")
  private String unblockNote;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ContractStatus status;

  @Column(name = "LISTED_UPDATED_STATUS")
  @Enumerated(EnumType.STRING)
  private ListedUpdatedStatus listedUpdatedStatus;

  @Column(name = "CONTRACT_VALUE")
  private Double contractValue;

  @Column(name = "DISBURSEMENT_CONTRACT_VALUE")
  private Double disbursementContractValue;

  @Column(name = "MATCHING_DATE")
  private Date matchingDate;

  @Column(name = "STOCK_EXCHANGE")
  @Enumerated(EnumType.STRING)
  private StockExchange stockExchange;

  @Column(name = "DOCUMENT_REJECTED_REASON")
  private String documentRejectedReason;

  @Column(name = "LISTED_DATE")
  @Temporal(TemporalType.DATE)
  private Date listedDate;

  @Column(name = "DEPOSITORY_STATUS")
  private String depositoryStatus;

  public static BppContract getInitBlock(String tradingCode) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c where c.tradingCode=:tradingCode",
      BppContract.class
    );
    query.setParameter(TRADING_CODE, tradingCode);
    BppContract res = query.getSingleResult();
    return res;
  }

  public static void deleteInitBlock(String tradingCode) {
    OPS.opsConnection.getSession().clear();
    OPS.opsConnection.getSession().beginTransaction();

    Query query = OPS.opsConnection.getSession().createQuery(
      "delete from BppContract ar where ar.tradingCode=:tradingCode");

    query.setParameter(TRADING_CODE, tradingCode);
    query.executeUpdate();

    OPS.opsConnection.getSession().getTransaction().commit();
  }

  /**
   * Update status in case "Kênh sản phẩm";"Không theo sản phẩm"
   *
   * @param idList
   */
  public static void updateListedStatusAndStatus(List<Integer> idList, String status, String listedUpdatedStatus) {
    if (idList.isEmpty()) {
      return;
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();
    try {
      session.beginTransaction();
      int size = idList.size();
      for (int i = 0; i < size; i++) {
        BppContract contract = session.get(BppContract.class, idList.get(i));
        if (status != null & !status.isEmpty()) {
          contract.setStatus(ContractStatus.fromValue(status));
        }
        if (listedUpdatedStatus != null && !listedUpdatedStatus.isEmpty()) {
          contract.setListedUpdatedStatus(ListedUpdatedStatus.fromValue(listedUpdatedStatus));
        }
      }
      session.getTransaction().commit();

    } finally {
//            session.close();
    }
  }

  public static List<BppContract> advancedSearch(Set<String> tcbsIds, String listedCodeOrBondCode,
                                                 String tradingCode, String pledgeCode, String status,
                                                 Date blockFromDate, Date blockToDate, Date unblockFromDate,
                                                 Date unblockToDate, String maker, String checker, BondType bondType) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "SELECT c FROM BppContract c LEFT JOIN FETCH c.unblock " +
        " WHERE (:tradingCode IS NULL OR LOWER(c.tradingCode) LIKE CONCAT('%',:tradingCode,'%')) " +
        " AND (:status IS NULL " +
        " OR (:status = 'phongtoa' AND c.status IN (com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_PHONG_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_UPLOAD_SCAN_PHONG_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.HOAN_THANH_PHONG_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_XAC_NHAN_GIAI_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_YEU_CAU_GIAI_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.TU_CHOI_GIAI_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.XAC_NHAN_TT_GIAI_TOA)) " +
        " OR (:status = 'giaitoa' AND c.status IN (com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_GIAI_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_UPLOAD_SCAN_GIAI_TOA, " +
        "                                           com.tcbs.automation.enumerable.orion.hercules.ContractStatus.HOAN_THANH_GIAI_TOA))) " +
        " AND (:pledgeCode IS NULL OR LOWER(COALESCE(c.block.pledgeCode,'')) LIKE CONCAT('%',:pledgeCode,'%')) " +
        " AND (:listedCodeOrBondCode IS NULL OR LOWER(COALESCE(c.listedCode,'')) LIKE CONCAT('%',:listedCodeOrBondCode,'%') OR LOWER(COALESCE(c.bondCode,'')) LIKE CONCAT('%',:listedCodeOrBondCode,'%')) "
        +
        " AND ((:blockFromDate IS NULL OR c.block.executedDate >= :blockFromDate) " +
        " AND (:blockToDate IS NULL OR c.block.executedDate <= :blockToDate)) " +
        " AND ((:unblockFromDate IS NULL OR c.unblock.executedDate >= :unblockFromDate) " +
        " AND (:unblockToDate IS NULL OR c.unblock.executedDate <= :unblockToDate)) " +
        " AND ('-1' IN (:tcbsIds) OR COALESCE(c.block.tcbsId,'') IN (:tcbsIds)) " +
        " AND (:maker IS NULL OR (LOWER(c.unblock.maker) LIKE CONCAT('%',:maker,'%')) " +
        "       OR (LOWER(c.block.maker) LIKE CONCAT('%',:maker,'%'))) " +
        " AND (:checker IS NULL OR (LOWER(c.unblock.checker) LIKE CONCAT('%',:checker,'%')) " +
        "       OR (LOWER(c.block.checker) LIKE CONCAT('%',:checker,'%'))) " +
        " AND c.status NOT IN (com.tcbs.automation.enumerable.orion.hercules.ContractStatus.HUY_YEU_CAU, " +
        "                   com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_XAC_NHAN_PHONG_TOA," +
        "                   com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_YEU_CAU_PHONG_TOA, " +
        "                   com.tcbs.automation.enumerable.orion.hercules.ContractStatus.TU_CHOI_PHONG_TOA)" +
        " AND ((COALESCE(:bondType, NULL) IS NULL OR c.bondType IN :bondType)) ",
      BppContract.class
    );
    query.setParameter("tcbsIds", tcbsIds);
    query.setParameter("listedCodeOrBondCode", listedCodeOrBondCode);
    query.setParameter(TRADING_CODE, tradingCode);
    query.setParameter("pledgeCode", pledgeCode);
    query.setParameter(STATUS, status);
    query.setParameter("blockFromDate", blockFromDate);
    query.setParameter("blockToDate", blockToDate);
    query.setParameter("unblockFromDate", unblockFromDate);
    query.setParameter("unblockToDate", unblockToDate);
    query.setParameter("maker", maker);
    query.setParameter("checker", checker);
    query.setParameter("bondType", bondType);
    return query.getResultList();
  }

  public static Integer save(BppContract contract) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Integer generatedId = (Integer) session.save(contract);
    session.getTransaction().commit();

    return generatedId;
  }

  public static void remove(BppContract contractEntity) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.remove(contractEntity);
    session.getTransaction().commit();
  }

  public static void removeCascade(List<Integer> listGeneratedId) {
    Session session = OPS.opsConnection.getSession();
    session.clear();

    session.beginTransaction();
    for (int id : listGeneratedId) {
      BppContract contract = session.get(BppContract.class, id);
      if (contract != null) {
        session.remove(contract);
      }
    }
    session.getTransaction().commit();
  }

  public static void removeListContract(List<BppContract> contracts) {
    if (contracts == null || contracts.isEmpty()) {
      return;
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();

    session.beginTransaction();
    for (BppContract contract : contracts) {
      session.remove(contract);
    }
    session.getTransaction().commit();
  }

  public static List<BppContract> getByIds(List<Integer> contractIds) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c where c.id IN (:contractIds)",
      BppContract.class
    );
    query.setParameter("contractIds", contractIds);
    List<BppContract> res = query.getResultList();
    return res;
  }

  public static BppContract getById(Integer contractId) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c where c.id = :contractId",
      BppContract.class
    );
    query.setParameter("contractId", contractId);
    BppContract res = query.getSingleResult();
    return res;
  }

  public static BppContract getByTradingCodeAndStatus(String tradingCode, ContractStatus status) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c where c.tradingCode=:tradingCode and c.status = :status",
      BppContract.class
    );
    query.setParameter(TRADING_CODE, tradingCode);
    query.setParameter(STATUS, status);
    BppContract res = query.getSingleResult();
    return res;
  }

  public static void deleteByTradingCodeAndStatus(String tradingCode, ContractStatus status) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    session.beginTransaction();

    Query query = session.createQuery(
      "delete from BppContract c where c.tradingCode=:tradingCode and c.status = :status");

    query.setParameter(TRADING_CODE, tradingCode);
    query.setParameter(STATUS, status);
    query.executeUpdate();

    session.getTransaction().commit();
  }

  public static void deleteByTradingCode(String tradingCode) {
    if (tradingCode == null) {
      return;
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }

    Query<BppContract> getContractsById = session.createQuery("SELECT c FROM BppContract c WHERE c.tradingCode = :tradingCode", BppContract.class);
    getContractsById.setParameter(TRADING_CODE, tradingCode);
    List<BppContract> contract = getContractsById.getResultList();
    if (contract.size() > 0) {
      contract.forEach(bppContract -> {
        session.remove(bppContract);
      });
    }

    session.getTransaction().commit();
  }

  public static void testQuerry() {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<BppContract> testQuerry = session
      .createQuery("SELECT c FROM BppContract c LEFT JOIN FETCH c.unblock WHERE c.block.id = '1913'", BppContract.class);
    List<BppContract> results = testQuerry.getResultList();
    System.out.println(results.get(0).getBondName());
    System.out.println(results.get(0).getBlock().getPhone());
  }

  public static void removeByBondCode(String bondCode) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }

    Query<BppContract> getContractsById = session.createQuery("SELECT c FROM BppContract c WHERE c.bondCode = :bondCode", BppContract.class);
    getContractsById.setParameter("bondCode", bondCode);
    List<BppContract> contract = getContractsById.getResultList();
    if (contract.size() > 0) {
      contract.forEach(bppContract -> {
        session.remove(bppContract);
      });
    }

    session.getTransaction().commit();
  }

  public static void removeByBondCodes(List<String> bondCodes) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }

    Query<BppContract> getContractsById = session.createQuery("SELECT c FROM BppContract c WHERE c.bondCode IN (:bondCodes)", BppContract.class);
    getContractsById.setParameter("bondCodes", bondCodes);
    List<BppContract> contract = getContractsById.getResultList();
    if (contract.size() > 0) {
      contract.forEach(bppContract -> {
        session.remove(bppContract);
      });
    }

    session.getTransaction().commit();
  }

  public static void updateStatusByID(Integer id, ContractStatus contractStatus, ListedUpdatedStatus listedUpdatedStatus) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    BppContract contract = session.get(BppContract.class, id);
    contract.setStatus(contractStatus);
    contract.setListedUpdatedStatus(listedUpdatedStatus);
    session.update(contract);
    session.getTransaction().commit();
  }

  public static void updateStatusAndBTByID(Integer id, ContractStatus contractStatus, ListedUpdatedStatus listedUpdatedStatus, BondType bondType) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    BppContract contract = session.get(BppContract.class, id);
    contract.setStatus(contractStatus);
    contract.setListedUpdatedStatus(listedUpdatedStatus);
    contract.setBondType(bondType);
    session.update(contract);
    session.getTransaction().commit();
  }

  public static List<BppContract> getPledgeByID(Integer pledgeID) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c LEFT JOIN FETCH c.block where c.block.id = :pledgeID",
      BppContract.class
    );
    query.setParameter(PLEDGE_ID, pledgeID);
    List<BppContract> res = query.getResultList();
    return res;
  }

  public static List<BppContract> getUnBlockByID(Integer pledgeID) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c LEFT JOIN FETCH c.unblock where c.unblock.id = :pledgeID",
      BppContract.class
    );
    query.setParameter(PLEDGE_ID, pledgeID);
    List<BppContract> res = query.getResultList();
    return res;
  }

  public static List<BppContract> getContractID(Integer pledgeID) {
    OPS.opsConnection.getSession().clear();

    Query<BppContract> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppContract c LEFT JOIN FETCH c.block where c.id = :pledgeID",
      BppContract.class
    );
    query.setParameter(PLEDGE_ID, pledgeID);
    List<BppContract> res = query.getResultList();
    return res;
  }

  /**
   * Get unblock contract by RM.
   *
   * @param pledgeId
   * @return List of BppContract
   */
  public static List<BppContract> findByBlockIdAndStatusInAndListedUpdatedStatusIn(Integer pledgeId) {
    Session session = null;
    List<BppContract> bppContractList = new ArrayList<>();
    try {
      session = OPS.opsConnection.getSession();
      session.clear();

      Query<BppContract> query = session.createQuery(
        "SELECT c FROM BppContract c WHERE c.block.id = :pledgeID AND c.status IN (com.tcbs.automation.enumerable.orion.hercules.ContractStatus.HOAN_THANH_PHONG_TOA) "
          +
          "AND (c.listedUpdatedStatus IS NULL " +
          "       OR c.listedUpdatedStatus IN (com.tcbs.automation.enumerable.orion.hercules.ListedUpdatedStatus.HOAN_THANH_BO_SUNG_HO_SO))");

      query.setParameter(PLEDGE_ID, pledgeId);
      bppContractList = query.getResultList();
    } finally {
//            session.close();
    }

    return bppContractList;
  }

  /**
   * Get unblock status contract by status and blockID.
   *
   * @param pledgeId
   * @return List of BppContract
   */
  public static List<BppContract> findByStatusIn(Integer pledgeId, List<ContractStatus> contractStatuses) {
    Session session = null;
    List<BppContract> bppContractList = new ArrayList<>();
    try {
      session = OPS.opsConnection.getSession();
      session.clear();

      Query<BppContract> query = session.createQuery("SELECT c FROM BppContract c WHERE c.block.id = :pledgeID AND c.status IN (:contractStatuses) ");

      query.setParameter(PLEDGE_ID, pledgeId);
      query.setParameter("contractStatuses", contractStatuses);
      bppContractList = query.getResultList();
    } finally {
//            session.close();
    }

    return bppContractList;
  }

  /**
   * Get unblock status contract by status and unblockId.
   *
   * @param pledgeId
   * @return List of BppContract
   */
  public static List<BppContract> findByUnblockIdAndStatusIn(Integer pledgeId, List<ContractStatus> contractStatuses) {
    Session session = null;
    List<BppContract> bppContractList = new ArrayList<>();
    try {
      session = OPS.opsConnection.getSession();
      session.clear();

      Query<BppContract> query = session.createQuery("SELECT c FROM BppContract c WHERE c.unblock.id = :pledgeID " +
        "AND c.status IN (:contractStatuses) ");

      query.setParameter(PLEDGE_ID, pledgeId);
      query.setParameter("contractStatuses", contractStatuses);
      bppContractList = query.getResultList();
    } finally {
//            session.close();
    }

    return bppContractList;
  }

  /**
   * Get unblock status contract by listedUpdatedStatus.
   *
   * @param pledgeId
   * @return List of BppContract
   */
  public static List<BppContract> findByListedUpdatedStatus(Integer pledgeId) {
    Session session = null;
    List<BppContract> bppContractList = new ArrayList<>();
    try {
      session = OPS.opsConnection.getSession();

      Query<BppContract> query = session.createQuery("SELECT c FROM BppContract c WHERE c.block.id = :pledgeID " +
        "AND c.listedUpdatedStatus IN (com.tcbs.automation.enumerable.orion.hercules.ListedUpdatedStatus.CAN_BO_SUNG_HO_SO, " +
        "com.tcbs.automation.enumerable.orion.hercules.ListedUpdatedStatus.TU_CHOI_HO_SO) ");

      query.setParameter(PLEDGE_ID, pledgeId);
      bppContractList = query.getResultList();
    } finally {
//            session.close();
    }

    return bppContractList;
  }

  /**
   * SQL Search by condition from input on screen.
   *
   * @param tcbsId
   * @param pledgeInforReqDto
   * @return
   */
  public static List<Tuple> obtainPledgeList(String sql
    , List<String> tcbsId
    , String pledgeCode
    , String createdDateFrom
    , String createdDateTo
    , List<String> statusList
    , List<String> listedUpdatedStatusList) {
    Session session = null;
    session = OPS.opsConnection.getSession();

    Query<Tuple> query = session.createNativeQuery(sql, Tuple.class);
    query.setParameter("tcbsId", tcbsId);
    if (!pledgeCode.isEmpty()) {
      query.setParameter("pledgeCode", pledgeCode.isEmpty() ? null : pledgeCode);
    }
    query.setParameter("createdDateFrom", createdDateFrom.isEmpty() ? null : createdDateFrom);
    query.setParameter("createdDateTo", createdDateTo.isEmpty() ? null : createdDateTo);
    query.setParameter("statusList", CollectionUtils.isEmpty(statusList) ? null : statusList);
    query.setParameter("listedUpdatedStatusList", CollectionUtils.isEmpty(listedUpdatedStatusList) ? null : listedUpdatedStatusList);

    return query.getResultList();
  }

  public static void updateStatusByTradingCode(String tradingCode, String status, Map<String, String> mapPro) {
    if (tradingCode.isEmpty()) {
      return;
    }
    if (status.isEmpty()) {
      return;
    }
    ContractStatus contractStatus = ContractStatus.fromValue(status);
    Session session = OPS.opsConnection.getSession();
    session.clear();

    session.beginTransaction();
    Query<BppContract> query = session.createQuery(
      "select c from BppContract c where c.tradingCode=:tradingCode",
      BppContract.class
    );
    query.setParameter(TRADING_CODE, tradingCode);
    BppContract res = query.getSingleResult();

    res.setStatus(contractStatus);
    if (res.getUnblock() != null) {
      res.setSellAccountName(mapPro.get("accountName"));
      res.setSellAccountNum(mapPro.get("accountNum"));
      res.setSellBank(mapPro.get("bank"));
      res.setSellBankCode(mapPro.get("bankCode"));
      res.setSellCitadCode(mapPro.get("citadCode"));
      res.setSellProvince(mapPro.get("province"));
      res.setSellBranch(mapPro.get("branch"));
    } else {
      res.setCouponAccountName(mapPro.get("accountName"));
      res.setCouponAccountNum(mapPro.get("accountNum"));
      res.setCouponBank(mapPro.get("bank"));
      res.setCouponBankCode(mapPro.get("bankCode"));
      res.setCouponCitadCode(mapPro.get("citadCode"));
      res.setCouponProvince(mapPro.get("province"));
      res.setCouponBranch(mapPro.get("branch"));
    }
    session.getTransaction().commit();
  }

  public static void updateUnblockId(List<Integer> idContracts, Integer idPledge) {
    if (idContracts.isEmpty()) {
      return;
    }
    ListedUpdatedStatus listedUpdatedStatus = null;
    Session session = OPS.opsConnection.getSession();
    session.clear();

    BppPledge pledge = session.get(BppPledge.class, idPledge);
    try {
      int size = idContracts.size();
      for (int i = 0; i < size; i++) {
        session.beginTransaction();

        BppContract contract = session.get(BppContract.class, idContracts.get(i));
        contract.setUnblock(pledge);

        session.getTransaction().commit();
      }
    } finally {
    }
  }

  /**
   * For obtainBondBlock API.
   *
   * @param idList
   * @param status
   * @param bondCode
   */
  public static void updateStatusAndBondCode(List<Integer> idList, String status, String bondCode, Integer unblockID, String depoStatus) {
    if (idList.isEmpty()) {
      return;
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();
    session.beginTransaction();
    int size = idList.size();
    for (int i = 0; i < size; i++) {
      BppContract contract = session.get(BppContract.class, idList.get(i));
      contract.setStatus(ContractStatus.fromValue(status));
      contract.setDepositoryStatus(depoStatus);
      contract.setBondCode(bondCode);
      if(unblockID != null) {
        BppPledge unblock = session.get(BppPledge.class, unblockID);
        contract.setUnblock(unblock);
      }
    }
    session.getTransaction().commit();
  }

  public static List<BppContract> getByBondCode(List<String> bondCodeList) {
    if (bondCodeList.isEmpty()) {
      return null;
    }
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Query<BppContract> query = session.createQuery("SELECT c FROM BppContract c LEFT JOIN FETCH c.block WHERE c.bondCode IN (:bondCodeList) " +
      " AND (c.depositoryStatus = 'CHUA_LUU_KY' OR c.depositoryStatus IS NULL) " +
      " AND c.status IN (com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_PHONG_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_UPLOAD_SCAN_PHONG_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.HOAN_THANH_PHONG_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_XAC_NHAN_GIAI_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_YEU_CAU_GIAI_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.TU_CHOI_GIAI_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.XAC_NHAN_TT_GIAI_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_GIAI_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.DA_UPLOAD_SCAN_GIAI_TOA, " +
      "                     com.tcbs.automation.enumerable.orion.hercules.ContractStatus.HOAN_THANH_GIAI_TOA)");
    query.setParameter("bondCodeList", bondCodeList);
    return query.getResultList();
  }

  public BigDecimal getSumLimitBond() {
    Session session = OPS.opsConnection.getSession();
    session.clear();

    Query<Double> query = OPS.opsConnection.getSession().createQuery(
      "SELECT COALESCE(SUM(c.disbursementContractValue), 0) FROM BppContract c " + // Sua sau
        " WHERE c.block.createdChannel IN ('THEO_SAN_PHAM') " +
        " AND c.status IN ('DA_XAC_NHAN_PHONG_TOA', " +
        "                     'DA_YEU_CAU_PHONG_TOA', " +
        "                     'TU_CHOI_PHONG_TOA', " +
        "                     'DA_PHONG_TOA', " +
        "                     'DA_UPLOAD_SCAN_PHONG_TOA', " +
        "                     'HOAN_THANH_PHONG_TOA', " +
        "                     'DA_XAC_NHAN_GIAI_TOA', " +
        "                     'DA_YEU_CAU_GIAI_TOA', " +
        "                     'TU_CHOI_GIAI_TOA', " +
        "                     'XAC_NHAN_TT_GIAI_TOA')", Double.class);

    return BigDecimal.valueOf(query.getSingleResult());
  }

  /**
   * Update status in case "Kênh sản phẩm";"Không theo sản phẩm"
   *
   * @param idList
   */
  public void updateStatus(List<Integer> idList) {
    if (idList.isEmpty()) {
      return;
    }
    ContractStatus contractStatus = null;
    Session session = OPS.opsConnection.getSession();
    try {
      int size = idList.size();
      for (int i = 0; i < size; i++) {
        session.beginTransaction();
        switch (i) {
          case 0:
            contractStatus = ContractStatus.DA_XAC_NHAN_PHONG_TOA;
            break;
          case 1:
            contractStatus = ContractStatus.DA_YEU_CAU_PHONG_TOA;
            break;
          case 2:
            contractStatus = ContractStatus.TU_CHOI_PHONG_TOA;
            break;
          case 3:
            contractStatus = ContractStatus.DA_PHONG_TOA;
            break;
          case 4:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_PHONG_TOA;
            break;
          case 5:
            contractStatus = ContractStatus.HOAN_THANH_PHONG_TOA;
            break;
          case 6:
            contractStatus = ContractStatus.DA_XAC_NHAN_GIAI_TOA;
            break;
          case 7:
            contractStatus = ContractStatus.DA_YEU_CAU_GIAI_TOA;
            break;
          case 8:
            contractStatus = ContractStatus.TU_CHOI_GIAI_TOA;
            break;
          case 9:
            contractStatus = ContractStatus.XAC_NHAN_TT_GIAI_TOA;
            break;
          case 10:
            contractStatus = ContractStatus.HUY_YEU_CAU; // Khong tinh han muc
            break;
          case 11:
            contractStatus = ContractStatus.DA_GIAI_TOA; // Khong tinh han muc
            break;
          case 12:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_GIAI_TOA; // Khong tinh han muc
            break;
          case 13:
            contractStatus = ContractStatus.HOAN_THANH_PHONG_TOA; // Khong tinh han muc
            break;
          default:
            contractStatus = ContractStatus.DA_YEU_CAU_PHONG_TOA;
        }

        BppContract contract = session.get(BppContract.class, idList.get(i));
        contract.setStatus(contractStatus);
//                session.update(contract);
        session.getTransaction().commit();
      }

    } finally {
//            session.close();
    }
  }

  /**
   * Update status in case "Kênh sản phẩm";"Không theo sản phẩm"
   *
   * @param idList
   */
  public void updateStatusBondAllPledge(List<Integer> idList) {
    if (idList.isEmpty()) {
      return;
    }
    ContractStatus contractStatus = null;
    ListedUpdatedStatus listedUpdatedStatus = null;
    Session session = OPS.opsConnection.getSession();
    try {
      int size = idList.size();
      for (int i = 0; i < size; i++) {
        session.beginTransaction();
        switch (i) {
          case 0:
            contractStatus = ContractStatus.DA_XAC_NHAN_PHONG_TOA;
            break;
          case 1:
            contractStatus = ContractStatus.DA_YEU_CAU_PHONG_TOA;
            break;
          case 2:
            contractStatus = ContractStatus.TU_CHOI_PHONG_TOA;
            break;
          case 3:
            contractStatus = ContractStatus.DA_PHONG_TOA;
            break;
          case 4:
            contractStatus = ContractStatus.DA_YEU_CAU_GIAI_TOA;
            break;
          case 5:
            contractStatus = ContractStatus.XAC_NHAN_TT_GIAI_TOA;
            break;
          case 6:
            contractStatus = ContractStatus.TU_CHOI_GIAI_TOA;
            break;
          case 7:
            contractStatus = ContractStatus.DA_GIAI_TOA;
            break;
          case 8:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_PHONG_TOA;
            listedUpdatedStatus = ListedUpdatedStatus.TU_CHOI_HO_SO;
            break;
          case 9:
            contractStatus = ContractStatus.DA_XAC_NHAN_GIAI_TOA;
            break;
          case 10:
            contractStatus = ContractStatus.DA_YEU_CAU_PHONG_TOA;
            break;
          case 11:
            contractStatus = ContractStatus.TU_CHOI_GIAI_TOA;
            break;
          case 12:
            contractStatus = ContractStatus.DA_YEU_CAU_GIAI_TOA;
            break;
          case 13:
            contractStatus = ContractStatus.XAC_NHAN_TT_GIAI_TOA;
            break;
          case 14:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_GIAI_TOA;
            break;
          case 15:
            contractStatus = ContractStatus.HOAN_THANH_GIAI_TOA;
            break;
          case 16:
            contractStatus = ContractStatus.DA_GIAI_TOA;
            break;
          case 17:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_GIAI_TOA;
            listedUpdatedStatus = ListedUpdatedStatus.TU_CHOI_HO_SO;
            break;
          case 18: // CASE 18,19: Tất cả các HĐ mua bond thuộc yêu cầu cầm cố đang ở trạng thái "DaYeuCauPhongToa"
            contractStatus = ContractStatus.DA_YEU_CAU_PHONG_TOA;
            break;
          case 19:
            contractStatus = ContractStatus.DA_YEU_CAU_PHONG_TOA;
            break;
          case 20: // CASE 20-22: Tất cả các HĐ mua bond thuộc yêu cầu cầm cố đang ở trạng thái "DaPhongToa", "DaUploadScanPhongToa", "HoanThanhPhongToa"
            contractStatus = ContractStatus.DA_PHONG_TOA;
            break;
          case 21:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_PHONG_TOA;
            break;
          case 22:
            contractStatus = ContractStatus.HOAN_THANH_PHONG_TOA;
            break;
          case 23: // CASE 23-25: Tất cả các HĐ mua bond thuộc yêu cầu cầm cố đang ở trạng thái "DaGiaiToa", "DaUploadScanGiaiToa", "HoanThanhGiaiToa"
            contractStatus = ContractStatus.DA_GIAI_TOA;
            break;
          case 24:
            contractStatus = ContractStatus.DA_UPLOAD_SCAN_GIAI_TOA;
            break;
          case 25:
            contractStatus = ContractStatus.HOAN_THANH_GIAI_TOA;
            break;
          default:
            contractStatus = ContractStatus.HOAN_THANH_PHONG_TOA;
            listedUpdatedStatus = ListedUpdatedStatus.HOAN_THANH_BO_SUNG_HO_SO;
        }

        BppContract contract = session.get(BppContract.class, idList.get(i));
        contract.setStatus(contractStatus);
        contract.setListedUpdatedStatus(listedUpdatedStatus);

        session.getTransaction().commit();
      }

    } finally {
//            session.close();
    }
  }

  public void updateListedStatusBond(List<Integer> idList, String bondType, String listedSts) {
    if (idList.isEmpty()) {
      return;
    }
    ListedUpdatedStatus listedUpdatedStatus = null;
    Session session = OPS.opsConnection.getSession();
    session.clear();
    try {
      int size = idList.size();
      for (int i = 0; i < size; i++) {
        session.beginTransaction();
        switch (i) {
          case 0:
            listedUpdatedStatus = ListedUpdatedStatus.CAN_BO_SUNG_HO_SO;
            break;
          case 1:
            listedUpdatedStatus = ListedUpdatedStatus.TU_CHOI_HO_SO;
            break;
          case 2:
            listedUpdatedStatus = ListedUpdatedStatus.CAN_BO_SUNG_HO_SO;
            break;
          default:
            listedUpdatedStatus = ListedUpdatedStatus.CAN_BO_SUNG_HO_SO;
        }

        BppContract contract = session.get(BppContract.class, idList.get(i));
        contract.setListedUpdatedStatus(listedUpdatedStatus);
        if ("OTC".equals(bondType)) {
          contract.setBondType(BondType.OTC);
        }
        if (!"CAN_BO_SUNG_HO_SO".equals(listedSts) && !"TU_CHOI_HO_SO".equals(listedSts)) {
          contract.setListedUpdatedStatus(ListedUpdatedStatus.fromValue(listedSts));
        }

        session.getTransaction().commit();
      }
    } finally {
    }
  }

  public void updateListedUpdStsAndReason(List<Integer> idList) {
    if (idList.isEmpty()) {
      return;
    }
    ListedUpdatedStatus listedUpdatedStatus = null;
    Session session = OPS.opsConnection.getSession();
    session.clear();

    try {
      int size = idList.size();
      for (int i = 0; i < size; i++) {
        session.beginTransaction();
        switch (i) {
          case 0:
            listedUpdatedStatus = ListedUpdatedStatus.CAN_BO_SUNG_HO_SO;
            break;
          default:
            listedUpdatedStatus = ListedUpdatedStatus.CAN_BO_SUNG_HO_SO;
        }

        BppContract contract = session.get(BppContract.class, idList.get(i));
        contract.setListedUpdatedStatus(listedUpdatedStatus);

        session.getTransaction().commit();
      }
    } finally {
    }
  }
}
