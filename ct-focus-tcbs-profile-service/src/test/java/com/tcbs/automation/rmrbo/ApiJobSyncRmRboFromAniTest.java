package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcbs.automation.cas.R3RdSyncTcb;
import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.*;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiJobSyncRmRboFromAni.csv", separator = '|')
public class ApiJobSyncRmRboFromAniTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;

  private List<AnIUserDto> activeUsersAni;

  @Before
  public void before() {
    if (!testCaseName.contains("x-api-key invalid")) {
      activeUsersAni = new ArrayList<>();
      getDataFromAnIByRole("RM", activeUsersAni);
      getDataFromAnIByRole("RBO", activeUsersAni);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API sync data from AnI")
  public void apiSyncDataFromAnI() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .baseUri(SYNC_DATA_FROM_ANI)
      .header("x-api-key", testCaseName.contains("x-api-key invalid") ? FMB_X_API_KEY : RMRBO_API_KEY)
      .contentType("application/json")
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<R3RdSyncTcb> activeUsersDB = R3RdSyncTcb.getListRmRboActive();
      assertEquals(activeUsersAni.size(), activeUsersDB.size());
      for (AnIUserDto activeUserAni : activeUsersAni) {
        boolean hasData = false;
        for (R3RdSyncTcb activeUserDB : activeUsersDB) {
          if (activeUserAni.getTcbsId().equals(activeUserDB.getTcbsId()) && activeUserAni.getRole().equals(activeUserDB.getRole())) {
            hasData = true;
            assertEquals("RM".equals(activeUserAni.getRole()) ? activeUserAni.getUserName() : activeUserAni.getCustodyCd(), activeUserDB.getUsername());
            assertEquals(activeUserAni.getBranch(), activeUserDB.getBranchCode());
            assertEquals(activeUserAni.getFullName(), activeUserDB.getFullName());
            assertEquals(Objects.nonNull(activeUserAni.getEmail()) ? activeUserAni.getEmail() : "", Objects.nonNull(activeUserDB.getEmail()) ? activeUserDB.getEmail() : "");
            assertEquals(activeUserAni.getRmCode(), activeUserDB.getSaleCode());
            assertEquals(activeUserAni.getRole(), activeUserDB.getRole());
            break;
          }
        }
        assertTrue(hasData);
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  public void getDataFromAnIByRole(String role, List<AnIUserDto> activeUsersAni) {
    int totalPage = 0;
    int pageSize = 1000;
    AnIUserResponse rs = getDataFromAnIByPage(role, 0, pageSize);
    if (Objects.nonNull(rs) && Objects.nonNull(rs.getResultSet1()) && rs.getResultSet1().size() > 0) {
      totalPage = rs.getResultSet2().get(0).getTotalPages();
      for (AnIUserDto user : rs.getResultSet1()) {
        user.setRole(role);
      }
      activeUsersAni.addAll(rs.getResultSet1());
    }
    if (totalPage > 1) {
      for (int i = 1; i <= totalPage; i++) {
        rs = getDataFromAnIByPage(role, i, pageSize);
        for (AnIUserDto user : rs.getResultSet1()) {
          user.setRole(role);
        }
        activeUsersAni.addAll(rs.getResultSet1());
      }
    }
  }

  public AnIUserResponse getDataFromAnIByPage(String role, int page, int size) {
    Response response = given()
      .baseUri(API_ANI_V2_DOMAIN+"/get"+role)
      .param("page", page)
      .param("size", size)
      .header("x-client-key", "RM".equals(role) ? API_KEY_ANI_V2_GET_RM : API_KEY_ANI_V2_GET_RBO)
      .header("x-api-key", RMRBO_API_KEY)
      .contentType("application/json")
      .get();
    return response.jsonPath().getObject("value", AnIUserResponse.class);
  }
}

@Getter
class AnIUserResponse {
  private List<AnIUserDto> resultSet1;
  private List<AnIUserPagingDto> resultSet2;
}

@Getter
@Setter
class AnIUserDto {
  @JsonProperty("branch")
  private String branch;
  @JsonProperty("rmcode")
  private String rmCode;
  @JsonProperty("fullname")
  private String fullName;
  @JsonProperty("email")
  private String email;
  @JsonProperty("username")
  private String userName;
  @JsonProperty("tcbsid")
  private String tcbsId;
  @JsonProperty("custodycd")
  private String custodyCd;
  private String role;
}

@Getter
class AnIUserPagingDto {
  private Integer curPage;
  private Integer rowNo;
  private Integer totalRow;
  private Integer totalPages;
}
