package com.aplazo.bnpl.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
  private String code;
  private String error;
  private Long timestamp;
  private String message;
  private String path;
}