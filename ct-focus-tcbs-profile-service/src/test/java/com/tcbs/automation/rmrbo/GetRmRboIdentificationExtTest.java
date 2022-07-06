package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsRelation;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_RM_RBO_IDENTIFICATION_EXT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/GetRmRboIdentificationExt.csv", separator = '|')
public class GetRmRboIdentificationExtTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String identifyCustodyCd;
  private String jwt;

  @Before
  public void before() {
    Actor actor = Actor.named("haihv");
    if (testCaseName.contains("valid request")) {
      LoginApi.withCredentials(identifyCustodyCd, "abc123").performAs(actor);
    } else {
      LoginApi.withCredentials("105C313993", "abc123").performAs(actor);
    }
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
    identifyCustodyCd = syncData(identifyCustodyCd);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get RM RBO identification external")
  public void getRmRboIdentification() {
    System.out.println("Test case name: " + testCaseName);

    Response response = given()
      .baseUri(GET_RM_RBO_IDENTIFICATION_EXT.replace("#identifyCustodyCd#", identifyCustodyCd))
      .header("Authorization", "Bearer " + jwt)
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 200) {
      List<Map<String, Object>> resListMap = response.jsonPath().get("");
      List<TcbsRelation> relationListDb = TcbsRelation.getByIdentifyCustodyCd(identifyCustodyCd);
      for (int i = 0; i < resListMap.size(); i++) {
        assertEquals(relationListDb.get(i).getCustodyCd(), resListMap.get(i).get("custodyCd"));
        assertEquals(relationListDb.get(i).getTcbsId(), resListMap.get(i).get("tcbsId"));
        assertEquals(relationListDb.get(i).getVipType(), resListMap.get(i).get("vipType"));
        assertEquals(relationListDb.get(i).getIdentifyType(), resListMap.get(i).get("identifyType"));
      }
    }
  }
}


