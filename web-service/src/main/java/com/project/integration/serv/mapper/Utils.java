package com.project.integration.serv.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
  public static <R, E> List<R> convertList(List<E> list, Function<E, R> converter) {
    return list.stream().map(converter).collect(Collectors.toList());
  }
}
