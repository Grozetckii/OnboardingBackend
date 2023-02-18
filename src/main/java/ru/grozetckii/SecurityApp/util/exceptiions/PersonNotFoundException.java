package ru.grozetckii.SecurityApp.util.exceptiions;

public class PersonNotFoundException extends RuntimeException{
    private String username;

    public PersonNotFoundException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
