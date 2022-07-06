package com.tcbs.automation.proinvestor;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsProInvestorDocument;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetProinvestorByTcbsid.csv", separator = '|')
public class ApiGetProinvestorByTcbsidTest {

  @Getter
  private String testCaseName;
  private String tcbsId;
  private String generated_id;
  private String getGenerated_tcbsId;
  private int statusCode;
  private String erroMess;
  private String valid_tcbsId;

  @Before
  public void Before() {
    List<TcbsProInvestorDocument> proInvestorList = TcbsProInvestorDocument.getListOfProInvestorByStatus("ACTIVE");
    String user_id = proInvestorList.get(0).getUserId().toString();

    valid_tcbsId = TcbsUser.getById(new BigDecimal(user_id)).getTcbsid();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get proinvestor by tcbsId")
  public void apiGetProinvestorByTcbsid() {
    System.out.println("Testcase Name: " + testCaseName);
    Response response = given()
      .baseUri(GET_PROINVESTOR_PROFILE_BY_TCBSID.replace("#tcbsId#", (tcbsId.contains("gen")) ? valid_tcbsId : tcbsId))
      .header("x-api-key",
        (testCaseName.contains("Has No Permission") ? ASSIGN_TASK_TO_MAKER_KEY : INTERNAL_PROINVESTOR))
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    System.out.println("Actual Status Code: " + response.getStatusCode() + " - " + response.jsonPath().get("message"));

    if (statusCode == 400) {
      assertEquals(erroMess, response.jsonPath().get("message"));
    } else if (statusCode == 200) {
      //recheck message of 200
      assertThat(response.jsonPath().get("isProInvestor"), is("1"));
      assertThat(response.jsonPath().get("documentType"), is(notNullValue()));
      assertThat(response.jsonPath().get("startDate"), is(notNullValue()));
      assertThat(response.jsonPath().get("endDate"), is(notNullValue()));
    }
  }


}
