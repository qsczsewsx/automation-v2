package com.tcbs.automation.tcinvest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "INV_HOLIDAY")
public class InvHoliday {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;
  @Column(name = "TIMESTAMP")
  private Date timestamp;
  @Column(name = "ACTIVE")
  private int active;
  @Column(name = "NAME")
  private String name;

  public InvHoliday(Date timestamp, int active, String name) {
    this.timestamp = timestamp;
    this.active = active;
    this.name = name;
  }

  @SuppressWarnings("unchecked")
  public List<InvHoliday> listHoliday() {
    if (session == null) {
      session = TcInvest.tcInvestDbConnection.getSession();
    }

    Query<InvHoliday> query = session.createQuery("from InvHoliday order by timestamp asc");
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  public void deleteHoliday(int id) {
    Query<InvHoliday> query = session.createQuery("DELETE InvHoliday WHERE id =: id");
    query.setParameter("id", id);
    query.executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public boolean checkDateIsHoliday(String datetime) {
    if (datetime != null) {
      SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
      Date date = null;
      try {
        date = format1.parse(datetime);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      String timestamp = format1.format(date) + " 00:00:00.000";
      Query<InvHoliday> query;
      if (session == null) {
        query = TcInvest.tcInvestDbConnection.getSession().createQuery("from InvHoliday where timestamp=:timestamp ");
        query.setParameter("timestamp", timestamp);
      } else {
        query = session.createQuery("from InvHoliday where timestamp=:timestamp ");
        query.setParameter("timestamp", timestamp);
      }

      List<InvHoliday> result = query.getResultList();
      if (result.size() > 0) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public String getMatchedDateByWorkingDate(String fromDate, int numberWorkingDate) throws ParseException {
    int numberDate = 0;
    int flagAdd = 1;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = format1.parse(fromDate);
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    if (numberWorkingDate < 0) {
      flagAdd = -1;
    }
    while (numberDate < numberWorkingDate * flagAdd) {
      cal.add(Calendar.DATE, flagAdd);
      if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || checkDateIsHoliday(format1.format(cal.getTime()))) {
        continue;
      } else {
        numberDate++;
      }
    }

    String result = format1.format(cal.getTime()) + " 00:00:00.000";
    return result;
  }

  public int insert() {
    return Integer.valueOf(session.save(this).toString());
  }
}
