package com.example.resumescreening;

public class HelperClass {
    private String name;
    private String email;
    private String username;
    private String password;
    private String religion;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }



    public HelperClass(String name, String email, String username, String password, String religion) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.religion = religion;
    }

    // Optional: Additional constructors or methods as needed

    public HelperClass() {
        // Default constructor required for Firebase
    }
}

