
package com.auca.methods;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.penalty.Penalty;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxdeclaration.TaxDeclaration;
import com.auca.taxobligation.TaxObligation;
import com.auca.taxpayment.TaxPayment;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class CalculateMethods {

    // =====================================================
    // 1. CALCULATE AND RECORD PENALTY
    // =====================================================

    public static void calculateAndRecordPenalty(Scanner scanner) {

        // STEP 1: CHECK STORAGE CAPACITY

        if (DataStore.penaltyCount >= DataStore.penalties.length) {
            System.out.println("Penalty storage is full!");
            return;
        }

        if (DataStore.obligationCount == 0) {
            System.out.println("No tax obligations registered!");
            return;
        }

        // STEP 2: DISPLAY REGISTERED OBLIGATIONS

        System.out.println("\n===== REGISTERED TAX OBLIGATIONS =====");

        for (int i = 0; i < DataStore.obligationCount; i++) {

            TaxObligation obligation = DataStore.taxObligations[i];

            if (obligation == null) {
                continue;
            }

            System.out.println(
                    (i + 1) + ". Obligation ID: "
                    + obligation.getObligationId()
                    + " | Taxpayer TIN: "
                    + obligation.getTaxpayer().getTin()
                    + " | Tax Type: "
                    + obligation.getTaxType().getTaxName()
                    + " | Due Date: "
                    + obligation.getDueDate()
            );
        }

        // STEP 3: SELECT OBLIGATION

        int choice;

        while (true) {

            try {

                System.out.print("Select obligation number: ");

                choice = Integer.parseInt(
                        scanner.nextLine().trim());

                if (choice < 1
                        || choice > DataStore.obligationCount) {

                    System.out.println("Invalid selection!");
                    continue;
                }

                if (DataStore.taxObligations[choice - 1] == null) {
                    System.out.println("Obligation not found!");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }

        TaxObligation selectedObligation =
                DataStore.taxObligations[choice - 1];

        // STEP 4: FIND DECLARATION FOR SELECTED OBLIGATION

        TaxDeclaration selectedDeclaration = null;

        for (int i = 0; i < DataStore.declarationCount; i++) {

            TaxDeclaration declaration =
                    DataStore.taxDeclarations[i];

            if (declaration == null
                    || declaration.getObligation() == null) {
                continue;
            }

            if (declaration.getObligation()
                    .getObligationId()
                    .equalsIgnoreCase(
                            selectedObligation.getObligationId())) {

                selectedDeclaration = declaration;
                break;
            }
        }

        if (selectedDeclaration == null) {
            System.out.println(
                    "No tax declaration exists for this obligation.");
            return;
        }

        // STEP 5: CHECK LATE FILING

        if (selectedDeclaration.getFilingDate() == null
                || !selectedDeclaration.getFilingDate()
                        .isAfter(selectedObligation.getDueDate())) {

            System.out.println(
                    "This declaration was not filed late.");
            System.out.println(
                    "No late-filing penalty is applicable.");
            return;
        }

        // STEP 6: CHECK DUPLICATE PENALTY

        String penaltyType = "LATE_FILING";

        for (int i = 0; i < DataStore.penaltyCount; i++) {

            Penalty existing = DataStore.penalties[i];

            if (existing == null
                    || existing.getObligation() == null) {
                continue;
            }

            if (existing.getObligation().getObligationId()
                    .equalsIgnoreCase(
                            selectedObligation.getObligationId())
                    && existing.getPenaltyType()
                            .equalsIgnoreCase(penaltyType)) {

                System.out.println(
                        "A late-filing penalty already exists for this obligation!");
                return;
            }
        }

        // STEP 7: ENTER PENALTY ID

        String penaltyId;

        while (true) {

            System.out.print("Enter Penalty ID: ");

            penaltyId = scanner.nextLine().trim();

            if (!penaltyId.matches("[A-Za-z0-9-]{2,30}")) {
                System.out.println(
                        "Penalty ID must contain 2-30 letters, numbers or hyphens.");
                continue;
            }

            boolean exists = false;

            for (int i = 0; i < DataStore.penaltyCount; i++) {

                Penalty existing = DataStore.penalties[i];

                if (existing != null
                        && existing.getPenaltyId()
                                .equalsIgnoreCase(penaltyId)) {

                    exists = true;
                    break;
                }
            }

            if (exists) {
                System.out.println("Penalty ID already exists!");
            } else {
                break;
            }
        }

        // STEP 8: ENTER BASE AMOUNT

        BigDecimal baseAmount;

        while (true) {

            try {

                System.out.print("Enter penalty base amount: ");

                baseAmount = new BigDecimal(
                        scanner.nextLine().trim());

                if (baseAmount.compareTo(BigDecimal.ZERO) <= 0
                        || baseAmount.compareTo(
                                new BigDecimal("999999999999.99")) > 0
                        || baseAmount.scale() > 2) {

                    System.out.println(
                            "Amount must be positive, have at most "
                            + "2 decimal places and not exceed "
                            + "999999999999.99.");

                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid amount! Enter a valid decimal number.");
            }
        }

        // STEP 9: ENTER PENALTY RATE

        BigDecimal penaltyRate;

        while (true) {

            try {

                System.out.print("Enter penalty rate (%): ");

                penaltyRate = new BigDecimal(
                        scanner.nextLine().trim());

                if (penaltyRate.compareTo(BigDecimal.ZERO) <= 0
                        || penaltyRate.compareTo(
                                new BigDecimal("100")) > 0
                        || penaltyRate.scale() > 2) {

                    System.out.println(
                            "Penalty rate must be greater than 0 "
                            + "and not exceed 100.");

                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid rate! Enter a valid number.");
            }
        }

        // STEP 10: CALCULATE PENALTY

        BigDecimal penaltyAmount = baseAmount
                .multiply(penaltyRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        // STEP 11: REVIEW PENALTY

        System.out.println("\n===== PENALTY REVIEW =====");

        System.out.println("Penalty ID: " + penaltyId);

        System.out.println("Obligation ID: "
                + selectedObligation.getObligationId());

        System.out.println("Taxpayer TIN: "
                + selectedObligation.getTaxpayer().getTin());

        System.out.println("Penalty Type: " + penaltyType);

        System.out.println("Base Amount: " + baseAmount);

        System.out.println("Penalty Rate: " + penaltyRate + "%");

        System.out.println("Penalty Amount: " + penaltyAmount);

        // STEP 12: CONFIRM

        System.out.print(
                "\nSave this penalty? (1=YES / 0=NO): ");

        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equals("1")
                && !confirmation.equalsIgnoreCase("YES")) {

            System.out.println("Penalty recording cancelled.");
            return;
        }

        // STEP 13: RECHECK BEFORE SAVING

        if (DataStore.penaltyCount >= DataStore.penalties.length) {
            System.out.println("Penalty storage is full!");
            return;
        }

        for (int i = 0; i < DataStore.penaltyCount; i++) {

            Penalty existing = DataStore.penalties[i];

            if (existing == null) {
                continue;
            }

            if (existing.getPenaltyId()
                    .equalsIgnoreCase(penaltyId)) {

                System.out.println("Penalty ID already exists!");
                return;
            }

            if (existing.getObligation() != null
                    && existing.getObligation().getObligationId()
                            .equalsIgnoreCase(
                                    selectedObligation.getObligationId())
                    && existing.getPenaltyType()
                            .equalsIgnoreCase(penaltyType)) {

                System.out.println(
                        "A penalty already exists for this obligation.");
                return;
            }
        }

        // STEP 14: CREATE AND SAVE PENALTY

        Penalty penalty = new Penalty(
                baseAmount,
                selectedObligation,
                penaltyAmount,
                penaltyId,
                penaltyRate,
                penaltyType
        );

        DataStore.penalties[DataStore.penaltyCount] = penalty;
        DataStore.penaltyCount++;

        System.out.println(
                "Penalty successfully recorded!");
    }

    // =====================================================
    // 2. CALCULATE TAXPAYER OUTSTANDING BALANCE
    // =====================================================

    public static BigDecimal calculateTaxpayerBalance(
            RegisteredTaxpayer taxpayer) {

        if (taxpayer == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalBalance = BigDecimal.ZERO;

        // LOOP THROUGH ALL ASSESSMENTS

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment assessment =
                    DataStore.taxAssessments[i];

            if (assessment == null) {
                continue;
            }

            if (!"CONFIRMED".equalsIgnoreCase(
                    assessment.getStatus())) {
                continue;
            }

            TaxDeclaration declaration =
                    assessment.getDeclaration();

            if (declaration == null
                    || declaration.getObligation() == null) {
                continue;
            }

            TaxObligation obligation =
                    declaration.getObligation();

            if (obligation.getTaxpayer() == null
                    || !obligation.getTaxpayer().getTin()
                            .equalsIgnoreCase(taxpayer.getTin())) {
                continue;
            }

            // CALCULATE ASSESSMENT TOTAL

            BigDecimal assessmentTotal =
                    assessment.getAssessedTax()
                            .add(assessment.getAdjustmentAmount());

            // CALCULATE PAYMENTS FOR THIS ASSESSMENT

            BigDecimal totalPayments = BigDecimal.ZERO;

            for (int j = 0; j < DataStore.paymentCount; j++) {

                TaxPayment payment =
                        DataStore.taxPayments[j];

                if (payment == null
                        || payment.getAssessment() == null) {
                    continue;
                }

                if (payment.getAssessment().getAssessmentId()
                        .equalsIgnoreCase(
                                assessment.getAssessmentId())) {

                    totalPayments = totalPayments.add(
                            payment.getAmount());
                }
            }

            // CALCULATE PENALTIES FOR THIS OBLIGATION ONLY ONCE

            BigDecimal penaltyTotal = BigDecimal.ZERO;

            boolean penaltyAlreadyCounted = false;

            for (int j = 0; j < i; j++) {

                TaxAssessment previousAssessment =
                        DataStore.taxAssessments[j];

                if (previousAssessment == null
                        || !"CONFIRMED".equalsIgnoreCase(
                                previousAssessment.getStatus())
                        || previousAssessment.getDeclaration() == null
                        || previousAssessment.getDeclaration()
                                .getObligation() == null) {
                    continue;
                }

                String previousObligationId =
                        previousAssessment.getDeclaration()
                                .getObligation().getObligationId();

                if (previousObligationId.equalsIgnoreCase(
                        obligation.getObligationId())) {

                    penaltyAlreadyCounted = true;
                    break;
                }
            }

            if (!penaltyAlreadyCounted) {

                for (int j = 0; j < DataStore.penaltyCount; j++) {

                    Penalty penalty =
                            DataStore.penalties[j];

                    if (penalty == null
                            || penalty.getObligation() == null) {
                        continue;
                    }

                    if (penalty.getObligation().getObligationId()
                            .equalsIgnoreCase(
                                    obligation.getObligationId())) {

                        penaltyTotal = penaltyTotal.add(
                                penalty.getPenaltyAmount());
                    }
                }
            }

            // CALCULATE OUTSTANDING BALANCE

            BigDecimal balance = assessmentTotal
                    .add(penaltyTotal)
                    .subtract(totalPayments);

            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                totalBalance = totalBalance.add(balance);
            }
        }

        return totalBalance;
    }
}
