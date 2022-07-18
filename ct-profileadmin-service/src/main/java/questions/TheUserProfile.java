package questions;

import com.tcbs.automation.cas.*;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Question;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.profileadmin.ProfileAdminKey.PA_USER_PROFILE_DETAIL;

public class TheUserProfile {
  public static Question<HashMap<String, Object>> forTcbsUserfromAPI() {
    return Question.about("Get user Profile")
      .answeredBy(actor -> {
        HashMap<String, Object> output;
        Response resp = actor.recall(PA_USER_PROFILE_DETAIL);
        output = resp.jsonPath().get();
        return output;
      });
  }

  public static Question<List<TcbsBankAccount>> forBankfromDB(String userId) {
    return Question.about("TCBS_BANK_ACCOUNT from database")
      .answeredBy(
        actor -> {
          TcbsBankAccount tcbsBankAccount = new TcbsBankAccount();
          return tcbsBankAccount.getListBanks(userId);
        }
      );
  }

  public static Question<TcbsUser> forTcbsUserfromDB(String tcbsId) {
    return Question.about("TCBS_USER from database")
      .answeredBy(
        actor -> {
          TcbsUser tcbsUser = new TcbsUser();
          return tcbsUser.getByTcbsId(tcbsId);
        }
      );
  }

  public static Question<TcbsAddress> forAddressfromDB(String userId) {
    return Question.about("TCBS_ADDRESS from database")
      .answeredBy(
        actor -> {
          TcbsAddress tcbsAddress = new TcbsAddress();
          return tcbsAddress.getByTcbsAddress(userId);
        }
      );
  }

  public static Question<TcbsUserOpenAccountQueue> forAllAddressfromDB(String userId) {
    return Question.about("TCBS_USER_OPENACCOUNT_QUEUE from database")
      .answeredBy(
        actor -> {
          TcbsUserOpenAccountQueue tcbsUserOpenaccountQueue = new TcbsUserOpenAccountQueue();
          return tcbsUserOpenaccountQueue.getByTcbsUserOpenAccountQueue(userId);
        }
      );
  }

  public static Question<TcbsUserInstrument> forUserInstrumentfromDB(String userId) {
    return Question.about("TCBS_USER_INSTRUMENT from database")
      .answeredBy(
        actor -> {
          TcbsUserInstrument tcbsUserInstrument = new TcbsUserInstrument();
          return tcbsUserInstrument.getByTcbsInstrument(userId);
        }
      );
  }

  public static Question<TcbsUserAccount> forUserAccountfromDB(String userId) {
    return Question.about("TCBS_USER_ACCOUNT from database")
      .answeredBy(
        actor -> {
          TcbsUserAccount tcbsUserAccount = new TcbsUserAccount();
          return tcbsUserAccount.getByTcbsAccount(userId);
        }
      );
  }

  public static Question<TcbsApplicationUser> forApplicationUserflexfromDB(String userId, String appId) {
    String flexId;
    return Question.about("TCBS_APPLICATION_USER_FLEXID rom database")
      .answeredBy(
        actor -> {
          TcbsApplicationUser tcbsApplicationUser = new TcbsApplicationUser();
          return tcbsApplicationUser.getByTcbsApplicationUserAppId2(userId, appId);
        }
      );
  }

  public static Question<TcbsIdentification> forIdentificationfromDB(String userId) {
    return Question.about("TCBS_IDENTIFICATION from database")
      .answeredBy(
        actor -> {
          TcbsIdentification tcbsIdentification = new TcbsIdentification();
          return tcbsIdentification.getByTcbsIdentification(userId);
        }
      );
  }
}
