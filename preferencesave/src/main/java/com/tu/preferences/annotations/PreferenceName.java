package com.tu.preferences.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @auther Bean zongbin.tu@msxf.com
 * @date 16/5/11
 */
@Retention(RetentionPolicy.CLASS) @Target({ ElementType.FIELD, ElementType.METHOD })
public @interface PreferenceName {
  /**
   * @return the desired name of the field when it is serialized or deserialized
   */
  String value();

  /**
   * @return the alternative names of the field when it is deserialized
   */
  String[] alternate() default {};
}
