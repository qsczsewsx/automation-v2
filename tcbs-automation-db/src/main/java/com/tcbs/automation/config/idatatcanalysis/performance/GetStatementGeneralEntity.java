package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetStatementGeneralEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> dataStatementGeneral(String cusId, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" WITH TRANS_SUM AS ( ")
      .append(" SELECT USER_ID  ")
      .append(" 		, COUNT(1) AS NUM_TRANS ")
      .append(" 		, COUNT(CASE WHEN PROFIT > 0 THEN 1 END) AS NUM_TRANS_GAIN ")
      .append(" 		, SUM(CASE WHEN OPEN_BALANCE < MT_VOLUME THEN OPEN_BALANCE ELSE MT_VOLUME END) AS NUM_CONT  ")
      .append(" 		, COUNT(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'B' THEN 1 END) AS GAIN_LONG_NUM_TRANS ")
      .append("  		, SUM(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'B' THEN ABS(POINTS) END) AS GAIN_LONG_POINT ")
      .append(" 		, SUM(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'B' THEN ")
      .append("   CASE WHEN OPEN_BALANCE < MT_VOLUME THEN OPEN_BALANCE ELSE MT_VOLUME END END ")
      .append(" ) AS GAIN_LONG_CONTRACT ")
      .append(" 		, SUM(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'B' THEN MINUTES END) AS GAIN_LONG_HOLDING ")
      .append(" 		, COUNT(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'S' THEN 1 END) AS GAIN_SHORT_NUM_TRANS ")
      .append(" 		, SUM(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'S' THEN ABS(POINTS) END) AS GAIN_SHORT_POINT ")
      .append(" 		, SUM(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'S' THEN ")
      .append("   CASE WHEN OPEN_BALANCE < MT_VOLUME THEN OPEN_BALANCE ELSE MT_VOLUME END  END ")
      .append(" ) AS GAIN_SHORT_CONTRACT ")
      .append(" 		, SUM(CASE WHEN PROFIT >= 0 AND OPEN_SIDE = 'S' THEN MINUTES END) AS GAIN_SHORT_HOLDING ")
      .append(" 		 ")
      .append(" 		, COUNT(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'B' THEN 1 END) AS LOSS_LONG_NUM_TRANS ")
      .append("  		, SUM(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'B' THEN ABS(POINTS) END) AS LOSS_LONG_POINT ")
      .append(" 		, SUM(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'B' THEN ")
      .append("   CASE WHEN OPEN_BALANCE < MT_VOLUME THEN OPEN_BALANCE ELSE MT_VOLUME END     END ")
      .append(" ) AS LOSS_LONG_CONTRACT ")
      .append(" 		, SUM(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'B' THEN MINUTES END) AS LOSS_LONG_HOLDING ")
      .append(" 		, COUNT(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'S' THEN 1 END) AS LOSS_SHORT_NUM_TRANS ")
      .append("  		, SUM(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'S' THEN ABS(POINTS) END) AS LOSS_SHORT_POINT ")
      .append(" 		, SUM(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'S' THEN ")
      .append("   CASE WHEN OPEN_BALANCE < MT_VOLUME THEN OPEN_BALANCE ELSE MT_VOLUME END  END ")
      .append(" ) AS LOSS_SHORT_CONTRACT ")
      .append(" 		, SUM(CASE WHEN PROFIT < 0 AND OPEN_SIDE = 'S' THEN MINUTES END) AS LOSS_SHORT_HOLDING ")
      .append(" FROM PE_FS_TRANS_FIFO_MATCHED_LOG ")
      .append(" WHERE USER_ID = :cusId AND TO_CHAR(REPORT_DATE,'yyyy-mm-dd') BETWEEN :fromDate AND :toDate ")
      .append(" AND IS_TRANS IN (1, 10) ")
      .append(" GROUP BY USER_ID ) ")
      .append("  SELECT :cusId AS USER_ID ")
      .append("  	, NVL(NUM_TRANS, 0) AS NUM_TRANS ")
      .append("  	, NVL(CASE WHEN NUM_TRANS = 0 THEN 0 ELSE NUM_TRANS_GAIN/NUM_TRANS END, 0) AS GAIN_TRANS_PER ")
      .append("  	, NVL(NUM_CONT, 0) AS NUM_CONTRACT ")
      .append("  	, NVL(CASE WHEN NUM_TRANS = 0 THEN 0 ELSE NUM_CONT/NUM_TRANS END, 0) AS AVG_CONTPER_TRANS ")
      .append("  	, NVL(GAIN_LONG_NUM_TRANS, 0) AS GAIN_LONG_NUM_TRANS ")
      .append("   	, NVL(CASE WHEN NUM_TRANS = 0 THEN 0 ELSE GAIN_LONG_POINT/GAIN_LONG_NUM_TRANS END, 0) AS GAIN_LONG_POINT_PER_CONTRACT ")
      .append("  	, NVL(GAIN_LONG_CONTRACT, 0) AS GAIN_LONG_CONTRACT ")
      .append("  	, NVL(GAIN_LONG_HOLDING, 0)/GAIN_LONG_NUM_TRANS AS GAIN_LONG_HOLDING ")
      .append("  	, NVL(GAIN_SHORT_NUM_TRANS, 0) AS GAIN_SHORT_NUM_TRANS ")
      .append("   	, NVL(CASE WHEN NUM_TRANS = 0 THEN 0 ELSE GAIN_SHORT_POINT/GAIN_SHORT_NUM_TRANS END, 0) AS GAIN_SHORT_POINT_PER_CONTRACT ")
      .append("  	, NVL(GAIN_SHORT_CONTRACT, 0) AS GAIN_SHORT_CONTRACT ")
      .append("  	, NVL(GAIN_SHORT_HOLDING, 0)/GAIN_SHORT_NUM_TRANS AS GAIN_SHORT_HOLDING ")
      .append("  	, NVL(LOSS_LONG_NUM_TRANS, 0) AS LOSS_LONG_NUM_TRANS ")
      .append("  	, NVL(CASE WHEN NUM_TRANS = 0 THEN 0 ELSE LOSS_LONG_POINT/LOSS_LONG_NUM_TRANS END, 0) AS LOSS_LONG_POINT_PER_CONTRACT ")
      .append("  	, NVL(LOSS_LONG_CONTRACT, 0) AS LOSS_LONG_CONTRACT ")
      .append("  	, NVL(LOSS_LONG_HOLDING, 0)/LOSS_LONG_NUM_TRANS AS LOSS_LONG_HOLDING ")
      .append("  	, NVL(LOSS_SHORT_NUM_TRANS, 0) AS LOSS_SHORT_NUM_TRANS ")
      .append("   	, NVL(CASE WHEN NUM_TRANS = 0 THEN 0 ELSE LOSS_SHORT_POINT/LOSS_SHORT_NUM_TRANS END, 0) AS LOSS_SHORT_POINT_PER_CONTRACT ")
      .append("  	, NVL(LOSS_SHORT_CONTRACT, 0) AS LOSS_SHORT_CONTRACT ")
      .append("  	, NVL(LOSS_SHORT_HOLDING, 0)/LOSS_SHORT_NUM_TRANS AS LOSS_SHORT_HOLDING ")
      .append("  FROM TRANS_SUM ")
      .append("  RIGHT JOIN dual ON 1=1  ");

    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("cusId", cusId)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
