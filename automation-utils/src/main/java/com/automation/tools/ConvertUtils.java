package com.automation.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.automation.cas.xxxxUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ConvertUtils {
  private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

  public static String stringToNull(String value) {
    return ("null".equals(value) ? null : value);
  }

  public static String hashMap2Json(HashMap<String, Object> hashMap) {
    Gson gson = new Gson();
    return gson.toJson(hashMap);
  }

  public static String hashMapList2Json(List<HashMap<String, Object>> hashMapList) {
    Gson gson = new Gson();
    return gson.toJson(hashMapList);
  }


  public static Map<String, Object> object2Map(Object o, Class<?> clazz) throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    for (Field f : clazz.getDeclaredFields()) {
      if (f.get(o) != null) {
        map.put(f.getName(), f.get(o).toString());
      }
    }
    return map;
  }

  public static String fileTxtToString(String filePath) {
    String text = "";
    try {
      text = new String(Files.readAllBytes(Paths.get(filePath)));
    } catch (IOException e) {
      logger.error(e.getMessage(), e.getStackTrace());
    }

    return text;
  }

  public static String fileTxtToString(String filePath, String charsetName) {
    String text = "";
    try {
      text = new String(Files.readAllBytes(Paths.get(filePath)), charsetName);
    } catch (IOException e) {
      logger.error(e.getMessage(), e.getStackTrace());
    }

    return text;
  }

  public static List<String> readFileByBreakLine(String filePath) {
    List<String> list = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + filePath))) {
      String line = br.readLine();
      while (line != null) {
        list.add(line);
        line = br.readLine();
      }
      return list;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * @param obj
   * @param fieldName
   * @return return value of key in Object
   */
  public static Object getValueByFieldName(Object obj, String fieldName) {
    Object output = null;
    List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields());
    for (Field f : fields) {
      f.setAccessible(true);
      if (f.getName().equals(fieldName)) {
        try {
          if (f.get(obj) != null) {
            output = f.get(obj);
          }
        } catch (IllegalAccessException e) {
          logger.error(e.getMessage(), e.getStackTrace());
        }
        break;
      }
    }
    return output;
  }

  public static String epochTimetoStringIso8601(String epochTime) {
    ZonedDateTime zdt;
    String humanReadableTime;
    try {
      Long converter = Long.parseLong(epochTime);
      zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(converter), ZoneId.of("Asia/Ho_Chi_Minh"));
      humanReadableTime = zdt.truncatedTo(ChronoUnit.NANOS).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      return humanReadableTime;
    } catch (NumberFormatException e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return null;
    }
  }

  public static String epochTimetoString(String epochTime) {
    ZonedDateTime zdt;
    String humanReadableTime;
    try {
      Long converter = Long.parseLong(epochTime);
      zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(converter), ZoneId.of("Asia/Ho_Chi_Minh"));
      humanReadableTime = zdt.truncatedTo(ChronoUnit.NANOS).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
      return humanReadableTime;
    } catch (NumberFormatException e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return null;
    }
  }

  public static String epochTimetoStringIso(String epochTime, String format) {
    ZonedDateTime zdt;
    String humanReadableTime;
    try {
      Long converter = Long.parseLong(epochTime);
      zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(converter), ZoneId.of("Asia/Ho_Chi_Minh"));
      humanReadableTime = zdt.truncatedTo(ChronoUnit.NANOS).format(DateTimeFormatter.ofPattern(format));
      return humanReadableTime;
    } catch (NumberFormatException e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static HashMap<String, String> hashMapObjTohashMapStr(HashMap<String, Object> input) {
    HashMap<String, String> output = new HashMap<>();
    //System.out.println("INPUT: " + input);
    for (String key : input.keySet()) {
      Object value = input.get(key);
      //System.out.println(
      //    "=> key: " + key + ", value type: " + value.getClass());
      //System.out.println("==>  value: " + value.toString());
      if (value instanceof HashMap) {
        //System.out.println("==> Found HashMap type for: " + key);
        hashMapObjTohashMapStr(key, (HashMap<String, Object>) value, output);
      } else {
        if (value != null) {
          output.put(key, value.toString());
        }
      }
    }
    return output;
  }

  @SuppressWarnings("unchecked")
  public static void hashMapObjTohashMapStr(String key, HashMap<String, Object> input,
                                            HashMap<String, String> output) {
    for (String subKey : input.keySet()) {
      Object value = input.get(subKey);
      String outKey = key + "." + subKey;
      // System.out.println("++++> outKey: " + outKey + ", value type: " + value.getClass()+ ", value: "+
      // value.toString());
      if (value instanceof HashMap) {
        // System.out.println("=> Found HashMap type for: " + outKey);
        hashMapObjTohashMapStr(outKey, (HashMap<String, Object>) value, output);
      } else {
        if (value != null) {
          output.put(outKey, value.toString());
        }
      }
    }
  }

  public static HashMap<String, Object> jsonToHashMap(JSONObject json) throws JSONException {
    HashMap<String, Object> retHashMap = new HashMap<>();

    if (json != JSONObject.NULL) {
      retHashMap = toHashMap(json);
    }
    return retHashMap;
  }

  public static HashMap<String, Object> toHashMap(JSONObject object) throws JSONException {
    HashMap<String, Object> hashMap = new HashMap<>();

    Iterator<String> keysItr = object.keys();
    while (keysItr.hasNext()) {
      String key = keysItr.next();
      Object value = object.get(key);

      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toHashMap((JSONObject) value);
      }
      hashMap.put(key, value);
    }
    return hashMap;
  }

  public static List<Object> toList(JSONArray array) throws JSONException {
    List<Object> list = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      Object value = array.get(i);
      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toHashMap((JSONObject) value);
      }
      list.add(value);
    }
    return list;
  }

  /**
   * Hàm dùng để chuyển format Date của môt List String
   */
  public List<Date> convertListStringToListDate(List<String> listDate, String formatDateInput) {
    List<Date> list = new ArrayList<>();
    for (int i = 0; i < listDate.size(); i++) {
      try {
        list.add(new SimpleDateFormat(formatDateInput).parse(listDate.get(i)));
      } catch (ParseException e) {
        logger.error(e.getMessage(), e.getStackTrace());
      }
    }
    return list;
  }

  public static String convertxxxxIdTo105C(String xxxxid) {
    xxxxUser xxxxUser = new xxxxUser();
    return xxxxUser.getByxxxxId(xxxxid).getUsername();
  }

  public static <T> T convert(Object source, Class<T> dstClass) {
    if (source == null) {
      return null;
    }
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setAmbiguityIgnored(true);
    return modelMapper.map(source, dstClass);
  }

  public static <T> List<T> convertArray(List<?> objects, Class<T> dstClass) {
    List<T> list = new ArrayList<T>();
    if (objects != null && objects.size() > 0) {
      for (Object object : objects) {
        list.add(convert(object, dstClass));
      }
    }
    return list;
  }


  public static <T> T convertJsonToObject(String jsonStr, Class<T> dstClass) {
    Gson gson = new GsonBuilder().setLenient().create();
    T object = gson.fromJson(jsonStr, dstClass);
    return object;
  }

  public static <T> List<T> convertJsonToArray(String jsonStr, Class<T> dstClass) {
    Gson gson = new GsonBuilder().setLenient().create();
    Type typeOfT = TypeToken.getParameterized(List.class, dstClass).getType();
    return gson.fromJson(jsonStr, typeOfT);
  }

  public static <T> List<T> convertJsonToArrayV2(String jsonStr, Class<T> dstClass) {
    Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd").create();
    Type typeOfT = TypeToken.getParameterized(List.class, dstClass).getType();
    return gson.fromJson(jsonStr, typeOfT);
  }

  public static String convert105CToxxxxId(String username) {
    xxxxUser xxxxUser = new xxxxUser();
    return xxxxUser.getByUserName(username).getxxxxid();
  }

  public static <T> T jsonToObject(String json, Class<?> className) {
    T object = null;
    try {
      object = (T) new ObjectMapper().readValue(json, className);
    } catch (Exception e) {
      logger.error(String.format("Can not parse %s to %s", json, className));
      logger.error(e.getMessage());
    }
    return object;
  }

  public static String parseObjectToJson(Object object, Class<?> className) {
    Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
    String json = gson.toJson(object, className);
    return json;
  }

  public static String epochTimetoStringIso8601Second(String epochTime) {
    ZonedDateTime zdt;
    String humanReadableTime;
    try {
      Long converter = Long.parseLong(epochTime);
      zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(converter), ZoneId.of("Asia/Ho_Chi_Minh"));
      humanReadableTime = zdt.truncatedTo(ChronoUnit.NANOS).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      return humanReadableTime;
    } catch (NumberFormatException e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return null;
    }
  }

  public static String longToTimeFormat(long timestamp) {
    SimpleDateFormat localDateTimeIsoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    localDateTimeIsoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return localDateTimeIsoFormat.format(timestamp);
  }

  public static String timeStampToTimeFormat(Timestamp timestamp) {
    SimpleDateFormat localDateTimeIsoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    localDateTimeIsoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return localDateTimeIsoFormat.format(timestamp);
  }

  public static ArrayList<Integer> parseStringArrayToInt(List<String> inputArr) {
    ArrayList<Integer> parsedList = new ArrayList<>();
    for (int i = 0; i < inputArr.size(); i++) {
      parsedList.add(Integer.parseInt(inputArr.get(i)));
    }
    return parsedList;
  }

}
