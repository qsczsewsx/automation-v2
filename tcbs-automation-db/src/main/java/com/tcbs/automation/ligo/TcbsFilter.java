package com.tcbs.automation.ligo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import java.text.ParseException;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tcbs_filter")
public class TcbsFilter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "name")
  private String name;
  @NotNull
  @Column(name = "filters")
  private String filters;
  @Column(name = "created_on")
  private Long createdOn;

  public static TcbsFilter getTcbsFilterById(Long id) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<TcbsFilter> query = session.createQuery("from TcbsFilter w where w.id = :id");
    query.setParameter("id", id);
    return query.getSingleResult();
  }
}
