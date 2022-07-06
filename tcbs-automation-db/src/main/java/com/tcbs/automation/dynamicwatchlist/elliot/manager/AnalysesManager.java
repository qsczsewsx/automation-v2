package com.tcbs.automation.dynamicwatchlist.elliot.manager;

import com.tcbs.automation.dynamicwatchlist.elliot.model.Analyses;
import com.tcbs.automation.dynamicwatchlist.elliot.model.Chart;
import com.tcbs.automation.dynamicwatchlist.elliot.model.CountTickers;

import java.util.List;

public interface AnalysesManager {
  void insertAnalyses(Analyses info);

  Analyses getAnalysesByTicker(String ticker) throws Exception;

  void deleteAnalysesByTicker(String ticker);

  List<Chart> getChartData(String indicator, Double max, Double min, Integer numberOfColumn);

  Double getMinMax(String indicator, String type);

  CountTickers getCount(String indicator,String transformIndicator, Double max, Double min);
}
