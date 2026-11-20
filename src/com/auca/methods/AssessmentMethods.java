
package com.auca.methods;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import com.auca.exceptions.ValidationException;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxdeclaration.TaxDeclaration;

public class AssessmentMethods {

    private static final BigDecimal MAX_AMOUNT =
            new BigDecimal("999999999999.99");

    // =====================================================
    // 1. VIEW DECLARATION HISTORY
    // =====================================================

    public static void viewDeclarationHistory() {

        System.out.println("\n========== DECLARATION HISTORY ==========");

        if (DataStore.declarationCount == 0) {
            System.out.println("No declaration records found.");
            return;
        }

        for (int i = 0; i < DataStore.declarationCount; i++) {

            TaxDeclaration declaration = DataStore.taxDeclarations[i];

            if (declaration == null) {
                continue;
            }

            System.out.println("\nDeclaration " + (i + 1));
            System.out.println("Declaration ID: "
                    + declaration.getDeclarationId());

            if (declaration.getObligation() != null) {

                System.out.println("Obligation ID: "
                        + declaration.getObligation().getObligationId());

                if (declaration.getObligation().getTaxpayer() != null) {
                    System.out.println("Taxpayer TIN: "
                            + declaration.getObligation()
                                    .getTaxpayer().getTin());
                }

                if (declaration.getObligation().getTaxType() != null) {
                    System.out.println("Tax Type: "
                            + declaration.getObligation()
                                    .getTaxType().getTaxName());
                }
            }

            System.out.println("Filing Date: "
                    + declaration.getFilingDate());

            System.out.println("Taxable Amount: "
                    + declaration.getTaxableAmount());

            System.out.println("Declared Tax: "
                    + declaration.getDeclaredTax());

            System.out.println("Status: " + declaration.getStatus());
            System.out.println("--------------------------------");
        }
    }

    // =====================================================
    // 2. VIEW ASSESSMENT HISTORY
    // =====================================================

    public static void viewAssessmentHistory() {

        System.out.println("\n========== ASSESSMENT HISTORY ==========");

        if (DataStore.assessmentCount == 0) {
            System.out.println("No assessment records found.");
            return;
        }

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment assessment = DataStore.taxAssessments[i];

            if (assessment == null) {
                continue;
            }

            System.out.println("\nAssessment " + (i + 1));
            System.out.println("Assessment ID: "
                    + assessment.getAssessmentId());

            if (assessment.getDeclaration() != null) {
                System.out.println("Declaration ID: "
                        + assessment.getDeclaration().getDeclarationId());

                if (assessment.getDeclaration().getObligation() != null
                        && assessment.getDeclaration().getObligation()
                                .getTaxpayer() != null) {

                    System.out.println("Taxpayer TIN: "
                            + assessment.getDeclaration().getObligation()
                                    .getTaxpayer().getTin());
                }
            }

            System.out.println("Assessed Tax: "
                    + assessment.getAssessedTax());

            System.out.println("Adjustment Amount: "
                    + assessment.getAdjustmentAmount());

            System.out.println("Assessment Total: "
                    + assessment.getAssessmentTotal());

            System.out.println("Assessment Date: "
                    + assessment.getAssessmentDate());

            System.out.println("Status: " + assessment.getStatus());
            System.out.println("--------------------------------");
        }
    }

    // =====================================================
    // 3. ASSESS TAX DECLARATION
    // =====================================================

    public static void assessDeclaration(Scanner scanner) {

        if (DataStore.assessmentCount >= DataStore.taxAssessments.length) {
            System.out.println("Assessment storage is full!");
            return;
        }

        if (DataStore.declarationCount == 0) {
            System.out.println("No declarations available.");
            return;
        }

        try {

            System.out.println("\n===== ASSESS TAX DECLARATION =====");

            // STEP 1: ENTER ASSESSMENT ID

            String assessmentId;

            while (true) {

                System.out.print("Enter Assessment ID: ");
                assessmentId = scanner.nextLine().trim();

                if (!assessmentId.matches("[A-Za-z0-9-]{2,30}")) {
                    System.out.println(
                            "Assessment ID must contain 2-30 letters, numbers or hyphens.");
                    continue;
                }

                if (assessmentIdExists(assessmentId)) {
                    System.out.println("Assessment ID already exists.");
                    continue;
                }

                break;
            }

            // STEP 2: DISPLAY AVAILABLE DECLARATIONS

            System.out.println("\n===== AVAILABLE DECLARATIONS =====");

            boolean declarationAvailable = false;

            for (int i = 0; i < DataStore.declarationCount; i++) {

                TaxDeclaration declaration = DataStore.taxDeclarations[i];

                if (declaration == null) {
                    continue;
                }

                if (declaration.getObligation() == null
                        || declaration.getObligation().getTaxpayer() == null) {
                    continue;
                }

                if (!isEligibleDeclaration(declaration)) {
                    continue;
                }

                if (isAlreadyAssessed(declaration)) {
                    continue;
                }

                declarationAvailable = true;

                System.out.println((i + 1)
                        + ". Declaration ID: "
                        + declaration.getDeclarationId()
                        + " | Taxpayer TIN: "
                        + declaration.getObligation().getTaxpayer().getTin()
                        + " | Declared Tax: "
                        + declaration.getDeclaredTax()
                        + " | Status: "
                        + declaration.getStatus());
            }

            if (!declarationAvailable) {
                System.out.println(
                        "No eligible declarations available for assessment.");
                return;
            }

            // STEP 3: SELECT DECLARATION

            TaxDeclaration selectedDeclaration = null;

            while (selectedDeclaration == null) {

                System.out.print("Select declaration number: ");

                try {

                    int choice = Integer.parseInt(
                            scanner.nextLine().trim());

                    if (choice < 1
                            || choice > DataStore.declarationCount) {

                        System.out.println("Invalid declaration selection.");
                        continue;
                    }

                    TaxDeclaration declaration =
                            DataStore.taxDeclarations[choice - 1];

                    if (declaration == null) {
                        System.out.println("Declaration does not exist.");
                        continue;
                    }

                    if (!isEligibleDeclaration(declaration)) {
                        System.out.println(
                                "This declaration is not eligible for assessment.");
                        continue;
                    }

                    if (isAlreadyAssessed(declaration)) {
                        System.out.println(
                                "This declaration has already been assessed.");
                        continue;
                    }

                    selectedDeclaration = declaration;

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }

            // STEP 4: ENTER ASSESSED TAX

            BigDecimal assessedTax = readMoney(
                    scanner, "Enter Assessed Tax: ");

            // STEP 5: ENTER ADJUSTMENT

            BigDecimal adjustmentAmount = BigDecimal.ZERO;

            System.out.println("Adjustment Amount: " + adjustmentAmount);

            // CALCULATE TOTAL

            BigDecimal assessmentTotal =
                    assessedTax.add(adjustmentAmount);

            if (assessmentTotal.signum() < 0
                    || assessmentTotal.compareTo(MAX_AMOUNT) > 0) {

                System.out.println("Invalid assessment total.");
                return;
            }

            LocalDate assessmentDate = LocalDate.now();
            String status = "DRAFT";

            // STEP 6: REVIEW

            System.out.println("\n===== REVIEW ASSESSMENT =====");

            System.out.println("Assessment ID: " + assessmentId);

            System.out.println("Declaration ID: "
                    + selectedDeclaration.getDeclarationId());

            System.out.println("Taxpayer TIN: "
                    + selectedDeclaration.getObligation()
                            .getTaxpayer().getTin());

            System.out.println("Declared Tax: "
                    + selectedDeclaration.getDeclaredTax());

            System.out.println("Assessed Tax: " + assessedTax);

            System.out.println("Adjustment Amount: "
                    + adjustmentAmount);

            System.out.println("Assessment Total: "
                    + assessmentTotal);

            System.out.println("Assessment Date: "
                    + assessmentDate);

            System.out.println("Status: " + status);

            System.out.print(
                    "\nConfirm and save assessment? (1=YES / 0=NO): ");

            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equals("1")
                    && !confirmation.equalsIgnoreCase("YES")) {

                System.out.println(
                        "Assessment cancelled. Nothing was saved.");
                return;
            }

            // STEP 7: CREATE AND VALIDATE

            TaxAssessment assessment = new TaxAssessment(
                    adjustmentAmount,
                    assessedTax,
                    assessmentDate,
                    assessmentId,
                    selectedDeclaration,
                    status
            );

            assessment.validate();

            // CHECK STORAGE AGAIN

            if (DataStore.assessmentCount >= DataStore.taxAssessments.length) {
                System.out.println("Assessment storage is full!");
                return;
            }

            // CHECK DUPLICATE AGAIN BEFORE SAVING

            if (assessmentIdExists(assessmentId)
                    || isAlreadyAssessed(selectedDeclaration)) {

                System.out.println(
                        "Duplicate assessment detected. Nothing was saved.");
                return;
            }

            DataStore.taxAssessments[DataStore.assessmentCount] =
                    assessment;

            DataStore.assessmentCount++;

            System.out.println(
                    "Tax assessment created successfully!");

        } catch (ValidationException e) {

            System.out.println("Validation error: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Error assessing declaration: " + e.getMessage());
        }
    }

    // =====================================================
    // 4. CONFIRM ASSESSMENT
    // =====================================================

    public static void confirmAssessment(Scanner scanner) {

        if (DataStore.assessmentCount == 0) {
            System.out.println("No assessments available.");
            return;
        }

        try {

            System.out.println("\n===== CONFIRM ASSESSMENT =====");
            System.out.println("\n===== AVAILABLE ASSESSMENTS =====");

            boolean draftAvailable = false;

            for (int i = 0; i < DataStore.assessmentCount; i++) {

                TaxAssessment assessment = DataStore.taxAssessments[i];

                if (assessment == null) {
                    continue;
                }

                System.out.println((i + 1)
                        + ". Assessment ID: "
                        + assessment.getAssessmentId()
                        + " | Assessed Tax: "
                        + assessment.getAssessedTax()
                        + " | Adjustment: "
                        + assessment.getAdjustmentAmount()
                        + " | Status: "
                        + assessment.getStatus());

                if ("DRAFT".equalsIgnoreCase(assessment.getStatus())) {
                    draftAvailable = true;
                }
            }

            if (!draftAvailable) {
                System.out.println("No DRAFT assessments available.");
                return;
            }

            // STEP 1: SELECT ASSESSMENT

            TaxAssessment selectedAssessment = null;

            while (selectedAssessment == null) {

                System.out.print("Select assessment number: ");

                try {

                    int choice = Integer.parseInt(
                            scanner.nextLine().trim());

                    if (choice < 1
                            || choice > DataStore.assessmentCount) {

                        System.out.println("Invalid selection.");
                        continue;
                    }

                    TaxAssessment assessment =
                            DataStore.taxAssessments[choice - 1];

                    if (assessment == null) {
                        System.out.println("Assessment not found.");
                        continue;
                    }

                    if (!"DRAFT".equalsIgnoreCase(assessment.getStatus())) {
                        System.out.println(
                                "Only DRAFT assessments can be confirmed.");
                        continue;
                    }

                    TaxDeclaration declaration =
                            assessment.getDeclaration();

                    if (declaration == null
                            || !isEligibleDeclaration(declaration)) {

                        System.out.println(
                                "This assessment has an invalid declaration.");
                        continue;
                    }

                    // VALIDATE BEFORE CONFIRMATION

                    assessment.validate();

                    BigDecimal total = assessment.getAssessmentTotal();

                    if (total.signum() < 0
                            || total.compareTo(MAX_AMOUNT) > 0) {

                        System.out.println(
                                "Assessment total is outside the allowed range.");
                        continue;
                    }

                    selectedAssessment = assessment;

                } catch (NumberFormatException e) {

                    System.out.println("Enter a valid number.");

                } catch (ValidationException e) {

                    System.out.println(
                            "Assessment validation failed: " + e.getMessage());
                }
            }

            // STEP 2: REVIEW

            BigDecimal total =
                    selectedAssessment.getAssessmentTotal();

            System.out.println("\n===== ASSESSMENT DETAILS =====");

            System.out.println("Assessment ID: "
                    + selectedAssessment.getAssessmentId());

            System.out.println("Declaration ID: "
                    + selectedAssessment.getDeclaration()
                            .getDeclarationId());

            System.out.println("Assessed Tax: "
                    + selectedAssessment.getAssessedTax());

            System.out.println("Adjustment Amount: "
                    + selectedAssessment.getAdjustmentAmount());

            System.out.println("Assessment Total: " + total);

            System.out.println("Current Status: "
                    + selectedAssessment.getStatus());

            // STEP 3: CONFIRM

            System.out.print(
                    "\nAre you sure you want to confirm? (1=YES / 0=NO): ");

            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equals("1")
                    && !confirmation.equalsIgnoreCase("YES")) {

                System.out.println(
                        "Confirmation cancelled. Status unchanged.");
                return;
            }

            // STEP 4: UPDATE STATUS

            selectedAssessment.setStatus("CONFIRMED");

            System.out.println(
                    "Assessment confirmed successfully!");

        } catch (Exception e) {

            System.out.println(
                    "Error confirming assessment: " + e.getMessage());
        }
    }

    // =====================================================
    // 5. CHECK DECLARATION ELIGIBILITY
    // =====================================================

    private static boolean isEligibleDeclaration(
            TaxDeclaration declaration) {

        if (declaration == null
                || declaration.getObligation() == null
                || declaration.getObligation().getTaxpayer() == null
                || declaration.getObligation().getTaxType() == null) {
            return false;
        }

        String status = declaration.getStatus();

        if ("REJECTED".equalsIgnoreCase(status)
                || "CANCELLED".equalsIgnoreCase(status)) {
            return false;
        }

        return declaration.getObligation().getTaxpayer().isActive()
                && declaration.getObligation().getTaxType().isActive();
    }

    // =====================================================
    // 6. CHECK DUPLICATE ASSESSMENT
    // =====================================================

    private static boolean isAlreadyAssessed(
            TaxDeclaration declaration) {

        if (declaration == null) {
            return false;
        }

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment existing = DataStore.taxAssessments[i];

            if (existing == null || existing.getDeclaration() == null) {
                continue;
            }

            if (existing.getDeclaration().getDeclarationId()
                    .equalsIgnoreCase(declaration.getDeclarationId())) {
                return true;
            }
        }

        return false;
    }

    // =====================================================
    // 7. CHECK DUPLICATE ASSESSMENT ID
    // =====================================================

    private static boolean assessmentIdExists(String assessmentId) {

        for (int i = 0; i < DataStore.assessmentCount; i++) {

            TaxAssessment existing = DataStore.taxAssessments[i];

            if (existing != null
                    && existing.getAssessmentId() != null
                    && existing.getAssessmentId()
                            .equalsIgnoreCase(assessmentId)) {
                return true;
            }
        }

        return false;
    }

    // =====================================================
    // 8. READ AND VALIDATE MONEY
    // =====================================================

    private static BigDecimal readMoney(
            Scanner scanner, String message) {

        while (true) {

            System.out.print(message);

            try {

                BigDecimal amount =
                        new BigDecimal(scanner.nextLine().trim());

                if (amount.signum() < 0
                        || amount.scale() > 2
                        || amount.compareTo(MAX_AMOUNT) > 0) {

                    System.out.println(
                            "Enter a non-negative amount with at most "
                            + "2 decimal places and a maximum of "
                            + MAX_AMOUNT + ".");

                    continue;
                }

                return amount;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid amount. Enter a valid monetary value.");
            }
        }
    }
}
