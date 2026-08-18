package com.web2.equipmentmaintenancecontrol.model;

import java.time.LocalDate;

public class User {

    private Integer id;
    private String cpf;
    private String name;
    private String email;
    // TODO: To define password storage method, this is just a placeholder for now :)
    private String password;
    private LocalDate birthDate;

    private Address address;
    private Phone phone;
    private Profile profile;

    public User(
            Integer id,
            String cpf,
            String name,
            String password,
            String email,
            LocalDate birthDate,
            Address address,
            Phone phone,
            Profile profile
    ) {
        this.id = id;
        this.cpf = cpf;
        this.name = name;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.profile = profile;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public Phone getPhone() { return phone; }
    public void setPhone(Phone phone) { this.phone = phone; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }
}