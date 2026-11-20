
package com.auca.methods;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.corporatetaxpayer.CorporateTaxpayer;
import com.auca.individualtaxpayer.IndividualTaxpayer;
import com.auca.penalty.Penalty;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxclearance.TaxClearance;
import com.auca.taxdeclaration.TaxDeclaration;
import com.auca.taxobligation.TaxObligation;
import com.auca.taxpayment.TaxPayment;
import com.auca.taxrefund.TaxRefund;
import java.math.BigDecimal;
import java.util.Scanner;

public class ViewRecordsMethods {

    // =========================================
    // VIEW ALL SYSTEM RECORDS
    // =========================================

    public static void viewAllRecords() {

        System.out.println("\n========== ALL SYSTEM RECORDS ==========");

        // INDIVIDUAL TAXPAYERS
        System.out.println("\n--- INDIVIDUAL TAXPAYERS ---");

        if (DataStore.individualCount == 0) {
            System.out.println("No individual taxpayers found.");
        }

        for (int i = 0; i < DataStore.individualCount; i++) {

            IndividualTaxpayer taxpayer =
                    DataStore.individualTaxpayers[i];

            if (taxpayer == null) {
                continue;
            }

            System.out.println("Name: " + taxpayer.getDisplayName());
            System.out.println("TIN: " + taxpayer.getTin());
            System.out.println("National ID: " + taxpayer.getNationalId());
            System.out.println("Status: " + taxpayer.getTaxpayerStatus());
            System.out.println("--------------------------------");
        }

        // CORPORATE TAXPAYERS
        System.out.println("\n--- CORPORATE TAXPAYERS ---");

        if (DataStore.corporateCount == 0) {
            System.out.println("No corporate taxpayers found.");
        }

        for (int i = 0; i < DataStore.corporateCount; i++) {

            CorporateTaxpayer taxpayer =
                    DataStore.corporateTaxpayers[i];

            if (taxpayer == null) {
                continue;
            }

            System.out.println("Company: " + taxpayer.getLegalName());
            System.out.println("TIN: " + taxpayer.getTin());
            System.out.println("Registration Number: "
                    + taxpayer.getCompanyRegistrationNo());
            System.out.println("Status: " + taxpayer.getTaxpayerStatus());
            System.out.println("--------------------------------");
        }

        // REVENUE OFFICERS
        System.out.println("\n--- REVENUE OFFICERS ---");

        if (DataStore.officerCount == 0) {
            System.out.println("No revenue officers found.");
        }

        for (int i = 0; i < DataStore.officerCount; i++) {

            if (DataStore.revenueOfficers[i] == null) {
                continue;
            }

            System.out.println("Name: "
                    + DataStore.revenueOfficers[i].getDisplayName());

            System.out.println("Staff Number: "
                    + DataStore.revenueOfficers[i].getStaffNumber());

            System.out.println("Department: "
                    + DataStore.revenueOfficers[i].getDepartment());

            System.out.println("Role: "
                    + DataStore.revenueOfficers[i].getRole());

            System.out.println("Active: "
                    + DataStore.revenueOfficers[i].isActive());

            System.out.println("--------------------------------");
        }

        // TAX TYPES
        System.out.println("\n--- TAX TYPES ---");

        if (DataStore.taxTypeCount == 0) {
            System.out.println("No tax types found.");
        }

        for (int i = 0; i < DataStore.taxTypeCount; i++) {

            if (DataStore.taxTypes[i] == null) {
                continue;
            }

            System.out.println("Tax ID: "
                    + DataStore.taxTypes[i].getTaxTypeId());

            System.out.println("Tax Code: "
                    + DataStore.taxTypes[i].getTaxCode());

            System.out.println("Tax Name: "
                    + DataStore.taxTypes[i].getTaxName());

            System.out.println("Rate: "
                    + DataStore.taxTypes[i].getRate() + "%");

            System.out.println("Filing Frequency: "
                    + DataStore.taxTypes[i].getFilingFrequency());

            System.out.println("Active: "
                    + DataStore.taxTypes[i].isActive());

            System.out.println("--------------------------------");
        }

        // TAX OBLIGATIONS
        System.out.println("\n--- TAX OBLIGATIONS ---");

        if (DataStore.obligationCount == 0) {
            System.out.println("No tax obligations found.");
        }

        for (int i = 0; i < DataStore.obligationCount; i++) {

            TaxObligation obligation = DataStore.taxObligations[i];

            if (obligation == null) {
                continue;
            }

            System.out.println("Obligation ID: "
                    + obligation.getObligationId());

            System.out.println("Taxpayer: "
                    + (obligation.getTaxpayer() == null
                    ? "N/A" : obligation.getTaxpayer().getDisplayName()));

            System.out.println("Taxpayer TIN: "
                    + (obligation.getTaxpayer() == null
                    ? "N/A" : obligation.getTaxpayer().getTin()));

            System.out.println("Tax Type: "
                    + (obligation.getTaxType() == null
                    ? "N/A" : obligation.getTaxType().getTaxName()));

            System.out.println("Period: "
                    + obligation.getPeriodStart()
                    + " to " + obligation.getPeriodEnd());

            System.out.println("Due Date: " + obligation.getDueDate());
            System.out.println("--------------------------------");
        }

        // TAX DECLARATIONS
        System.out.println("\n--- TAX DECLARATIONS ---");

        if (DataStore.declarationCount == 0) {
            System.out.println("No tax declarations found.");
        }

        for (int i = 0; i < DataStore.declarationCount; i++) {

            TaxDeclaration declaration = DataStore.taxDeclarations[i];

            if (declaration == null) {
                continue;
            }

            System.out.println("Declaration ID: "
                    + declaration.getDeclarationId());

            System.out.println("Filing Date: "
                    + declaration.getFilingDate());

            System.out.println("Taxable Amount: "
                    + declaration.getTaxableAmount());

            System.out.println("Declared Tax: "
                    + declaration.getDeclaredTax());

            System.out.println("Status: " + declaration.getStatus());
            System.out.println("--------------------------------");
        }

        // TAX ASSESSMENTS
        System.out.println("\n--- TAX ASSESSMENTS ---");

        if (DataStore.assessmentCount == 0) {
            System.out.println("No tax assessments found.");
        }

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment assessment = DataStore.taxAssessments[i];

            if (assessment == null) {
                continue;
            }

            System.out.println("Assessment ID: "
                    + assessment.getAssessmentId());

            System.out.println("Assessed Tax: "
                    + assessment.getAssessedTax());

            System.out.println("Adjustment: "
                    + assessment.getAdjustmentAmount());

            System.out.println("Total: "
                    + assessment.getAssessmentTotal());

            System.out.println("Assessment Date: "
                    + assessment.getAssessmentDate());

            System.out.println("Status: " + assessment.getStatus());
            System.out.println("--------------------------------");
        }

        // TAX PAYMENTS
        System.out.println("\n--- TAX PAYMENTS ---");

        if (DataStore.paymentCount == 0) {
            System.out.println("No tax payments found.");
        }

        for (int i = 0; i < DataStore.paymentCount; i++) {

            TaxPayment payment = DataStore.taxPayments[i];

            if (payment == null) {
                continue;
            }

            System.out.println("Payment ID: " + payment.getPaymentId());
            System.out.println("Reference: " + payment.getPaymentReference());
            System.out.println("Amount: " + payment.getAmount());
            System.out.println("Date: " + payment.getPaymentDate());
            System.out.println("Method: " + payment.getPaymentMethod());
            System.out.println("--------------------------------");
        }

        // PENALTIES
        System.out.println("\n--- PENALTIES ---");

        if (DataStore.penaltyCount == 0) {
            System.out.println("No penalties found.");
        }

        for (int i = 0; i < DataStore.penaltyCount; i++) {

            Penalty penalty = DataStore.penalties[i];

            if (penalty == null) {
                continue;
            }

            penalty.printDetails();
            System.out.println("--------------------------------");
        }

        // REFUNDS
        System.out.println("\n--- TAX REFUNDS ---");

        if (DataStore.refundCount == 0) {
            System.out.println("No tax refunds found.");
        }

        for (int i = 0; i < DataStore.refundCount; i++) {

            TaxRefund refund = DataStore.taxRefunds[i];

            if (refund == null) {
                continue;
            }

            refund.printDetails();
            System.out.println("--------------------------------");
        }

        // CLEARANCES
        System.out.println("\n--- TAX CLEARANCES ---");

        if (DataStore.clearanceCount == 0) {
            System.out.println("No tax clearances found.");
        }

        for (int i = 0; i < DataStore.clearanceCount; i++) {

            TaxClearance clearance = DataStore.taxClearances[i];

            if (clearance == null) {
                continue;
            }

            clearance.printDetails();
            System.out.println("--------------------------------");
        }

        System.out.println("\n========== END OF SYSTEM RECORDS ==========");
    }


    // =========================================
    // VIEW TAXPAYER STATEMENT
    // =========================================

    public static void viewTaxpayerStatement(Scanner scanner) {

        System.out.print("Enter Taxpayer TIN: ");
        String tin = scanner.nextLine().trim();

        if (tin.isEmpty()) {
            System.out.println("TIN cannot be empty.");
            return;
        }

        RegisteredTaxpayer taxpayer = findTaxpayerByTin(tin);

        if (taxpayer == null) {
            System.out.println("Taxpayer not found!");
            return;
        }

        System.out.println("\n========== TAXPAYER STATEMENT ==========");
        System.out.println("TIN: " + taxpayer.getTin());
        System.out.println("Name: " + taxpayer.getDisplayName());
        System.out.println("Email: " + taxpayer.getEmail());
        System.out.println("Status: " + taxpayer.getTaxpayerStatus());

        BigDecimal totalOutstanding = BigDecimal.ZERO;

        // TAX OBLIGATIONS
        System.out.println("\n---------- TAX OBLIGATIONS ----------");

        for (int i = 0; i < DataStore.obligationCount; i++) {

            TaxObligation obligation = DataStore.taxObligations[i];

            if (obligation == null || obligation.getTaxpayer() == null) {
                continue;
            }

            if (!obligation.getTaxpayer().getTin().equalsIgnoreCase(tin)) {
                continue;
            }

            System.out.println("\nObligation ID: "
                    + obligation.getObligationId());

            System.out.println("Tax Type: "
                    + (obligation.getTaxType() == null
                    ? "N/A" : obligation.getTaxType().getTaxName()));

            System.out.println("Period: "
                    + obligation.getPeriodStart()
                    + " to " + obligation.getPeriodEnd());

            System.out.println("Due Date: " + obligation.getDueDate());

            // DECLARATIONS
            for (int j = 0; j < DataStore.declarationCount; j++) {

                TaxDeclaration declaration = DataStore.taxDeclarations[j];

                if (declaration == null || declaration.getObligation() == null) {
                    continue;
                }

                if (!declaration.getObligation().getObligationId()
                        .equalsIgnoreCase(obligation.getObligationId())) {
                    continue;
                }

                System.out.println("\nDeclaration ID: "
                        + declaration.getDeclarationId());

                System.out.println("Filing Date: "
                        + declaration.getFilingDate());

                System.out.println("Taxable Amount: "
                        + declaration.getTaxableAmount());

                System.out.println("Declared Tax: "
                        + declaration.getDeclaredTax());

                System.out.println("Declaration Status: "
                        + declaration.getStatus());

                // ASSESSMENTS
                for (int k = 0; k < DataStore.assessmentCount; k++) {

                    TaxAssessment assessment = DataStore.taxAssessments[k];

                    if (assessment == null || assessment.getDeclaration() == null) {
                        continue;
                    }

                    if (!assessment.getDeclaration().getDeclarationId()
                            .equalsIgnoreCase(declaration.getDeclarationId())) {
                        continue;
                    }

                    System.out.println("\nAssessment ID: "
                            + assessment.getAssessmentId());

                    System.out.println("Assessed Tax: "
                            + assessment.getAssessedTax());

                    System.out.println("Adjustment: "
                            + assessment.getAdjustmentAmount());

                    BigDecimal assessmentTotal =
                            assessment.getAssessmentTotal();

                    System.out.println("Assessment Total: "
                            + assessmentTotal);

                    System.out.println("Assessment Status: "
                            + assessment.getStatus());

                    if (!"CONFIRMED".equalsIgnoreCase(assessment.getStatus())) {
                        continue;
                    }

                    BigDecimal payments = BigDecimal.ZERO;
                    BigDecimal penaltyTotal = BigDecimal.ZERO;

                    // PAYMENTS
                    for (int p = 0; p < DataStore.paymentCount; p++) {

                        TaxPayment payment = DataStore.taxPayments[p];

                        if (payment == null || payment.getAssessment() == null) {
                            continue;
                        }

                        if (payment.getAssessment().getAssessmentId()
                                .equalsIgnoreCase(assessment.getAssessmentId())) {

                            payments = payments.add(payment.getAmount());

                            System.out.println("Payment: "
                                    + payment.getAmount()
                                    + " | Reference: "
                                    + payment.getPaymentReference());
                        }
                    }

                    // PENALTIES
                    for (int p = 0; p < DataStore.penaltyCount; p++) {

                        Penalty penalty = DataStore.penalties[p];

                        if (penalty == null || penalty.getObligation() == null) {
                            continue;
                        }

                        if (penalty.getObligation().getObligationId()
                                .equalsIgnoreCase(obligation.getObligationId())) {

                            penaltyTotal = penaltyTotal.add(
                                    penalty.getPenaltyAmount());

                            System.out.println("Penalty: "
                                    + penalty.getPenaltyAmount()
                                    + " | Type: "
                                    + penalty.getPenaltyType());
                        }
                    }

                    BigDecimal balance = assessmentTotal
                            .add(penaltyTotal)
                            .subtract(payments);

                    if (balance.signum() < 0) {
                        balance = BigDecimal.ZERO;
                    }

                    totalOutstanding = totalOutstanding.add(balance);

                    System.out.println("Total Payments: " + payments);
                    System.out.println("Outstanding Balance: " + balance);
                }
            }
        }

        // REFUND REQUESTS
        System.out.println("\n---------- REFUND REQUESTS ----------");

        for (int i = 0; i < DataStore.refundCount; i++) {

            TaxRefund refund = DataStore.taxRefunds[i];

            if (refund == null || refund.getTaxpayer() == null) {
                continue;
            }

            if (refund.getTaxpayer().getTin().equalsIgnoreCase(tin)) {

                System.out.println("Refund ID: " + refund.getRefundId());
                System.out.println("Requested Amount: "
                        + refund.getRequestedAmount());
                System.out.println("Request Date: " + refund.getRequestDate());
                System.out.println("Status: " + refund.getStatus());
            }
        }

        // TAX CLEARANCES
        System.out.println("\n---------- TAX CLEARANCES ----------");

        for (int i = 0; i < DataStore.clearanceCount; i++) {

            TaxClearance clearance = DataStore.taxClearances[i];

            if (clearance == null || clearance.getTaxpayer() == null) {
                continue;
            }

            if (clearance.getTaxpayer().getTin().equalsIgnoreCase(tin)) {

                System.out.println("Clearance ID: "
                        + clearance.getClearanceId());

                System.out.println("Request Date: "
                        + clearance.getRequestDate());

                System.out.println("Expiry Date: "
                        + clearance.getExpiryDate());

                System.out.println("Status: " + clearance.getStatus());
            }
        }

        System.out.println("\n======================================");
        System.out.println("TOTAL OUTSTANDING BALANCE: " + totalOutstanding);
        System.out.println("======================================");
    }


    // =========================================
    // VIEW OUTSTANDING BALANCES
    // =========================================

    public static void viewOutstandingBalances() {

        BigDecimal grandTotal = BigDecimal.ZERO;

        System.out.println("\n========== OUTSTANDING TAX BALANCES ==========");

        // INDIVIDUAL TAXPAYERS
        for (int i = 0; i < DataStore.individualCount; i++) {

            IndividualTaxpayer taxpayer =
                    DataStore.individualTaxpayers[i];

            if (taxpayer == null) {
                continue;
            }

            BigDecimal taxpayerBalance = calculateTaxpayerBalance(taxpayer);

            if (taxpayerBalance.compareTo(BigDecimal.ZERO) > 0) {

                System.out.println("\nTIN: " + taxpayer.getTin());
                System.out.println("Name: " + taxpayer.getDisplayName());
                System.out.println("Outstanding Balance: " + taxpayerBalance);

                grandTotal = grandTotal.add(taxpayerBalance);
            }
        }

        // CORPORATE TAXPAYERS
        for (int i = 0; i < DataStore.corporateCount; i++) {

            CorporateTaxpayer taxpayer =
                    DataStore.corporateTaxpayers[i];

            if (taxpayer == null) {
                continue;
            }

            BigDecimal taxpayerBalance = calculateTaxpayerBalance(taxpayer);

            if (taxpayerBalance.compareTo(BigDecimal.ZERO) > 0) {

                System.out.println("\nTIN: " + taxpayer.getTin());
                System.out.println("Company: " + taxpayer.getLegalName());
                System.out.println("Outstanding Balance: " + taxpayerBalance);

                grandTotal = grandTotal.add(taxpayerBalance);
            }
        }

        System.out.println("\n---------------------------------------");
        System.out.println("TOTAL OUTSTANDING TAX: " + grandTotal);
        System.out.println("=======================================");
    }


    // =========================================
    // VIEW TAX REPORTS
    // =========================================

    public static void viewTaxReports() {

        BigDecimal totalAssessedTax = BigDecimal.ZERO;
        BigDecimal totalCollected = BigDecimal.ZERO;
        BigDecimal totalPenalties = BigDecimal.ZERO;
        BigDecimal totalOutstanding = BigDecimal.ZERO;
        BigDecimal totalRefunds = BigDecimal.ZERO;

        int confirmedAssessments = 0;
        int pendingAssessments = 0;
        int approvedRefunds = 0;
        int pendingRefunds = 0;

        // ASSESSED TAX
        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment assessment = DataStore.taxAssessments[i];

            if (assessment == null) {
                continue;
            }

            if ("CONFIRMED".equalsIgnoreCase(assessment.getStatus())) {

                totalAssessedTax = totalAssessedTax.add(
                        assessment.getAssessmentTotal());

                confirmedAssessments++;

            } else if ("DRAFT".equalsIgnoreCase(assessment.getStatus())) {
                pendingAssessments++;
            }
        }

        // COLLECTED TAX
        for (int i = 0; i < DataStore.paymentCount; i++) {

            TaxPayment payment = DataStore.taxPayments[i];

            if (payment != null && payment.getAmount() != null) {
                totalCollected = totalCollected.add(payment.getAmount());
            }
        }

        // PENALTIES
        for (int i = 0; i < DataStore.penaltyCount; i++) {

            Penalty penalty = DataStore.penalties[i];

            if (penalty != null && penalty.getPenaltyAmount() != null) {
                totalPenalties = totalPenalties.add(
                        penalty.getPenaltyAmount());
            }
        }

        // REFUNDS
        for (int i = 0; i < DataStore.refundCount; i++) {

            TaxRefund refund = DataStore.taxRefunds[i];

            if (refund == null) {
                continue;
            }

            if (refund.getRequestedAmount() != null) {
                totalRefunds = totalRefunds.add(refund.getRequestedAmount());
            }

            if ("APPROVED".equalsIgnoreCase(refund.getStatus())) {
                approvedRefunds++;
            } else if ("PENDING".equalsIgnoreCase(refund.getStatus())) {
                pendingRefunds++;
            }
        }

        // OUTSTANDING BALANCES
        for (int i = 0; i < DataStore.individualCount; i++) {

            IndividualTaxpayer taxpayer =
                    DataStore.individualTaxpayers[i];

            if (taxpayer != null) {
                totalOutstanding = totalOutstanding.add(
                        calculateTaxpayerBalance(taxpayer));
            }
        }

        for (int i = 0; i < DataStore.corporateCount; i++) {

            CorporateTaxpayer taxpayer =
                    DataStore.corporateTaxpayers[i];

            if (taxpayer != null) {
                totalOutstanding = totalOutstanding.add(
                        calculateTaxpayerBalance(taxpayer));
            }
        }

        // DISPLAY REPORT
        System.out.println("\n==============================================");
        System.out.println("             TAX ADMINISTRATION REPORT");
        System.out.println("==============================================");

        System.out.println("\nTAXPAYER STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Individual Taxpayers: " + DataStore.individualCount);
        System.out.println("Corporate Taxpayers: " + DataStore.corporateCount);
        System.out.println("Total Taxpayers: "
                + (DataStore.individualCount + DataStore.corporateCount));

        System.out.println("\nREGISTRATION STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Revenue Officers: " + DataStore.officerCount);
        System.out.println("Registered Tax Types: " + DataStore.taxTypeCount);
        System.out.println("Tax Obligations: " + DataStore.obligationCount);

        System.out.println("\nDECLARATION STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Declarations Filed: " + DataStore.declarationCount);

        System.out.println("\nASSESSMENT STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Confirmed Assessments: " + confirmedAssessments);
        System.out.println("Draft Assessments: " + pendingAssessments);
        System.out.println("Total Confirmed Tax Assessed: " + totalAssessedTax);

        System.out.println("\nPAYMENT STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Total Payments Recorded: " + DataStore.paymentCount);
        System.out.println("Total Tax Collected: " + totalCollected);

        System.out.println("\nPENALTY STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Total Penalties Recorded: " + DataStore.penaltyCount);
        System.out.println("Total Penalty Amount: " + totalPenalties);

        System.out.println("\nREFUND STATISTICS");
        System.out.println("----------------------------------------------");
        System.out.println("Total Refund Requests: " + DataStore.refundCount);
        System.out.println("Approved Refunds: " + approvedRefunds);
        System.out.println("Pending Refunds: " + pendingRefunds);
        System.out.println("Total Requested Refund Amount: " + totalRefunds);

        System.out.println("\nFINANCIAL SUMMARY");
        System.out.println("----------------------------------------------");
        System.out.println("Total Tax Assessed: " + totalAssessedTax);
        System.out.println("Total Tax Collected: " + totalCollected);
        System.out.println("Total Penalties: " + totalPenalties);
        System.out.println("Total Outstanding Tax: " + totalOutstanding);

        System.out.println("\n==============================================");
        System.out.println("             END OF TAX REPORT");
        System.out.println("==============================================");
    }


    // =========================================
    // CALCULATE TAXPAYER BALANCE
    // =========================================

    private static BigDecimal calculateTaxpayerBalance(
            RegisteredTaxpayer taxpayer) {

        BigDecimal totalOutstanding = BigDecimal.ZERO;

        if (taxpayer == null || taxpayer.getTin() == null) {
            return totalOutstanding;
        }

        for (int i = 0; i < DataStore.obligationCount; i++) {

            TaxObligation obligation = DataStore.taxObligations[i];

            if (obligation == null || obligation.getTaxpayer() == null) {
                continue;
            }

            if (!obligation.getTaxpayer().getTin()
                    .equalsIgnoreCase(taxpayer.getTin())) {
                continue;
            }

            BigDecimal assessedTotal = BigDecimal.ZERO;
            BigDecimal totalPaid = BigDecimal.ZERO;
            BigDecimal penaltyTotal = BigDecimal.ZERO;

            // CONFIRMED ASSESSMENTS
            for (int j = 0; j < DataStore.assessmentCount; j++) {

                TaxAssessment assessment = DataStore.taxAssessments[j];

                if (assessment == null
                        || assessment.getDeclaration() == null
                        || assessment.getDeclaration().getObligation() == null) {
                    continue;
                }

                if (!"CONFIRMED".equalsIgnoreCase(assessment.getStatus())) {
                    continue;
                }

                if (!assessment.getDeclaration().getObligation()
                        .getObligationId()
                        .equalsIgnoreCase(obligation.getObligationId())) {
                    continue;
                }

                assessedTotal = assessedTotal.add(
                        assessment.getAssessmentTotal());

                // PAYMENTS FOR THIS ASSESSMENT
                for (int k = 0; k < DataStore.paymentCount; k++) {

                    TaxPayment payment = DataStore.taxPayments[k];

                    if (payment == null || payment.getAssessment() == null) {
                        continue;
                    }

                    if (payment.getAssessment().getAssessmentId()
                            .equalsIgnoreCase(assessment.getAssessmentId())) {

                        totalPaid = totalPaid.add(payment.getAmount());
                    }
                }
            }

            // PENALTIES FOR THIS OBLIGATION
            for (int j = 0; j < DataStore.penaltyCount; j++) {

                Penalty penalty = DataStore.penalties[j];

                if (penalty == null || penalty.getObligation() == null) {
                    continue;
                }

                if (penalty.getObligation().getObligationId()
                        .equalsIgnoreCase(obligation.getObligationId())) {

                    penaltyTotal = penaltyTotal.add(
                            penalty.getPenaltyAmount());
                }
            }

            BigDecimal balance = assessedTotal
                    .add(penaltyTotal)
                    .subtract(totalPaid);

            if (balance.signum() > 0) {
                totalOutstanding = totalOutstanding.add(balance);
            }
        }

        return totalOutstanding;
    }


    // =========================================
    // FIND TAXPAYER BY TIN
    // =========================================

    private static RegisteredTaxpayer findTaxpayerByTin(String tin) {

        for (int i = 0; i < DataStore.individualCount; i++) {

            IndividualTaxpayer taxpayer =
                    DataStore.individualTaxpayers[i];

            if (taxpayer != null
                    && taxpayer.getTin().equalsIgnoreCase(tin)) {
                return taxpayer;
            }
        }

        for (int i = 0; i < DataStore.corporateCount; i++) {

            CorporateTaxpayer taxpayer =
                    DataStore.corporateTaxpayers[i];

            if (taxpayer != null
                    && taxpayer.getTin().equalsIgnoreCase(tin)) {
                return taxpayer;
            }
        }

        return null;
    }
}
