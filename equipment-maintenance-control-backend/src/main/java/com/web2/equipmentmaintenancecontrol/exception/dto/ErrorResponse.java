package com.web2.equipmentmaintenancecontrol.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    Instant timestamp, int status, String code, String message, String path) {}
