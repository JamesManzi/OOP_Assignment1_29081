
package com.auca.taxclearance;

import java.time.LocalDate;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Validatable;
import com.auca.revenueofficer.RevenueOfficer;

public class TaxClearance implements Identifiable, Validatable {

    private String clearanceId;
    private RegisteredTaxpayer taxpayer;
    private LocalDate requestDate;
    private LocalDate expiryDate;
    private String status;
    private RevenueOfficer approvedBy;

    public TaxClearance(RevenueOfficer approvedBy, String clearanceId,
            LocalDate expiryDate, LocalDate requestDate,
            String status, RegisteredTaxpayer taxpayer) {

        this.approvedBy = approvedBy;
        this.clearanceId = clearanceId;
        this.expiryDate = expiryDate;
        this.requestDate = requestDate;
        this.status = status;
        this.taxpayer = taxpayer;
    }

    // Getters and setters

    public String getClearanceId() {
        return clearanceId;
    }

    public void setClearanceId(String clearanceId) {
        this.clearanceId = clearanceId;
    }

    public RegisteredTaxpayer getTaxpayer() {
        return taxpayer;
    }

    public void setTaxpayer(RegisteredTaxpayer taxpayer) {
        this.taxpayer = taxpayer;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RevenueOfficer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(RevenueOfficer approvedBy) {
        this.approvedBy = approvedBy;
    }

    // Identifiable methods

    @Override
    public String getId() {
        return clearanceId;
    }

    @Override
    public String getDisplayName() {
        return "Tax Clearance - " + clearanceId;
    }

    @Override
    public boolean isActive() {

        return "APPROVED".equalsIgnoreCase(status)
                && expiryDate != null
                && !expiryDate.isBefore(LocalDate.now());
    }

    // Summary

    @Override
    public String getSummary() {

        return "Clearance ID: " + clearanceId
                + "\nTaxpayer: "
                + (taxpayer == null ? "N/A" : taxpayer.getDisplayName())
                + "\nTIN: "
                + (taxpayer == null ? "N/A" : taxpayer.getTin())
                + "\nRequest Date: " + requestDate
                + "\nExpiry Date: " + expiryDate
                + "\nApproved By: "
                + (approvedBy == null ? "N/A" : approvedBy.getDisplayName())
                + "\nStatus: " + status;
    }

    // Print details

    @Override
    public void printDetails() {

        System.out.println("\n========== TAX CLEARANCE DETAILS ==========");

        System.out.println("Clearance ID: " + clearanceId);

        System.out.println("Taxpayer: "
                + (taxpayer == null ? "N/A" : taxpayer.getDisplayName()));

        System.out.println("TIN: "
                + (taxpayer == null ? "N/A" : taxpayer.getTin()));

        System.out.println("Request Date: " + requestDate);

        System.out.println("Expiry Date: " + expiryDate);

        System.out.println("Approved By: "
                + (approvedBy == null ? "N/A" : approvedBy.getDisplayName()));

        System.out.println("Status: " + status);

        System.out.println("Active: " + isActive());

        System.out.println("============================================");
    }

    // Validation

    @Override
    public void validate() throws ValidationException {

        // Validate clearance ID
        if (clearanceId == null
                || !clearanceId.matches("[A-Za-z0-9-]{2,30}")) {

            throw new ValidationException(
                    "Clearance ID must contain 2-30 letters, digits or hyphens.");
        }

        // Validate taxpayer
        if (taxpayer == null) {

            throw new ValidationException(
                    "Registered taxpayer is required.");
        }

        if (!taxpayer.isActive()) {

            throw new ValidationException(
                    "Only active taxpayers can request tax clearance.");
        }

        taxpayer.validate();

        // Validate revenue officer
        if (approvedBy == null) {

            throw new ValidationException(
                    "Approving revenue officer is required.");
        }

        if (!approvedBy.isActive()) {

            throw new ValidationException(
                    "Only active revenue officers can approve tax clearance.");
        }

        approvedBy.validate();

        // Validate request date
        if (requestDate == null) {

            throw new ValidationException(
                    "Request date is required.");
        }

        if (requestDate.isAfter(LocalDate.now())) {

            throw new ValidationException(
                    "Request date cannot be in the future.");
        }

        // Validate expiry date
        if (expiryDate == null) {

            throw new ValidationException(
                    "Expiry date is required.");
        }

        if (!expiryDate.isAfter(requestDate)) {

            throw new ValidationException(
                    "Expiry date must be after request date.");
        }

        // Validate status
        if (status == null
                || !(status.equalsIgnoreCase("PENDING")
                || status.equalsIgnoreCase("APPROVED")
                || status.equalsIgnoreCase("REJECTED")
                || status.equalsIgnoreCase("EXPIRED")
                || status.equalsIgnoreCase("CANCELLED"))) {

            throw new ValidationException(
                    "Status must be PENDING, APPROVED, REJECTED, EXPIRED or CANCELLED.");
        }
    }
}
