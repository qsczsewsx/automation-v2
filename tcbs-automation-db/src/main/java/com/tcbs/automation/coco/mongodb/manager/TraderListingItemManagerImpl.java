package com.tcbs.automation.coco.mongodb.manager;

import com.mongodb.MongoClient;
import com.tcbs.automation.coco.mongodb.FilterCriteriaItem;
import com.tcbs.automation.coco.mongodb.model.SortItem;
import com.tcbs.automation.coco.mongodb.model.TraderListingItem;
import com.tcbs.automation.constants.coco.Constants;
import lombok.extern.slf4j.Slf4j;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.coco.CocoServiceConfig.MONGO_DB_NAME;
import static com.tcbs.automation.constants.coco.Constants.MIN_EQUITY;

@Slf4j
public class TraderListingItemManagerImpl implements TraderListingItemManager {
  private static final Map<String, String> cri2Field;

  static {
    Map<String, String> aMap = new HashMap<>();
    aMap.put(FilterCriteriaItem.CRI_KEY_TCBSID, "tcbsID");
    aMap.put(FilterCriteriaItem.CRI_KEY_TRADER_ID, "traderID");
    aMap.put(FilterCriteriaItem.CRI_KEY_FULLNAME, "accountInfo.fullname");
    aMap.put(FilterCriteriaItem.CRI_KEY_AVATAR, "accountInfo.avatar");
    aMap.put(FilterCriteriaItem.CRI_KEY_RANK, "accountInfo.rank");
    aMap.put(FilterCriteriaItem.CRI_KEY_STATUS, "accountInfo.status");
    aMap.put(FilterCriteriaItem.CRI_KEY_NO_COPIER, "iCopyInfo.noCopier");
    aMap.put(FilterCriteriaItem.CRI_KEY_NO_FOLLOWER, "iCopyInfo.noFollower");
    aMap.put(FilterCriteriaItem.CRI_KEY_POPULAR, "iCopyInfo.viewIn30Days");
    aMap.put(FilterCriteriaItem.CRI_KEY_EDITOR_CHOICE, "iCopyInfo.editorChoice");
    aMap.put(FilterCriteriaItem.CRI_KEY_TOTAL_AUM, "iCopyInfo.totalAUM");
    aMap.put(FilterCriteriaItem.CRI_KEY_RISK_IN_MONTH, "investmentInfo.riskInMonth");
    aMap.put(FilterCriteriaItem.CRI_KEY_PNL_12_MONTH, "investmentInfo.pnl12Month");
    aMap.put(FilterCriteriaItem.CRI_KEY_PNL_30_DAYS, "investmentInfo.pnlLast30Days");
    aMap.put(FilterCriteriaItem.CRI_KEY_RANKING_INFO, "rankingInfo.generalRankScore");
    aMap.put(FilterCriteriaItem.CRI_KEY_EQUITY, "iCopyInfo.equity");
    aMap.put(FilterCriteriaItem.CRI_KEY_BLACKLIST, "accountInfo.blacklist");
    cri2Field = Collections.unmodifiableMap(aMap);
  }

  private final Datastore database;
  private final String tcbsIdString;

  public TraderListingItemManagerImpl(MongoClient client) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, MONGO_DB_NAME);
    this.tcbsIdString = "tcbsID";
  }

  @Override
  public List<TraderListingItem> findTraderByTcbsIdSortBy(List<String> tcbsIds, List<SortItem> sortBy) {
    return findTraderByTcbsIdSortBy(tcbsIds, sortBy, false, null, null);
  }

  @Override
  public List<TraderListingItem> findTraderByTcbsIdSortBy(List<String> tcbsIds, List<SortItem> sortBy, boolean activeOnly, Integer page, Integer size) {
    Sort[] sorts = new Sort[sortBy.size()];
    int idx = 0;
    for (SortItem s : sortBy) {
      String key = cri2Field.get(s.getKey());
      if (key != null) {
        sorts[idx++] = s.isAsc() ? Sort.ascending(key) : Sort.descending(key);
      }
    }

    Query<TraderListingItem> query = database.createQuery(TraderListingItem.class)
      .field(tcbsIdString)
      .in(tcbsIds)
      .order(sorts).project("accountInfo.avatar", false)
      .project(cri2Field.get(FilterCriteriaItem.CRI_KEY_AVATAR), false); // not get avatar

    if (activeOnly) {
      query = query.field(cri2Field.get(FilterCriteriaItem.CRI_KEY_STATUS)).equal(Constants.TraderStatus.ACTIVE);
    }

    if (page != null && size != null) {
      query = query.limit(size).offset(page * size);
    }

    return query.asList();
  }

  @Override
  public List<TraderListingItem> findTraderTop(List<SortItem> sortBy, Integer page, Integer size) {
    Sort[] sorts = new Sort[sortBy.size()];
    int idx = 0;
    for (SortItem s : sortBy) {
      String key = cri2Field.get(s.getKey());
      if (key != null) {
        sorts[idx++] = s.isAsc() ? Sort.ascending(key) : Sort.descending(key);
      }
    }
    Query<TraderListingItem> query = database.createQuery(TraderListingItem.class)
      .field(cri2Field.get(FilterCriteriaItem.CRI_KEY_STATUS)).equal(Constants.TraderStatus.ACTIVE)
      .field(cri2Field.get(FilterCriteriaItem.CRI_KEY_EQUITY)).greaterThanOrEq(MIN_EQUITY)
      .field(cri2Field.get(FilterCriteriaItem.CRI_KEY_BLACKLIST)).notEqual(true)
      .order(sorts)
      .limit(size)
      .offset(page * size)
      .project(cri2Field.get(FilterCriteriaItem.CRI_KEY_AVATAR), false); // not get avatar

    log.info("query: {}", query.toString());

    return query.asList();
  }

  @Override
  public TraderListingItem findOneTraderByTcbsId(String tcbsId, boolean excludeAvatar) {
    Query<TraderListingItem> query = database.createQuery(TraderListingItem.class)
      .field(tcbsIdString)
      .equal(tcbsId);
    if (excludeAvatar) {
      query = query.project(cri2Field.get(FilterCriteriaItem.CRI_KEY_AVATAR), false); // not get avatar
    }
    return query.get();
  }
}
