package main.java.com.web2.equipmentmaintenancecontrol.enums;

public enum ServiceRequestStatus {
    ABERTA("Cinza", "#808080"),
    ORCADA("Marrom", "#8B4513"),
    REJEITADA("Vermelho", "#DC3545"),
    APROVADA("Amarelo", "#FFC107"),
    REDIRECIONADA("Roxo", "#6F42C1"),
    ARRUMADA("Azul", "#007BFF"),
    PAGA("Alaranjado", "#FD7E14"),
    FINALIZADA("Verde", "#28A745");

    private final String colorName;
    private final String colorHex;

    ServiceRequestStatus(String colorName, String colorHex){
        this.colorName = colorName;
        this.colorHex = colorHex;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorHex() {
        return colorHex;
    }
}