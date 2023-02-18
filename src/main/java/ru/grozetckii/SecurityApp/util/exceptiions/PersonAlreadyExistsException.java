package ru.grozetckii.SecurityApp.util.exceptiions;

public class PersonAlreadyExistsException extends RuntimeException{
    private String username;

    public PersonAlreadyExistsException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
