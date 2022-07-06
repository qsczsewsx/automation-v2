package com.tcbs.automation.clients.hfc;

import com.tcbs.automation.clients.hfc.dto.*;
import com.tcbs.automation.constants.coco.Constants;
import io.restassured.http.ContentType;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static com.tcbs.automation.config.coco.CocoServiceConfig.*;
import static java.net.HttpURLConnection.HTTP_OK;
//import static net.serenitybdd.rest.SerenityRest.given;
import static io.restassured.RestAssured.given;

@Slf4j
public class HfcClientImpl implements HfcClient {

  public HfcClientImpl() {
    // default constructors
  }

  @Override
  public QueryResPage<CashStatementResDto> getCashStatements(CashStatementReqDto req) {
    String baseUri = HFC_URL + HFC_TRANS_HIST + CASH_STATEMENTS;
    Response response = given()
      .params(req.toHfcQueryParamMap())
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header(Constants.X_API_KEY, COCO_INTERNAL_X_API_KEY)
      .get();
    if (response.statusCode() == HTTP_OK) {
      QueryResPage<CashStatementResDto> res = response.as(new TypeRef<QueryResPage<CashStatementResDto>>() {
      });
      return res;
    }
    return null;
  }

  @Override
  public QueryResPage<StockStatementResDto> getStockStatements(StockStatementReqDto req) {
    String baseUri = HFC_URL + HFC_TRANS_HIST + STOCK_STATEMENTS;
    Response response = given()
      .params(req.toHfcQueryParamMap())
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header(Constants.X_API_KEY, COCO_INTERNAL_X_API_KEY)
      .get();
    if (response.statusCode() == HTTP_OK) {
      QueryResPage<StockStatementResDto> res = response.as(new TypeRef<QueryResPage<StockStatementResDto>>() {
      });
      return res;
    }
    return null;
  }

  @Override
  public CashBODBalanceResDto getCashBODBalance(CashBODBalanceReqDto req) {
    String baseUri = HFC_URL + HFC_TRANS_HIST + CASH_BOD_BALANCE;
    Response response = given()
      .params(req.toHfcQueryParamMap())
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header(Constants.X_API_KEY, COCO_INTERNAL_X_API_KEY)
      .get();
    if (response.statusCode() == HTTP_OK) {
      CashBODBalanceResDto res = response.as(CashBODBalanceResDto.class);
      return res;
    }
    return null;
  }
}
