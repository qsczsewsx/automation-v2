package com.tcbs.automation.portfolio;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class PortfolioSit {
  public static final HibernateEdition porConnection = Database.PORTFOLIO.getConnection();
  public static final HibernateEdition porfolioIsailConnection = Database.PORTFOLIO_ISAIL.getConnection();
  public static final HibernateEdition goodsOrchestratorConnection = Database.GOODS_ORCHESTRATOR.getConnection();
}
