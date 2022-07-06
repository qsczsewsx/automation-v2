package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "INDUSTRY")
public class Industry {
  public static Session session = null;
  private static Map<Object, Industry> mapIndustry = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "NAME_EN")
  private String nameEn;

  public static Industry getIndustryById(Object idOrCode) {
    if (mapIndustry.isEmpty()) {
      List<Industry> list = getAllIndustryFromDb();
      for (Industry industry : list) {
        mapIndustry.put(industry.getId(), industry);
        mapIndustry.put(industry.getCode(), industry);
      }
    }

    return mapIndustry.containsKey(idOrCode) ? mapIndustry.get(idOrCode) : new Industry();
  }

  public static List<Industry> getAllIndustryFromDb() {
    Query<Industry> query = session.createQuery("from Industry");
    return query.getResultList();
  }
}
