package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.VsdTransaction;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.poi.util.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/ImportFileUpdateVSD.csv", separator = '|')
public class ApiImportFileUpdateVSDTest {
  @Getter
  private String testcaseName;
  private String file;
  private String note;
  private int statusCode;
  private String errorMsg;
  private int size;
  private int appUserStatus;
  private int stpStatus;
  private int appId;
  private String mimeType;
  private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);
  private String custodyCode;
  private static final String STR_LESS_THAN_500 = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Lessthan500.txt");
  private static final String STR_CONTAIN_500 = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Contain500.txt");
  private static final String STR_MORE_THAN_500 = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Morethan500.txt");
  private List<String> listCodeCustody;

  @Before
  public void setup() {

    if (testcaseName.contains("more than 500 CUSTODYCD")) {
      listCodeCustody = new ArrayList<>(Arrays.asList(STR_MORE_THAN_500.split(",")));
    } else if (testcaseName.contains("includes 500 CUSTODYCD")) {
      listCodeCustody = new ArrayList<>(Arrays.asList(STR_CONTAIN_500.split(",")));
    } else if (testcaseName.contains("less than 500 values") || testcaseName.contains("format (yyyy/mm/dd)")) {
      listCodeCustody = new ArrayList<>(Arrays.asList(STR_LESS_THAN_500.split(",")));
    } else if (testcaseName.contains("successList") || testcaseName.contains("failedList")) {
      listCodeCustody = new ArrayList<>(Collections.singletonList(custodyCode));
    }

    if (custodyCode.isEmpty()) {
      TcbsApplicationUser.updateStatusAppByList(listCodeCustody, "4", "2");
      VsdTransaction.updateStatusByList(listCodeCustody, "2");
    } else {
      TcbsApplicationUser.updateStatusAppByList(listCodeCustody, Integer.toString(appId), Integer.toString(appUserStatus));
      VsdTransaction.updateStatusByList(listCodeCustody, Integer.toString(stpStatus));
    }
  }


  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API import file update vsd")
  public void ImportFileUpdateVSD() throws IOException {
    System.out.println("Testcase Name: " + testcaseName);

    final byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream(file));
    RequestSpecification responseSpecification = given()
      .baseUri(IMPORT_FILE_UPDATE_VSD)
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Maker role") ? ASSIGN_TASK_TO_AMOPS_MAKER : STP_AUTHORIZATION_KEY))
      .contentType("multipart/form-data;");

    Response response;

    if (testcaseName.contains("body is missing")) {
      response = responseSpecification.post();
    } else {
      MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(bytes)
        .controlName("file")
        .mimeType(mimeType)
        .fileName(file).build();
      response = responseSpecification
        .formParam("note", note)
        .multiPart(multiPartSpecification)
        .post();
    }
    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      if (testcaseName.contains("successList")) {
        List<String> listCustodyCD = response.jsonPath().get("data.successList");
        assertThat(listCustodyCD.size(), is(size));
      } else if (testcaseName.contains("failedList")) {
        List<String> listCustodyCD = response.jsonPath().get("data.failedList");
        assertThat(listCustodyCD.size(), is(size));
      }
    } else if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }

  }
}
