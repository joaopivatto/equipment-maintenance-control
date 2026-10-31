package com.web2.equipmentmaintenancecontrol.model.profile.dtos;

public record AddressResponse(
        String zipCode,
        String street,
        Integer number,
        String complement,
        String neighborhood,
        String city,
        String state) {}