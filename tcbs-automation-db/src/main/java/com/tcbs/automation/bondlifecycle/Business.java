package com.tcbs.automation.bondlifecycle;

import com.tcbs.automation.bondlifecycle.dto.BusinessDto;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BUSINESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business extends ApprovalEntity {
  @Id
  @Column(name = "ID", updatable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @OneToOne
  @JoinColumn(name = "BOND_TYPE_CODE")
  private ReferenceData bondTypeCodeRef;

  @Step
  public static List<Business> getBusinessByIds(List<Integer> ids) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Business> query = session.createNativeQuery("select * from BUSINESS where  id in :businessIds",
      Business.class);
    query.setParameter("businessIds", ids);
    List<Business> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return new ArrayList<>();
    }
    return results;
  }

  @Step
  public static Business getBusinessById(Integer id) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Business> query = session.createNativeQuery("select * from BUSINESS where  id =:businessId",
      Business.class);
    query.setParameter("businessId", id);
    List<Business> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static void deleteBusinessByName(String name) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BUSINESS a where a.name =:name");
    query.setParameter("name", name);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(Business object) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(object);
    trans.commit();

  }

  @Step
  public static void deleteBusinessById(Integer id) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BUSINESS a where a.id =:businessId");
    query.setParameter("businessId", id);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteBusinessByIds(List<Integer> businessIds) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BUSINESS a where a.id in :businessIds");
    query.setParameter("businessIds", businessIds);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static List<BusinessDto> findAllByKey(String findKey) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Business> query = session.createNativeQuery(
      "SELECT c.* FROM BUSINESS c LEFT JOIN REFERENCE_DATA ref on c.APPROVAL_STATUS  = ref.CODE " +
        "on LEFT JOIN REFERENCE_DATA ref1 on c.BOND_TYPE_CODE  = ref1.CODE " +
        "WHERE c.STATUS <> 'INACTIVE' and " +
        "(UPPER(c.NAME_NO_ACCENT) like '%' || :findKey || '%' or UPPER(ref.NAME_NO_ACCENT) like '%' || :findKey || '%' or UPPER(ref1.NAME_NO_ACCENT) like '%' || :findKey || '%')",
      Business.class);
    query.setParameter("findKey", findKey);
    List<Business> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return new ArrayList<>();
    }
    List<BusinessDto> listDto = new ArrayList<>();
    for (Business business : list) {
      listDto.add((BusinessDto) ConvertUtils.convert(business, BusinessDto.class));
    }
    return listDto;
  }


  @Step
  public static List<Business> findAllActive() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Business> query = session.createNativeQuery("SELECT * FROM BUSINESS WHERE STATUS = 'ACTIVE'", Business.class);
    return query.getResultList();
  }
}
