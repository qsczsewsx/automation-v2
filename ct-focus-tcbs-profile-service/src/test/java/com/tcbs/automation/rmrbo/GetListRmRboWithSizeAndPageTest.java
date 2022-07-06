package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.tools.StringUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/GetListRmRboWithSizeAndPage.csv", separator = '|')
public class GetListRmRboWithSizeAndPageTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String page;
  private String size;
  private String errMess;

  @Before
  public void before() {
    size = syncData(size);
    page = syncData(page);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify Api get RMRBO list with page and size")
  public void getListRMRBOWithPageAndSize() {
    System.out.println("Test case name: " + testCaseName);

    Response response = given()
      .baseUri(GET_RM_RBO_LIST_WITH_PAGINATION)
      .header("x-api-key", "Bearer " + (testCaseName.contains("user having no permission") ? FMB_X_API_KEY : API_KEY))
      .urlEncodingEnabled(false)
      .param("page", StringUtils.isNumber(page) ? Integer.valueOf(page) : page)
      .param("size", StringUtils.isNumber(size) ? Integer.valueOf(size) : size)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 200) {
      if (testCaseName.contains("valid params")) {
        assertEquals(size, response.jsonPath().get("paging.size").toString());
      } else {
        assertEquals(Integer.valueOf(0), response.jsonPath().get("paging.totalCnt"));
        assertEquals(Integer.valueOf(0), response.jsonPath().get("paging.curSize"));
        Integer pageResult = Integer.parseInt(page);
        assertEquals(pageResult, response.jsonPath().get("paging.page"));
      }

    } else if (statusCode == 400) {
      assertEquals(errMess, response.jsonPath().get("message"));
    }

  }
}
