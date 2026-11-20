
package com.auca.taxdeclaration;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.taxobligation.TaxObligation;

public class TaxDeclaration implements Identifiable, Validatable {

    private String declarationId;
    private TaxObligation obligation;
    private LocalDate filingDate;
    private BigDecimal taxableAmount;
    private BigDecimal declaredTax;
    private String status;

    public TaxDeclaration(
            String declarationId,
            BigDecimal declaredTax,
            LocalDate filingDate,
            TaxObligation obligation,
            String status,
            BigDecimal taxableAmount) {

        this.declarationId = declarationId;
        this.declaredTax = declaredTax;
        this.filingDate = filingDate;
        this.obligation = obligation;
        this.status = status;
        this.taxableAmount = taxableAmount;
    }

    // GETTERS AND SETTERS

    public String getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(String declarationId) {
        this.declarationId = declarationId;
    }

    public TaxObligation getObligation() {
        return obligation;
    }

    public void setObligation(TaxObligation obligation) {
        this.obligation = obligation;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getDeclaredTax() {
        return declaredTax;
    }

    public void setDeclaredTax(BigDecimal declaredTax) {
        this.declaredTax = declaredTax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // IDENTIFIABLE METHODS

    @Override
    public String getId() {
        return declarationId;
    }

    @Override
    public String getDisplayName() {
        return "Tax Declaration - " + declarationId;
    }

    @Override
    public boolean isActive() {
        return status != null
                && !status.equalsIgnoreCase("REJECTED")
                && !status.equalsIgnoreCase("CANCELLED");
    }

    // SUMMARY

    @Override
    public String getSummary() {
        return "Declaration ID: " + declarationId
                + " | Obligation ID: "
                + (obligation != null ? obligation.getObligationId() : "N/A")
                + " | Filing Date: " + filingDate
                + " | Taxable Amount: " + taxableAmount
                + " | Declared Tax: " + declaredTax
                + " | Status: " + status;
    }

    // PRINT DETAILS

    @Override
    public void printDetails() {
        System.out.println("\n===== TAX DECLARATION DETAILS =====");

        System.out.println("Declaration ID: " + declarationId);

        System.out.println("Obligation ID: "
                + (obligation != null
                        ? obligation.getObligationId() : "N/A"));

        System.out.println("Taxpayer: "
                + (obligation != null && obligation.getTaxpayer() != null
                        ? obligation.getTaxpayer().getDisplayName() : "N/A"));

        System.out.println("Tax Type: "
                + (obligation != null && obligation.getTaxType() != null
                        ? obligation.getTaxType().getTaxName() : "N/A"));

        System.out.println("Filing Date: " + filingDate);
        System.out.println("Taxable Amount: " + taxableAmount);
        System.out.println("Declared Tax: " + declaredTax);
        System.out.println("Status: " + status);
    }

    // VALIDATION

    @Override
    public void validate() throws ValidationException {

        BigDecimal maximum =
                new BigDecimal("999999999999.99");

        if (declarationId == null
                || !declarationId.matches("[A-Za-z0-9-]{2,30}")) {
            throw new ValidationException(
                    "Declaration ID must contain 2-30 letters, digits or hyphens.");
        }

        if (obligation == null) {
            throw new ValidationException(
                    "Tax obligation is required.");
        }

        obligation.validate();

        if (filingDate == null) {
            throw new ValidationException(
                    "Filing date is required.");
        }

        if (filingDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Filing date cannot be in the future.");
        }

        if (taxableAmount == null
                || taxableAmount.signum() < 0
                || taxableAmount.compareTo(maximum) > 0
                || taxableAmount.scale() > 2) {
            throw new ValidationException(
                    "Invalid taxable amount.");
        }

        if (declaredTax == null
                || declaredTax.signum() < 0
                || declaredTax.compareTo(maximum) > 0
                || declaredTax.scale() > 2) {
            throw new ValidationException(
                    "Invalid declared tax.");
        }

        if (status == null
                || !(status.equalsIgnoreCase("PENDING")
                || status.equalsIgnoreCase("LATE")
                || status.equalsIgnoreCase("REJECTED")
                || status.equalsIgnoreCase("CANCELLED"))) {
            throw new ValidationException(
                    "Invalid declaration status.");
        }
    }
}
