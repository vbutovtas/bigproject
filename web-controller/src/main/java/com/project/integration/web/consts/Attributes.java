package com.project.integration.web.consts;

import static com.project.integration.web.annotation.CheckFileSize.MAX_FILE_SIZE;

import java.util.Arrays;
import java.util.List;

public interface Attributes {

  String TOKEN = "token";
  String ROLE  = "role";
  String STATUS  = "status";
  String ID  = "id";
  String CLAIMS  = "claims";
  String AUTHORIZATION  = "Authorization";
  String TOKEN_BEGINNING_IN_HEADER  = "Bearer ";
  List<String> MEDIA_TYPES = Arrays.asList(
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
          "application/msword");

  String MAX_FILE_SIZE_ERROR_MSG = "Max file size is " + MAX_FILE_SIZE * 0.001 + " KB";
}
