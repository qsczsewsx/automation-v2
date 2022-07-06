package com.tcbs.automation.config.idatatcanalysis;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class TcAnalysisConfig {
  private static final Config conf = new ConfigImpl("idatatcanalysis").getConf();

  // iData - Price Market
  public static final String TCANALYSIS_STOCK_PRICE_MARKET_URL = conf.getString("tcanalysis.stockPrice.getCurrentPriceUrl");
  // TCanalysis - Ticker
  public static final String TCANALYSIS_STOCK_FOREIGN_IN_MONTH_TRADE_URL = conf
    .getString("tcanalysis.ticker.foreignInMonthTradeUrl");
  public static final String TCANALYSIS_STOCK_QUARTER_FINANCE_INFO_URL = conf
    .getString("tcanalysis.ticker.quarterFinancialInfoUrl");
  public static final String TCANALYSIS_STOCK_TICKER_OVERVIEW_PATH = conf
    .getString("tcanalysis.ticker.overviewUrl");
  public static final String TCANALYSIS_STOCK_RSI_URL = conf.getString("tcanalysis.ticker.rsiInMonthUrl");
  public static final String TCANALYSIS_STOCK_RATIO_URL = conf.getString("tcanalysis.ticker.tickerRatioUrl");
  public static final String TCANALYSIS_STICKER_PRICE_VOLATILITY = conf
    .getString("tcanalysis.ticker.priceVolatility");
  public static final String TCANALYSIS_STICKER_STOCK_RECOMMEND = conf
    .getString("tcanalysis.ticker.stockRecommend");
  public static final String TCANALYSIS_STICKER_STOCK_SAME_INDUSTRY = conf
    .getString("tcanalysis.ticker.stockSameIndustry");

  // TCanalysis - ticker
  public static final String TCANALYSIS_TICKER_V1 = conf.getString("tcanalysis.tickerV1");
  public static final String TCANALYSIS_TICKER_ACTIVITY_NEWS_URL = conf
    .getString("tcanalysis.ticker.activityNewsUrl");
  public static final String TCANALYSIS_TICKER_EVENT_NEWS_URL = conf.getString("tcanalysis.ticker.eventNewsUrl");
  public static final String TCANALYSIS_TICKER_INDICATOR_LIST_STOCK = conf.getString("tcanalysis.ticker.indicator");
  public static final String TCANALYSIS_TICKER_INDICATOR_ALL_STOCK = conf.getString("tcanalysis.ticker.indicatorAll");

  // TCanalysis - Company
  public static final String TCANALYSIS_COMPANY_V1 = conf.getString("tcanalysis.companyV1");
  public static final String TCANALYSIS_COMPANY_OVERVIEW_URL = conf.getString("tcanalysis.company.overview");
  public static final String TCANALYSIS_COMPANY_SHAREHOLDER_URL = conf
    .getString("tcanalysis.company.largeShareHolder");
  public static final String TCANALYSIS_COMPANY_KEYOFFICER_URL = conf.getString("tcanalysis.company.keyOfficer");
  public static final String TCANALYSIS_COMPANY_SUBCOMPANY_URL = conf.getString("tcanalysis.company.subCompany");
  public static final String TCANALYSIS_COMPANY_AUDITFIRM_URL = conf.getString("tcanalysis.company.auditFirm");
  public static final String TCANALYSIS_COMPANY_INSIDERDEALING_URL = conf
    .getString("tcanalysis.company.insiderDealing");
  public static final String TCANALYSIS_COMPANY_DIVIDENDPAYMENT_URL = conf
    .getString("tcanalysis.company.dividendPaymentHis");

  // TCanalysis - News&Event
  public static final String TCANALYSIS_NEWSEVENT_V1 = conf.getString("tcanalysis.newsEventV1");
  public static final String TCANALYSIS_NEWSEVENT_INDUSTRY_URL = conf.getString("tcanalysis.news-event.industry");
  public static final String TCANALYSIS_NEWSEVENT_ACTIVITYNEWS_URL = conf
    .getString("tcanalysis.news-event.activityNews");
  public static final String TCANALYSIS_NEWSEVENT_EVENTNEWS_URL = conf
    .getString("tcanalysis.news-event.eventNews ");
  public static final String TCANALYSIS_NEWSEVENT_ACTNEWSDETAIL_URL = conf
    .getString("tcanalysis.news-event.activityNewsDetail");

  // TCanalysis - DataCharts
  public static final String TCANALYSIS_DATACHARTS_V1 = conf.getString("tcanalysis.dataChartsV1");
  public static final String TCANALYSIS_DATACHARTS_COUNT_VA_URL = conf.getString("tcanalysis.dataCharts.countVAUrl");
  public static final String TCANALYSIS_DATACHARTS_INDICATOR_URL = conf.getString("tcanalysis.dataCharts.indicatorUrl");
  public static final String TCANALYSIS_DATACHARTS_VOLUME_FOREIGN_URL = conf.getString("tcanalysis.dataCharts.volumeForeignUrl");

  // TCanalysis - TcData 
  public static final String TCANALYSIS_TCDATA_V1 = conf.getString("tcanalysis.tcdataV1");
  public static final String TCANALYSIS_TCDATA_STOCK_PRICE = conf.getString("tcanalysis.tcdata.priceinfo");
  public static final String TCANALYSIS_TCDATA_INDEX = conf.getString("tcanalysis.tcdata.index");
  public static final String TCANALYSIS_TCDATA_FOREIGN_TRADE = conf.getString("tcanalysis.tcdata.foreigntrade");
  public static final String TCANALYSIS_TCDATA_COMPANY = conf.getString("tcanalysis.tcdata.company");
  public static final String TCANALYSIS_TCDATA_INDUSTRY = conf.getString("tcanalysis.tcdata.industry");
  public static final String TCANALYSIS_TCDATA_CASH_DIVIDEND = conf.getString("tcanalysis.tcdata.cashdividend");
  public static final String TCANALYSIS_TCDATA_SHARE_ISSUE = conf.getString("tcanalysis.tcdata.shareissue");
  public static final String TCANALYSIS_TCDATA_TREASURY_STOCK = conf.getString("tcanalysis.tcdata.treasurystock");
  public static final String TCANALYSIS_TCDATA_CASHFLOW_STATEMENT = conf.getString("tcanalysis.tcdata.cashflowstatement");
  public static final String TCANALYSIS_TCDATA_INCOME_STATEMENT = conf.getString("tcanalysis.tcdata.incomestatement");
  public static final String TCANALYSIS_TCDATA_BALANCESHEET_STATEMENT = conf.getString("tcanalysis.tcdata.balancesheetstatement");
  public static final String TCANALYSIS_TCDATA_KEYSTATISTIC_STATEMENT = conf.getString("tcanalysis.tcdata.keystatisticstatement");
  public static final String TCANALYSIS_TCDATA_PROJECTION_TICKERS = conf.getString("tcanalysis.tcdata.projectionticker");
  public static final String TCANALYSIS_TCDATA_PROJECTION_VERSIONS = conf.getString("tcanalysis.tcdata.projectionversion");
  public static final String TCANALYSIS_TCDATA_RATIO_HISTORY = conf.getString("tcanalysis.tcdata.historyfinancialratio");
  public static final String TCANALYSIS_TCDATA_RATIO_FORECAST_VERSIONS = conf.getString("tcanalysis.tcdata.forecastratiobyversion");
  public static final String TCANALYSIS_TCDATA_RATIO_FORECAST_TICKERS = conf.getString("tcanalysis.tcdata.forecastrationbyticker");

  // TCanalysis - Finance
  public static final String TCANALYSIS_FINANCE_V1 = conf.getString("tcanalysis.financeV1");
  public static final String TCANALYSIS_FINANCE_CASHFLOW_URL = conf.getString("tcanalysis.finance.cashflowUrl");
  public static final String TCANALYSIS_FINANCE_BALANCESHEET_URL = conf
    .getString("tcanalysis.finance.balancesheetUrl");
  public static final String TCANALYSIS_FINANCE_INCOMESTATEMENT_URL = conf
    .getString("tcanalysis.finance.incomestatementUrl");
  public static final String TCANALYSIS_FINANCE_FINANCIALRATIO_URL = conf
    .getString("tcanalysis.finance.financialratioUrl");
  public static final String TCANALYSIS_FINANCE_CASHFLOWANALYZE_URL = conf
    .getString("tcanalysis.finance.cashflowanalyzeUrl");
  public static final String TCANALYSIS_FINANCE_TOOLTIP_URL = conf.getString("tcanalysis.finance.tooltipUrl");

  public static final String TCANALYSIS_ASSET_PROPORTION_URL = conf.getString("tcanalysis.finance.assetProportionUrl");
  // TCanalysis - Finance
  public static final String TCANALYSIS_PROJECTION_V1 = conf.getString("tcanalysis.projectionV1");
  public static final String TCANALYSIS_PROJECTION_DATA_URL = conf.getString("tcanalysis.projection.dataUrl");
  public static final String TCANALYSIS_PROJECTION_RATIO_URL = conf.getString("tcanalysis.projection.ratioUrl");

  // TCanalysis - Rating
  public static final String TCANALYSIS_RATING_V1 = conf.getString("tcanalysis.ratingV1");
  public static final String TCANALYSIS_RATING_GENERAL_URL = conf.getString("tcanalysis.rating.generalUrl");
  public static final String TCANALYSIS_RATING_BUSINESSMODEL_URL = conf.getString("tcanalysis.rating.businessModelUrl");
  public static final String TCANALYSIS_RATING_BUSINESSOPERATION_URL = conf.getString("tcanalysis.rating.businessOperationUrl");
  public static final String TCANALYSIS_RATING_FINANCIALHEALTH_URL = conf.getString("tcanalysis.rating.financialHealthUrl");
  public static final String TCANALYSIS_RATING_LIQUIDITY_URL = conf.getString("tcanalysis.rating.liquidityUrl");
  public static final String TCANALYSIS_RATING_PRICECHANGE_URL = conf.getString("tcanalysis.rating.priceChangeUrl");
  public static final String TCANALYSIS_RATING_VALUATION_URL = conf.getString("tcanalysis.rating.valuationUrl");
  public static final String TCANALYSIS_RATING_DETAIL_SINGLE_URL = conf.getString("tcanalysis.rating.detailSingleUrl");
  public static final String TCANALYSIS_RATING_DETAIL_MANY_URL = conf.getString("tcanalysis.rating.detailManyUrl");

  // TransInfo
  public static final String TCANALYSIS_TRANSINFOV1 = conf.getString("tcanalysis.transinfoV1");
  public static final String TRANSINFO_FA_URL = conf.getString("tcanalysis.transinfo.faUrl");

  // Indicator
  public static final String INDICATOR_RS = conf.getString("tcanalysis.indicator.rsUrl");
  public static final String TCANALYSIS_INDICATORV1 = conf.getString("tcanalysis.indicatorV1");

  // TCAnalysis - HisRecommend
  public static final String TCANALYSIS_RECOMMEND_V1 = conf.getString("tcanalysis.recommendV1");
  public static final String TCANALYSIS_RECOMMEND_HISTORY_URL = conf.getString("tcanalysis.recommend.hisUrl");
  public static final String TCANALYSIS_RECOMMEND_STATISTIC_URL = conf.getString("tcanalysis.recommend.statisticUrl");

  // TCAnalysis - Evaluation
  public static final String TCANALYSIS_EVALUATION_V1 = conf.getString("tcanalysis.evaluationV1");
  public static final String TCANALYSIS_EVALUATION_INFO_URL = conf.getString("tcanalysis.evaluation.evaluationUrl");
  public static final String TCANALYSIS_EVALUATION_AVG_STOCK_PRICE_URL = conf.getString("tcanalysis.evaluation.avgStockPriceUrl");
  public static final String TCANALYSIS_EVALUATION_HISTORICAL_CHART_URL = conf.getString("tcanalysis.evaluation.historicalChartUrl");
  public static final String TCANALYSIS_EVALUATION_ENTERPRISE_CALCULATE_URL = conf.getString("tcanalysis.evaluation.enterpriseCalculateUrl");
  public static final Double TCANALYSIS_EVALUATION_GROWTH = conf.getDouble("tcanalysis.evaluation.growth");
  public static final Double TCANALYSIS_EVALUATION_WACC = conf.getDouble("tcanalysis.evaluation.wacc");
  public static final String TCANALYSIS_EVALUATION_PRICE_ALL_URL = conf.getString("tcanalysis.evaluation.allEvaluationPriceUrl");

  //Icalendar service
  public static final String ICALENDAR_SERVICE_V1_DOMAIN = conf.getString("tcanalysis.iCalendarServiceDomain");
  public static final String ICALENDAR_SERVICE_EVENT_INFO = conf.getString("tcanalysis.iCalendarService.eventInfoUrl");

  //iSail
  public static final String AL_CHEMIST_SERVICE_V_1_DOMAIN = conf.getString("tcanalysis.internalIDataAlchemist");
  public static final String ISAIL_GET_STATISTICS_URL = conf.getString("tcanalysis.iSail.getAllStatisticsUrl");
  public static final String ISAIL_X_API_KEY = conf.getString("tcanalysis.iSail.x-api-key");
}
