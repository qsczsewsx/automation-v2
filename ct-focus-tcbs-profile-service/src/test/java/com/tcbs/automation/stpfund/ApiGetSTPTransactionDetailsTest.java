package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.VsdTransaction;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/GetSTPTransactionDetail.csv", separator = '|')
public class ApiGetSTPTransactionDetailsTest {
  @Getter
  private String testcaseName;
  private int id;
  private int statusCode;
  private String errorMsg;

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API retry stp transaction")

  public void GetSTPTransacionDetails() throws JsonProcessingException {
    System.out.println("Testcase Name: " + testcaseName);

    Response response = given()
      .baseUri(GET_STP_TRANSACTION_DETAILS.replace("{id}", String.valueOf(id)))
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Authorization key") ? FMB_X_API_KEY : STP_AUTHORIZATION_KEY))
      .when()
      .get();

    if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    } else {

      VsdTransaction vsdTransaction = VsdTransaction.getById(String.valueOf(id));
      TcbsUser tcbsUser = TcbsUser.getById(vsdTransaction.getUserId());

      assertEquals(response.jsonPath().get("tcbsId"), tcbsUser.getTcbsid());
      assertEquals(response.jsonPath().get("code105C"), tcbsUser.getUsername());
      assertEquals(response.jsonPath().get("fullName"), tcbsUser.getLastname().concat(" ".concat(tcbsUser.getFirstname())));
      assertEquals(response.jsonPath().get("product"), vsdTransaction.getProduct());
      assertEquals(response.jsonPath().get("type"), vsdTransaction.getType());
      assertEquals(response.jsonPath().get("subType"), vsdTransaction.getSubType());
      assertEquals(response.jsonPath().get("status").toString(), vsdTransaction.getStatus().toString());
      assertEquals(response.jsonPath().get("step"), vsdTransaction.getStep());
      assertEquals(response.jsonPath().get("channel"), vsdTransaction.getChannel());
      assertEquals(response.jsonPath().get("errType"), Objects.nonNull(vsdTransaction.getErrType()) ? vsdTransaction.getErrType().intValue() : null);
      assertEquals(response.jsonPath().get("errCode"), vsdTransaction.getErrCode());
      assertEquals(response.jsonPath().get("errMsg"), vsdTransaction.getErrMsg());
      if (Objects.isNull(vsdTransaction.getPayload())) {
        assertNull(response.jsonPath().get("payload"));
      } else {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(vsdTransaction.getPayload(), Map.class);
        assertEquals(response.jsonPath().get("payload"), map);
      }

    }

  }
}
