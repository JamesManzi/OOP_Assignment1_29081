
package com.auca.abstracts;

import java.time.LocalDate;

import com.auca.exceptions.ValidationException;

public abstract class RegisteredTaxpayer extends TaxParty {

    private String tin;
    private LocalDate registrationDate;
    private String taxpayerStatus;
    private String district;
    private String sector;
    private String taxOffice;

    public RegisteredTaxpayer(
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

        super(firstName, lastName, email, phoneNumber, dateOfBirth);

        this.tin = tin;
        this.registrationDate = registrationDate;
        this.taxpayerStatus = taxpayerStatus;
        this.district = district;
        this.sector = sector;
        this.taxOffice = taxOffice;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getTaxpayerStatus() {
        return taxpayerStatus;
    }

    public void setTaxpayerStatus(String taxpayerStatus) {
        this.taxpayerStatus = taxpayerStatus;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    @Override
    public boolean isActive() {
        return taxpayerStatus != null
                && taxpayerStatus.equalsIgnoreCase("ACTIVE");
    }

    @Override
    public String getSummary() {
        return super.getSummary()
                + ", TIN: " + tin
                + ", Status: " + taxpayerStatus;
    }

    @Override
    public void printDetails() {
        super.printDetails();

        System.out.println("TIN: " + tin);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Taxpayer Status: " + taxpayerStatus);
        System.out.println("District: " + district);
        System.out.println("Sector: " + sector);
        System.out.println("Tax Office: " + taxOffice);
    }

    @Override
    public void validate() throws ValidationException {

        super.validate();

        if (tin == null || !tin.matches("[0-9]{9}")) {
            throw new ValidationException("TIN must contain exactly 9 digits.");
        }

        if (registrationDate == null
                || registrationDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid registration date.");
        }

        if (taxpayerStatus == null
                || !(taxpayerStatus.equalsIgnoreCase("ACTIVE")
                || taxpayerStatus.equalsIgnoreCase("INACTIVE"))) {
            throw new ValidationException("Status must be ACTIVE or INACTIVE.");
        }

        if (district == null || district.trim().isEmpty()) {
            throw new ValidationException("District is required.");
        }

        if (sector == null || sector.trim().isEmpty()) {
            throw new ValidationException("Sector is required.");
        }

        if (taxOffice == null || taxOffice.trim().isEmpty()) {
            throw new ValidationException("Tax office is required.");
        }

        if (!getDateOfBirth().isBefore(registrationDate)) {
            throw new ValidationException(
                    "Date of birth must be before registration date.");
        }
    }
}
