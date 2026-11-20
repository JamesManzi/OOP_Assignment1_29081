
package com.auca.taxassessment;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.taxdeclaration.TaxDeclaration;

public class TaxAssessment implements Identifiable, Validatable {

    private String assessmentId;
    private TaxDeclaration declaration;
    private BigDecimal assessedTax;
    private BigDecimal adjustmentAmount;
    private LocalDate assessmentDate;
    private String status;

    public TaxAssessment(
            BigDecimal adjustmentAmount,
            BigDecimal assessedTax,
            LocalDate assessmentDate,
            String assessmentId,
            TaxDeclaration declaration,
            String status) {

        this.adjustmentAmount = adjustmentAmount;
        this.assessedTax = assessedTax;
        this.assessmentDate = assessmentDate;
        this.assessmentId = assessmentId;
        this.declaration = declaration;
        this.status = status;
    }

    // GETTERS AND SETTERS

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public TaxDeclaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(TaxDeclaration declaration) {
        this.declaration = declaration;
    }

    public BigDecimal getAssessedTax() {
        return assessedTax;
    }

    public void setAssessedTax(BigDecimal assessedTax) {
        this.assessedTax = assessedTax;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
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
        return assessmentId;
    }

    @Override
    public String getDisplayName() {
        return "Tax Assessment - " + assessmentId;
    }

    @Override
    public boolean isActive() {
        return status != null
                && !status.equalsIgnoreCase("CANCELLED");
    }

    // SUMMARY

    @Override
    public String getSummary() {
        return "Assessment ID: " + assessmentId
                + " | Declaration ID: "
                + (declaration != null
                        ? declaration.getDeclarationId() : "N/A")
                + " | Assessed Tax: " + assessedTax
                + " | Adjustment: " + adjustmentAmount
                + " | Total: " + getAssessmentTotal()
                + " | Date: " + assessmentDate
                + " | Status: " + status;
    }

    // CALCULATE TOTAL

    public BigDecimal getAssessmentTotal() {
        if (assessedTax == null || adjustmentAmount == null) {
            return BigDecimal.ZERO;
        }

        return assessedTax.add(adjustmentAmount);
    }

    // PRINT DETAILS

    @Override
    public void printDetails() {
        System.out.println("\n===== TAX ASSESSMENT DETAILS =====");

        System.out.println("Assessment ID: " + assessmentId);

        System.out.println("Declaration ID: "
                + (declaration != null
                        ? declaration.getDeclarationId() : "N/A"));

        System.out.println("Assessed Tax: " + assessedTax);
        System.out.println("Adjustment Amount: " + adjustmentAmount);
        System.out.println("Assessment Total: " + getAssessmentTotal());
        System.out.println("Assessment Date: " + assessmentDate);
        System.out.println("Status: " + status);
    }

    // VALIDATION

    @Override
    public void validate() throws ValidationException {

        BigDecimal maximum =
                new BigDecimal("999999999999.99");

        if (assessmentId == null
                || !assessmentId.matches("[A-Za-z0-9-]{2,30}")) {
            throw new ValidationException(
                    "Assessment ID must contain 2-30 letters, digits or hyphens.");
        }

        if (declaration == null) {
            throw new ValidationException(
                    "Tax declaration is required.");
        }

        declaration.validate();

        if ("REJECTED".equalsIgnoreCase(declaration.getStatus())
                || "CANCELLED".equalsIgnoreCase(declaration.getStatus())) {
            throw new ValidationException(
                    "Rejected or cancelled declarations cannot be assessed.");
        }

        if (assessmentDate == null
                || assessmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Assessment date is required and cannot be in the future.");
        }

        if (assessedTax == null
                || assessedTax.signum() < 0
                || assessedTax.compareTo(maximum) > 0
                || assessedTax.scale() > 2) {
            throw new ValidationException(
                    "Invalid assessed tax amount.");
        }

        if (adjustmentAmount == null
                || adjustmentAmount.compareTo(maximum) > 0
                || adjustmentAmount.compareTo(maximum.negate()) < 0
                || adjustmentAmount.scale() > 2) {
            throw new ValidationException(
                    "Invalid adjustment amount.");
        }

        BigDecimal total = assessedTax.add(adjustmentAmount);

        if (total.signum() < 0 || total.compareTo(maximum) > 0) {
            throw new ValidationException(
                    "Assessment total must be non-negative and within the maximum amount.");
        }

        if (status == null
                || !(status.equalsIgnoreCase("DRAFT")
                || status.equalsIgnoreCase("CONFIRMED")
                || status.equalsIgnoreCase("CANCELLED"))) {
            throw new ValidationException(
                    "Assessment status must be DRAFT, CONFIRMED or CANCELLED.");
        }
    }
}
