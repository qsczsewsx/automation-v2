package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdRole;
import com.tcbs.automation.cas.R3RdUser;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.rmrbo.ApiValidateAndActiveRoleTest.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiUpdateRoleRmRbo.csv", separator = '|')
public class UpdateRoleRmRboFromBackendTest {
  private static Logger logger = LoggerFactory.getLogger(UpdateRoleRmRboFromBackendTest.class);
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String custodyCode;
  private String bankCode;
  private String role;
  private String masterRole;
  private String note;
  private String status;
  private String branchCode;
  private String expected;
  private String roleBefore;
  private int statusBefore;
  private Boolean emailTechcombank;
  private String inListHrm;
  private String passElearning;
  private String actor;
  private HashMap<String, Object> body;
  private TcbsUser tcbsUser;

  private List<String> oldWso2RoleList;

  private R3RdUser oldR3RdUser;

  @Before
  public void before() {
    if ("checker".equals(actor)) {
      actor = RMRBO_CHECKER_AUTHORIZATION_KEY;
    } else if ("maker".equals(actor)) {
      actor = "eyJ4NXQiOiJaalJtWVRNd05USmpPV1U1TW1Jek1qZ3pOREkzWTJJeU1tSXlZMkV6TWpkaFpqVmlNamMwWmciLCJraWQiOiJaalJtWVRNd05USmpPV1U1TW1Jek1qZ3pOREkzWTJJeU1tSXlZMkV6TWpkaFpqVmlNamMwWmdfUlMyNTYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJSTVJCT19NQU5BR0VNRU5UX01BS0VSIiwiYXVkIjoiQnJvRlVtMWtmNld2dFdKN01oTUFKT25kbFZjYSIsIm5iZiI6MTY1NDc0MDMzMCwiYXpwIjoiQnJvRlVtMWtmNld2dFdKN01oTUFKT25kbFZjYSIsInNjb3BlIjoib3BlbmlkIiwiaXNzIjoiaHR0cHM6XC9cL3dzbzJpcy1zaXQudGNicy5jb20udm5cL29hdXRoMlwvdG9rZW4iLCJuYW1lIjoiUk1SQk9fTUFOQUdFTUVOVF9NQUtFUiIsImdyb3VwcyI6WyJJbnRlcm5hbFwvZXZlcnlvbmUiLCJBcHBsaWNhdGlvblwvUk1SQk9fTUFOQUdFTUVOVF9NQUtFUiJdLCJleHAiOjE2NTQ3NDM5MzAsImlhdCI6MTY1NDc0MDMzMCwianRpIjoiZTEwNGU2YTItYWIyYS00MTFkLThmOTktNWRhNjhjNmJlMDE5In0.iRBxt6QUIa1y3uV7lx4tN5Xgu_0SQAqIYKbrkzPJQKtsT6_T1CqUcYE9WydCRQxy_o-C2wYEYy3ey0ruPlrVuB8agW9NCsN0PBEKLq89ZGqnWDiESPqG59tSuL73xxHkjxmE3liht4yr9ORJnr3n8FCi3ViV4f1baTu_U3O8o9_qDT1dEBU7KhPAdYYlGP7YdjeR_RzzsC9U-C1H1H9ZmluWRLVqOJm9-vPM2SJOzCgg73dCTns0pgBdVtuzhcT6bCKN6lXTpz4GI9gCGhr5Bzz85BVipUdp80i5isphTE_dwraZszU3of_haBhE-AWabHJIPqcEi32DELXEUL3tpQ";
    } else {
      actor = TCBSPROFILE_AUTHORIZATION;
    }

    body = new HashMap<>();
    custodyCode = syncData(custodyCode);
    body.put("custodyCode", custodyCode);
    bankCode = syncData(bankCode);
    body.put("bankCode", bankCode);
    role = syncData(role);
    body.put("role", role);
    masterRole = syncData(masterRole);
    body.put("masterRole", masterRole);
    branchCode = syncData(branchCode);
    body.put("branchCode", branchCode);
    note = syncData(note);
    body.put("note", note);
    if (!"null".equals(status)) {
      body.put("status", status);
    }

    if (!"empty".equals(custodyCode) && !"null".equals(custodyCode) && !testCaseName.contains("custodyCode not exist in db")) {
      try {
        tcbsUser = TcbsUser.getByUserName(custodyCode);
        String fullName = tcbsUser.getLastname() + " " + tcbsUser.getFirstname();
        String email;
        if (Boolean.TRUE.equals(emailTechcombank)) {
          email = custodyCode + UUID.randomUUID() + "@techcombank.com.vn";
        } else {
          email = custodyCode + UUID.randomUUID() + "@gmail.com.vn";
        }
        TcbsUser.updateEmailByUsername(email, custodyCode);
        tcbsUser.setEmail(email);
        setUpR3RdSyncTcbData(tcbsUser, inListHrm);
        oldR3RdUser = setUpR3RdUserData(tcbsUser, roleBefore, "TCB", statusBefore);
        setUpR3RdLearningData(tcbsUser, passElearning);

        oldWso2RoleList = getRoleInWso2By105C(custodyCode);
      } catch (NoResultException ex) {
        logger.error("{} not found", custodyCode);
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API update role RM/RBO in backend")
  public void updateRoleRMRBOFromBackend() {
    System.out.println("Test case name: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(RMRBO_BACKEND_UPDATE_ROLE)
      .header("Authorization", "Bearer " + actor)
      .contentType("application/json");
    Response response;
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    System.out.println(body);
    System.out.println(response.jsonPath().prettyPrint());
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      R3RdUser r3RdUser = null;
      try {
        r3RdUser = R3RdUser.getByTcbsId(tcbsUser.getTcbsid());
      } catch (NoResultException ex) {
        logger.error("{} not found", tcbsUser.getTcbsid());
      }
      List<String> newWso2RoleList = getRoleInWso2By105C(tcbsUser.getUsername());
      if (testCaseName.contains("inactive in r3rd_user and wso2")) {
        assertNotNull(r3RdUser);
        assertEquals(new BigDecimal(0), r3RdUser.getStatus());
        R3RdRole r3RdRole = R3RdRole.getRoleByNameAndBankCode(role, bankCode);
        assertEquals(r3RdRole.getId(), r3RdUser.getRoleId());
        assertFalse(newWso2RoleList.contains(role));
      } else if (testCaseName.contains("active in r3rd_user and wso2")) {
        assertNotNull(r3RdUser);
        assertEquals(ACTIVE_STATUS, r3RdUser.getStatus());
        R3RdRole r3RdRole = R3RdRole.getRoleByNameAndBankCode(role, bankCode);
        assertEquals(r3RdRole.getId(), r3RdUser.getRoleId());
        assertEquals(masterRole, r3RdUser.getMasterRole());
        assertEquals(bankCode, r3RdUser.getBankCode());
        assertEquals(branchCode, r3RdUser.getBranchCode());
        assertTrue(newWso2RoleList.contains(role));
        for (String r : CONFIG_ROLE_LIST) {
          if (!r.equals(role)) {
            assertFalse(newWso2RoleList.contains(r));
          }
        }
      }
    } else if (statusCode == 403) {
      assertEquals(errorMessage, response.jsonPath().get("errorMessage"));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
      if (Objects.nonNull(tcbsUser)) {
        List<String> newWso2RoleList = getRoleInWso2By105C(tcbsUser.getUsername());
        for (String r : oldWso2RoleList) {
          assertTrue(newWso2RoleList.contains(r));
        }
      }
    }
  }
}
