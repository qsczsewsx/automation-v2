package com.tcbs.automation;

public enum Database {
  TCBOND("tcbond"),
  TCWEALTH("tcwealth"),
  CAS("cas"),
  TCINVEST_ORCL("tcinvest_orcl"),
  FLEX("flex"),
  TIMELINE("timeline"),
  DWH("dwh"),
  AWS_TCBS_DWH("aws_tcbs_dwh"),
  TCBS_DWH("tcbs_dwh"),
  TCBS_DWH_WRITE("tcbs_dwh_write"),
  TCBS_DWH_STAGING("tcbs_dwh_staging"),
  DATA_STORAGE("data_storage"),
  DWH_AP("dwh-ap"),
  HFC_DATA("hfc-data"),
  IDATA("idata"),
  INBOX("inbox"),
  OTP("otp"),
  OTP_WT("otp_wt"),
  SURVEY("survey"),
  TCPOINT("tcpoint"),
  TCPOINTS("tcpoints"),
  PORTFOLIO("portfolio"),
  ANATTA("anatta"),
  ANICCA("anicca"),
  IPARTNER("ipartner"),
  EDCM("edcm"),
  OPS("ops"),
  DOCPORTAL("docportal"),
  FUND_STATION("fund_station"),
  CASERVICE("caservice"),
  IXU("ixu"),
  STOXPLUS("stoxplus"),
  STOXPLUS_V2("stoxplus_v2"),
  STAGING("staging"),
  AWS_STAGING_DWH("aws_staging_dwh"),
  AWS_RISK_CLOUD("aws_risk_cloud"),
  FOUR_EYES("foureyes"),
  PROJECTION("projection"),
  ISTOCK("istock"),
  DEVMANA("devmana"),
  INTERMANA("intermana"),
  BONDLIFECYCLE("bondlifecycle"),
  H2H("h2h"),
  SILKGATE("silkgate"),
  DBAPI1("tcbs-dbapi1"),
  DBAPI2("tcbs-dbapi2"),
  EVOTING("evoting"),
  STOCK_MARKET("stockmarket"),
  FUTURES_MARKET("futuresmarket"),
  STOCK_GATE("stockgate"),
  COCO("coco"),
  LIGO("ligo"),
  INVESTING_BUNDLE("investingbundle"),
  COMAN("coman"),
  SOCIAL_INVEST("socialinvest"),
  MOKSHA("moksha"),
  BOND_FEE_MANAGEMENT("bondfeemanagement"),
  IRIS("iris"),
  AWS_IRIS("aws_iris"),
  FLEXOPS("flexops"),
  ISQUARE("isquare"),
  IVOUCHER("ivoucher"),
  TCA("tca"),
  REDSHIFT_STAGING("tcbs_dwh_staging"),
  REDSHIFT_STOCK_MARKET("tcbs_dwh_stockmarket_data"),
  PORTFOLIO_ISAIL("portfolio_isail"),
  AOS("aos"),
  GOODS_ORCHESTRATOR("goods_orchestrator"),
  ICALENDAR_TIMELINE("iCalendarTimeline");

  private String value;

  Database(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public HibernateEdition getConnection() {
    DbInfo dbInfo = DbConstant.getDbInfo(this.value);
    return dbInfo.getDbConnection();
  }

}
