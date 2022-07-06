package com.tcbs.automation.clients.flex;

import com.tcbs.automation.clients.flex.dto.*;
import com.tcbs.automation.clients.flex.exception.FlexApiResException;
import com.tcbs.automation.clients.krema.dto.AccountsSEResDto;
import com.tcbs.automation.clients.krema.dto.CashInvestmentItemResDto;
import com.tcbs.automation.clients.krema.dto.CashInvestmentResDto;
import com.tcbs.automation.constants.coco.Constants;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.tcbs.automation.config.coco.CocoServiceConfig.*;
import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;
import static java.net.HttpURLConnection.HTTP_OK;
import static net.serenitybdd.rest.SerenityRest.given;

@Slf4j
public class FlexClientImpl implements FlexClient {
  @Override
  public AccountResDto getAccountList(QueryAccountListParam param) {
    String baseUri = FLEX_NOTOKEN_URL + FLEX_ACCOUNTS;

    Response response = given()
      .params(param.toFlexQueryParam())
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header(Constants.X_API_KEY, QE_X_API_KEY)
      .get();
    if (response.statusCode() == HTTP_OK) {
      AccountResDto res = response.as(AccountResDto.class);
      return res;
    }
    return null;
  }

  @Override
  public Optional<CashInvestmentItemResDto> getCashInvestment(String accountNo) {
    String baseUri = FLEX_URL + FLEX_ACCOUNTS + "/" + accountNo + FLEX_CASH_INVESTMENTS;
    Response response = given()
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header(Constants.X_API_KEY, QE_X_API_KEY)
      .get();
    if (response.statusCode() == HTTP_OK) {
      CashInvestmentResDto res = response.as(CashInvestmentResDto.class);
      if (res.getData() != null && !res.getData().isEmpty()) {
        return Optional.of(res.getData().get(0));
      }
    }
    return Optional.empty();
  }

  @Override
  public AccountsSEResDto getAccountSE(String accountNo) throws FlexApiResException {
    String baseUri = FLEX_URL + FLEX_ACCOUNTS + "/" + accountNo + FLEX_SE;
    Response response = given()
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header(Constants.X_API_KEY, QE_X_API_KEY)
      .get();
    if (response.statusCode() == HTTP_OK) {
      try {
        AccountsSEResDto res = response.as(AccountsSEResDto.class);
        return res;
      } catch (Exception ex) {
        log.error("Flex SE API response invalid format: {}, ex: {}", response.jsonPath().get(), ex);
        throw new FlexApiResException("Flex SE API response invalid format");
      }
    } else {
      log.error("Flex SE API return error, status code: {}, body: {}", response.getStatusCode(), response.jsonPath().get());
      throw new FlexApiResException("Flex SE API return error: " + response.jsonPath().get());
    }
  }
}
