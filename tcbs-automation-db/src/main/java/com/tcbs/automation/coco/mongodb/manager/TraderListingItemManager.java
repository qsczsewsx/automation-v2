package com.tcbs.automation.coco.mongodb.manager;

import com.tcbs.automation.coco.mongodb.model.SortItem;
import com.tcbs.automation.coco.mongodb.model.TraderListingItem;

import java.util.List;

public interface TraderListingItemManager {
  List<TraderListingItem> findTraderByTcbsIdSortBy(List<String> tcbsIds, List<SortItem> sortBy);

  List<TraderListingItem> findTraderByTcbsIdSortBy(List<String> tcbsIds, List<SortItem> sortBy, boolean activeOnly, Integer page, Integer size);

  List<TraderListingItem> findTraderTop(List<SortItem> sortBy, Integer page, Integer size);

  TraderListingItem findOneTraderByTcbsId(String tcbsId, boolean excludeAvatar);
}
