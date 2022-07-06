package com.tcbs.automation.coco.mongodb;

import com.tcbs.automation.coco.mongodb.model.SortItem;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {
  public static List<SortItem> toSortList(String sortReq, String[] supportedSortField) {
    return sortReq != null ? Arrays.stream(sortReq.split(",")).filter(StringUtils::isNoneBlank)
      .map(req -> req.split(":"))
      .filter(s -> ArrayUtils.contains(supportedSortField, s[0]))
      .map(s -> {
        if (s.length != 2) {
          return null;
        }
        return SortItem.builder()
          .key(s[0])
          .isAsc(s[1].equalsIgnoreCase("asc"))
          .build();
      })
      .collect(Collectors.toList()) : new ArrayList<>();
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
