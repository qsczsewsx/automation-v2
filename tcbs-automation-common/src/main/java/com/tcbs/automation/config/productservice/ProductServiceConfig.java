package com.tcbs.automation.config.productservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ProductServiceConfig {
  public static final String PRODUCT_FILE_PATH = "/src/test/resources/data/workflowengine/approveProdFail/";
  private static final Config conf = new ConfigImpl("productservice").getConf();

  //Product service
  public static final String PRODUCT_DOMAIN = conf.getString("product-service.domain");
  public static final String PRODUCT_DOMAIN_BACK = conf.getString("product-service.domain_back");
  public static final String PRODUCTS = conf.getString("product-service.products");
  public static final String PRODUCT_FINANCIAL_TERM = conf.getString("product-service.financial-terms");
  public static final String PRODUCT_X_API_KEY = conf.getString("product-service.x-api-key");
  public static final String PRODUCT_NO_PAGING = conf.getString("product-service.no-paging");
  public static final String PRODUCT_BONDS = conf.getString("product-service.bonds");
  public static final String PRODUCT_DETAIL = conf.getString("product-service.detail");
  public static final String PRODUCT_EDCM = conf.getString("product-service.domaim_edcm");
  public static final String PRODUCT_BOND = conf.getString("product-service.bond");
  public static final String PRODUCT_ACTIVE = conf.getString("product-service.active");


}
