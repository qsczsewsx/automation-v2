package com.tcbs.automation.tca.assetproportion;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GetAssetProportionEntity {
  @Step
  public static List<HashMap<String, Object>> getBalanceSheetData(String ticker, String yearly) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select top 5 t1.Ticker as ticker, YearReport*10 + LengthReport as year_quarter , YearReport as year, LengthReport as quarter, ");
    queryBuilder.append(" cast( round(BSA53 /1000000000, 0)  as int) as asset , cast(round(BSA1 /1000000000, 0) as int) as shortAsset, cast(round(BSA23/1000000000, 0) as int) as longAsset,  ");
    queryBuilder.append(" case when (BSA1 + BSA23) = 0 then 0 else cast(round(BSA1/(BSA1 + BSA23), 2) as float) end as shortAssetPct, ");
    queryBuilder.append(" cast(round(BSA54/1000000000, 0) as int) as debt, cast(round(BSA78/1000000000, 0) as int) as equity, cast(round(BSA55/1000000000, 0) as int) as shortDebt, cast(round((BSA1 - BSA55)/1000000000, 0) as int) as workingCapital, ");
    queryBuilder.append(" case when (BSA54 + BSA78) = 0 then 0 else cast(round(BSA54 / (BSA54 + BSA78), 2) as float) end as debtPct  ");
    queryBuilder.append(" from STX_FSC_BALANCESHEET t1 left join stx_cpf_Organization t2 on t1.Ticker = t2.Ticker ");
    queryBuilder.append(" where t1.Ticker = :ticker and LengthReport IN :lstQuarter and t2.ComTypeCode NOT IN ('NH') ");
    queryBuilder.append(" order by YearReport desc , LengthReport desc; ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("lstQuarter", Arrays.asList(getListQuarter(yearly)))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static Integer[] getListQuarter(String yearly) {
    Integer[] lstQuarter = new Integer[]{1, 2, 3, 4};
    if (StringUtils.equals("1", yearly) || StringUtils.equalsIgnoreCase("NULL", yearly) || StringUtils.isEmpty(yearly)) {
      lstQuarter = new Integer[]{5};
    }
    return lstQuarter;
  }

  @Step
  public static List<HashMap<String, Object>> getTopAssetData(String ticker, String yearly, String fieldName) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" ; with tmp_data as ( ");
    queryBuilder.append(" 	SELECT * ");
    queryBuilder.append(" 	FROM  ( ");
    queryBuilder.append(" 		select top 5 t1.ticker, YearReport , LengthReport, ").append(fieldName);
    queryBuilder.append(" 		from STX_FSC_BALANCESHEET t1 left join stx_cpf_Organization t2 on t1.Ticker = t2.Ticker  ");
    queryBuilder.append(" 		where t1.Ticker = :ticker and LengthReport IN :lstQuarter and t2.ComTypeCode NOT IN ('NH') ");
    queryBuilder.append(" 		order by YearReport *10 + LengthReport desc ");
    queryBuilder.append(" 	) t1 ");
    queryBuilder.append(" 	UNPIVOT   ");
    queryBuilder.append(" 	( val FOR field_name IN (").append(fieldName).append(") ) as t2 ");
    queryBuilder.append(" ), tmp_top as ( ");
    queryBuilder.append(" 	select * from ( ");
    queryBuilder.append(" 	select YearReport, LengthReport, field_name, ROW_NUMBER() OVER (partition by YearReport *10 + LengthReport order by val desc) as rn ");
    queryBuilder.append(" 	from tmp_data ");
    queryBuilder.append(" 	) a where rn <= 4 ");
    queryBuilder.append(" ), tmp_sum_val as ( ");
    queryBuilder.append(" 	select Ticker ,YearReport,LengthReport, YearReport *10 + LengthReport as year_quarter ,  sum(val) as sum_val  ");
    queryBuilder.append(" 	from tmp_data group by YearReport *10 + LengthReport, Ticker, YearReport, LengthReport ");
    queryBuilder.append(" ), tmp_top_4 as ( ");
    queryBuilder.append(" 	select a.YearReport *10 + a.LengthReport as year_quarter ,  sum(val) as sum_val  ");
    queryBuilder.append(" 	from tmp_data a inner join tmp_top b on a.field_name = b.field_name and a.YearReport = b.YearReport and a.LengthReport = b.LengthReport ");
    queryBuilder.append(" 	group by a.YearReport *10 + a.LengthReport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" select a.ticker,a.YearReport*10 + a.LengthReport as year_quarter, cast(round(val/1000000000, 0) as int) as val, a.field_name, rn ");
    queryBuilder.append(" from tmp_data a inner join tmp_top b on a.field_name = b.field_name and a.YearReport = b.YearReport and a.LengthReport = b.LengthReport ");
    queryBuilder.append(" UNION ALL ");
    queryBuilder.append(" select c.Ticker , YearReport* 10 + LengthReport as year_quarter, cast(round((c.sum_val - d.sum_val) / 1000000000, 0) as int) val, 'others', 5 ");
    queryBuilder.append(" from tmp_sum_val c  ");
    queryBuilder.append(" inner join tmp_top_4 d on c.year_quarter = d.year_quarter ");
    queryBuilder.append(" order by year_quarter desc, rn asc ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("lstQuarter", Arrays.asList(getListQuarter(yearly)))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
