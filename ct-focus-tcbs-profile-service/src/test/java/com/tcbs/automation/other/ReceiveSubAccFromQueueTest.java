package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsBankSubaccount;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.common.KafkaSender;
import com.tcbs.automation.tools.SerenityTools;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.*;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.*;

import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/ReceiveSubAccFromQueue.csv", separator = '|')
public class ReceiveSubAccFromQueueTest {
  private static KafkaSender kafkaSender;
  private String testCaseName;
  private String afAcctno;
  private String custodyId;
  private String productCodes;
  private String accountStatus;
  private String expectAccountType;
  private String expectUserType;
  private String isIA;
  private String isIaPaid;
  private String autoTransfer;
  private String bankAccountNo;
  private int expectStatus;
  private int expectIA;
  private TcbsUser tcbsUser;
  private String userId;
  private int code;

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
    afAcctno = syncData(afAcctno);
    custodyId = syncData(custodyId);
    productCodes = syncData(productCodes);
    accountStatus = syncData(accountStatus);
    expectAccountType = syncData(expectAccountType);
    expectUserType = syncData(expectUserType);
    isIA = syncData(isIA);
    isIaPaid = syncData(isIaPaid);
    autoTransfer = syncData(autoTransfer);
    bankAccountNo = syncData(bankAccountNo);
    tcbsUser = TcbsUser.getByUserName(custodyId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void performTest() throws InterruptedException {
    List<String> productCodeList = new ArrayList<String>(Arrays.asList(productCodes.split(",")));
    Map<String, Object> body = new HashMap<>();
    body.put("afAcctno", afAcctno);
    body.put("custodyId", custodyId);
    body.put("productCodes", productCodeList);
    body.put("accountStatus", accountStatus);
    body.put("isIA", isIA);
    body.put("isDefault", "N");
    body.put("bankName", "TCBHN");
    body.put("isIaPaid", isIaPaid);
    body.put("autoTransfer", autoTransfer);
    body.put("bankAccount", bankAccountNo);

    Gson gson = new Gson();
    SerenityTools.manualReport("kafka message", gson.toJson(body));

    kafkaSender.send("notify.flexsync.accounts", gson.toJson(body));
    Thread.sleep(5000);

    userId = tcbsUser.getId().toString();

    if (code == 200) {
      TcbsBankSubaccount bankSubAccount = TcbsBankSubaccount.getBankByAccountNoAndUserId(afAcctno, userId).get(0);
      assertThat("verify account type", bankSubAccount.getAccountType(), is(expectAccountType));
      assertThat("verify user type", bankSubAccount.getUserType(), is(expectUserType));
      assertThat("verify status", bankSubAccount.getStatus(), is(new BigDecimal(expectStatus)));
      TcbsBankIaaccount bankIaaccount = TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString()).get(0);
      assertThat("verify isIaPaid", bankIaaccount.getIsIaPaid(), is(isIaPaid));
      assertThat("verify autoTranfer", bankIaaccount.getAutoTransfer(), is(autoTransfer));
      assertThat("verify isIA", bankIaaccount.getStatus(), is(new BigDecimal(expectIA)));
    }
  }

  @After
  public void after() throws InterruptedException {
    Thread.sleep(2000);
    TcbsBankSubaccount.deleteByAccountNo(afAcctno);
    if (testCaseName.contains("accountStatus as close") || testCaseName.contains("productCodes as")) {
      TcbsBankIaaccount.deleteByUserId(userId);
    }
  }
}