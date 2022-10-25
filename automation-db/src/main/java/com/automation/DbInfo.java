package com.automation;


public class DbInfo {
  private String username;
  private String password;
  private String url;
  private String configFile;

  public DbInfo(String username, String password, String url, String configFile) {
    this.username = username;
    this.password = password;
    this.url = url;
    this.configFile = configFile;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getUrl() {
    return url;
  }

  public String getConfigFile() {
    return configFile;
  }

  public HibernateEdition getDbConnection() {
    return new HibernateEdition(
      this.configFile,
      this.username,
      this.password,
      this.url);
  }
}
