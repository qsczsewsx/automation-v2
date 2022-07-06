package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsRelation;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/GetRmRboIdentificationInt.csv", separator = '|')
public class GetRmRboIdentificationIntTest {

  private static String gen_custodyCd;
  private static String gen_identifyCustodyCd;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String custodyCd;
  private String identifyCustodyCd;
  private String errMess;

  @BeforeClass
  public static void beforeClass() {
    List<TcbsRelation> tcbsRelationList = TcbsRelation.getByStatus(1);
    gen_custodyCd = tcbsRelationList.get(2).getCustodyCd();
    gen_identifyCustodyCd = tcbsRelationList.get(2).getIdentifyCustodyCd();
  }

  @Before
  public void before() {
    custodyCd = syncData(custodyCd);
    identifyCustodyCd = syncData(identifyCustodyCd);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get RM RBO identification Internal")
  public void getRmRboIdentification() {
    System.out.println("Test case name: " + testCaseName);

    if (custodyCd.equalsIgnoreCase("gen")) {
      custodyCd = gen_custodyCd;
    }
    if (identifyCustodyCd.equalsIgnoreCase("gen")) {
      identifyCustodyCd = gen_identifyCustodyCd;
    }

    Response response = given()
      .baseUri(GET_RM_RBO_IDENTIFICATION_INT)
      .header("x-api-key", statusCode == 401 ? FMB_X_API_KEY : API_KEY)
      .param("custodyCd", custodyCd.isEmpty() ? null : custodyCd)
      .param("identifyCustodyCd", identifyCustodyCd.isEmpty() ? null : identifyCustodyCd)
      .urlEncodingEnabled(false)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 200) {
      if (!testCaseName.contains("with params containing")) {
        verifyRelation(response, identifyCustodyCd, custodyCd);
      }
    } else {
      assertEquals(errMess, response.jsonPath().get("message"));
    }
  }

  private void verifyRelation(Response response, String identifyCustodyCd, String custodyCd) {
    List<Map<String, Object>> resListMap = response.jsonPath().get("");
    if (!custodyCd.isEmpty()) {
      TcbsRelation relationDb = TcbsRelation.getByCustodyCd(custodyCd);
      assertEquals(relationDb.getIdentifyCustodyCd(), resListMap.get(0).get("identifyCustodyCd"));
      assertEquals(relationDb.getTcbsId(), resListMap.get(0).get("tcbsId"));
      assertEquals(relationDb.getVipType(), resListMap.get(0).get("vipType"));
      assertEquals(relationDb.getIdentifyType(), resListMap.get(0).get("identifyType"));
    } else if (!identifyCustodyCd.isEmpty()) {
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
