package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.VsdImportStock;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPLOAD_STOCK_VSD;
import static common.CallApiUtils.callNewOBUploadVsdApi;
import static common.CallApiUtils.getNewOBUploadVsdApiRequest;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiUploadStockVSD.csv", separator = '|')
public class ApiUploadStockVSDTest {

  private static final List<String> actions = new ArrayList<>();
  private static String taskID;
  private static String checkerTaskID;
  @Getter
  private String testCaseName;
  @Getter
  private String fileKey;
  private String filePath;
  private String fileName;
  private int statusCode;
  private String erroMsg;
  private List<VsdImportStock> stockListOriginal = new ArrayList<>();
  private List<VsdImportStock> stockListChanged = new ArrayList<>();

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Upload Stock VSD")
  public void verifyApiUploadStockVSDTest() {

    System.out.println("TestCaseName : " + testCaseName);

    stockListOriginal = VsdImportStock.getByFileName(fileName);

    RequestSpecification request = getNewOBUploadVsdApiRequest(UPLOAD_STOCK_VSD);
    Response response = callNewOBUploadVsdApi(fileKey, filePath, fileName, request);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      int totalUpload = response.jsonPath().getInt("totalUpload");
      int totalImport = response.jsonPath().getInt("totalImport");
      stockListChanged = VsdImportStock.getByFileName(fileName);
      assertEquals(stockListOriginal.size(), stockListChanged.size());
      assertEquals(stockListOriginal.size(), totalUpload);
      assertEquals(stockListChanged.size(), totalImport);
      for (int i = 0; i < stockListChanged.size(); i++) {
        assertNotEquals(stockListOriginal.get(i).getId(), stockListChanged.get(i).getId());
        assertEquals(stockListOriginal.get(i).getCustodyCode(), stockListChanged.get(i).getCustodyCode());
        assertEquals(stockListOriginal.get(i).getName(), stockListChanged.get(i).getName());
        assertEquals(stockListOriginal.get(i).getType(), stockListChanged.get(i).getType());
        assertEquals(stockListOriginal.get(i).getIdNumber1(), stockListChanged.get(i).getIdNumber1());
        assertEquals(stockListOriginal.get(i).getIdNumber2(), stockListChanged.get(i).getIdNumber2());
        assertEquals(stockListOriginal.get(i).getIdType(), stockListChanged.get(i).getIdType());
        assertEquals(stockListOriginal.get(i).getIddate(), stockListChanged.get(i).getIddate());
      }

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertTrue(actualMessage.contains(erroMsg));
    } else {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

}
