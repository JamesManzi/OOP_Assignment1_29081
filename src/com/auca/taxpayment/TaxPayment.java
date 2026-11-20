
package com.auca.taxpayment;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.taxassessment.TaxAssessment;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxPayment implements Identifiable, Validatable {

    private String paymentId;
    private TaxAssessment assessment;
    private String paymentReference;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;

    public TaxPayment(
            BigDecimal amount,
            TaxAssessment assessment,
            LocalDate paymentDate,
            String paymentId,
            String paymentMethod,
            String paymentReference) {

        this.amount = amount;
        this.assessment = assessment;
        this.paymentDate = paymentDate;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
    }

    // GETTERS AND SETTERS

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public TaxAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(TaxAssessment assessment) {
        this.assessment = assessment;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // IDENTIFIABLE METHODS

    @Override
    public String getId() {
        return paymentId;
    }

    @Override
    public String getDisplayName() {
        return "Tax Payment - " + paymentId;
    }

    @Override
    public boolean isActive() {
        return assessment != null
                && "CONFIRMED".equalsIgnoreCase(
                        assessment.getStatus());
    }

    // SUMMARY

    @Override
    public String getSummary() {
        return "Payment ID: " + paymentId
                + " | Reference: " + paymentReference
                + " | Assessment ID: "
                + (assessment != null
                        ? assessment.getAssessmentId() : "N/A")
                + " | Amount: " + amount
                + " | Date: " + paymentDate
                + " | Method: " + paymentMethod;
    }

    // PRINT DETAILS

    @Override
    public void printDetails() {

        System.out.println("\n===== TAX PAYMENT DETAILS =====");

        System.out.println("Payment ID: " + paymentId);
        System.out.println("Payment Reference: " + paymentReference);

        System.out.println("Assessment ID: "
                + (assessment != null
                        ? assessment.getAssessmentId() : "N/A"));

        System.out.println("Amount: " + amount);
        System.out.println("Payment Date: " + paymentDate);
        System.out.println("Payment Method: " + paymentMethod);
    }

    // VALIDATION

    @Override
    public void validate() throws ValidationException {

        BigDecimal maximum =
                new BigDecimal("999999999999.99");

        if (paymentId == null
                || !paymentId.matches("[A-Za-z0-9-]{2,30}")) {
            throw new ValidationException(
                    "Payment ID must contain 2-30 letters, digits or hyphens.");
        }

        if (paymentReference == null
                || paymentReference.trim().length() < 2
                || paymentReference.trim().length() > 60) {
            throw new ValidationException(
                    "Payment reference must contain 2-60 characters.");
        }

        if (assessment == null) {
            throw new ValidationException(
                    "Assessment is required for payment.");
        }

        assessment.validate();

        if (!"CONFIRMED".equalsIgnoreCase(
                assessment.getStatus())) {
            throw new ValidationException(
                    "Only confirmed assessments can receive payments.");
        }

        if (amount == null
                || amount.signum() <= 0
                || amount.compareTo(maximum) > 0
                || amount.scale() > 2) {
            throw new ValidationException(
                    "Payment amount must be positive, have at most "
                    + "2 decimal places and not exceed 999999999999.99.");
        }

        if (paymentDate == null) {
            throw new ValidationException(
                    "Payment date is required.");
        }

        if (paymentDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Payment date cannot be in the future.");
        }

        if (assessment.getAssessmentDate() != null
                && paymentDate.isBefore(
                        assessment.getAssessmentDate())) {
            throw new ValidationException(
                    "Payment date cannot be before the assessment date.");
        }

        if (paymentMethod == null
                || !(paymentMethod.equalsIgnoreCase("Mobile Money")
                || paymentMethod.equalsIgnoreCase("Bank Transfer")
                || paymentMethod.equalsIgnoreCase("Cash")
                || paymentMethod.equalsIgnoreCase("Card"))) {
            throw new ValidationException(
                    "Invalid payment method.");
        }
    }
}
