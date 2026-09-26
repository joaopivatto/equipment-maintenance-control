package com.web2.equipmentmaintenancecontrol.domain.security;

public record HashedSaltedPassword(String hash, String salt) {}
