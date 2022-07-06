package com.tcbs.automation.coco.mongodb.manager;

import com.tcbs.automation.coco.mongodb.model.CustomerRatingSummary;

public interface CustomerRatingSummaryManager {
  CustomerRatingSummary findOneTraderByTcbsId(String tcbsId);
}
