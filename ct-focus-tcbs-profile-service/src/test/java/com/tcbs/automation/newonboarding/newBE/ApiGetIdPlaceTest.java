package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsIdPlace;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_ID_PLACE;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetIdPlace.csv", separator = '|')
public class ApiGetIdPlaceTest {

  LinkedHashMap<String, Object> body = new LinkedHashMap<>();
  @Getter
  private String testCaseName;
  @Getter
  private String identityNo;
  private String issueDate;
  private int statusCode;
  private String errorMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Get Id Place")
  public void verifyApiGetIdPlaceTest() {

    System.out.println("TestCaseName : " + testCaseName);

    body.put("identityNo", identityNo);
    body.put("issueDate", issueDate);

    Response response = given()
      .baseUri(GET_ID_PLACE)
      .header("Authorization", "Bearer " + TOKEN)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      Timestamp mytime = Timestamp.valueOf(LocalDateTime.parse(issueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
      Timestamp fromtime = Timestamp.valueOf(LocalDateTime.parse("2012-06-30T17:00:00.000Z", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
      Timestamp totime = Timestamp.valueOf(LocalDateTime.parse("2018-10-10T17:00:00.000Z", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
      List<HashMap<String, Object>> resList = response.jsonPath().getList("");
      assertTrue(resList.size() > 0);

      List<TcbsIdPlace> tcbsIdPlaceList = new ArrayList<>();
      if (identityNo.length() == 9) {
        for (int i = 0; i < resList.size(); i++) {

          HashMap<String, Object> res = resList.get(i);
          String resId = String.valueOf(res.get("id"));
          String resIdPlace = String.valueOf(res.get("idPlace"));
          String resProvinceCode = String.valueOf(res.get("provinceCode"));

          tcbsIdPlaceList = TcbsIdPlace.getListByProvinceCode(identityNo.substring(0, 2));
          if (tcbsIdPlaceList.size() > 0) {
            assertEquals(tcbsIdPlaceList.get(i).getId(), new BigDecimal(resId));
            assertEquals(tcbsIdPlaceList.get(i).getName(), resIdPlace);
            assertEquals(tcbsIdPlaceList.get(i).getProvinceCode(), resProvinceCode);
          } else {
            tcbsIdPlaceList = TcbsIdPlace.getListData();
            assertEquals(tcbsIdPlaceList.get(i).getId(), new BigDecimal(resId));
            assertEquals(tcbsIdPlaceList.get(i).getName(), resIdPlace);
            assertEquals(tcbsIdPlaceList.get(i).getProvinceCode(), resProvinceCode);
          }
        }
      } else {
        for (int i = 0; i < resList.size(); i++) {
          HashMap<String, Object> res = resList.get(i);

          String resId = String.valueOf(res.get("id"));
          String resIdPlace = (String) res.get("idPlace");
          String resProvinceCode = (String) res.get("provinceCode");

          TcbsIdPlace tcbsIdPlace;
          if (mytime.before(fromtime)) {
            tcbsIdPlaceList = TcbsIdPlace.getListByProvinceCode(identityNo.substring(0, 2));
            if (tcbsIdPlaceList.size() > 0) {
              assertEquals(tcbsIdPlaceList.get(i).getId(), new BigDecimal(resId));
              assertEquals(tcbsIdPlaceList.get(i).getName(), resIdPlace);
              assertEquals(tcbsIdPlaceList.get(i).getProvinceCode(), resProvinceCode);
            } else {
              tcbsIdPlaceList = TcbsIdPlace.getListData();
              assertEquals(tcbsIdPlaceList.get(i).getId(), new BigDecimal(resId));
              assertEquals(tcbsIdPlaceList.get(i).getName(), resIdPlace);
              assertEquals(tcbsIdPlaceList.get(i).getProvinceCode(), resProvinceCode);
            }

          } else if (mytime.after(fromtime) && mytime.before(totime)) {
            tcbsIdPlace = TcbsIdPlace.getByProvinceCode("AA0");

            assertEquals(tcbsIdPlace.getId(), new BigDecimal(resId));
            assertEquals(tcbsIdPlace.getName(), resIdPlace);
            assertEquals("AA0", resProvinceCode);

          } else if (mytime.after(totime)) {
            tcbsIdPlace = TcbsIdPlace.getByProvinceCode("AA1");

            assertEquals(tcbsIdPlace.getId(), new BigDecimal(resId));
            assertEquals(tcbsIdPlace.getName(), resIdPlace);
            assertEquals("AA1", resProvinceCode);
          }

        }
      }

    } else if (response.statusCode() == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }

}
