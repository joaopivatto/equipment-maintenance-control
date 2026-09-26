package com.web2.equipmentmaintenancecontrol.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  EQUIPMENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Equipment type not found"),
  EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Equipment not found"),
  CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "Customer not found"),
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error"),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal error"),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
  MAINTENANCE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Maintenance request not found");

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
