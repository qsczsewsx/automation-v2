package matchers;

import com.tcbs.automation.cas.*;
import com.tcbs.automation.tools.DateTimeUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.publicmatcher.Verifier.verify;
import static com.tcbs.automation.tools.DateTimeUtils.DATE_FORMAT_VN;
import static com.tcbs.automation.tools.DateTimeUtils.ISO_STANDARD_DATE;
import static org.hamcrest.Matchers.equalTo;

public class UserProfileMatcher {

  public static Matcher<HashMap<String, Object>> sameAs(TcbsUser tcbsUser, TcbsAddress tcbsAddress, TcbsUserOpenaccountQueue tcbsUserOpenaccountQueue, TcbsUserAccount tcbsUserAccount,
                                                        TcbsApplicationUser tcbsApplicationUsers2, TcbsApplicationUser tcbsApplicationUsers4, TcbsIdentification tcbsIdentification,
                                                        TcbsUserInstrument tcbsUserInstrument) {
    return new TypeSafeMatcher<HashMap<String, Object>>() {
      Description mismatchDescriber = new StringDescription();

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(HashMap<String, Object> tcbsUserfromAPI) {
        String expectedBirthday = "";
        String expectedCreatedDate = "";
        String expectedIdDate = "";

       // Date inputBirthday = tcbsUser.getBirthday();
       // Date inputCreatedDate = tcbsUser.getCreatedDate();

        Date inputExpectedDate = tcbsIdentification.getIdDate();
      //  expectedBirthday = DateTimeUtils.convertDateToString(inputBirthday, DATE_FORMAT_VN);
      //  expectedCreatedDate = DateTimeUtils.convertDateToString(inputCreatedDate, DATE_FORMAT_VN);
        expectedIdDate = DateTimeUtils.convertDateToString(inputExpectedDate, DATE_FORMAT_VN);

        mismatchDescriber.appendText(String.format("\n====Validating userprofile: <%s>,<%s> ======", tcbsUser.getFirstname(), tcbsUser.getLastname()));
        return verify(tcbsUserfromAPI,
          all(
            has("user.firstName", equalTo(tcbsUser.getFirstname())))
            .and(
              has("user.lastName", equalTo(tcbsUser.getLastname())))
            .and(
              has("user.email", equalTo(tcbsUser.getEmail())))
            .and(
              has("user.phone", equalTo(tcbsUser.getPhone())))
            .and(
              has("user.gender", equalTo(tcbsUser.getGender())))
            .and(
              has("user.birthday", equalTo(expectedBirthday)))
            .and(
              has("user.status", equalTo(tcbsUser.getStatus())))
            .and(
              has("user.accountStatus", equalTo(tcbsUser.getAccountStatus())))
            .and(
              has("user.custType", equalTo(tcbsUser.getCustype())))
            .and(
              has("user.createdDate", equalTo(expectedCreatedDate)))
            .and(
              has("user.originalId", equalTo(tcbsAddress.getOriginalId())))
            .and(
              has("user.permanentAddress", equalTo(tcbsUserOpenaccountQueue.getPermanentaddress())))
            .and(
              has("user.bankAccounts.0.accountNo", equalTo(tcbsUserAccount.getBankAccountNo())))
            .and(
              has("user.bankAccounts.0.accountName", equalTo(tcbsUserAccount.getBankAccountName())))
            .and(
              has("user.bankAccounts.0.bankCode", equalTo(tcbsUserAccount.getBankCode())))
            .and(
              has("user.bankAccounts.0.bankName", equalTo(tcbsUserAccount.getBankName())))
            .and(
              has("user.bankAccounts.0.bankBranch", equalTo(tcbsUserAccount.getBankBranch())))
            .and(
              has("user.bankAccounts.0.bankProvince", equalTo(tcbsUserAccount.getBankprovince())))
            .and(
              has("user.statusVSDFlexActivated", equalTo(tcbsApplicationUsers2.getStatus())))
            .and(
              has("user.statusVSDFundActivated", equalTo(tcbsApplicationUsers4.getStatus())))
            .and(
              has("user.IDNumber", equalTo(tcbsIdentification.getIdNumber())))
            .and(
              has("user.IDDate", equalTo(expectedIdDate)))
            .and(
              has("user.IDPlace", equalTo(tcbsIdentification.getIdPlace())))
            .and(
              has("user.nationality", equalTo(tcbsUserInstrument.getCitizenship())))
            .and(
              has("user.job", equalTo(tcbsUserInstrument.getJob())))
          , mismatchDescriber);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("\nUser profile from API should be the same as database");
      }

      @Override
      protected void describeMismatchSafely(HashMap<String, Object> theMismatchItem, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}
