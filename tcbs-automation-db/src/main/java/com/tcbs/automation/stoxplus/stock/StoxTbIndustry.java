package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.stoxplus.Stoxplus;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stox_tb_Industry", schema = "dbo")
public class StoxTbIndustry {
  @Id
  private short id;
  private String name;
  private String code;
  private Integer parentId;
  private Boolean deleted;
  private String des;
  private Integer level;
  private String text;
  private String sName;
  private String desEn;
  private String nameEn;
  private String sNameEn;
  private String desJp;
  private String nameJp;
  private String sNameJp;

  public static StoxTbIndustry getIndustryId(String lang, Short id) {
    List<Short> list = new ArrayList<>();
    list.add(id);
    StoxTbIndustry industry = new StoxTbIndustry();
    List<StoxTbIndustry> listResult = industry.getIndustryId(lang, list);
    if (CollectionUtils.isNotEmpty(listResult)) {
      return listResult.get(0);
    }
    return null;
  }

  @Id
  @Column(name = "ID")
  public short getId() {
    return id;
  }

  public void setId(short id) {
    this.id = id;
  }

  @Basic
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "Code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "ParentID")
  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  @Basic
  @Column(name = "Deleted")
  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  @Basic
  @Column(name = "Des")
  public String getDes() {
    return des;
  }

  public void setDes(String des) {
    this.des = des;
  }

  @Basic
  @Column(name = "Level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  @Basic
  @Column(name = "Text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Basic
  @Column(name = "SName")
  public String getsName() {
    return sName;
  }

  public void setsName(String sName) {
    this.sName = sName;
  }

  @Basic
  @Column(name = "Des_En")
  public String getDesEn() {
    return desEn;
  }

  public void setDesEn(String desEn) {
    this.desEn = desEn;
  }

  @Basic
  @Column(name = "Name_En")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Basic
  @Column(name = "sName_En")
  public String getsNameEn() {
    return sNameEn;
  }

  public void setsNameEn(String sNameEn) {
    this.sNameEn = sNameEn;
  }

  @Basic
  @Column(name = "Des_JP")
  public String getDesJp() {
    return desJp;
  }

  public void setDesJp(String desJp) {
    this.desJp = desJp;
  }

  @Basic
  @Column(name = "Name_JP")
  public String getNameJp() {
    return nameJp;
  }

  public void setNameJp(String nameJp) {
    this.nameJp = nameJp;
  }

  @Basic
  @Column(name = "sName_JP")
  public String getsNameJp() {
    return sNameJp;
  }

  public void setsNameJp(String sNameJp) {
    this.sNameJp = sNameJp;
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
    return id == that.id &&
      Objects.equals(name, that.name) &&
      Objects.equals(code, that.code) &&
      Objects.equals(parentId, that.parentId) &&
      Objects.equals(deleted, that.deleted) &&
      Objects.equals(des, that.des) &&
      Objects.equals(level, that.level) &&
      Objects.equals(text, that.text) &&
      Objects.equals(sName, that.sName) &&
      Objects.equals(desEn, that.desEn) &&
      Objects.equals(nameEn, that.nameEn) &&
      Objects.equals(sNameEn, that.sNameEn) &&
      Objects.equals(desJp, that.desJp) &&
      Objects.equals(nameJp, that.nameJp) &&
      Objects.equals(sNameJp, that.sNameJp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, parentId, deleted, des, level, text, sName, desEn, nameEn, sNameEn, desJp, nameJp, sNameJp);
  }

  @Step
  public List<StoxTbIndustry> getIndustry(String lang, int level) {
    Query<StoxTbIndustry> query = Stoxplus.stoxDbConnection.getSession().createQuery(
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
    Stoxplus.stoxDbConnection.closeSession();
    return results;
  }

  @Step
  public List<StoxTbIndustry> getIndustryByParentId(String lang, List<Integer> listParentId) {
    if (CollectionUtils.isEmpty(listParentId)) {
      return new ArrayList<>();
    }
    Query<StoxTbIndustry> query = Stoxplus.stoxDbConnection.getSession().createQuery(
      "select a " +
        " from StoxTbIndustry as a " +
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
    Stoxplus.stoxDbConnection.closeSession();
    return results;
  }

  @Step
  public List<StoxTbIndustry> getIndustryId(String lang, List<Short> listId) {
    if (CollectionUtils.isEmpty(listId)) {
      return new ArrayList<>();
    }

    Query<StoxTbIndustry> query = Stoxplus.stoxDbConnection.getSession().createQuery(
      "select a " +
        " from StoxTbIndustry as a " +
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
    Stoxplus.stoxDbConnection.closeSession();
    return results;
  }

}
