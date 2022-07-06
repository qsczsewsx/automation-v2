package questions.oldregister;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Question;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_REGISTER_RESP;

public class TheOldRegisterQuestion {

  public static Question<HashMap<String, Object>> fromApi() {
    return Question.about("Get all response after update old registered customer")
      .answeredBy(actor -> {
        HashMap<String, Object> output;
        Response resp = actor.recall(TCBS_PS_OLD_REGISTER_RESP);
        output = resp.jsonPath().get();
        return output;
      });
  }

  public static Question<HashMap<String, Object>> inputJsonToMap(String inputBody) {
    return Question.about("convert json string to hashmap")
      .answeredBy(actor -> {
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        ObjectMapper mapperObj = new ObjectMapper();
        try {
          resultMap = mapperObj.readValue(inputBody,
            new TypeReference<HashMap<String, Object>>() {
            });
          System.out.println("Output Map: " + resultMap);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        List<LinkedHashMap> tcbsBankAccounts = mapperObj.convertValue(resultMap.get("bankAccounts"), new TypeReference<List<LinkedHashMap>>() {
        });
        tcbsBankAccounts.get(0).put("accountNo", tcbsBankAccounts.get(0).get("accountNo").toString().replaceAll("[^a-zA-Z0-9]", ""));
        tcbsBankAccounts.get(0).put("accountName", tcbsBankAccounts.get(0).get("accountName").toString().trim());
        resultMap.put("bankAccounts", tcbsBankAccounts);
        return resultMap;
      });
  }


}
