
package com.auca.methods;

import com.auca.abstracts.RegisteredTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.revenueofficer.RevenueOfficer;
import com.auca.taxobligation.TaxObligation;
import com.auca.taxtype.TaxType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RegisterMethods {

    // =====================================================
    // 1. READ DATE
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
    // 2. REGISTER REVENUE OFFICER
    // =====================================================

    public static void registerRevenueOfficer(Scanner scanner) {

        if (DataStore.officerCount >= DataStore.revenueOfficers.length) {
            System.out.println("Revenue Officer storage is full.");
            return;
        }

        try {
            System.out.println("\n===== REGISTER REVENUE OFFICER =====");

            // STEP 1: IDENTIFICATION DETAILS

            System.out.println("\n--- STEP 1: IDENTIFICATION DETAILS ---");

            String firstName = readRequiredText(
                    scanner, "First Name: ", 2, 60);

            String lastName = readRequiredText(
                    scanner, "Last Name: ", 2, 60);

            String email;

            while (true) {
                System.out.print("Email: ");
                email = scanner.nextLine().trim();

                if (email.length() <= 120
                        && email.matches(
                                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    break;
                }

                System.out.println(
                        "Enter a valid email address (maximum 120 characters).");
            }

            String phoneNumber;

            while (true) {
                System.out.print("Phone Number: ");
                phoneNumber = scanner.nextLine().trim();

                if (phoneNumber.matches("^\\+?[0-9]{10,15}$")) {
                    break;
                }

                System.out.println(
                        "Phone must contain 10-15 digits, optionally starting with +.");
            }

            LocalDate dateOfBirth;

            while (true) {
                try {
                    System.out.print("Date of Birth (YYYY-MM-DD): ");

                    dateOfBirth = LocalDate.parse(
                            scanner.nextLine().trim());

                    if (!dateOfBirth.isBefore(LocalDate.now())) {
                        System.out.println(
                                "Date of birth must be before today.");
                        continue;
                    }

                    break;

                } catch (DateTimeParseException e) {
                    System.out.println(
                            "Invalid date. Use YYYY-MM-DD.");
                }
            }

            // STEP 2: REVENUE OFFICER DETAILS

            System.out.println("\n--- STEP 2: REVENUE OFFICER DETAILS ---");

            String staffNumber;

            while (true) {
                System.out.print("Staff Number: ");
                staffNumber = scanner.nextLine().trim();

                if (staffNumber.length() < 2
                        || staffNumber.length() > 30) {
                    System.out.println(
                            "Staff number must contain 2-30 characters.");
                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.officerCount; i++) {
                    RevenueOfficer existing =
                            DataStore.revenueOfficers[i];

                    if (existing != null
                            && existing.getStaffNumber()
                                    .equalsIgnoreCase(staffNumber)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "Staff number already exists.");
                    continue;
                }

                break;
            }

            String department = readRequiredText(
                    scanner, "Department: ", 2, 60);

            String role = readRequiredText(
                    scanner, "Role: ", 2, 60);

            LocalDate hireDate;

            while (true) {
                try {
                    System.out.print("Hire Date (YYYY-MM-DD): ");

                    hireDate = LocalDate.parse(
                            scanner.nextLine().trim());

                    if (hireDate.isAfter(LocalDate.now())) {
                        System.out.println(
                                "Hire date cannot be in the future.");
                        continue;
                    }

                    if (!hireDate.isAfter(dateOfBirth)) {
                        System.out.println(
                                "Hire date must be after date of birth.");
                        continue;
                    }

                    break;

                } catch (DateTimeParseException e) {
                    System.out.println(
                            "Invalid date. Use YYYY-MM-DD.");
                }
            }

            BigDecimal approvalLimit;

            while (true) {
                try {
                    System.out.print("Approval Limit: ");

                    approvalLimit = new BigDecimal(
                            scanner.nextLine().trim());

                    if (approvalLimit.signum() < 0
                            || approvalLimit.stripTrailingZeros().scale() > 2
                            || approvalLimit.compareTo(
                                    new BigDecimal("999999999999.99")) > 0) {

                        System.out.println(
                                "Approval limit must be nonnegative, "
                                + "have at most 2 decimal places, and "
                                + "not exceed 999999999999.99.");

                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Enter a valid monetary amount.");
                }
            }

            Boolean active;

            while (true) {
                System.out.print("Is Officer Active? (YES/NO): ");

                String activeInput =
                        scanner.nextLine().trim();

                if (activeInput.equalsIgnoreCase("YES")) {
                    active = true;
                    break;

                } else if (activeInput.equalsIgnoreCase("NO")) {
                    active = false;
                    break;

                } else {
                    System.out.println(
                            "Please enter YES or NO.");
                }
            }

            // STEP 3: CREATE AND VALIDATE

            System.out.println("\n--- STEP 3: VALIDATION ---");

            RevenueOfficer officer = new RevenueOfficer(
                    firstName,
                    lastName,
                    email,
                    phoneNumber,
                    dateOfBirth,
                    staffNumber,
                    department,
                    role,
                    hireDate,
                    approvalLimit,
                    active
            );

            officer.validate();

            System.out.println(
                    "Officer information passed validation.");

            // STEP 4: REVIEW AND SAVE

            System.out.println("\n--- STEP 4: REVIEW AND SAVE ---");

            System.out.println("\nPERSONAL INFORMATION");
            System.out.println("Party ID: " + officer.getId());
            System.out.println("First Name: " + officer.getFirstName());
            System.out.println("Last Name: " + officer.getLastName());
            System.out.println("Email: " + officer.getEmail());
            System.out.println("Phone Number: " + officer.getPhoneNumber());
            System.out.println("Date of Birth: " + officer.getDateOfBirth());

            System.out.println("\nOFFICER INFORMATION");
            System.out.println("Staff Number: " + officer.getStaffNumber());
            System.out.println("Department: " + officer.getDepartment());
            System.out.println("Role: " + officer.getRole());
            System.out.println("Hire Date: " + officer.getHireDate());
            System.out.println("Approval Limit: " + officer.getApprovalLimit());
            System.out.println("Active: " + officer.getActive());

            System.out.println("\nSummary: " + officer.getSummary());

            System.out.println("\n1. SAVE");
            System.out.println("0. CANCEL");

            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {

                // RECHECK STAFF NUMBER

                for (int i = 0; i < DataStore.officerCount; i++) {
                    RevenueOfficer existing =
                            DataStore.revenueOfficers[i];

                    if (existing != null
                            && existing.getStaffNumber()
                                    .equalsIgnoreCase(staffNumber)) {
                        System.out.println(
                                "Staff number already exists. Not saved.");
                        return;
                    }
                }

                if (DataStore.officerCount
                        >= DataStore.revenueOfficers.length) {
                    System.out.println(
                            "Revenue Officer storage is full.");
                    return;
                }

                officer.validate();

                DataStore.revenueOfficers[DataStore.officerCount] =
                        officer;

                DataStore.officerCount++;

                System.out.println(
                        "Revenue Officer registered successfully!");

            } else if (choice.equals("0")) {

                System.out.println(
                        "Registration cancelled. No record saved.");

            } else {
                System.out.println(
                        "Invalid choice. No record saved.");
            }

        } catch (ValidationException e) {
            System.out.println(
                    "Validation failed: " + e.getMessage());

        } catch (Exception e) {
            System.out.println(
                    "Registration failed: " + e.getMessage());
        }
    }

    // =====================================================
    // 3. REGISTER TAX TYPE
    // =====================================================

    public static void registerTaxType(Scanner scanner) {

        if (DataStore.taxTypeCount >= DataStore.taxTypes.length) {
            System.out.println("Tax Type storage is full.");
            return;
        }

        try {
            System.out.println("\n===== REGISTER TAX TYPE =====");

            // STEP 1: IDENTIFICATION

            System.out.println("\n--- STEP 1: IDENTIFICATION ---");

            String taxTypeId;

            while (true) {
                System.out.print("Tax Type ID: ");

                taxTypeId = scanner.nextLine().trim();

                if (!taxTypeId.matches("[A-Za-z0-9-]{2,30}")) {
                    System.out.println(
                            "Tax Type ID must contain 2-30 letters, "
                            + "digits, or hyphens.");
                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.taxTypeCount; i++) {
                    TaxType existing = DataStore.taxTypes[i];

                    if (existing != null
                            && existing.getTaxTypeId()
                                    .equalsIgnoreCase(taxTypeId)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "Tax Type ID already exists.");
                    continue;
                }

                break;
            }

            String taxCode;

            while (true) {
                System.out.print("Tax Code: ");

                taxCode = scanner.nextLine().trim().toUpperCase();

                if (!taxCode.matches("[A-Z0-9-]{2,20}")) {
                    System.out.println(
                            "Tax Code must contain 2-20 uppercase "
                            + "letters, digits, or hyphens.");
                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.taxTypeCount; i++) {
                    TaxType existing = DataStore.taxTypes[i];

                    if (existing != null
                            && existing.getTaxCode()
                                    .equalsIgnoreCase(taxCode)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "Tax Code already exists.");
                    continue;
                }

                break;
            }

            // STEP 2: TAX DETAILS

            System.out.println("\n--- STEP 2: TAX DETAILS ---");

            String taxName = readRequiredText(
                    scanner, "Tax Name: ", 2, 60);

            // STEP 3: VALIDATION AND CONFIGURATION

            System.out.println(
                    "\n--- STEP 3: VALIDATION AND CONFIGURATION ---");

            BigDecimal rate;

            while (true) {
                try {
                    System.out.print("Tax Rate (%): ");

                    rate = new BigDecimal(
                            scanner.nextLine().trim());

                    if (rate.stripTrailingZeros().scale() > 2
                            || rate.compareTo(BigDecimal.ZERO) < 0
                            || rate.compareTo(
                                    new BigDecimal("100")) > 0) {

                        System.out.println(
                                "Rate must be between 0 and 100, "
                                + "with at most 2 decimal places.");

                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Enter a valid tax rate.");
                }
            }

            String filingFrequency;

            while (true) {
                System.out.println("\nFiling Frequency");
                System.out.println("1. MONTHLY");
                System.out.println("2. QUARTERLY");
                System.out.println("3. ANNUAL");

                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();

                if (choice.equals("1")) {
                    filingFrequency = "MONTHLY";
                    break;

                } else if (choice.equals("2")) {
                    filingFrequency = "QUARTERLY";
                    break;

                } else if (choice.equals("3")) {
                    filingFrequency = "ANNUAL";
                    break;

                } else {
                    System.out.println(
                            "Invalid choice. Try again.");
                }
            }

            Boolean active;

            while (true) {
                System.out.print("Is Tax Type Active? (YES/NO): ");

                String answer = scanner.nextLine().trim();

                if (answer.equalsIgnoreCase("YES")) {
                    active = true;
                    break;

                } else if (answer.equalsIgnoreCase("NO")) {
                    active = false;
                    break;

                } else {
                    System.out.println(
                            "Enter YES or NO.");
                }
            }

            // CREATE OBJECT

            TaxType taxType = new TaxType();

            taxType.setTaxTypeId(taxTypeId);
            taxType.setTaxCode(taxCode);
            taxType.setTaxName(taxName);
            taxType.setRate(rate);
            taxType.setFilingFrequency(filingFrequency);
            taxType.setActive(active);

            // VALIDATE OBJECT

            System.out.println(
                    "\nChecking tax type information...");

            taxType.validate();

            System.out.println(
                    "Tax Type passed validation.");

            // STEP 4: REVIEW

            System.out.println(
                    "\n--- STEP 4: REVIEW TAX TYPE ---");

            System.out.println(
                    "Tax Type ID: " + taxType.getTaxTypeId());

            System.out.println(
                    "Tax Code: " + taxType.getTaxCode());

            System.out.println(
                    "Tax Name: " + taxType.getTaxName());

            System.out.println(
                    "Rate: " + taxType.getRate() + "%");

            System.out.println(
                    "Filing Frequency: "
                    + taxType.getFilingFrequency());

            System.out.println(
                    "Active: " + taxType.getActive());

            System.out.println("\nSUMMARY");
            System.out.println(taxType.getSummary());

            System.out.println("\n1. SAVE");
            System.out.println("0. CANCEL");

            System.out.print("Choose option: ");

            String saveChoice = scanner.nextLine().trim();

            if (saveChoice.equals("1")) {

                // CHECK DUPLICATES AGAIN

                for (int i = 0; i < DataStore.taxTypeCount; i++) {
                    TaxType existing = DataStore.taxTypes[i];

                    if (existing != null
                            && (existing.getTaxTypeId()
                                    .equalsIgnoreCase(taxTypeId)
                            || existing.getTaxCode()
                                    .equalsIgnoreCase(taxCode))) {

                        System.out.println(
                                "Duplicate ID or tax code. Not saved.");

                        return;
                    }
                }

                if (DataStore.taxTypeCount
                        >= DataStore.taxTypes.length) {
                    System.out.println(
                            "Tax Type storage is full.");
                    return;
                }

                taxType.validate();

                DataStore.taxTypes[DataStore.taxTypeCount] =
                        taxType;

                DataStore.taxTypeCount++;

                System.out.println(
                        "Tax Type registered successfully!");

            } else if (saveChoice.equals("0")) {

                System.out.println(
                        "Registration cancelled. No record saved.");

            } else {

                System.out.println(
                        "Invalid choice. No record saved.");
            }

        } catch (ValidationException e) {

            System.out.println(
                    "Validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Tax Type registration failed: " + e.getMessage());
        }
    }

    // =====================================================
    // 4. CREATE TAX OBLIGATION
    // =====================================================

    public static void createTaxObligation(Scanner scanner) {

        if (DataStore.obligationCount
                >= DataStore.taxObligations.length) {
            System.out.println("Obligation storage is full!");
            return;
        }

        try {
            System.out.println("\n===== CREATE TAX OBLIGATION =====");

            // STEP 1: IDENTIFICATION

            System.out.println("\n--- STEP 1: IDENTIFICATION ---");

            String obligationId;

            while (true) {
                System.out.print("Enter Obligation ID: ");

                obligationId = scanner.nextLine().trim();

                if (!obligationId.matches("[A-Za-z0-9-]{2,30}")) {
                    System.out.println(
                            "Obligation ID must contain 2-30 letters, "
                            + "digits, or hyphens.");
                    continue;
                }

                boolean exists = false;

                for (int i = 0; i < DataStore.obligationCount; i++) {
                    TaxObligation existing =
                            DataStore.taxObligations[i];

                    if (existing != null
                            && existing.getObligationId()
                                    .equalsIgnoreCase(obligationId)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    System.out.println(
                            "This Obligation ID already exists.");
                    continue;
                }

                break;
            }

            // STEP 2: SELECT TAXPAYER

            System.out.println("\n--- STEP 2: SELECT TAXPAYER ---");

            int totalTaxpayers =
                    DataStore.individualCount
                    + DataStore.corporateCount;

            if (totalTaxpayers == 0) {
                System.out.println(
                        "No registered taxpayers available.");
                return;
            }

            int number = 1;

            for (int i = 0; i < DataStore.individualCount; i++) {

                if (DataStore.individualTaxpayers[i] == null) {
                    continue;
                }

                System.out.println(
                        number + ". Individual: "
                        + DataStore.individualTaxpayers[i].getDisplayName()
                        + " | TIN: "
                        + DataStore.individualTaxpayers[i].getTin()
                        + " | Status: "
                        + DataStore.individualTaxpayers[i].getTaxpayerStatus());

                number++;
            }

            for (int i = 0; i < DataStore.corporateCount; i++) {

                if (DataStore.corporateTaxpayers[i] == null) {
                    continue;
                }

                System.out.println(
                        number + ". Corporate: "
                        + DataStore.corporateTaxpayers[i].getLegalName()
                        + " | TIN: "
                        + DataStore.corporateTaxpayers[i].getTin()
                        + " | Status: "
                        + DataStore.corporateTaxpayers[i].getTaxpayerStatus());

                number++;
            }

            RegisteredTaxpayer taxpayer = null;

            while (taxpayer == null) {

                System.out.print(
                        "Select taxpayer number: ");

                try {
                    int choice = Integer.parseInt(
                            scanner.nextLine().trim());

                    if (choice < 1 || choice > totalTaxpayers) {
                        System.out.println(
                                "Invalid taxpayer selection.");
                        continue;
                    }

                    if (choice <= DataStore.individualCount) {
                        taxpayer =
                                DataStore.individualTaxpayers[choice - 1];

                    } else {
                        taxpayer =
                                DataStore.corporateTaxpayers[
                                        choice - DataStore.individualCount - 1];
                    }

                    if (taxpayer == null) {
                        System.out.println(
                                "Selected taxpayer record is unavailable.");
                        continue;
                    }

                    if (!taxpayer.isActive()) {
                        System.out.println(
                                "Selected taxpayer is not active.");
                        taxpayer = null;
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Please enter a valid number.");
                }
            }

            // STEP 3: SELECT TAX TYPE

            System.out.println("\n--- STEP 3: SELECT TAX TYPE ---");

            if (DataStore.taxTypeCount == 0) {
                System.out.println(
                        "No tax types have been registered.");
                return;
            }

            int activeTaxTypeCount = 0;

            for (int i = 0; i < DataStore.taxTypeCount; i++) {

                TaxType existing = DataStore.taxTypes[i];

                if (existing != null && existing.isActive()) {

                    activeTaxTypeCount++;

                    // Display actual array position.
                    System.out.println(
                            (i + 1) + ". "
                            + existing.getTaxName()
                            + " | Code: " + existing.getTaxCode()
                            + " | ID: " + existing.getTaxTypeId());
                }
            }

            if (activeTaxTypeCount == 0) {
                System.out.println(
                        "No active tax types are available.");
                return;
            }

            TaxType taxType = null;

            while (taxType == null) {

                System.out.print(
                        "Select tax type number: ");

                try {
                    int choice = Integer.parseInt(
                            scanner.nextLine().trim());

                    if (choice < 1
                            || choice > DataStore.taxTypeCount) {
                        System.out.println(
                                "Invalid tax type selection.");
                        continue;
                    }

                    taxType = DataStore.taxTypes[choice - 1];

                    if (taxType == null || !taxType.isActive()) {
                        System.out.println(
                                "Selected tax type is inactive or unavailable.");
                        taxType = null;
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Please enter a valid number.");
                }
            }

            // STEP 4: ENTER TAX PERIOD

            System.out.println("\n--- STEP 4: ENTER TAX PERIOD ---");

            LocalDate periodStart = readDate(
                    scanner, "Enter Period Start Date");

            LocalDate periodEnd;

            while (true) {

                periodEnd = readDate(
                        scanner, "Enter Period End Date");

                if (periodEnd.isBefore(periodStart)) {
                    System.out.println(
                            "Period end date cannot be before period start date.");
                    continue;
                }

                break;
            }

            LocalDate dueDate;

            while (true) {

                dueDate = readDate(
                        scanner, "Enter Due Date");

                if (!dueDate.isAfter(periodEnd)) {
                    System.out.println(
                            "Due date must be after the period end date.");
                    continue;
                }

                break;
            }

            // STEP 5: CHECK OVERLAPPING OBLIGATIONS

            System.out.println(
                    "\n--- STEP 5: CHECK EXISTING OBLIGATIONS ---");

            for (int i = 0; i < DataStore.obligationCount; i++) {

                TaxObligation existing =
                        DataStore.taxObligations[i];

                if (existing == null
                        || existing.getTaxpayer() == null
                        || existing.getTaxType() == null) {
                    continue;
                }

                boolean sameTaxpayer =
                        existing.getTaxpayer().getTin()
                                .equalsIgnoreCase(taxpayer.getTin());

                boolean sameTaxType =
                        existing.getTaxType().getTaxTypeId()
                                .equalsIgnoreCase(taxType.getTaxTypeId());

                boolean overlapping =
                        !periodStart.isAfter(existing.getPeriodEnd())
                        && !periodEnd.isBefore(existing.getPeriodStart());

                if (sameTaxpayer && sameTaxType && overlapping) {

                    System.out.println(
                            "An obligation already exists for this taxpayer, "
                            + "tax type and overlapping period.");

                    return;
                }
            }

            System.out.println(
                    "No overlapping obligations found.");

            // STEP 6: CREATE AND VALIDATE OBJECT

            System.out.println("\n--- STEP 6: VALIDATION ---");

            TaxObligation obligation = new TaxObligation(
                    dueDate,
                    obligationId,
                    periodEnd,
                    periodStart,
                    taxType,
                    taxpayer
            );

            obligation.validate();

            System.out.println(
                    "Tax obligation passed validation.");

            // STEP 7: REVIEW

            System.out.println(
                    "\n===== REVIEW TAX OBLIGATION =====");

            System.out.println(
                    "Obligation ID: "
                    + obligation.getObligationId());

            System.out.println(
                    "Taxpayer: " + taxpayer.getDisplayName());

            System.out.println(
                    "Taxpayer TIN: " + taxpayer.getTin());

            System.out.println(
                    "Tax Type: " + taxType.getTaxName());

            System.out.println(
                    "Tax Rate: " + taxType.getRate() + "%");

            System.out.println(
                    "Period Start: " + obligation.getPeriodStart());

            System.out.println(
                    "Period End: " + obligation.getPeriodEnd());

            System.out.println(
                    "Due Date: " + obligation.getDueDate());

            System.out.println("\nSUMMARY");
            System.out.println(obligation.getSummary());

            System.out.println("\n1. SAVE");
            System.out.println("0. CANCEL");

            System.out.print("Choose option: ");

            String confirmation =
                    scanner.nextLine().trim();

            if (confirmation.equals("1")
                    || confirmation.equalsIgnoreCase("YES")) {

                // RECHECK STORAGE CAPACITY

                if (DataStore.obligationCount
                        >= DataStore.taxObligations.length) {
                    System.out.println(
                            "Obligation storage is full!");
                    return;
                }

                // RECHECK DUPLICATE ID AND OVERLAPPING PERIOD

                for (int i = 0; i < DataStore.obligationCount; i++) {

                    TaxObligation existing =
                            DataStore.taxObligations[i];

                    if (existing == null) {
                        continue;
                    }

                    if (existing.getObligationId()
                            .equalsIgnoreCase(obligationId)) {
                        System.out.println(
                                "Obligation ID already exists. Not saved.");
                        return;
                    }

                    if (existing.getTaxpayer() == null
                            || existing.getTaxType() == null) {
                        continue;
                    }

                    boolean sameTaxpayer =
                            existing.getTaxpayer().getTin()
                                    .equalsIgnoreCase(taxpayer.getTin());

                    boolean sameTaxType =
                            existing.getTaxType().getTaxTypeId()
                                    .equalsIgnoreCase(taxType.getTaxTypeId());

                    boolean overlapping =
                            !periodStart.isAfter(existing.getPeriodEnd())
                            && !periodEnd.isBefore(existing.getPeriodStart());

                    if (sameTaxpayer && sameTaxType && overlapping) {
                        System.out.println(
                                "Overlapping obligation detected. Not saved.");
                        return;
                    }
                }

                // FINAL VALIDATION

                obligation.validate();

                // SAVE TO SHARED DATASTORE

                DataStore.taxObligations[DataStore.obligationCount] =
                        obligation;

                DataStore.obligationCount++;

                System.out.println(
                        "Tax Obligation created successfully!");

            } else if (confirmation.equals("0")
                    || confirmation.equalsIgnoreCase("NO")) {

                System.out.println(
                        "Operation cancelled. Nothing was saved.");

            } else {

                System.out.println(
                        "Invalid choice. Nothing was saved.");
            }

        } catch (ValidationException e) {

            System.out.println(
                    "Validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Unable to create obligation: " + e.getMessage());
        }
    }

    // =====================================================
    // 5. REUSABLE REQUIRED TEXT INPUT
    // =====================================================

    private static String readRequiredText(
            Scanner scanner,
            String prompt,
            int min,
            int max) {

        while (true) {

            System.out.print(prompt);

            String value = scanner.nextLine().trim();

            if (value.length() < min
                    || value.length() > max) {

                System.out.println(
                        "Enter between " + min
                        + " and " + max + " characters.");

                continue;
            }

            return value;
        }
    }
}
