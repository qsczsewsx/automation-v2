package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.common.KafkaSender;
import com.tcbs.automation.tools.DateTimeUtils;
import com.tcbs.automation.tools.SerenityTools;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/RegisterIACtgFromQueueData.csv", separator = '|')
public class RegisterIAFromQueueTest {
  private static KafkaSender kafkaSender;
  private String testCaseName;
  private String custodyCode;
  private String bankAccount;
  private String accountName;
  private String destinationType;
  private String messageType;
  private String sender;
  private String eKycType;
  private int code;
  private int numberRecord;
  private int statusBefore;
  private int statusAfter;
  private String flexStatus;
  private int numberRecordIaOld;
  private TcbsUser tcbsUser;

  @BeforeClass
  public static void beforeClass() {
    kafkaSender = KafkaSender.init();
  }

  @AfterClass
  public static void afterClass() {
    kafkaSender.close();
  }

  @Before
  public void before() {
    custodyCode = syncData(custodyCode);
    bankAccount = syncData(bankAccount);
    accountName = syncData(accountName);
    destinationType = syncData(destinationType);
    messageType = syncData(messageType);
    sender = syncData(sender);
    eKycType = syncData(eKycType);

    tcbsUser = TcbsUser.getByUserName(custodyCode);
    List<TcbsBankIaaccount> bankIaaccountList = TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString());
    numberRecordIaOld = bankIaaccountList == null ? 0 : bankIaaccountList.size();
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyRegisterAndUnregisterIACtgFromQueue() throws InterruptedException {
    UUID partnerTxnId = UUID.randomUUID();
    String partnerRegId;
    if (testCaseName.contains("case partnerRegId is null")) {
      partnerRegId = null;
    } else {
      partnerRegId = String.valueOf(new Date().getTime());
    }
    Map<String, Object> body = new HashMap<>();
    body.put("sender", sender);
    body.put("type", messageType);
    body.put("id", partnerTxnId);

    Map<String, Object> data = new HashMap<>();
    data.put("mesageId", partnerTxnId);
    data.put("destinationType", destinationType);

    Map<String, Object> accountData = new HashMap<>();
    accountData.put("partnerTxnId", partnerTxnId);
    accountData.put("transactionId", "");
    accountData.put("transTime",
      new SimpleDateFormat(DateTimeUtils.DATE_ISO_FORMAT_LOCAL_NO_MIL).format(new Date()));
    accountData.put("bankAccount", bankAccount);
    accountData.put("custodyCode", custodyCode);
    accountData.put("accountName", accountName);
    accountData.put("description", "dang ky thanh cong");
    accountData.put("subAccountNumber", "105C071588A");

    Map<String, Object> mapExtInfo = new HashMap<>();
    mapExtInfo.put("SCHEDULE_TYPE", "MONTHLY");
    mapExtInfo.put("PARTNER_REGISTER_ID", partnerRegId);
    mapExtInfo.put("EKYC_TYPE", eKycType);
    mapExtInfo.put("PAY_REF_NO", custodyCode);
    mapExtInfo.put("EMAIL", "trankhanhlinh@gmail.com");
    mapExtInfo.put("TRANSACTION_TYPE", null);
    mapExtInfo.put("PAY_REF_INFO", "");
    mapExtInfo.put("ADDRESS", "135 Thai Ha, Dong Da, Ha Noi, VN");
    mapExtInfo.put("PHONE_NUMBER", "0857471588");
    mapExtInfo.put("SCHEDULE_PERIOD", "20180418");
    mapExtInfo.put("TAX_CODE", "");
    mapExtInfo.put("ID_CARD", "893876529599");
    mapExtInfo.put("RECORD_NO", "1");

    accountData.put("extInfoMap", mapExtInfo);

    List<Map<String, Object>> accountList = new ArrayList<>();
    accountList.add(accountData);
    data.put("accountList", accountList);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    body.put("data", gson.toJson(data));

    SerenityTools.manualReport("kafka message", gson.toJson(body));


    kafkaSender.send("notify.isocias.req.ia", gson.toJson(body));
    Thread.sleep(1000);

    List<TcbsBankIaaccount> bankIaaccountList = TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString());
    assertThat("verify number of record IA account", bankIaaccountList.size(), is(numberRecordIaOld + numberRecord));

    if (code == 200) {
      assertThat("verify data in IA account table",
        bankIaaccountList.get(0),
        all(hasProperty("accountNo", is(bankAccount)))
          .and(hasProperty("bankCode", is("01201001")))
          .and(hasProperty("accountName", is(accountName)))
          .and(hasProperty("status", is(BigDecimal.valueOf(statusBefore))))
          .and(hasProperty("autoTransfer", is("N")))
          .and(hasProperty("isIaPaid", is("N")))
          .and(hasProperty("bankSource", is("CTG")))
      );

      Response response = given()
        .baseUri(AUTO_RETRY_TASK)
        .header("x-api-key", API_KEY)
        .when()
        .get();
      assertThat("verify status of job retry task", response.getStatusCode(), is(200));

      Thread.sleep(3000);
      assertThat("verify IA data after call flex",
        TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString()).get(0).getStatus(),
        anyOf(is(BigDecimal.valueOf(statusAfter)), is(BigDecimal.valueOf(2)))
      );

      // check IA status from flex
      Response responseFlex = given()
        .baseUri(FLEX_ACCOUNT_INFO)
        .header("x-api-key", API_KEY)
        .param("marginAccount", "N")
        .param("eKycType", "normal")
        .param("custodyID", custodyCode)
        .when()
        .get();
      assertThat("verify IA status from flex",
        responseFlex.jsonPath().get(),
        hasProperty("data.0.isIA", is(flexStatus))
      );
    }
  }
}
