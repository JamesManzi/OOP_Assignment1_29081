
package com.auca.taxobligation;

import java.time.LocalDate;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.taxtype.TaxType;

public class TaxObligation implements Identifiable, Validatable {

    private String obligationId;
    private RegisteredTaxpayer taxpayer;
    private TaxType taxType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate dueDate;

    // CONSTRUCTOR

    public TaxObligation(
            LocalDate dueDate,
            String obligationId,
            LocalDate periodEnd,
            LocalDate periodStart,
            TaxType taxType,
            RegisteredTaxpayer taxpayer) {

        this.dueDate = dueDate;
        this.obligationId = obligationId;
        this.periodEnd = periodEnd;
        this.periodStart = periodStart;
        this.taxType = taxType;
        this.taxpayer = taxpayer;
    }

    // GETTERS AND SETTERS

    public String getObligationId() {
        return obligationId;
    }

    public void setObligationId(String obligationId) {
        this.obligationId = obligationId;
    }

    public RegisteredTaxpayer getTaxpayer() {
        return taxpayer;
    }

    public void setTaxpayer(RegisteredTaxpayer taxpayer) {
        this.taxpayer = taxpayer;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // IDENTIFIABLE METHODS

    @Override
    public String getDisplayName() {
        return "Tax Obligation - " + obligationId;
    }

    @Override
    public String getId() {
        return obligationId;
    }

    @Override
    public boolean isActive() {
        return taxpayer != null
                && taxpayer.isActive()
                && taxType != null
                && taxType.isActive();
    }

    // SUMMARY

    @Override
    public String getSummary() {

        String taxpayerName = taxpayer == null
                ? "Not assigned"
                : taxpayer.getDisplayName();

        String taxName = taxType == null
                ? "Not assigned"
                : taxType.getTaxName();

        return "Obligation ID: " + obligationId
                + ", Taxpayer: " + taxpayerName
                + ", Tax Type: " + taxName
                + ", Period: " + periodStart + " to " + periodEnd
                + ", Due Date: " + dueDate;
    }

    // PRINT DETAILS

    @Override
    public void printDetails() {

        System.out.println("\n===== TAX OBLIGATION DETAILS =====");

        System.out.println("Obligation ID: " + obligationId);

        if (taxpayer != null) {
            System.out.println(
                    "Taxpayer: " + taxpayer.getDisplayName());
            System.out.println(
                    "TIN: " + taxpayer.getTin());
        } else {
            System.out.println("Taxpayer: Not assigned");
        }

        if (taxType != null) {
            System.out.println(
                    "Tax Type: " + taxType.getTaxName());
            System.out.println(
                    "Tax Code: " + taxType.getTaxCode());
            System.out.println(
                    "Tax Rate: " + taxType.getRate() + "%");
        } else {
            System.out.println("Tax Type: Not assigned");
        }

        System.out.println("Period Start: " + periodStart);
        System.out.println("Period End: " + periodEnd);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Active: " + isActive());
    }

    // VALIDATION

    @Override
    public void validate() throws ValidationException {

        // Obligation ID
        if (obligationId == null
                || !obligationId.matches("[A-Za-z0-9-]{2,30}")) {

            throw new ValidationException(
                    "Obligation ID must contain 2-30 letters, digits, or hyphens.");
        }

        // Taxpayer
        if (taxpayer == null) {

            throw new ValidationException(
                    "A registered taxpayer must be assigned.");
        }

        if (!taxpayer.isActive()) {

            throw new ValidationException(
                    "Taxpayer must be active.");
        }

        // Tax Type
        if (taxType == null) {

            throw new ValidationException(
                    "A tax type must be assigned.");
        }

        taxType.validate();

        if (!taxType.isActive()) {

            throw new ValidationException(
                    "Tax type must be active.");
        }

        // Period Start
        if (periodStart == null) {

            throw new ValidationException(
                    "Period start date is required.");
        }

        // Period End
        if (periodEnd == null) {

            throw new ValidationException(
                    "Period end date is required.");
        }

        if (periodEnd.isBefore(periodStart)) {

            throw new ValidationException(
                    "Period end date cannot be before period start date.");
        }

        // Due Date
        if (dueDate == null) {

            throw new ValidationException(
                    "Due date is required.");
        }

        if (!dueDate.isAfter(periodEnd)) {

            throw new ValidationException(
                    "Due date must be after the period end date.");
        }
    }
}
