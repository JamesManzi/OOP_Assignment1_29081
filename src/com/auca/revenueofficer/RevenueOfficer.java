
package com.auca.revenueofficer;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.auca.abstracts.TaxParty;
import com.auca.exceptions.ValidationException;

public class RevenueOfficer extends TaxParty {

    private String staffNumber;
    private String department;
    private String role;
    private LocalDate hireDate;
    private BigDecimal approvalLimit;
    private Boolean active;

    public RevenueOfficer(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String staffNumber,
            String department,
            String role,
            LocalDate hireDate,
            BigDecimal approvalLimit,
            Boolean active) {

        super(firstName, lastName, email, phoneNumber, dateOfBirth);

        this.staffNumber = staffNumber;
        this.department = department;
        this.role = role;
        this.hireDate = hireDate;
        this.approvalLimit = approvalLimit;
        this.active = active;
    }

    // GETTERS AND SETTERS

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getApprovalLimit() {
        return approvalLimit;
    }

    public void setApprovalLimit(BigDecimal approvalLimit) {
        this.approvalLimit = approvalLimit;
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
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String getId() {
        return getPartyId();
    }

    @Override
    public boolean isActive() {
        return active != null && active;
    }

    // SUMMARY

    @Override
    public String getSummary() {

        return super.getSummary()
                + ", Staff Number: " + staffNumber
                + ", Department: " + department
                + ", Role: " + role
                + ", Active: " + active;
    }

    // PRINT DETAILS

    @Override
    public void printDetails() {

        super.printDetails();

        System.out.println("\n===== REVENUE OFFICER DETAILS =====");
        System.out.println("Staff Number: " + staffNumber);
        System.out.println("Department: " + department);
        System.out.println("Role: " + role);
        System.out.println("Hire Date: " + hireDate);
        System.out.println("Approval Limit: " + approvalLimit);
        System.out.println("Active: " + active);
    }

    // VALIDATION

    @Override
    public void validate() throws ValidationException {

        // Validate inherited personal information.
        super.validate();

        // Staff number validation.
        if (staffNumber == null ||
                !staffNumber.matches("[A-Za-z0-9-]{2,30}")) {

            throw new ValidationException(
                    "Staff number must contain 2-30 letters, digits, or hyphens."
            );
        }

        // Department validation.
        if (department == null ||
                department.trim().length() < 2 ||
                department.trim().length() > 60) {

            throw new ValidationException(
                    "Department must contain 2-60 characters."
            );
        }

        // Role validation.
        if (role == null ||
                role.trim().length() < 2 ||
                role.trim().length() > 60) {

            throw new ValidationException(
                    "Role must contain 2-60 characters."
            );
        }

        // Hire date validation.
        if (hireDate == null) {
            throw new ValidationException(
                    "Hire date is required."
            );
        }

        if (hireDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Hire date cannot be in the future."
            );
        }

        if (getDateOfBirth() != null &&
                !hireDate.isAfter(getDateOfBirth())) {

            throw new ValidationException(
                    "Hire date must be after date of birth."
            );
        }

        // Approval limit validation.
        if (approvalLimit == null) {
            throw new ValidationException(
                    "Approval limit is required."
            );
        }

        if (approvalLimit.signum() < 0) {
            throw new ValidationException(
                    "Approval limit cannot be negative."
            );
        }

        if (approvalLimit.scale() > 2) {
            throw new ValidationException(
                    "Approval limit cannot have more than 2 decimal places."
            );
        }

        if (approvalLimit.compareTo(
                new BigDecimal("999999999999.99")) > 0) {

            throw new ValidationException(
                    "Approval limit exceeds the maximum allowed amount."
            );
        }

        // Active status validation.
        if (active == null) {
            throw new ValidationException(
                    "Officer active status is required."
            );
        }
    }
}
