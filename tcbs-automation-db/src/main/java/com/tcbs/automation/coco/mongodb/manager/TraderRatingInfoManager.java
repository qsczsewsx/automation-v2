package com.tcbs.automation.coco.mongodb.manager;

import com.tcbs.automation.coco.mongodb.model.TraderRatingInfo;

import java.util.List;

public interface TraderRatingInfoManager {
  List<TraderRatingInfo> findByTcbsId(String tcbsId);
}
