package com.user_service.Dtos;

public class RegisterResponse {

    private Long id;
    private String email;
    private String cvu;
    private String alias;

    public RegisterResponse() {}

    public RegisterResponse(Long id, String email, String cvu, String alias) {
        this.id = id;
        this.email = email;
        this.cvu = cvu;
        this.alias = alias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCvu() {
        return cvu;
    }

    public void setCvu(String cvu) {
        this.cvu = cvu;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
