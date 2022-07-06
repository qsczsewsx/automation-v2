package com.tcbs.automation.coco.mongodb.manager;

import com.tcbs.automation.coco.mongodb.model.CopierActionInfo;

import java.util.List;

public interface CopierActionManger {
  List<CopierActionInfo> findByTcbsIdOrderByActionDateDesc(String tcbsId, int page, int size);
}
