
package com.auca.abstracts;

import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;

public abstract class TaxParty implements Identifiable, Validatable {

    private static int nextId = 1;

    private String partyId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public TaxParty(String firstName, String lastName, String email,
                    String phoneNumber, LocalDate dateOfBirth) {

        this.partyId = "PTY" + String.format("%04d", nextId++);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPartyId() {
        return partyId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getId() {
        return partyId;
    }

    @Override
    public abstract boolean isActive();

    @Override
    public String getSummary() {
        return "Party ID: " + partyId
                + ", Name: " + getDisplayName()
                + ", Email: " + email;
    }

    @Override
    public void printDetails() {
        System.out.println("Party ID: " + partyId);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Date of Birth: " + dateOfBirth);
    }

    @Override
    public void validate() throws ValidationException {

        if (firstName == null || firstName.trim().length() < 2
                || firstName.trim().length() > 60) {
            throw new ValidationException("First name must contain 2-60 characters.");
        }

        if (lastName == null || lastName.trim().length() < 2
                || lastName.trim().length() > 60) {
            throw new ValidationException("Last name must contain 2-60 characters.");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Invalid email address.");
        }

        if (phoneNumber == null
                || !phoneNumber.matches("\\+?[0-9]{10,15}")) {
            throw new ValidationException("Invalid phone number.");
        }

        if (dateOfBirth == null || !dateOfBirth.isBefore(LocalDate.now())) {
            throw new ValidationException("Date of birth must be in the past.");
        }
    }
}
