
package com.auca.individualtaxpayer;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.exceptions.ValidationException;

public class IndividualTaxpayer extends RegisteredTaxpayer {

    private String nationalId;
    private String occupation;
    private String businessName;
    private LocalDate businessStartDate;
    private BigDecimal annualTurnover;
    private Boolean resident;

    public IndividualTaxpayer(
            BigDecimal annualTurnover,
            String businessName,
            LocalDate businessStartDate,
            String nationalId,
            String occupation,
            Boolean resident,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String tin,
            LocalDate registrationDate,
            String taxpayerStatus,
            String district,
            String sector,
            String taxOffice) {

        super(firstName, lastName, email, phoneNumber, dateOfBirth,
                tin, registrationDate, taxpayerStatus, district, sector, taxOffice);

        this.annualTurnover = annualTurnover;
        this.businessName = businessName;
        this.businessStartDate = businessStartDate;
        this.nationalId = nationalId;
        this.occupation = occupation;
        this.resident = resident;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public LocalDate getBusinessStartDate() {
        return businessStartDate;
    }

    public void setBusinessStartDate(LocalDate businessStartDate) {
        this.businessStartDate = businessStartDate;
    }

    public BigDecimal getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(BigDecimal annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public Boolean getResident() {
        return resident;
    }

    public void setResident(Boolean resident) {
        this.resident = resident;
    }

    @Override
    public String getSummary() {
        return super.getSummary()
                + ", National ID: " + nationalId
                + ", Occupation: " + occupation;
    }

    @Override
    public void printDetails() {
        super.printDetails();

        System.out.println("National ID: " + nationalId);
        System.out.println("Occupation: " + occupation);
        System.out.println("Business Name: " + businessName);
        System.out.println("Business Start Date: " + businessStartDate);
        System.out.println("Annual Turnover: " + annualTurnover);
        System.out.println("Resident: " + resident);
    }

    @Override
    public void validate() throws ValidationException {

        super.validate();

        if (nationalId == null || !nationalId.matches("[0-9]{16}")) {
            throw new ValidationException(
                    "National ID must contain exactly 16 digits.");
        }

        if (occupation == null || occupation.trim().length() < 2
                || occupation.trim().length() > 60) {
            throw new ValidationException(
                    "Occupation must contain 2-60 characters.");
        }

        if (businessName == null || businessName.trim().isEmpty()) {
            throw new ValidationException("Business name is required.");
        }

        if (businessStartDate == null
                || !businessStartDate.isBefore(getRegistrationDate())) {
            throw new ValidationException(
                    "Business start date must be before registration date.");
        }

        if (annualTurnover == null || annualTurnover.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(
                    "Annual turnover cannot be negative or empty.");
        }

        if (annualTurnover.scale() > 2
                || annualTurnover.compareTo(
                        new BigDecimal("999999999999.99")) > 0) {
            throw new ValidationException(
                    "Annual turnover must have at most 2 decimal places and cannot exceed 999999999999.99.");
        }

        if (resident == null) {
            throw new ValidationException(
                    "Please specify whether the taxpayer is resident.");
        }
    }
}
