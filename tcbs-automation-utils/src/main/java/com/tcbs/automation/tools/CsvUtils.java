package com.tcbs.automation.tools;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class CsvUtils {
  private static final String urlProject = System.getProperty("user.dir");

  public static List<HashMap<String, Object>> readFileCsv(String pathFile, String separate) {
    File fileName = new File(pathFile);
    List<HashMap<String, Object>> data = new ArrayList<>();
    String line;
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
      new FileInputStream(fileName), StandardCharsets.UTF_8))) {
      String[] header = br.readLine().split(Pattern.quote(separate));
      while ((line = br.readLine()) != null) {
        HashMap<String, Object> row = new HashMap<>();
        String[] value = line.split(Pattern.quote(separate), -1);
        for (int i = 0; i < header.length; i++) {
          if (value[i].trim().equals("null")) {
            row.put(header[i].trim(), null);
          } else {
            row.put(header[i].trim(), value[i].trim());
          }
        }
        data.add(row);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return data;
  }

  public static void writeFileCsv(String pathFile, List<String[]> data) {
    File file = new File(urlProject + pathFile);
    try {
      FileWriter outPut = new FileWriter(file);
      CSVWriter writer = new CSVWriter(outPut, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
      writer.writeAll(data);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
