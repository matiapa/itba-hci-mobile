package com.pi.gymapp.api.models;

public class VerifyEmailData {
    private String email;

    private String code;

    public VerifyEmailData(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
}
