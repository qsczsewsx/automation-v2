package com.tcbs.automation.tools;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {
  R apply(T t) throws E;

  static <T, R, E extends Throwable> Function<T, R> uncheckedEither(ThrowingFunction<T, R, E> f, R either) {
    return t -> {
      try {
        return f.apply(t);
      } catch (Throwable e) {
        return either;
      }
    };
  }

  static <T, R, E extends Throwable> Function<T, R> unchecked(ThrowingFunction<T, R, E> f) {
    return ThrowingFunction.uncheckedEither(f, null);
  }

  static <T, R, E extends Throwable> Function<T, R> uncheckedThrow(ThrowingFunction<T, R, E> f) {
    return t -> {
      try {
        return f.apply(t);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    };
  }
}
