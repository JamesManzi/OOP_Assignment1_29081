
package com.auca.methods;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.penalty.Penalty;
import com.auca.revenueofficer.RevenueOfficer;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxclearance.TaxClearance;
import com.auca.taxdeclaration.TaxDeclaration;
import com.auca.taxobligation.TaxObligation;
import com.auca.taxpayment.TaxPayment;
import com.auca.taxrefund.TaxRefund;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RequestMethods {

    private static final BigDecimal MAX_AMOUNT =
            new BigDecimal("999999999999.99");

    public static LocalDate readDate(Scanner scanner, String message) {

        while (true) {
            try {
                System.out.print(message + " (YYYY-MM-DD): ");
                return LocalDate.parse(scanner.nextLine().trim());

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Please use YYYY-MM-DD.");
            }
        }
    }

    // =========================================================
    // REQUEST TAX CLEARANCE
    // =========================================================

    public static void requestIssueTaxClearance(Scanner scanner) {

        if (DataStore.clearanceCount >= DataStore.taxClearances.length) {
            System.out.println("Tax clearance storage is full!");
            return;
        }

        try {
            System.out.println("\n===== REGISTERED TAXPAYERS =====");

            if (DataStore.individualCount == 0
                    && DataStore.corporateCount == 0) {
                System.out.println("No taxpayers are registered.");
                return;
            }

            for (int i = 0; i < DataStore.individualCount; i++) {

                if (DataStore.individualTaxpayers[i] != null) {
                    System.out.println(
                        "TIN: " + DataStore.individualTaxpayers[i].getTin()
                        + " | Name: "
                        + DataStore.individualTaxpayers[i].getDisplayName()
                        + " | Status: "
                        + DataStore.individualTaxpayers[i].getTaxpayerStatus()
                    );
                }
            }

            for (int i = 0; i < DataStore.corporateCount; i++) {

                if (DataStore.corporateTaxpayers[i] != null) {
                    System.out.println(
                        "TIN: " + DataStore.corporateTaxpayers[i].getTin()
                        + " | Company: "
                        + DataStore.corporateTaxpayers[i].getLegalName()
                        + " | Status: "
                        + DataStore.corporateTaxpayers[i].getTaxpayerStatus()
                    );
                }
            }

            // Find taxpayer
            System.out.print("\nEnter Taxpayer TIN: ");
            String tin = scanner.nextLine().trim();

            RegisteredTaxpayer taxpayer = findTaxpayerByTin(tin);

            if (taxpayer == null) {
                System.out.println("Taxpayer not found!");
                return;
            }

            if (!taxpayer.isActive()) {
                System.out.println("Taxpayer is not active!");
                return;
            }

            LocalDate today = LocalDate.now();

            // Check existing valid clearance
            for (int i = 0; i < DataStore.clearanceCount; i++) {

                TaxClearance existing = DataStore.taxClearances[i];

                if (existing == null || existing.getTaxpayer() == null) {
                    continue;
                }

                if (existing.getTaxpayer().getTin().equalsIgnoreCase(tin)
                        && "APPROVED".equalsIgnoreCase(existing.getStatus())
                        && existing.getExpiryDate() != null
                        && !existing.getExpiryDate().isBefore(today)) {

                    System.out.println(
                        "This taxpayer already has a valid tax clearance!"
                    );
                    return;
                }
            }

            // Check unresolved declarations
            for (int i = 0; i < DataStore.declarationCount; i++) {

                TaxDeclaration declaration =
                        DataStore.taxDeclarations[i];

                if (declaration == null
                        || declaration.getObligation() == null
                        || declaration.getObligation().getTaxpayer() == null) {
                    continue;
                }

                String declarationTin =
                        declaration.getObligation().getTaxpayer().getTin();

                if (!declarationTin.equalsIgnoreCase(tin)) {
                    continue;
                }

                boolean hasConfirmedAssessment = false;

                for (int j = 0; j < DataStore.assessmentCount; j++) {

                    TaxAssessment assessment =
                            DataStore.taxAssessments[j];

                    if (assessment == null
                            || assessment.getDeclaration() == null) {
                        continue;
                    }

                    if (assessment.getDeclaration().getDeclarationId()
                            .equalsIgnoreCase(declaration.getDeclarationId())
                            && "CONFIRMED".equalsIgnoreCase(
                                    assessment.getStatus())) {

                        hasConfirmedAssessment = true;
                        break;
                    }
                }

                if (!hasConfirmedAssessment) {
                    System.out.println(
                        "Clearance denied! Unresolved declaration: "
                        + declaration.getDeclarationId()
                    );
                    return;
                }
            }

            // Check overdue obligations and unpaid balances
            for (int i = 0; i < DataStore.obligationCount; i++) {

                TaxObligation obligation =
                        DataStore.taxObligations[i];

                if (obligation == null || obligation.getTaxpayer() == null) {
                    continue;
                }

                if (!obligation.getTaxpayer().getTin()
                        .equalsIgnoreCase(tin)) {
                    continue;
                }

                if (obligation.getDueDate() == null
                        || !obligation.getDueDate().isBefore(today)) {
                    continue;
                }

                TaxDeclaration declaration =
                        findDeclarationByObligation(
                                obligation.getObligationId());

                if (declaration == null) {
                    System.out.println(
                        "Clearance denied! Overdue obligation has no declaration: "
                        + obligation.getObligationId()
                    );
                    return;
                }

                TaxAssessment assessment =
                        findConfirmedAssessment(declaration);

                if (assessment == null) {
                    System.out.println(
                        "Clearance denied! Overdue obligation has no confirmed assessment."
                    );
                    return;
                }

                BigDecimal amountDue =
                        assessment.getAssessedTax()
                        .add(assessment.getAdjustmentAmount());

                // Add penalties for this obligation
                for (int j = 0; j < DataStore.penaltyCount; j++) {

                    Penalty penalty = DataStore.penalties[j];

                    if (penalty == null || penalty.getObligation() == null) {
                        continue;
                    }

                    if (penalty.getObligation().getObligationId()
                            .equalsIgnoreCase(obligation.getObligationId())) {

                        amountDue = amountDue.add(
                                penalty.getPenaltyAmount());
                    }
                }

                // Calculate payments for this assessment
                BigDecimal totalPaid =
                        calculatePayments(assessment);

                BigDecimal outstanding =
                        amountDue.subtract(totalPaid);

                if (outstanding.compareTo(BigDecimal.ZERO) > 0) {

                    System.out.println(
                        "Clearance denied! Overdue unpaid obligation: "
                        + obligation.getObligationId()
                    );

                    System.out.println(
                        "Outstanding balance: " + outstanding
                    );
                    return;
                }
            }

            // Generate unique clearance ID
            String clearanceId;

            while (true) {

                System.out.print("\nEnter Clearance ID: ");
                clearanceId = scanner.nextLine().trim();

                if (!clearanceId.matches("[A-Za-z0-9-]{2,30}")) {
                    System.out.println(
                        "Clearance ID must contain 2-30 letters, digits, or hyphens."
                    );
                    continue;
                }

                if (clearanceIdExists(clearanceId)) {
                    System.out.println("Clearance ID already exists!");
                    continue;
                }

                break;
            }

            LocalDate requestDate = today;

            LocalDate expiryDate;

            while (true) {

                expiryDate = readDate(
                    scanner, "Enter expiry date");

                if (!expiryDate.isAfter(requestDate)) {
                    System.out.println(
                        "Expiry date must be after the request date!"
                    );
                    continue;
                }

                break;
            }

            // Display active officers
            System.out.println("\n===== ACTIVE REVENUE OFFICERS =====");

            int activeCount = 0;

            for (int i = 0; i < DataStore.officerCount; i++) {

                RevenueOfficer officer =
                        DataStore.revenueOfficers[i];

                if (officer != null && officer.isActive()) {

                    System.out.println(
                        "Staff Number: " + officer.getStaffNumber()
                        + " | Name: " + officer.getDisplayName()
                    );

                    activeCount++;
                }
            }

            if (activeCount == 0) {
                System.out.println("No active Revenue Officers available!");
                return;
            }

            System.out.print(
                "\nEnter Approving Officer Staff Number: ");

            String staffNumber = scanner.nextLine().trim();

            RevenueOfficer approvedBy =
                    findActiveOfficer(staffNumber);

            if (approvedBy == null) {
                System.out.println("Active Revenue Officer not found!");
                return;
            }

            // Review
            System.out.println("\n===== TAX CLEARANCE REVIEW =====");
            System.out.println("Clearance ID: " + clearanceId);
            System.out.println("Taxpayer TIN: " + taxpayer.getTin());
            System.out.println(
                "Taxpayer Name: " + taxpayer.getDisplayName());
            System.out.println("Request Date: " + requestDate);
            System.out.println("Expiry Date: " + expiryDate);
            System.out.println(
                "Approved By: " + approvedBy.getStaffNumber());

            System.out.print(
                "\nIssue this tax clearance? (YES/NO): ");

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("YES")) {
                System.out.println(
                    "Tax clearance request cancelled. No record saved.");
                return;
            }

            // Create and validate
            TaxClearance clearance = new TaxClearance(
                approvedBy,
                clearanceId,
                expiryDate,
                requestDate,
                "APPROVED",
                taxpayer
            );

            clearance.validate();

            // Recheck capacity and duplicate ID
            if (DataStore.clearanceCount >= DataStore.taxClearances.length
                    || clearanceIdExists(clearanceId)) {
                System.out.println(
                    "Storage full or clearance ID already exists. Not saved.");
                return;
            }

            clearance.validate();

            DataStore.taxClearances[DataStore.clearanceCount] = clearance;
            DataStore.clearanceCount++;

            System.out.println(
                "\nTax clearance successfully issued!");

        } catch (ValidationException e) {

            System.out.println(
                "Clearance validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                "Unable to issue tax clearance: " + e.getMessage());
        }
    }

    // =========================================================
    // REQUEST AND APPROVE REFUND
    // =========================================================

    public static void requestApproveRefund(Scanner scanner) {

        if (DataStore.refundCount >= DataStore.taxRefunds.length) {
            System.out.println("Refund storage is full!");
            return;
        }

        if (DataStore.assessmentCount == 0) {
            System.out.println("No assessments available!");
            return;
        }

        try {
            System.out.println("\n===== CONFIRMED ASSESSMENTS =====");

            int availableCount = 0;

            for (int i = 0; i < DataStore.assessmentCount; i++) {

                TaxAssessment assessment =
                        DataStore.taxAssessments[i];

                if (assessment == null
                        || assessment.getDeclaration() == null
                        || assessment.getDeclaration().getObligation() == null
                        || assessment.getDeclaration().getObligation()
                                .getTaxpayer() == null) {
                    continue;
                }

                if ("CONFIRMED".equalsIgnoreCase(
                        assessment.getStatus())) {

                    System.out.println(
                        "Assessment ID: " + assessment.getAssessmentId()
                        + " | Taxpayer TIN: "
                        + assessment.getDeclaration().getObligation()
                                .getTaxpayer().getTin()
                    );

                    availableCount++;
                }
            }

            if (availableCount == 0) {
                System.out.println(
                    "No confirmed assessments available!");
                return;
            }

            // Select assessment
            System.out.print("\nEnter Assessment ID: ");
            String assessmentId = scanner.nextLine().trim();

            TaxAssessment selectedAssessment =
                    findConfirmedAssessmentById(assessmentId);

            if (selectedAssessment == null) {
                System.out.println(
                    "Confirmed assessment not found!");
                return;
            }

            RegisteredTaxpayer taxpayer = selectedAssessment
                    .getDeclaration()
                    .getObligation()
                    .getTaxpayer();

            TaxObligation selectedObligation = selectedAssessment
                    .getDeclaration()
                    .getObligation();

            // Calculate assessment total
            BigDecimal assessmentTotal =
                    selectedAssessment.getAssessedTax()
                    .add(selectedAssessment.getAdjustmentAmount());

            // Add penalties
            BigDecimal totalPenalties = BigDecimal.ZERO;

            for (int i = 0; i < DataStore.penaltyCount; i++) {

                Penalty penalty = DataStore.penalties[i];

                if (penalty == null || penalty.getObligation() == null) {
                    continue;
                }

                if (penalty.getObligation().getObligationId()
                        .equalsIgnoreCase(
                                selectedObligation.getObligationId())) {

                    totalPenalties = totalPenalties.add(
                            penalty.getPenaltyAmount());
                }
            }

            BigDecimal totalAmountDue =
                    assessmentTotal.add(totalPenalties);

            // Calculate payments
            BigDecimal totalPaid =
                    calculatePayments(selectedAssessment);

            // Calculate overpayment
            BigDecimal overpayment =
                    totalPaid.subtract(totalAmountDue);

            System.out.println("\n===== REFUND ELIGIBILITY =====");
            System.out.println("Taxpayer TIN: " + taxpayer.getTin());
            System.out.println("Assessment Total: " + assessmentTotal);
            System.out.println("Total Penalties: " + totalPenalties);
            System.out.println("Total Amount Due: " + totalAmountDue);
            System.out.println("Total Paid: " + totalPaid);
            System.out.println("Overpayment: " + overpayment);

            if (overpayment.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println(
                    "No overpayment exists. Refund request rejected.");
                return;
            }

            // Check duplicate pending refund
            for (int i = 0; i < DataStore.refundCount; i++) {

                TaxRefund existing = DataStore.taxRefunds[i];

                if (existing == null || existing.getAssessment() == null) {
                    continue;
                }

                if (existing.getAssessment().getAssessmentId()
                        .equalsIgnoreCase(
                                selectedAssessment.getAssessmentId())
                        && "PENDING".equalsIgnoreCase(
                                existing.getStatus())) {

                    System.out.println(
                        "A pending refund request already exists for this assessment!");
                    return;
                }
            }

            // Enter unique refund ID
            String refundId;

            while (true) {

                System.out.print("\nEnter Refund ID: ");
                refundId = scanner.nextLine().trim();

                if (!refundId.matches("[A-Za-z0-9-]{2,30}")) {
                    System.out.println(
                        "Refund ID must contain 2-30 letters, digits, or hyphens.");
                    continue;
                }

                if (refundIdExists(refundId)) {
                    System.out.println("Refund ID already exists!");
                    continue;
                }

                break;
            }

            // Requested amount
            BigDecimal requestedAmount;

            while (true) {

                try {
                    System.out.print(
                        "Enter requested refund amount: ");

                    requestedAmount = new BigDecimal(
                            scanner.nextLine().trim());

                    if (requestedAmount.compareTo(BigDecimal.ZERO) <= 0
                            || requestedAmount.compareTo(overpayment) > 0
                            || requestedAmount.compareTo(MAX_AMOUNT) > 0
                            || requestedAmount.stripTrailingZeros()
                                    .scale() > 2) {

                        System.out.println(
                            "Amount must be positive, have at most "
                            + "2 decimal places, and not exceed "
                            + overpayment
                        );
                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {
                    System.out.println(
                        "Enter a valid monetary amount!");
                }
            }

            // Request date
            LocalDate requestDate = readDate(
                    scanner, "Enter refund request date");

            if (requestDate.isAfter(LocalDate.now())) {
                System.out.println(
                    "Request date cannot be in the future!");
                return;
            }

            // Review
            System.out.println("\n===== REFUND REQUEST REVIEW =====");
            System.out.println("Refund ID: " + refundId);
            System.out.println("Taxpayer TIN: " + taxpayer.getTin());
            System.out.println(
                "Assessment ID: "
                + selectedAssessment.getAssessmentId());
            System.out.println("Overpayment: " + overpayment);
            System.out.println("Requested Amount: " + requestedAmount);
            System.out.println("Request Date: " + requestDate);

            System.out.print(
                "\nSave refund request? (YES/NO): ");

            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("YES")) {
                System.out.println(
                    "Refund request cancelled. Nothing was saved.");
                return;
            }

            // Create pending refund
            TaxRefund refund = new TaxRefund(
                selectedAssessment,
                refundId,
                requestDate,
                requestedAmount,
                "PENDING",
                taxpayer
            );

            refund.validate();

            // Recheck before saving
            if (DataStore.refundCount >= DataStore.taxRefunds.length
                    || refundIdExists(refundId)) {
                System.out.println(
                    "Refund storage full or duplicate ID. Not saved.");
                return;
            }

            refund.validate();

            DataStore.taxRefunds[DataStore.refundCount] = refund;
            DataStore.refundCount++;

            System.out.println(
                "Refund request successfully saved!");

            // Ask whether to approve now
            System.out.print(
                "\nApprove this refund now? (YES/NO): ");

            String approveChoice =
                    scanner.nextLine().trim();

            if (!approveChoice.equalsIgnoreCase("YES")) {
                System.out.println("Refund remains PENDING.");
                return;
            }

            // Display active officers
            System.out.println(
                "\n===== ACTIVE REVENUE OFFICERS =====");

            int activeCount = 0;

            for (int i = 0; i < DataStore.officerCount; i++) {

                RevenueOfficer officer =
                        DataStore.revenueOfficers[i];

                if (officer != null && officer.isActive()) {

                    System.out.println(
                        "Staff Number: " + officer.getStaffNumber()
                        + " | Name: " + officer.getDisplayName()
                        + " | Approval Limit: "
                        + officer.getApprovalLimit()
                    );

                    activeCount++;
                }
            }

            if (activeCount == 0) {
                System.out.println(
                    "No active Revenue Officers available.");
                System.out.println("Refund remains PENDING.");
                return;
            }

            // Select officer
            System.out.print(
                "\nEnter approving officer Staff Number: ");

            String staffNumber = scanner.nextLine().trim();

            RevenueOfficer selectedOfficer =
                    findActiveOfficer(staffNumber);

            if (selectedOfficer == null) {
                System.out.println(
                    "Active Revenue Officer not found.");
                System.out.println("Refund remains PENDING.");
                return;
            }

            // Verify approval limit
            if (selectedOfficer.getApprovalLimit() == null
                    || requestedAmount.compareTo(
                            selectedOfficer.getApprovalLimit()) > 0) {

                System.out.println(
                    "Approval denied! Amount exceeds officer's approval limit.");
                System.out.println("Refund remains PENDING.");
                return;
            }

            // Final approval review
            System.out.println("\n===== APPROVAL REVIEW =====");
            System.out.println("Refund ID: " + refundId);
            System.out.println(
                "Requested Amount: " + requestedAmount);
            System.out.println(
                "Officer: " + selectedOfficer.getStaffNumber());
            System.out.println(
                "Officer Approval Limit: "
                + selectedOfficer.getApprovalLimit());

            System.out.print(
                "\nConfirm refund approval? (YES/NO): ");

            String finalApproval =
                    scanner.nextLine().trim();

            if (finalApproval.equalsIgnoreCase("YES")) {

                // Recheck officer status and limit
                if (!selectedOfficer.isActive()
                        || requestedAmount.compareTo(
                                selectedOfficer.getApprovalLimit()) > 0) {

                    System.out.println(
                        "Approval conditions are no longer satisfied.");
                    System.out.println("Refund remains PENDING.");
                    return;
                }

                refund.setStatus("APPROVED");

                System.out.println(
                    "Refund successfully APPROVED!");

            } else {
                System.out.println(
                    "Approval cancelled. Refund remains PENDING.");
            }

        } catch (ValidationException e) {

            System.out.println(
                "Refund validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                "Unable to process refund: " + e.getMessage());
        }
    }

    // =========================================================
    // HELPER METHODS
    // =========================================================

    private static RegisteredTaxpayer findTaxpayerByTin(String tin) {

        for (int i = 0; i < DataStore.individualCount; i++) {

            if (DataStore.individualTaxpayers[i] != null
                    && DataStore.individualTaxpayers[i].getTin()
                            .equalsIgnoreCase(tin)) {

                return DataStore.individualTaxpayers[i];
            }
        }

        for (int i = 0; i < DataStore.corporateCount; i++) {

            if (DataStore.corporateTaxpayers[i] != null
                    && DataStore.corporateTaxpayers[i].getTin()
                            .equalsIgnoreCase(tin)) {

                return DataStore.corporateTaxpayers[i];
            }
        }

        return null;
    }

    private static TaxDeclaration findDeclarationByObligation(
            String obligationId) {

        for (int i = 0; i < DataStore.declarationCount; i++) {

            TaxDeclaration declaration =
                    DataStore.taxDeclarations[i];

            if (declaration == null || declaration.getObligation() == null) {
                continue;
            }

            if (declaration.getObligation().getObligationId()
                    .equalsIgnoreCase(obligationId)) {

                return declaration;
            }
        }

        return null;
    }

    private static TaxAssessment findConfirmedAssessment(
            TaxDeclaration declaration) {

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment assessment =
                    DataStore.taxAssessments[i];

            if (assessment == null || assessment.getDeclaration() == null) {
                continue;
            }

            if (assessment.getDeclaration().getDeclarationId()
                    .equalsIgnoreCase(declaration.getDeclarationId())
                    && "CONFIRMED".equalsIgnoreCase(
                            assessment.getStatus())) {

                return assessment;
            }
        }

        return null;
    }

    private static TaxAssessment findConfirmedAssessmentById(
            String assessmentId) {

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment assessment =
                    DataStore.taxAssessments[i];

            if (assessment == null) {
                continue;
            }

            if (assessment.getAssessmentId()
                    .equalsIgnoreCase(assessmentId)
                    && "CONFIRMED".equalsIgnoreCase(
                            assessment.getStatus())) {

                return assessment;
            }
        }

        return null;
    }

    private static BigDecimal calculatePayments(
            TaxAssessment assessment) {

        BigDecimal totalPaid = BigDecimal.ZERO;

        for (int i = 0; i < DataStore.paymentCount; i++) {

            TaxPayment payment = DataStore.taxPayments[i];

            if (payment == null || payment.getAssessment() == null) {
                continue;
            }

            if (payment.getAssessment().getAssessmentId()
                    .equalsIgnoreCase(assessment.getAssessmentId())) {

                totalPaid = totalPaid.add(payment.getAmount());
            }
        }

        return totalPaid;
    }

    private static RevenueOfficer findActiveOfficer(
            String staffNumber) {

        for (int i = 0; i < DataStore.officerCount; i++) {

            RevenueOfficer officer =
                    DataStore.revenueOfficers[i];

            if (officer != null
                    && officer.getStaffNumber() != null
                    && officer.getStaffNumber()
                            .equalsIgnoreCase(staffNumber)
                    && officer.isActive()) {

                return officer;
            }
        }

        return null;
    }

    private static boolean clearanceIdExists(String clearanceId) {

        for (int i = 0; i < DataStore.clearanceCount; i++) {

            TaxClearance clearance =
                    DataStore.taxClearances[i];

            if (clearance != null
                    && clearance.getClearanceId()
                            .equalsIgnoreCase(clearanceId)) {

                return true;
            }
        }

        return false;
    }

    private static boolean refundIdExists(String refundId) {

        for (int i = 0; i < DataStore.refundCount; i++) {

            TaxRefund refund = DataStore.taxRefunds[i];

            if (refund != null
                    && refund.getRefundId()
                            .equalsIgnoreCase(refundId)) {

                return true;
            }
        }

        return false;
    }
}
