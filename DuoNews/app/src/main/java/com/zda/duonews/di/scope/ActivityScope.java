package com.zda.duonews.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by MR on 2017/7/27.
 */
@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
