package com.zda.daggertest.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by MR on 2017/7/11.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Wang {
}
