package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsBankSubaccount;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.common.KafkaSender;
import com.tcbs.automation.tools.DateTimeUtils;
import com.tcbs.automation.tools.SerenityTools;
import common.CallApiUtils;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.*;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.DELETE_CACHE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/ReceiveISaveFromQueue.csv", separator = '|')
public class ReceiveISaveFromQueueTest {
  private static KafkaSender kafkaSender;
  private String testCaseName;
  private String accountId;
  private String custodyCd;
  private String accountType;
  private String status;
  private String createdAt;
  private TcbsUser tcbsUser;
  private int code;
  private String accountNo;

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
    accountId = syncData(accountId);
    custodyCd = syncData(custodyCd);
    accountType = syncData(accountType);
    status = syncData(status);
    createdAt = new SimpleDateFormat(DateTimeUtils.DATE_ISO_FORMAT_T_LOCAL_STR).format(new Date());
    tcbsUser = TcbsUser.getByUserName(custodyCd);
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void performTest() throws InterruptedException {
    Map<String, Object> body = new HashMap<>();
    body.put("accountId", accountId);
    body.put("custodyCd", custodyCd);
    body.put("accountType", accountType);
    body.put("status", status);
    body.put("createdAt", createdAt);

    Gson gson = new Gson();
    SerenityTools.manualReport("kafka message", gson.toJson(body));

    kafkaSender.send("sit.internal.p2p.account", gson.toJson(body));
    Thread.sleep(1000);

    if (code == 200) {
      TcbsBankSubaccount bankSubAccount = TcbsBankSubaccount.getSubAccountByUserIdAndAccountType(tcbsUser.getId().toString(), "ISAVE");
      accountNo = bankSubAccount.getAccountNo();
      assertThat("verify ISave accountNo", accountNo, is(accountId));
    }
  }

  @After
  public void after() throws InterruptedException {
    if (code == 200) {
      Thread.sleep(2000);
      TcbsBankSubaccount.deleteByAccountNo(accountNo);
      CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
    }
  }
}
