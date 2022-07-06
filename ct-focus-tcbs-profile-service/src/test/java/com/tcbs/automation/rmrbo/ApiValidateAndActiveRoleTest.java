package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiValidateAndActiveRole.csv", separator = '|')
public class ApiValidateAndActiveRoleTest {
  private static final Logger logger = LoggerFactory.getLogger(ApiValidateAndActiveRoleTest.class);
  private static final String DATA_TCB = "TCB";
  protected static BigDecimal ACTIVE_STATUS = new BigDecimal(1);
  protected static final List<String> CONFIG_ROLE_LIST = Arrays.asList(ConvertRole.RM.shortName, ConvertRole.RBO.shortName, ConvertRole.IWP.shortName);
  private static final String APPLICATION_JSON = "application/json";
  private static final String DATA_X_API_KEY = "x-api-key";

  private HashMap<String, Object> body;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;

  private String expected;
  private Boolean emailTechcombank;
  private String inListHrm;
  private String passElearning;
  private String roleBefore;
  private String bankCodeBefore;
  private R3RdUser oldR3RdUser;
  private TcbsUser tcbsUser;

  private List<String> oldWso2RoleList;

  @Before
  public void before() {
    body = new HashMap<>();
    if (StringUtils.isNotBlank(tcbsId)) {
      try {
        tcbsUser = TcbsUser.getByTcbsId(tcbsId);
        String email;
        if (Boolean.TRUE.equals(emailTechcombank)) {
          email = tcbsId + UUID.randomUUID() + "@techcombank.com.vn";
        } else {
          email = tcbsId + UUID.randomUUID() + "@gmail.com.vn";
        }
        TcbsUser.updateEmailByTcbsid(email, tcbsId);
        tcbsUser.setEmail(email);
        setUpR3RdSyncTcbData(tcbsUser, inListHrm);
        oldR3RdUser = setUpR3RdUserData(tcbsUser, roleBefore, bankCodeBefore, 1);
        setUpR3RdLearningData(tcbsUser, passElearning);

        oldWso2RoleList = getRoleInWso2By105C(tcbsUser.getUsername());
      } catch (NoResultException ex) {
        logger.error("{} not found", tcbsId);
      }
    }

    if (testCaseName.contains("not with tcbsId")) {
      body.put("tcbsId", new ArrayList<>());
    } else {
      body.put("tcbsId", Collections.singletonList(tcbsId));
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API validate and active role")
  public void apiValidateAndActiveRole() {
    System.out.println("Testcase Name : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(VALIDATE_AND_ACTIVE_ROLE)
      .header(DATA_X_API_KEY, testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : RMRBO_API_KEY)
      .contentType(APPLICATION_JSON);

    Response response;
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      R3RdUser r3RdUser = null;
      try {
        r3RdUser = R3RdUser.getByTcbsId(tcbsId);
      } catch (NoResultException ex) {
        logger.error("{} not found", tcbsId);
      }
      List<String> newWso2RoleList = getRoleInWso2By105C(tcbsUser.getUsername());
      if (expected.contains("not insert into r3rd_user")) {
        assertNull(r3RdUser);
        for (String r : oldWso2RoleList) {
          assertTrue(newWso2RoleList.contains(r));
        }
      } else if (expected.contains("not inactive in r3rd_user")) {
        assertNotNull(r3RdUser);
        assertEquals(oldR3RdUser.getStatus(), r3RdUser.getStatus());
        assertEquals(oldR3RdUser.getMasterRole(), r3RdUser.getMasterRole());
        assertEquals(oldR3RdUser.getRoleId(), r3RdUser.getRoleId());
        for (String r : oldWso2RoleList) {
          assertTrue(newWso2RoleList.contains(r));
        }
      } else if (expected.contains("inactive in r3rd_user")) {
        assertNotNull(r3RdUser);
        assertEquals(new BigDecimal(0), r3RdUser.getStatus());
        assertFalse(newWso2RoleList.contains(roleBefore));
      } else if (expected.contains("active in r3rd_user")) {
        assertNotNull(r3RdUser);
        assertEquals(ACTIVE_STATUS, r3RdUser.getStatus());
        R3RdRole r3RdRole = R3RdRole.getRoleByNameAndBankCode(inListHrm, r3RdUser.getBankCode());
        assertEquals(r3RdRole.getId(), r3RdUser.getRoleId());
        assertEquals("NORMAL", r3RdUser.getMasterRole());
        assertTrue(newWso2RoleList.contains(inListHrm));
        for (String r : CONFIG_ROLE_LIST) {
          if (!r.equals(inListHrm)) {
            assertFalse(newWso2RoleList.contains(r));
          }
        }
        if (testCaseName.contains("update agency in r3rd_user")) {
          assertEquals("CTO", r3RdUser.getAgency());
        }
      }
    }
    if (statusCode == 401) {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  public static List<String> getRoleInWso2By105C(String code105C) {
    List<String> roles = new ArrayList<>();
    Response res = given()
      .header(DATA_X_API_KEY, RMRBO_API_KEY)
      .contentType(APPLICATION_JSON).get(WSO2_GET_LIST_ROLE_BY_USERNAME + "USER105C/" + code105C);
    if (res.getStatusCode() == 200) {
      List<String> tmp = res.jsonPath().getList("data");
      for (String r : tmp) {
        roles.add(ConvertRole.getShortName(r));
      }
    }

    return roles;
  }

  public static void updateRoleInWso2By105C(String code105C, List<String> rolesAdd, List<String> rolesRemove) {
    List<String> tmpAdd = new ArrayList<>();
    for (String r : rolesAdd) {
      tmpAdd.add(ConvertRole.getFullName(r));
    }
    List<String> tmpRemove = new ArrayList<>();
    for (String r : rolesRemove) {
      tmpRemove.add(ConvertRole.getFullName(r));
    }

    HashMap<String, Object> req = new HashMap<>();
    req.put("userName", "USER105C/" + code105C);
    req.put("lstRoleAdd", tmpAdd);
    req.put("lstRoleRemove", tmpRemove);
    System.out.println(req);
    Response res = given()
      .baseUri(WSO2_UPDATE_ROLE_OF_USER)
      .header(DATA_X_API_KEY, RMRBO_API_KEY)
      .contentType(APPLICATION_JSON)
      .body(req)
      .post();
    System.out.println(res.jsonPath().prettyPrint());
  }

  public static R3RdUser setUpR3RdUserData(TcbsUser tcbsUser, String roleBefore, String bankCodeBefore, int statusBefore) {
    R3RdUser.deleteByTcbsId(tcbsUser.getTcbsid());
    R3RdUser oldR3RdUser = null;
    if (StringUtils.isNotEmpty(roleBefore)) {
      String role = roleBefore.contains(ConvertRole.RM.shortName) ? ConvertRole.RM.shortName : ConvertRole.RBO.shortName;
      updateRoleInWso2By105C(tcbsUser.getUsername(), Collections.singletonList(role), new ArrayList<>());
      String masterRole = roleBefore.contains("S") || roleBefore.contains("H") ? roleBefore : "NORMAL";
      R3RdRole r3RdRole = R3RdRole.getRoleByNameAndBankCode(role, bankCodeBefore);
      oldR3RdUser = R3RdUser.builder()
        .tcbsId(tcbsUser.getTcbsid())
        .username("khangnm")
        .custodyCode(tcbsUser.getUsername())
        .fullName(tcbsUser.getFullName())
        .email(tcbsUser.getEmail())
        .bankCode(DATA_TCB)
        .branchCode("NTD")
        .agency("267")
        .roleId(r3RdRole.getId())
        .status(new BigDecimal(statusBefore))
        .masterRole(masterRole)
        .build();
      oldR3RdUser.insert();
    } else {
      updateRoleInWso2By105C(tcbsUser.getUsername(), new ArrayList<>(), CONFIG_ROLE_LIST);
    }

    return oldR3RdUser;
  }

  public static void setUpR3RdLearningData(TcbsUser tcbsUser, String passELearning) {
    R3RdElearning.deleteByTcbsId(tcbsUser.getTcbsid());
    if (StringUtils.isNotBlank(passELearning)) {
      R3RdElearning r3RdElearning = R3RdElearning.builder()
        .tcbsId(tcbsUser.getTcbsid())
        .fullName(tcbsUser.getFullName())
        .courseStatus(ACTIVE_STATUS)
        .username(tcbsUser.getUsername())
        .bankCode(DATA_TCB)
        .role(passELearning)
        .courseCode("EA2132133")
        .build();
      r3RdElearning.insert();
    }
  }

  public static void setUpR3RdSyncTcbData(TcbsUser tcbsUser, String inListHrm) {
    R3RdSyncTcb.deleteByTcbsId(tcbsUser.getTcbsid());
    if (StringUtils.isNotBlank(inListHrm)) {
      R3RdSyncTcb r3RdSyncTcb = R3RdSyncTcb.builder()
        .email(tcbsUser.getEmail())
        .branchCode("CTO")
        .tcbsId(tcbsUser.getTcbsid())
        .username("khangnm")
        .role(inListHrm)
        .fullName(tcbsUser.getFullName())
        .saleCode(tcbsUser.getTcbsid())
        .status(ACTIVE_STATUS)
        .build();
      r3RdSyncTcb.insert();
    }
  }
}

enum ConvertRole {
  RM("Application/RMs", "RM"), RBO("Application/RBO", "RBO"), IWP("Application/IWPs", "IWP");

  public final String fullName;
  public final String shortName;

  ConvertRole(String fullName, String shortName) {
    this.fullName = fullName;
    this.shortName = shortName;
  }

  public static String getShortName(String fullName) {
    for (ConvertRole r : ConvertRole.values()) {
      if (fullName.equals(r.fullName)) {
        return r.shortName;
      }
    }
    return fullName;
  }

  public static String getFullName(String shortName) {
    for (ConvertRole r : ConvertRole.values()) {
      if (shortName.equals(r.shortName)) {
        return r.fullName;
      }
    }
    return shortName;
  }
}