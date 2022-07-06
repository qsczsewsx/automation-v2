package com.tcbs.automation.docportal;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocPortal {
  public static final HibernateEdition CONNECTION = Database.DOCPORTAL.getConnection();
}
