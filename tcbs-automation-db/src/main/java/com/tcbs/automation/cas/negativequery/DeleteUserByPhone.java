package com.tcbs.automation.cas.negativequery;

import com.tcbs.automation.cas.CAS;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tcbs.automation.cas.CAS.createNativeQuery;

@Getter
@Setter
@NoArgsConstructor
@Component
public class DeleteUserByPhone {
  public static void deleteUserByPhone(String phone) throws IOException {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      List<String> sqls = buildQuery();
      for (String sql : sqls) {
        Query query = createNativeQuery(sql);
        query.setParameter("phone", phone);
        query.executeUpdate();
      }
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
    }
  }

  static List<String> buildQuery() {
    return new ArrayList<>(Arrays.asList(
      "delete from TCBS_USER_VIDEO_ASK where USER_ID in (select ID from TCBS_USER where phone = :phone)",
      "delete from TCBS_NEW_ONBOARDING_STATUS where USER_ID in (select ID from TCBS_USER where phone = :phone)",
      "delete from TCBS_CONTRACT_ADDITIONAL_INFO where USER_ID in (select ID from TCBS_USER where phone = :phone)",
      "delete from TCBS_USER where phone = :phone",
      "DELETE FROM TCBS_PERSON_ENTERPRISE tpe WHERE ENTERPRISE_ID IN (SELECT id FROM TCBS_USER_OPENACCOUNT_QUEUE tuoq WHERE PHONE = :phone)",
      "delete from TCBS_USER_OPENACCOUNT_QUEUE where phone = :phone"
    ));
  }
}
