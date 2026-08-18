package main.java.com.web2.equipmentmaintenancecontrol.model;

public class Phone {

    private Integer id;
    private String number;

    public Phone() {

    }

    public Phone(Integer id, String number) {
        this.id = id;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
