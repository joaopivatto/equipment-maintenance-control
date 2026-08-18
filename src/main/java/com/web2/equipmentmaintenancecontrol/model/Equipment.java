package com.web2.equipmentmaintenancecontrol.model;

public class Equipment {
    private Integer id;
    private String description;
    private EquipmentType type;


    public Equipment(Integer id, String description, EquipmentType type) {
        this.id = id;
        this.description = description;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

}