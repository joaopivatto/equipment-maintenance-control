package com.web2.equipmentmaintenancecontrol.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
  EQUIPMENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Equipment type not found"),
  EQUIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Equipment not found"),
  CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "Customer not found"),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
  INVALID_CPF(HttpStatus.BAD_REQUEST, "Invalid CPF"),
  CUSTOMER_EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Customer email already exists"),
  CUSTOMER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Customer already exists"),
  EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "Employee not found"),
  EMPLOYEE_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Employee email already exists"),
  EMPLOYEE_CANNOT_DEACTIVATE_SELF(HttpStatus.CONFLICT, "Employee cannot deactivate themself"),
  EMPLOYEE_ALREADY_INACTIVE(HttpStatus.CONFLICT, "Employee is already inactive"),
  EMPLOYEE_LAST_ACTIVE(HttpStatus.CONFLICT, "The last active employee cannot be deactivated"),
  VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error"),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal error"),
  MAINTENANCE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Maintenance request not found"),
  INVALID_MAINTENANCE_REQUEST_STATUS(
      HttpStatus.CONFLICT, "Maintenance request is not in a valid status for this action"),
  EMAIL_PROVIDER_INTEGRATION_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR, "Error while integrating with email provider (Resend)");

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
