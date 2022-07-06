package matchers.oldregister;

import com.tcbs.automation.cas.TcbsAddress;
import com.tcbs.automation.cas.TcbsBankAccount;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.tools.DateTimeUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.publicmatcher.Verifier.verify;
import static com.tcbs.automation.tools.DateTimeUtils.ISO_8601_FORMAT_1;
import static org.hamcrest.Matchers.equalTo;

public class UpdatedOldCustomerMatcher {

  public static Matcher<HashMap<String, Object>> sameAs(TcbsUser tcbsUser, TcbsAddress tcbsAddress, TcbsIdentification tcbsIdentification, List<TcbsBankAccount> tcbsBankAccount) {
    return new TypeSafeMatcher<HashMap<String, Object>>() {
      final Description mismatchDescriber = new StringDescription();

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(HashMap<String, Object> actualResult) {
        String expectedBirthday = "";
        String expectedCreatedDate = "";
        String expectedIdDate = "";

        Date inputBirthday = tcbsUser.getBirthday();
        Date inputExpectedDate = tcbsIdentification.getIdDate();
        expectedBirthday = DateTimeUtils.convertDateToString(inputBirthday, ISO_8601_FORMAT_1);
        expectedIdDate = DateTimeUtils.convertDateToString(inputExpectedDate, ISO_8601_FORMAT_1);

        mismatchDescriber.appendText(String.format("\n====Validating userprofile: <%s>,<%s> ======", tcbsUser.getFirstname(), tcbsUser.getLastname()));
        return verify(actualResult,
          all(
            has("personalInfo.fullName", equalTo(tcbsUser.getLastname() + " " + tcbsUser.getFirstname())))
            .and(
              has("personalInfo.phoneNumber", equalTo(tcbsUser.getPhone())))
            .and(
              has("personalInfo.email", equalTo(tcbsUser.getEmail())))
            .and(
              has("personalInfo.birthday", equalTo(expectedBirthday)))
            .and(
              has("personalInfo.contactAddress", equalTo(tcbsAddress.getAddress())))
            .and(
              has("personalInfo.identityCard.idNumber", equalTo(tcbsIdentification.getIdNumber())))
            .and(
              has("personalInfo.identityCard.idPlace", equalTo(tcbsIdentification.getIdPlace())))
            .and(
              has("personalInfo.identityCard.idDate", equalTo(expectedIdDate)))
            .and(
              has("bankAccounts.0.accountNo", equalTo(tcbsBankAccount.get(0).getBankAccountNo())))
            .and(
              has("bankAccounts.0.accountName", equalTo(tcbsBankAccount.get(0).getBankAccountName())))
            .and(
              has("bankAccounts.0.bankCode", equalTo(tcbsBankAccount.get(0).getBankCode())))
            .and(
              has("bankAccounts.0.bankName", equalTo(tcbsBankAccount.get(0).getBankName())))
            .and(
              has("bankAccounts.0.bankProvince", equalTo(tcbsBankAccount.get(0).getBankprovince())))
            .and(
              has("bankAccounts.0.branchCode", equalTo(tcbsBankAccount.get(0).getBankBranch())))
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
