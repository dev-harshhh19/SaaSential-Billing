package com.saasential.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  private int status;
  private String message;
  private T data;
  private String error;

  public static <T> ApiResponse<T> success(String message, T data) {
      return ApiResponse.<T>builder().status(200).message(message).data(data).build();
  }

  public static <T> ApiResponse<T> success(String message) {
      return ApiResponse.<T>builder().status(200).message(message).build();
  }

  public static <T> ApiResponse<T> error(String message) {
      return ApiResponse.<T>builder().status(400).message(message).build();
  }

  public static <T> ApiResponse<T> error(String message, Object errors) {
      return ApiResponse.<T>builder().status(400).message(message).error(errors.toString()).build();
  }
}
