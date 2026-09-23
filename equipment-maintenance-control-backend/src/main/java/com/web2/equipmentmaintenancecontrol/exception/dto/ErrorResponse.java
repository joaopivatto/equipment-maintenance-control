package com.web2.equipmentmaintenancecontrol.exception.dto;

import java.time.Instant;

public record ErrorResponse(
    Instant timestamp, int status, String code, String message, String path) {}
