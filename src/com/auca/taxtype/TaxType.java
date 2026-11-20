
package com.auca.taxtype;

import java.math.BigDecimal;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;

public class TaxType implements Identifiable, Validatable {

    private String taxTypeId;
    private String taxCode;
    private String taxName;
    private BigDecimal rate;
    private String filingFrequency;
    private Boolean active;

    // GETTERS AND SETTERS

    public String getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(String taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getFilingFrequency() {
        return filingFrequency;
    }

    public void setFilingFrequency(String filingFrequency) {
        this.filingFrequency = filingFrequency;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // IDENTIFIABLE METHODS

    @Override
    public String getDisplayName() {
        return taxName;
    }

    @Override
    public String getId() {
        return taxTypeId;
    }

    @Override
    public boolean isActive() {
        return active != null && active;
    }

    // SUMMARY

    @Override
    public String getSummary() {

        return "Tax Type ID: " + taxTypeId
                + ", Tax Code: " + taxCode
                + ", Tax Name: " + taxName
                + ", Rate: " + rate + "%"
                + ", Filing Frequency: " + filingFrequency
                + ", Active: " + active;
    }

    // PRINT DETAILS

    @Override
    public void printDetails() {

        System.out.println("\n===== TAX TYPE DETAILS =====");

        System.out.println("Tax Type ID: " + taxTypeId);
        System.out.println("Tax Code: " + taxCode);
        System.out.println("Tax Name: " + taxName);
        System.out.println("Tax Rate: " + rate + "%");
        System.out.println("Filing Frequency: " + filingFrequency);
        System.out.println("Active: " + active);
    }

    // VALIDATION

    @Override
    public void validate() throws ValidationException {

        // Tax Type ID validation
        if (taxTypeId == null ||
                !taxTypeId.matches("[A-Za-z0-9-]{2,30}")) {

            throw new ValidationException(
                    "Tax Type ID must contain 2-30 letters, digits, or hyphens."
            );
        }

        // Tax Code validation
        if (taxCode == null ||
                !taxCode.matches("[A-Z0-9-]{2,20}")) {

            throw new ValidationException(
                    "Tax Code must contain 2-20 uppercase letters, digits, or hyphens."
            );
        }

        // Tax Name validation
        if (taxName == null ||
                taxName.trim().length() < 2 ||
                taxName.trim().length() > 60) {

            throw new ValidationException(
                    "Tax Name must contain 2-60 characters."
            );
        }

        // Tax Rate validation
        if (rate == null) {

            throw new ValidationException(
                    "Tax rate is required."
            );
        }

        if (rate.compareTo(BigDecimal.ZERO) < 0 ||
                rate.compareTo(new BigDecimal("100")) > 0) {

            throw new ValidationException(
                    "Tax rate must be between 0 and 100."
            );
        }

        if (rate.scale() > 2) {

            throw new ValidationException(
                    "Tax rate cannot have more than 2 decimal places."
            );
        }

        // Filing Frequency validation
        if (filingFrequency == null ||
                !(filingFrequency.equals("MONTHLY") ||
                  filingFrequency.equals("QUARTERLY") ||
                  filingFrequency.equals("ANNUAL"))) {

            throw new ValidationException(
                    "Filing frequency must be MONTHLY, QUARTERLY, or ANNUAL."
            );
        }

        // Active status validation
        if (active == null) {

            throw new ValidationException(
                    "Tax Type active status is required."
            );
        }
    }
}
