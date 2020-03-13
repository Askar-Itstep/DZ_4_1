package com.example.dz_4_1;

public class EmailContact {
    public					String		lastName;
    public 					String		firstName;
    public					String		email;

    public EmailContact() {
    }

    public EmailContact(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;

    }
    @Override
    public String toString() {
        return "EmailContact{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
