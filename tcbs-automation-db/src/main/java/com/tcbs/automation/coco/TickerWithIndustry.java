package com.tcbs.automation.coco;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerWithIndustry {

  @ColumnIndex(name = "TICKER", index = 0, ignoreIfNull = true)
  private String ticker;
  @ColumnIndex(name = "ExchangeCode", index = 1, ignoreIfNull = true)
  private String exchangeCode;
  @ColumnIndex(name = "ShortName", index = 2, ignoreIfNull = true)
  private String shortName;
  @ColumnIndex(name = "EnglishName", index = 3, ignoreIfNull = true)
  private String englishName;
  @ColumnIndex(name = "IndustryCode", index = 4, ignoreIfNull = true)
  private String industryCode;
  @ColumnIndex(name = "IndustryName", index = 5, ignoreIfNull = true)
  private String industryName;
  @ColumnIndex(name = "IndustryNameEn", index = 6, ignoreIfNull = true)
  private String industryNameEn;

  public static List getIndustryByTicker(List<String> tickerCodes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, ParseException, IllegalAccessException {
    if (CollectionUtils.isEmpty(tickerCodes)) {
      return new ArrayList<>();
    }
    List<TickerWithIndustry> tickerWithIndustries = new ArrayList<>();
    Query<?> query = TcAnalysis.tcaDbConnection.getSession()
      .createNativeQuery(
        "SELECT st.Ticker as TICKER, " +
          "(CASE WHEN st.ComGroupCode = 'VNINDEX' THEN 'HSX' " +
          "WHEN st.ComGroupCode = 'HNXIndex' THEN 'HNX' " +
          "WHEN st.ComGroupCode = 'UpcomIndex' THEN 'UPC' " +
          "END) AS ExchangeCode, " +
          "st.OrganName as ShortName, " +
          "st.en_OrganName as EnglishName, " +
          "ind.IdLevel2 AS IndustryCode, " +
          "ind.Namel2 AS IndustryName, " +
          "ind.NameEnl2 AS IndustryNameEn " +
          "FROM stx_cpf_Organization st " +
          "LEFT JOIN view_idata_industry ind ON st.IcbCode = ind.IdLevel4 " +
          "WHERE st.Status = 1 " +
          "AND st.ComGroupCode IN ('VNINDEX', 'HNXIndex', 'UpcomIndex')" +
                " AND st.Ticker IN :tickers"
      );
    query.setParameter("tickers", tickerCodes);
    List results = query.getResultList();

    TcAnalysis.tcaDbConnection.closeSession();

    tickerWithIndustries = ReflexData.convertResultSetToObj(results, TickerWithIndustry.class);
    return tickerWithIndustries;
  }
}
