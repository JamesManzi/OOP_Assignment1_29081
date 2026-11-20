
package com.auca.penalty;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.taxobligation.TaxObligation;
import java.math.BigDecimal;

public class Penalty implements Identifiable, Validatable {

    private String penaltyId;
    private TaxObligation obligation;
    private String penaltyType;
    private BigDecimal baseAmount;
    private BigDecimal penaltyRate;
    private BigDecimal penaltyAmount;

    public Penalty(BigDecimal baseAmount, TaxObligation obligation,
            BigDecimal penaltyAmount, String penaltyId,
            BigDecimal penaltyRate, String penaltyType) {

        this.baseAmount = baseAmount;
        this.obligation = obligation;
        this.penaltyAmount = penaltyAmount;
        this.penaltyId = penaltyId;
        this.penaltyRate = penaltyRate;
        this.penaltyType = penaltyType;
    }

    // Getters and setters

    public String getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(String penaltyId) {
        this.penaltyId = penaltyId;
    }

    public TaxObligation getObligation() {
        return obligation;
    }

    public void setObligation(TaxObligation obligation) {
        this.obligation = obligation;
    }

    public String getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    // Identifiable methods

    @Override
    public String getId() {
        return penaltyId;
    }

    @Override
    public String getDisplayName() {
        return "Penalty - " + penaltyId;
    }

    @Override
    public boolean isActive() {
        return obligation != null && obligation.isActive();
    }

    // Summary

    @Override
    public String getSummary() {

        return "Penalty ID: " + penaltyId
                + "\nPenalty Type: " + penaltyType
                + "\nObligation ID: "
                + (obligation == null ? "N/A" : obligation.getObligationId())
                + "\nBase Amount: " + baseAmount
                + "\nPenalty Rate: " + penaltyRate + "%"
                + "\nPenalty Amount: " + penaltyAmount;
    }

    // Print details

    @Override
    public void printDetails() {

        System.out.println("\n========== PENALTY DETAILS ==========");

        System.out.println("Penalty ID: " + penaltyId);

        System.out.println("Penalty Type: " + penaltyType);

        System.out.println("Obligation ID: "
                + (obligation == null ? "N/A" : obligation.getObligationId()));

        System.out.println("Base Amount: " + baseAmount);

        System.out.println("Penalty Rate: " + penaltyRate + "%");

        System.out.println("Penalty Amount: " + penaltyAmount);

        System.out.println("Active: " + isActive());

        System.out.println("=====================================");
    }

    // Validation

    @Override
    public void validate() throws ValidationException {

        BigDecimal maximumAmount = new BigDecimal("999999999999.99");

        // Validate penalty ID
        if (penaltyId == null
                || !penaltyId.matches("[A-Za-z0-9-]{2,30}")) {

            throw new ValidationException(
                    "Penalty ID must contain 2-30 letters, digits or hyphens.");
        }

        // Validate obligation
        if (obligation == null) {
            throw new ValidationException(
                    "Tax obligation is required.");
        }

        obligation.validate();

        // Validate penalty type
        if (penaltyType == null
                || penaltyType.trim().length() < 2
                || penaltyType.trim().length() > 60) {

            throw new ValidationException(
                    "Penalty type must contain 2-60 characters.");
        }

        // Validate base amount
        if (baseAmount == null
                || baseAmount.compareTo(BigDecimal.ZERO) < 0
                || baseAmount.compareTo(maximumAmount) > 0
                || baseAmount.scale() > 2) {

            throw new ValidationException(
                    "Base amount must be between 0 and 999999999999.99 "
                    + "with maximum 2 decimal places.");
        }

        // Validate penalty rate
        if (penaltyRate == null
                || penaltyRate.compareTo(BigDecimal.ZERO) < 0
                || penaltyRate.compareTo(new BigDecimal("100")) > 0
                || penaltyRate.scale() > 2) {

            throw new ValidationException(
                    "Penalty rate must be between 0 and 100 "
                    + "with maximum 2 decimal places.");
        }

        // Validate penalty amount
        if (penaltyAmount == null
                || penaltyAmount.compareTo(BigDecimal.ZERO) < 0
                || penaltyAmount.compareTo(maximumAmount) > 0
                || penaltyAmount.scale() > 2) {

            throw new ValidationException(
                    "Penalty amount must be between 0 and 999999999999.99 "
                    + "with maximum 2 decimal places.");
        }

        // Verify penalty calculation
        BigDecimal expectedPenalty = baseAmount
                .multiply(penaltyRate)
                .divide(new BigDecimal("100"), 2,
                        java.math.RoundingMode.HALF_UP);

        if (penaltyAmount.compareTo(expectedPenalty) != 0) {

            throw new ValidationException(
                    "Penalty amount does not match the base amount "
                    + "and penalty rate. Expected: " + expectedPenalty);
        }
    }
}
