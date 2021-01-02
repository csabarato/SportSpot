package com.sportspot.sportspot.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class UserDataModel {

    private String _id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {

        if (username != null && !username.isEmpty()) {
            return username;
        } else if (
            firstName != null && !firstName.isEmpty() &&
            lastName != null && !lastName.isEmpty()) {
                return firstName +" "+ lastName;
        } else {
            return email;
        }
    }

    public String toJSON() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
