
package com.auca.methods;

import com.auca.exceptions.ValidationException;
import com.auca.taxdeclaration.TaxDeclaration;
import com.auca.taxobligation.TaxObligation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TaxDeclarationMethods {

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

    public static void fileTaxDeclaration(Scanner scanner) {

        if (DataStore.declarationCount >= DataStore.taxDeclarations.length) {
            System.out.println("Declaration storage is full!");
            return;
        }

        try {

            System.out.println("\n===== FILE TAX DECLARATION =====");

            // STEP 1: ENTER DECLARATION ID

            String declarationId;

            while (true) {

                System.out.print("Enter Declaration ID: ");
                declarationId = scanner.nextLine().trim();

                if (declarationId.isEmpty()) {
                    System.out.println(
                            "Declaration ID cannot be empty.");
                    continue;
                }

                if (!declarationId.matches("[A-Za-z0-9-]{2,30}")) {
                    System.out.println(
                            "ID must contain 2-30 letters, numbers or hyphens.");
                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.declarationCount; i++) {

                    TaxDeclaration existing =
                            DataStore.taxDeclarations[i];

                    if (existing != null
                            && existing.getDeclarationId()
                                    .equalsIgnoreCase(declarationId)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "Declaration ID already exists.");
                } else {
                    break;
                }
            }

            // STEP 2: SELECT TAX OBLIGATION

            if (DataStore.obligationCount == 0) {
                System.out.println(
                        "No tax obligations available.");
                return;
            }

            System.out.println(
                    "\n===== AVAILABLE TAX OBLIGATIONS =====");

            for (int i = 0; i < DataStore.obligationCount; i++) {

                TaxObligation obligation =
                        DataStore.taxObligations[i];

                if (obligation == null) {
                    continue;
                }

                System.out.println(
                        (i + 1)
                        + ". Obligation ID: "
                        + obligation.getObligationId()
                        + " | Taxpayer TIN: "
                        + obligation.getTaxpayer().getTin()
                        + " | Tax Type: "
                        + obligation.getTaxType().getTaxName());
            }

            TaxObligation selectedObligation = null;

            while (selectedObligation == null) {

                System.out.print(
                        "Select obligation number: ");

                try {

                    int choice = Integer.parseInt(
                            scanner.nextLine().trim());

                    if (choice < 1
                            || choice > DataStore.obligationCount) {

                        System.out.println(
                                "Invalid selection.");
                        continue;
                    }

                    selectedObligation =
                            DataStore.taxObligations[choice - 1];

                    if (selectedObligation == null
                            || selectedObligation.getTaxpayer() == null
                            || selectedObligation.getTaxType() == null) {

                        System.out.println(
                                "Invalid obligation records.");
                        selectedObligation = null;
                        continue;
                    }

                    if (!selectedObligation.getTaxpayer().isActive()
                            || !selectedObligation.getTaxType().isActive()) {

                        System.out.println(
                                "Taxpayer or tax type is no longer active.");
                        selectedObligation = null;
                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Enter a valid number.");
                }
            }

            // CHECK EXISTING ACTIVE DECLARATION

            for (int i = 0; i < DataStore.declarationCount; i++) {

                TaxDeclaration existing =
                        DataStore.taxDeclarations[i];

                if (existing == null
                        || existing.getObligation() == null) {
                    continue;
                }

                boolean sameObligation =
                        existing.getObligation().getObligationId()
                                .equalsIgnoreCase(
                                        selectedObligation.getObligationId());

                boolean activeDeclaration =
                        !"REJECTED".equalsIgnoreCase(existing.getStatus())
                        && !"CANCELLED".equalsIgnoreCase(
                                existing.getStatus());

                if (sameObligation && activeDeclaration) {

                    System.out.println(
                            "This obligation already has an active declaration.");
                    return;
                }
            }

            // STEP 3: ENTER DECLARATION DETAILS

            LocalDate filingDate = readDate(
                    scanner,
                    "Enter Filing Date");

            if (filingDate.isAfter(LocalDate.now())) {

                System.out.println(
                        "Filing date cannot be in the future.");
                return;
            }

            BigDecimal taxableAmount;

            while (true) {

                System.out.print(
                        "Enter Taxable Amount: ");

                try {

                    taxableAmount = new BigDecimal(
                            scanner.nextLine().trim());

                    if (taxableAmount.signum() < 0
                            || taxableAmount.compareTo(
                                    new BigDecimal("999999999999.99")) > 0
                            || taxableAmount.scale() > 2) {

                        System.out.println(
                                "Amount must be non-negative, have at most "
                                + "2 decimal places and not exceed "
                                + "999999999999.99.");
                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Enter a valid monetary amount.");
                }
            }

            BigDecimal declaredTax;

            while (true) {

                System.out.print(
                        "Enter Declared Tax: ");

                try {

                    declaredTax = new BigDecimal(
                            scanner.nextLine().trim());

                    if (declaredTax.signum() < 0
                            || declaredTax.compareTo(
                                    new BigDecimal("999999999999.99")) > 0
                            || declaredTax.scale() > 2) {

                        System.out.println(
                                "Tax must be non-negative, have at most "
                                + "2 decimal places and not exceed "
                                + "999999999999.99.");
                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Enter a valid tax amount.");
                }
            }

            // STEP 4: CALCULATE EXPECTED TAX

            BigDecimal rate =
                    selectedObligation.getTaxType().getRate();

            BigDecimal expectedTax = taxableAmount
                    .multiply(rate)
                    .divide(
                            new BigDecimal("100"),
                            2,
                            RoundingMode.HALF_UP);

            BigDecimal difference = declaredTax
                    .subtract(expectedTax)
                    .abs();

            System.out.println(
                    "\n===== TAX CALCULATION =====");

            System.out.println(
                    "Taxable Amount: " + taxableAmount);

            System.out.println(
                    "Tax Rate: " + rate + "%");

            System.out.println(
                    "Expected Tax: " + expectedTax);

            System.out.println(
                    "Declared Tax: " + declaredTax);

            System.out.println(
                    "Difference: " + difference);

            if (difference.compareTo(
                    new BigDecimal("0.01")) > 0) {

                System.out.println(
                        "Declaration rejected: declared tax does not match "
                        + "the expected tax.");
                return;
            }

            // DETERMINE FILING STATUS

            String status;

            if (filingDate.isAfter(
                    selectedObligation.getDueDate())) {

                status = "LATE";

                System.out.println(
                        "Warning: This declaration is filed late.");

            } else {

                status = "PENDING";
            }

            // STEP 5: REVIEW

            System.out.println(
                    "\n===== REVIEW DECLARATION =====");

            System.out.println(
                    "Declaration ID: " + declarationId);

            System.out.println(
                    "Obligation ID: "
                    + selectedObligation.getObligationId());

            System.out.println(
                    "Taxpayer TIN: "
                    + selectedObligation.getTaxpayer().getTin());

            System.out.println(
                    "Tax Type: "
                    + selectedObligation.getTaxType().getTaxName());

            System.out.println(
                    "Filing Date: " + filingDate);

            System.out.println(
                    "Taxable Amount: " + taxableAmount);

            System.out.println(
                    "Declared Tax: " + declaredTax);

            System.out.println(
                    "Expected Tax: " + expectedTax);

            System.out.println(
                    "Status: " + status);

            System.out.print(
                    "\nConfirm declaration? (YES/NO): ");

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("YES")) {

                System.out.println(
                        "Declaration cancelled. Nothing was saved.");
                return;
            }

            // STEP 6: CREATE AND VALIDATE DECLARATION

            TaxDeclaration declaration =
                    new TaxDeclaration(
                            declarationId,
                            declaredTax,
                            filingDate,
                            selectedObligation,
                            status,
                            taxableAmount);

            declaration.validate();

            // STEP 7: SAVE TO SHARED DATASTORE

            DataStore.taxDeclarations[
                    DataStore.declarationCount] = declaration;

            DataStore.declarationCount++;

            System.out.println(
                    "\nTax declaration filed successfully!");

        } catch (ValidationException e) {

            System.out.println(
                    "Validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Error filing declaration: " + e.getMessage());
        }
    }
}
