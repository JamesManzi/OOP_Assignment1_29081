
package com.auca.methods;

import com.auca.exceptions.ValidationException;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxpayment.TaxPayment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PaymentMethods {

    // =====================================================
    // 1. READ PAYMENT DATE
    // =====================================================

    public static LocalDate readDate(Scanner scanner, String message) {

        while (true) {

            try {

                System.out.print(message + " (YYYY-MM-DD): ");

                String input = scanner.nextLine().trim();

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date! Please use YYYY-MM-DD.");
            }
        }
    }

    // =====================================================
    // 2. RECORD TAX PAYMENT
    // =====================================================

    public static void recordTaxPayment(Scanner scanner) {

        if (DataStore.paymentCount >= DataStore.taxPayments.length) {
            System.out.println("Payment storage is full!");
            return;
        }

        try {

            System.out.println("\n===== RECORD TAX PAYMENT =====");

            // STEP 1: PAYMENT ID

            String paymentId;

            while (true) {

                System.out.print("Enter Payment ID: ");

                paymentId = scanner.nextLine().trim();

                if (!paymentId.matches("[A-Za-z0-9-]{2,30}")) {

                    System.out.println(
                            "Payment ID must contain 2-30 letters, "
                            + "numbers or hyphens.");

                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.paymentCount; i++) {

                    TaxPayment existing =
                            DataStore.taxPayments[i];

                    if (existing != null
                            && existing.getPaymentId()
                                    .equalsIgnoreCase(paymentId)) {

                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "Payment ID already exists.");
                } else {
                    break;
                }
            }

            // STEP 2: PAYMENT REFERENCE

            String paymentReference;

            while (true) {

                System.out.print("Enter Payment Reference: ");

                paymentReference = scanner.nextLine().trim();

                if (paymentReference.length() < 2
                        || paymentReference.length() > 60) {

                    System.out.println(
                            "Payment reference must contain "
                            + "2-60 characters.");

                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.paymentCount; i++) {

                    TaxPayment existing =
                            DataStore.taxPayments[i];

                    if (existing != null
                            && existing.getPaymentReference()
                                    .equalsIgnoreCase(paymentReference)) {

                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "Payment reference already exists.");
                } else {
                    break;
                }
            }

            // STEP 3: CHECK AVAILABLE ASSESSMENTS

            if (DataStore.assessmentCount == 0) {

                System.out.println(
                        "No assessments available.");

                return;
            }

            System.out.println(
                    "\n===== CONFIRMED ASSESSMENTS =====");

            int availableCount = 0;

            for (int i = 0;
                    i < DataStore.assessmentCount; i++) {

                TaxAssessment assessment =
                        DataStore.taxAssessments[i];

                if (assessment == null) {
                    continue;
                }

                if (!"CONFIRMED".equalsIgnoreCase(
                        assessment.getStatus())) {
                    continue;
                }

                BigDecimal total =
                        assessment.getAssessedTax()
                                .add(assessment.getAdjustmentAmount());

                BigDecimal paid = BigDecimal.ZERO;

                for (int j = 0;
                        j < DataStore.paymentCount; j++) {

                    TaxPayment existingPayment =
                            DataStore.taxPayments[j];

                    if (existingPayment == null
                            || existingPayment.getAssessment() == null) {
                        continue;
                    }

                    if (existingPayment.getAssessment()
                            .getAssessmentId()
                            .equalsIgnoreCase(
                                    assessment.getAssessmentId())) {

                        paid = paid.add(
                                existingPayment.getAmount());
                    }
                }

                BigDecimal balance = total.subtract(paid);

                if (balance.signum() > 0) {

                    availableCount++;

                    System.out.println(
                            "Assessment ID: "
                            + assessment.getAssessmentId());

                    System.out.println(
                            "Outstanding Balance: " + balance);

                    System.out.println(
                            "Declaration ID: "
                            + assessment.getDeclaration()
                                    .getDeclarationId());

                    System.out.println(
                            "------------------------------------------------");
                }
            }

            if (availableCount == 0) {

                System.out.println(
                        "No confirmed assessments with outstanding balances.");

                return;
            }

            // STEP 4: SELECT ASSESSMENT

            TaxAssessment selectedAssessment = null;

            while (selectedAssessment == null) {

                System.out.print("Enter Assessment ID: ");

                String selectedId =
                        scanner.nextLine().trim();

                for (int i = 0;
                        i < DataStore.assessmentCount; i++) {

                    TaxAssessment assessment =
                            DataStore.taxAssessments[i];

                    if (assessment != null
                            && assessment.getAssessmentId()
                                    .equalsIgnoreCase(selectedId)) {

                        selectedAssessment = assessment;
                        break;
                    }
                }

                if (selectedAssessment == null) {

                    System.out.println(
                            "Assessment not found.");

                    continue;
                }

                if (!"CONFIRMED".equalsIgnoreCase(
                        selectedAssessment.getStatus())) {

                    System.out.println(
                            "Only confirmed assessments accept payments.");

                    selectedAssessment = null;
                    continue;
                }

                // CALCULATE OUTSTANDING BALANCE

                BigDecimal assessmentTotal =
                        selectedAssessment.getAssessedTax()
                                .add(selectedAssessment.getAdjustmentAmount());

                BigDecimal totalPaid = BigDecimal.ZERO;

                for (int i = 0;
                        i < DataStore.paymentCount; i++) {

                    TaxPayment existingPayment =
                            DataStore.taxPayments[i];

                    if (existingPayment == null
                            || existingPayment.getAssessment() == null) {
                        continue;
                    }

                    if (existingPayment.getAssessment()
                            .getAssessmentId()
                            .equalsIgnoreCase(
                                    selectedAssessment.getAssessmentId())) {

                        totalPaid = totalPaid.add(
                                existingPayment.getAmount());
                    }
                }

                BigDecimal balance =
                        assessmentTotal.subtract(totalPaid);

                if (balance.signum() <= 0) {

                    System.out.println(
                            "This assessment has already been fully paid.");

                    selectedAssessment = null;
                }
            }

            // STEP 5: CALCULATE OUTSTANDING BALANCE

            BigDecimal assessmentTotal =
                    selectedAssessment.getAssessedTax()
                            .add(selectedAssessment.getAdjustmentAmount());

            BigDecimal totalPaid = BigDecimal.ZERO;

            for (int i = 0;
                    i < DataStore.paymentCount; i++) {

                TaxPayment existingPayment =
                        DataStore.taxPayments[i];

                if (existingPayment == null
                        || existingPayment.getAssessment() == null) {
                    continue;
                }

                if (existingPayment.getAssessment()
                        .getAssessmentId()
                        .equalsIgnoreCase(
                                selectedAssessment.getAssessmentId())) {

                    totalPaid = totalPaid.add(
                            existingPayment.getAmount());
                }
            }

            BigDecimal outstandingBalance =
                    assessmentTotal.subtract(totalPaid);

            // STEP 6: ENTER PAYMENT AMOUNT

            BigDecimal amount;

            while (true) {

                System.out.println(
                        "Outstanding Balance: "
                        + outstandingBalance);

                System.out.print("Enter Payment Amount: ");

                try {

                    amount = new BigDecimal(
                            scanner.nextLine().trim());

                    if (amount.signum() <= 0) {

                        System.out.println(
                                "Payment must be greater than zero.");

                        continue;
                    }

                    if (amount.stripTrailingZeros().scale() > 2
                            || amount.compareTo(
                                    new BigDecimal("999999999999.99")) > 0) {

                        System.out.println(
                                "Payment must have at most 2 decimal "
                                + "places and cannot exceed "
                                + "999999999999.99.");

                        continue;
                    }

                    if (amount.compareTo(
                            outstandingBalance) > 0) {

                        System.out.println(
                                "Payment cannot exceed outstanding balance.");

                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Enter a valid monetary amount.");
                }
            }

            // STEP 7: PAYMENT DATE

            LocalDate paymentDate = readDate(
                    scanner, "Enter Payment Date");

            if (paymentDate.isAfter(LocalDate.now())) {

                System.out.println(
                        "Payment date cannot be in the future.");

                return;
            }

            // STEP 8: PAYMENT METHOD

            String paymentMethod;

            while (true) {

                System.out.println(
                        "\nSelect Payment Method:");

                System.out.println("1. Mobile Money");
                System.out.println("2. Bank Transfer");
                System.out.println("3. Cash");
                System.out.println("4. Card");

                System.out.print("Choose method: ");

                String choice =
                        scanner.nextLine().trim();

                switch (choice) {

                    case "1":
                        paymentMethod = "Mobile Money";
                        break;

                    case "2":
                        paymentMethod = "Bank Transfer";
                        break;

                    case "3":
                        paymentMethod = "Cash";
                        break;

                    case "4":
                        paymentMethod = "Card";
                        break;

                    default:
                        System.out.println(
                                "Invalid payment method.");

                        continue;
                }

                break;
            }

            // STEP 9: REVIEW PAYMENT

            System.out.println(
                    "\n===== PAYMENT REVIEW =====");

            System.out.println(
                    "Payment ID: " + paymentId);

            System.out.println(
                    "Payment Reference: " + paymentReference);

            System.out.println(
                    "Assessment ID: "
                    + selectedAssessment.getAssessmentId());

            System.out.println(
                    "Payment Amount: " + amount);

            System.out.println(
                    "Payment Date: " + paymentDate);

            System.out.println(
                    "Payment Method: " + paymentMethod);

            System.out.println(
                    "Previous Balance: " + outstandingBalance);

            System.out.println(
                    "Remaining Balance: "
                    + outstandingBalance.subtract(amount));

            System.out.print(
                    "\nConfirm payment? (1=YES / 0=NO): ");

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equals("1")
                    && !confirmation.equalsIgnoreCase("YES")) {

                System.out.println(
                        "Payment cancelled. Nothing was saved.");

                return;
            }

            // STEP 10: RECHECK BEFORE SAVING

            if (DataStore.paymentCount
                    >= DataStore.taxPayments.length) {

                System.out.println(
                        "Payment storage is full!");

                return;
            }

            for (int i = 0;
                    i < DataStore.paymentCount; i++) {

                TaxPayment existing =
                        DataStore.taxPayments[i];

                if (existing == null) {
                    continue;
                }

                if (existing.getPaymentId()
                        .equalsIgnoreCase(paymentId)) {

                    System.out.println(
                            "Payment ID already exists.");

                    return;
                }

                if (existing.getPaymentReference()
                        .equalsIgnoreCase(paymentReference)) {

                    System.out.println(
                            "Payment reference already exists.");

                    return;
                }
            }

            // STEP 11: CREATE AND VALIDATE PAYMENT

            TaxPayment payment = new TaxPayment(
                    amount,
                    selectedAssessment,
                    paymentDate,
                    paymentId,
                    paymentMethod,
                    paymentReference
            );

            payment.validate();

            // STEP 12: SAVE TO SHARED DATASTORE

            DataStore.taxPayments[DataStore.paymentCount] =
                    payment;

            DataStore.paymentCount++;

            System.out.println(
                    "Payment recorded successfully!");

        } catch (ValidationException e) {

            System.out.println(
                    "Payment validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Error recording payment: " + e.getMessage());
        }
    }
}
