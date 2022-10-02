package com.project.integration.web.consts;

public interface PropertiesKeys {
  String JWT_SECRET = "${jwt.secret}";
  String JWT_EXPIRATION_DATE_IN_SEC = "${jwt.expirationDateInSec}";
  String JWT_ABLE_FOR_REFRESH_TIME_IN_SEC = "${jwt.ableForRefreshTimeInSec}";
}