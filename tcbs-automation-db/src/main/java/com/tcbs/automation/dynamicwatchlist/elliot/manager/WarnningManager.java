package com.tcbs.automation.dynamicwatchlist.elliot.manager;

import com.tcbs.automation.dynamicwatchlist.elliot.model.Warnning;

import java.util.List;

public interface WarnningManager {
  void insertWarnning(Warnning info);

  List<Warnning> getWarnningByTcbsid(String tcbsid) throws Exception;

  void deleteWarnningByTcbsid(String tcbsid);
}
