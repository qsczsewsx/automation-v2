package common;

import com.tcbs.automation.tools.SerenityTools;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tcbs.automation.config.ixu.IxuConfig.IVOUCHER_CREATE_VINID_CODE_URL;
import static com.tcbs.automation.config.ixu.IxuConfig.IVOUCHER_SEARCH_VINID_CODE_URL;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.IVOUCHER_DOMAIN;
import static net.serenitybdd.rest.SerenityRest.given;

public class VinidUtils {
  private final SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @SneakyThrows
  public List<String> getListActiveCode(String campaignCode, String prefix, Integer numberCode) {
    List<String> result = searchIvoucherCode(campaignCode);
    if (result.size() >= numberCode) {
      SerenityTools.manualReport("list active code", result.toString());
      return result;
    }
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    endDate.add(Calendar.DATE, 7);
    String createCodeBody = "{\n" +
      "\"campaignCode\": \"" + campaignCode + "\",\n" +
      "\"codeLength\": 6,\n" +
      "\"endDate\": \"" + simpleDate.format(endDate.getTime()) + "\",\n" +
      "\"numberCode\": " + numberCode + ",\n" +
      "\"prefix\": \"" + prefix + "\",\n" +
      "\"startDate\": \"" + simpleDate.format(startDate.getTime()) + "\",\n" +
      "\"status\": \"ACTIVE\"\n" +
      "}";
    Response createCodeResponse = given()
      .baseUri(IVOUCHER_DOMAIN + IVOUCHER_CREATE_VINID_CODE_URL)
      .contentType(ContentType.JSON)
      .header("x-api-key", "Bearer " + API_KEY)
      .body(createCodeBody)
      .when()
      .post();

    if (createCodeResponse.statusCode() == 200) {
      SerenityTools.manualReport("list active code", result.toString());
      return searchIvoucherCode(campaignCode);
    } else {
      SerenityTools.failStep(new Exception("Error when call ivoucher create vinid code, status code is " + createCodeResponse.statusCode()));
      return new ArrayList<>();
    }
  }

  private List<String> searchIvoucherCode(String campaignCode) throws ParseException {
    List<String> result = new ArrayList<>();
    int pageIndex = 0;
    int pageSize = 1000;
    int totalPage;

    do {
      String getCodeBody = "{\n" +
        "\"campaignCode\": \"" + campaignCode + "\",\n" +
        "\"pageIndex\": " + pageIndex + ",\n" +
        "\"pageSize\": " + pageSize + "\n" +
        "}";

      Response getCodeResponse = given()
        .baseUri(IVOUCHER_DOMAIN + IVOUCHER_SEARCH_VINID_CODE_URL)
        .contentType(ContentType.JSON)
        .header("x-api-key", "Bearer " + API_KEY)
        .body(getCodeBody)
        .when()
        .post();
      if (getCodeResponse.statusCode() == 200) {
        totalPage = getCodeResponse.jsonPath().get("paginate.totalPage");
        List<Map<String, Object>> dataList = getCodeResponse.jsonPath().getList("data");
        for (Map<String, Object> data : dataList) {
          if (data.get("status").toString().equalsIgnoreCase("ACTIVE")
            && (data.get("startDate") == null || simpleDate.parse(data.get("startDate").toString()).compareTo(new Date()) <= 0)
            && (data.get("endDate") == null || simpleDate.parse(data.get("endDate").toString()).compareTo(new Date()) >= 0)) {
            result.add(data.get("code").toString());
          }
        }
      } else {
        SerenityTools.failStep(new Exception("Error when call ivoucher get vinid code, status code is " + getCodeResponse.statusCode()));
        return new ArrayList<>();
      }
      pageIndex++;
    } while (pageIndex < totalPage);
    return result;
  }
}
