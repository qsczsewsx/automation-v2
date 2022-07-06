package com.tcbs.automation.questions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcbs.automation.models.ProfileDto;
import com.tcbs.automation.models.page.PageResponseDto;
import com.tcbs.automation.tools.ConvertUtils;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Question;

public class ApiImprovement {

  private static final String FILTER_PATTERN = "(.+?)(\\?=)(.+?)#";

  public static Question<ProfileDto> fromSearchAPI(String path) {
    return Question.about("ProfileDto from API").answeredBy(actor -> {
      Response response = actor.recall(path);
      ProfileDto profileDto = response.as(ProfileDto.class);
      return profileDto;
    });
  }

  public static Question<List<ProfileDto>> fromSearchAPIAsList(String path) {
    return Question.about("List ProfileDto from API").answeredBy(actor -> {
      Response response = actor.recall(path);
      String json = response.getBody().asString();
      List<ProfileDto> dtos = null;
      try {
        dtos = ConvertUtils.convertJsonToArray(json, ProfileDto.class);
      } catch (Exception e) {
        // Ignore
      }
      return null == dtos
        ? ConvertUtils.convertJsonToObject(json, PageResponseDto.class).getContent()
        : dtos;
    });
  }

  public static Question<ProfileDto> fromSearchAPIAsOne(String path) {
    return Question.about("List ProfileDto from API").answeredBy(actor -> {
      Response response = actor.recall(path);
      String json = response.getBody().asString();
      return ConvertUtils.convertJsonToObject(json, ProfileDto.class);
    });
  }

  public static Question<List<ProfileDto>> fromSearchAPIAsListFromPage(String path) {
    return Question.about("List ProfileDto from API(Pageable)").answeredBy(actor -> {
      Response response = actor.recall(path);
      String json = response.getBody().asString();
      return ConvertUtils.convertJsonToObject(json, PageResponseDto.class).getContent();
    });
  }

  public static Map<String, String> getQueryParams(String params) {
    final Map<String, String> parameters = new HashMap<String, String>();
    final Pattern pattern = Pattern.compile(FILTER_PATTERN);
    final Matcher matcher = pattern.matcher(params + "#");
    while (matcher.find()) {
      parameters.put(matcher.group(1), (String) matcher.group(3));
    }
    return parameters;
  }

}