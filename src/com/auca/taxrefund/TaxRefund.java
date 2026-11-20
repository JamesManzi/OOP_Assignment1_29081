
package com.auca.taxrefund;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.taxassessment.TaxAssessment;

public class TaxRefund implements Identifiable, Validatable {

    private String refundId;
    private RegisteredTaxpayer taxpayer;
    private TaxAssessment assessment;
    private BigDecimal requestedAmount;
    private LocalDate requestDate;
    private String status;

    public TaxRefund(TaxAssessment assessment, String refundId,
            LocalDate requestDate, BigDecimal requestedAmount,
            String status, RegisteredTaxpayer taxpayer) {

        this.assessment = assessment;
        this.refundId = refundId;
        this.requestDate = requestDate;
        this.requestedAmount = requestedAmount;
        this.status = status;
        this.taxpayer = taxpayer;
    }

    // Getters and setters

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public RegisteredTaxpayer getTaxpayer() {
        return taxpayer;
    }

    public void setTaxpayer(RegisteredTaxpayer taxpayer) {
        this.taxpayer = taxpayer;
    }

    public TaxAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(TaxAssessment assessment) {
        this.assessment = assessment;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Identifiable methods

    @Override
    public String getId() {
        return refundId;
    }

    @Override
    public String getDisplayName() {
        return "Tax Refund - " + refundId;
    }

    @Override
    public boolean isActive() {
        return "PENDING".equalsIgnoreCase(status);
    }

    // Summary

    @Override
    public String getSummary() {

        return "Refund ID: " + refundId
                + "\nTaxpayer: "
                + (taxpayer == null ? "N/A" : taxpayer.getDisplayName())
                + "\nAssessment ID: "
                + (assessment == null ? "N/A" : assessment.getAssessmentId())
                + "\nRequested Amount: " + requestedAmount
                + "\nRequest Date: " + requestDate
                + "\nStatus: " + status;
    }

    // Print details

    @Override
    public void printDetails() {

        System.out.println("\n========== TAX REFUND DETAILS ==========");

        System.out.println("Refund ID: " + refundId);

        System.out.println("Taxpayer: "
                + (taxpayer == null ? "N/A" : taxpayer.getDisplayName()));

        System.out.println("Taxpayer TIN: "
                + (taxpayer == null ? "N/A" : taxpayer.getTin()));

        System.out.println("Assessment ID: "
                + (assessment == null ? "N/A" : assessment.getAssessmentId()));

        System.out.println("Requested Amount: " + requestedAmount);

        System.out.println("Request Date: " + requestDate);

        System.out.println("Status: " + status);

        System.out.println("Active: " + isActive());

        System.out.println("=========================================");
    }

    // Validation

    @Override
    public void validate() throws ValidationException {

        BigDecimal maximumAmount = new BigDecimal("999999999999.99");

        // Validate refund ID
        if (refundId == null
                || !refundId.matches("[A-Za-z0-9-]{2,30}")) {

            throw new ValidationException(
                    "Refund ID must contain 2-30 letters, digits or hyphens.");
        }

        // Validate taxpayer
        if (taxpayer == null) {

            throw new ValidationException(
                    "A registered taxpayer is required.");
        }

        if (!taxpayer.isActive()) {

            throw new ValidationException(
                    "Only active taxpayers can request refunds.");
        }

        taxpayer.validate();

        // Validate assessment
        if (assessment == null) {

            throw new ValidationException(
                    "Tax assessment is required.");
        }

        assessment.validate();

        if (!"CONFIRMED".equalsIgnoreCase(assessment.getStatus())) {

            throw new ValidationException(
                    "Refund requires a confirmed tax assessment.");
        }

        // Verify taxpayer and assessment belong together
        if (assessment.getDeclaration() == null
                || assessment.getDeclaration().getObligation() == null
                || assessment.getDeclaration().getObligation().getTaxpayer() == null
                || !assessment.getDeclaration().getObligation()
                        .getTaxpayer().getTin().equals(taxpayer.getTin())) {

            throw new ValidationException(
                    "The selected assessment does not belong to this taxpayer.");
        }

        // Validate requested amount
        if (requestedAmount == null
                || requestedAmount.compareTo(BigDecimal.ZERO) <= 0
                || requestedAmount.compareTo(maximumAmount) > 0
                || requestedAmount.scale() > 2) {

            throw new ValidationException(
                    "Requested refund must be positive, "
                    + "within the maximum amount, and have at most 2 decimal places.");
        }

        // Validate request date
        if (requestDate == null) {

            throw new ValidationException(
                    "Refund request date is required.");
        }

        if (requestDate.isAfter(LocalDate.now())) {

            throw new ValidationException(
                    "Refund request date cannot be in the future.");
        }

        if (assessment.getAssessmentDate() != null
                && requestDate.isBefore(assessment.getAssessmentDate())) {

            throw new ValidationException(
                    "Refund request date cannot be before the assessment date.");
        }

        // Validate status
        if (status == null
                || !(status.equalsIgnoreCase("PENDING")
                || status.equalsIgnoreCase("APPROVED")
                || status.equalsIgnoreCase("REJECTED")
                || status.equalsIgnoreCase("PAID")
                || status.equalsIgnoreCase("CANCELLED"))) {

            throw new ValidationException(
                    "Status must be PENDING, APPROVED, REJECTED, PAID or CANCELLED.");
        }
    }
}
