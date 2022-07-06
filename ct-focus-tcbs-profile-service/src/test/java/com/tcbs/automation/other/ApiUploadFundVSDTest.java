package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.VsdImportFund;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPLOAD_FUND_VSD;
import static common.CallApiUtils.callNewOBUploadVsdApi;
import static common.CallApiUtils.getNewOBUploadVsdApiRequest;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiUploadFundVSD.csv", separator = '|')
public class ApiUploadFundVSDTest {

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
  private List<VsdImportFund> fundListOriginal = new ArrayList<>();
  private List<VsdImportFund> fundListChanged = new ArrayList<>();

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Upload Fund VSD")
  public void verifyApiUploadFundVSDTest() {

    System.out.println("TestCaseName : " + testCaseName);

    fundListOriginal = VsdImportFund.getByFileName(fileName);

    RequestSpecification request = getNewOBUploadVsdApiRequest(UPLOAD_FUND_VSD);
    Response response = callNewOBUploadVsdApi(fileKey, filePath, fileName, request);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      int totalUpload = response.jsonPath().getInt("totalUpload");
      int totalImport = response.jsonPath().getInt("totalImport");
      fundListChanged = VsdImportFund.getByFileName(fileName);
      assertEquals(fundListOriginal.size(), fundListChanged.size());
      assertEquals(fundListOriginal.size(), totalUpload);
      assertEquals(fundListChanged.size(), totalImport);
      for (int i = 0; i < fundListChanged.size(); i++) {
        assertNotEquals(fundListOriginal.get(i).getId(), fundListChanged.get(i).getId());
        assertEquals(fundListOriginal.get(i).getIDate(), fundListChanged.get(i).getIDate());
        assertEquals(fundListOriginal.get(i).getVsymbol(), fundListChanged.get(i).getVsymbol());
        assertEquals(fundListOriginal.get(i).getTrade(), fundListChanged.get(i).getTrade());
        assertEquals(fundListOriginal.get(i).getFullname(), fundListChanged.get(i).getFullname());
        assertEquals(fundListOriginal.get(i).getIdcode(), fundListChanged.get(i).getIdcode());
        assertEquals(fundListOriginal.get(i).getIdtype(), fundListChanged.get(i).getIdtype());
        assertEquals(fundListOriginal.get(i).getIddate(), fundListChanged.get(i).getIddate());
        assertEquals(fundListOriginal.get(i).getIdplace(), fundListChanged.get(i).getIdplace());
        assertEquals(fundListOriginal.get(i).getSex(), fundListChanged.get(i).getSex());
        assertEquals(fundListOriginal.get(i).getBirthdate(), fundListChanged.get(i).getBirthdate());
        assertEquals(fundListOriginal.get(i).getFax(), fundListChanged.get(i).getFax());
        assertEquals(fundListOriginal.get(i).getAddress(), fundListChanged.get(i).getAddress());
        assertEquals(fundListOriginal.get(i).getPhone(), fundListChanged.get(i).getPhone());
        assertEquals(fundListOriginal.get(i).getEmail(), fundListChanged.get(i).getEmail());
        assertEquals(fundListOriginal.get(i).getCountry(), fundListChanged.get(i).getCountry());
        assertEquals(fundListOriginal.get(i).getCusttype(), fundListChanged.get(i).getCusttype());
        assertEquals(fundListOriginal.get(i).getDbcode(), fundListChanged.get(i).getDbcode());
        assertEquals(fundListOriginal.get(i).getTaxno(), fundListChanged.get(i).getTaxno());
        assertEquals(fundListOriginal.get(i).getCustodycd(), fundListChanged.get(i).getCustodycd());
        assertEquals(fundListOriginal.get(i).getAcctype(), fundListChanged.get(i).getAcctype());
        assertEquals(fundListOriginal.get(i).getBankacc(), fundListChanged.get(i).getBankacc());
        assertEquals(fundListOriginal.get(i).getBankname(), fundListChanged.get(i).getBankname());
        assertEquals(fundListOriginal.get(i).getCitybank(), fundListChanged.get(i).getCitybank());
        assertEquals(fundListOriginal.get(i).getRefname1(), fundListChanged.get(i).getRefname1());
        assertEquals(fundListOriginal.get(i).getRefpost1(), fundListChanged.get(i).getRefpost1());
        assertEquals(fundListOriginal.get(i).getRefmobile1(), fundListChanged.get(i).getRefmobile1());
        assertEquals(fundListOriginal.get(i).getRefidcode1(), fundListChanged.get(i).getRefidcode1());
        assertEquals(fundListOriginal.get(i).getRefiddate1(), fundListChanged.get(i).getRefiddate1());
        assertEquals(fundListOriginal.get(i).getRefidplace1(), fundListChanged.get(i).getRefidplace1());
        assertEquals(fundListOriginal.get(i).getRefcountry1(), fundListChanged.get(i).getRefcountry1());
        assertEquals(fundListOriginal.get(i).getRefaddress1(), fundListChanged.get(i).getRefaddress1());
        assertEquals(fundListOriginal.get(i).getSymbol(), fundListChanged.get(i).getSymbol());
        assertEquals(fundListOriginal.get(i).getMbname(), fundListChanged.get(i).getMbname());
        assertEquals(fundListOriginal.get(i).getCustname(), fundListChanged.get(i).getCustname());
        assertEquals(fundListOriginal.get(i).getCfuidcode(), fundListChanged.get(i).getCfuidcode());
        assertEquals(fundListOriginal.get(i).getCfuiddate(), fundListChanged.get(i).getCfuiddate());
        assertEquals(fundListOriginal.get(i).getCfuidplace(), fundListChanged.get(i).getCfuidplace());
        assertEquals(fundListOriginal.get(i).getCfuadress(), fundListChanged.get(i).getCfuadress());
        assertEquals(fundListOriginal.get(i).getCfuphone(), fundListChanged.get(i).getCfuphone());
        assertEquals(fundListOriginal.get(i).getRate(), fundListChanged.get(i).getRate());
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
