package com.tcbs.automation.tca.industry;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "stx_mst_IcbIndustry", schema = "dbo")
public class StoxTbIndustry {
  private String id;
  private String name;
  private String parentId;
  private String nameEn;
  private int level;
  @Id
  private long oldId;

  public static StoxTbIndustry getIndustryId(String lang, String id) {
    List<String> list = new ArrayList<>();
    list.add(id);
    StoxTbIndustry industry = new StoxTbIndustry();
    List<StoxTbIndustry> listResult = industry.getIndustryId(lang, list);
    if (CollectionUtils.isNotEmpty(listResult)) {
      return listResult.get(0);
    }
    return null;
  }

  @Step
  public static String getOldIndustry(long oid) {
    Query<StoxTbIndustry> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select a  " +
        " from StoxTbIndustry as a " +
        " where a.oldId = :oid "
      , StoxTbIndustry.class
    );
    query.setParameter("oid", oid);
    List<String> results = query.getResultList().stream().map(s -> s.getId()).collect(Collectors.toList());
    TcAnalysis.tcaDbConnection.closeSession();
    return results.get(0);
  }

  @Step
  public static List<String> getIcbCodeSameLv2(String id) {
    String sql = "SELECT IdLevel2 FROM view_idata_industry vii WHERE IdLevel4  = :id";
    List<Map<String, Object>> queryResult = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
      .setParameter("id", id).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    List<String> results = queryResult.stream().map(s -> s.get("IdLevel2").toString()).collect(Collectors.toList());
    return getIcbCodeByLv2(results.get(0));
  }

  @Step
  public static List<String> getIcbCodeByLv2(String id) {
    String sql = "SELECT IdLevel4 FROM view_idata_industry vii WHERE IdLevel2  = :id";
    List<Map<String, Object>> queryResult = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
      .setParameter("id", id).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return queryResult.stream().map(s -> s.get("IdLevel4").toString()).collect(Collectors.toList());
  }

  @Basic
  @Column(name = "IcbCode")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Basic
  @Column(name = "IcbName")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "ParentIcbCode")
  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  @Basic
  @Column(name = "en_IcbName")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Basic
  @Column(name = "IcbLevel")
  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  @Id
  @Column(name = "IndustryID")
  public long getOldId() {
    return oldId;
  }

  public void setOldId(long level) {
    this.oldId = level;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoxTbIndustry that = (StoxTbIndustry) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(name, that.name) &&
      Objects.equals(parentId, that.parentId) &&
      Objects.equals(nameEn, that.nameEn) &&
      level == that.level;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, parentId, nameEn, level);
  }

  @Step
  public List<StoxTbIndustry> getIndustry(String lang, int level) {
    Query<StoxTbIndustry> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select a " +
        " from StoxTbIndustry as a " +
        " where a.level = :level "
      , StoxTbIndustry.class
    );
    query.setParameter("level", level);
    List<StoxTbIndustry> results = query.getResultList();
    results.forEach(item -> {
      if ("en".equalsIgnoreCase(lang)) {
        item.setName(item.getNameEn());
      }
    });
    TcAnalysis.tcaDbConnection.closeSession();
    return results;
  }

  @Step
  public List<StoxTbIndustry> getIndustryByParentId(String lang, List<String> listParentId) {
    if (CollectionUtils.isEmpty(listParentId)) {
      return new ArrayList<>();
    }
    Query<StoxTbIndustry> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select a from StoxTbIndustry as a " +
        " where a.parentId in :listParentId "
      , StoxTbIndustry.class
    );
    query.setParameter("listParentId", listParentId);
    List<StoxTbIndustry> results = query.getResultList();
    results.forEach(item -> {
      if ("en".equalsIgnoreCase(lang)) {
        item.setName(item.getNameEn());
      }
    });
    TcAnalysis.tcaDbConnection.closeSession();
    return results;
  }

  @Step
  public List<StoxTbIndustry> getIndustryId(String lang, List<String> listId) {
    if (CollectionUtils.isEmpty(listId)) {
      return new ArrayList<>();
    }

    Query<StoxTbIndustry> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select a  from StoxTbIndustry as a " +
        " where a.id in :listId "
      , StoxTbIndustry.class
    );
    query.setParameter("listId", listId);
    List<StoxTbIndustry> results = query.getResultList();
    results.forEach(item -> {
      if ("en".equalsIgnoreCase(lang)) {
        item.setName(item.getNameEn());
      }
    });
    TcAnalysis.tcaDbConnection.closeSession();
    return results;
  }
}
