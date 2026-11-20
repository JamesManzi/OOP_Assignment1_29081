
package com.auca.methods;

import com.auca.abstracts.TaxParty;
import com.auca.corporatetaxpayer.CorporateTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.individualtaxpayer.IndividualTaxpayer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TaxPayerMethods {

    public static void registerIndividualTaxpayer(Scanner scanner) {

        System.out.println("\n===== REGISTER INDIVIDUAL TAXPAYER =====");

        String[] data = new String[17];

        String[] fieldNames = {
            "First Name", "Last Name", "Email", "Phone Number",
            "Date of Birth", "TIN", "Registration Date",
            "Taxpayer Status", "District", "Sector", "Tax Office",
            "National ID", "Occupation", "Business Name",
            "Business Start Date", "Annual Turnover", "Resident"
        };

        try {

            int step = 1;

            while (step <= 4) {

                if (step == 1) {

                    System.out.println(
                            "\n===== STEP 1 OF 4: PERSONAL DETAILS =====");

                    readIndividualFields(scanner, data, 0, 5);

                } else if (step == 2) {

                    System.out.println(
                            "\n===== STEP 2 OF 4: REGISTRATION DETAILS =====");

                    readIndividualFields(scanner, data, 5, 11);

                } else if (step == 3) {

                    System.out.println(
                            "\n===== STEP 3 OF 4: INDIVIDUAL DETAILS =====");

                    readIndividualFields(scanner, data, 11, 17);

                } else if (step == 4) {

                    System.out.println(
                            "\n===== STEP 4 OF 4: REVIEW ALL DETAILS =====");

                    System.out.println(
                            "Party ID: Generated automatically");

                    for (int i = 0; i < data.length; i++) {
                        System.out.println(
                                (i + 1) + ". "
                                + fieldNames[i] + ": " + data[i]);
                    }

                    System.out.println("\n1. SAVE");
                    System.out.println("2. EDIT FIELD");
                    System.out.println("3. BACK");
                    System.out.println("0. CANCEL");

                    System.out.print("Choose: ");

                    String choice = scanner.nextLine().trim();

                    if (choice.equals("1")) {

                        if (DataStore.individualCount
                                >= DataStore.individualTaxpayers.length) {

                            System.out.println(
                                    "Individual taxpayer storage is full.");
                            return;
                        }

                        // Check duplicate TIN across both taxpayer types.
                        for (int i = 0;
                                i < DataStore.individualCount; i++) {

                            if (DataStore.individualTaxpayers[i]
                                    .getTin().equals(data[5])) {

                                System.out.println("TIN already exists.");
                                return;
                            }
                        }

                        for (int i = 0;
                                i < DataStore.corporateCount; i++) {

                            if (DataStore.corporateTaxpayers[i]
                                    .getTin().equals(data[5])) {

                                System.out.println("TIN already exists.");
                                return;
                            }
                        }

                        // Check duplicate national ID.
                        for (int i = 0;
                                i < DataStore.individualCount; i++) {

                            if (DataStore.individualTaxpayers[i]
                                    .getNationalId().equals(data[11])) {

                                System.out.println(
                                        "National ID already exists.");
                                return;
                            }
                        }

                        IndividualTaxpayer taxpayer =
                                new IndividualTaxpayer(
                                    new BigDecimal(data[15]),
                                    data[13],
                                    LocalDate.parse(data[14]),
                                    data[11],
                                    data[12],
                                    data[16].equalsIgnoreCase("YES"),
                                    data[0],
                                    data[1],
                                    data[2],
                                    data[3],
                                    LocalDate.parse(data[4]),
                                    data[5],
                                    LocalDate.parse(data[6]),
                                    data[7],
                                    data[8],
                                    data[9],
                                    data[10]
                                );

                        taxpayer.validate();

                        System.out.println(
                                "\n===== TAXPAYER SUMMARY =====");

                        System.out.println(
                                "Unique ID: " + taxpayer.getId());

                        System.out.println(
                                "Summary: " + taxpayer.getSummary());

                        System.out.print(
                                "\nConfirm final save? (YES/NO): ");

                        String confirmation =
                                scanner.nextLine().trim();

                        if (confirmation.equalsIgnoreCase("YES")) {

                            taxpayer.printDetails();

                            DataStore.individualTaxpayers[
                                    DataStore.individualCount] = taxpayer;

                            DataStore.individualCount++;

                            System.out.println(
                                    "Individual taxpayer registered successfully!");

                        } else {

                            System.out.println(
                                    "Registration cancelled. Nothing was saved.");
                        }

                        return;

                    } else if (choice.equals("2")) {

                        System.out.print(
                                "Enter field number to edit (1-17): ");

                        try {

                            int field = Integer.parseInt(
                                    scanner.nextLine().trim());

                            if (field < 1 || field > 17) {
                                System.out.println(
                                        "Invalid field number.");
                                continue;
                            }

                            int index = field - 1;

                            data[index] = readIndividualField(
                                    scanner, data, index);

                        } catch (NumberFormatException e) {

                            System.out.println(
                                    "Enter a valid field number.");
                        }

                    } else if (choice.equals("3")) {

                        step = 3;

                    } else if (choice.equals("0")) {

                        System.out.println(
                                "Registration cancelled. No record saved.");
                        return;

                    } else {

                        System.out.println("Invalid choice.");
                    }

                    continue;
                }

                // Navigation after completing each step.
                System.out.println("\n1. NEXT");
                System.out.println("2. BACK");
                System.out.println("0. CANCEL");

                System.out.print("Choose: ");

                String navigation = scanner.nextLine().trim();

                if (navigation.equals("1")) {

                    step++;

                } else if (navigation.equals("2")) {

                    if (step > 1) {
                        step--;
                    } else {
                        System.out.println(
                                "You are already at the first step.");
                    }

                } else if (navigation.equals("0")) {

                    System.out.println(
                            "Registration cancelled. No record saved.");
                    return;

                } else {

                    System.out.println(
                            "Invalid navigation option.");
                }
            }

        } catch (ValidationException e) {

            System.out.println(
                    "Validation failed: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Registration failed: " + e.getMessage());
        }
    }


    public static void registerCorporateTaxpayer(Scanner scanner) {

        System.out.println("\n===== REGISTER CORPORATE TAXPAYER =====");

        try {

            if (DataStore.corporateCount
                    >= DataStore.corporateTaxpayers.length) {

                System.out.println(
                        "Corporate taxpayer storage is full.");
                return;
            }

            // STEP 1: REPRESENTATIVE INFORMATION
            System.out.println(
                    "\n===== STEP 1: REPRESENTATIVE INFORMATION =====");

            String firstName = readRequiredText(
                    scanner, "First Name: ", 2, 60);

            if (!firstName.matches("[a-zA-Z\\s'-]+")) {
                throw new IllegalArgumentException(
                        "First name must contain valid letters.");
            }

            String lastName = readRequiredText(
                    scanner, "Last Name: ", 2, 60);

            if (!lastName.matches("[a-zA-Z\\s'-]+")) {
                throw new IllegalArgumentException(
                        "Last name must contain valid letters.");
            }

            String email;

            while (true) {

                email = readRequiredText(
                        scanner, "Email: ", 1, 120);

                if (email.matches(
                        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    break;
                }

                System.out.println(
                        "Enter a valid email address.");
            }

            String phoneNumber;

            while (true) {

                phoneNumber = readRequiredText(
                        scanner, "Phone Number: ", 1, 16);

                if (phoneNumber.matches("\\+?[0-9]{10,15}")) {
                    break;
                }

                System.out.println(
                        "Phone must contain 10-15 digits, optionally starting with +.");
            }

            LocalDate dateOfBirth =
                    readCorporateDate(scanner, "Date of Birth");

            // STEP 2: TAX REGISTRATION INFORMATION
            System.out.println(
                    "\n===== STEP 2: TAX REGISTRATION INFORMATION =====");

            String tin;

            while (true) {

                tin = readRequiredText(
                        scanner, "TIN (9 digits): ", 1, 9);

                if (!tin.matches("[0-9]{9}")) {
                    System.out.println(
                            "TIN must contain exactly 9 digits.");
                    continue;
                }

                boolean duplicate = false;

                for (int i = 0;
                        i < DataStore.individualCount; i++) {

                    if (DataStore.individualTaxpayers[i]
                            .getTin().equals(tin)) {

                        duplicate = true;
                        break;
                    }
                }

                for (int i = 0;
                        i < DataStore.corporateCount; i++) {

                    if (DataStore.corporateTaxpayers[i]
                            .getTin().equals(tin)) {

                        duplicate = true;
                        break;
                    }
                }

                if (duplicate) {

                    System.out.println(
                            "This TIN is already registered.");
                    continue;
                }

                break;
            }

            LocalDate registrationDate =
                    readCorporateDate(scanner, "Registration Date");

            if (!dateOfBirth.isBefore(registrationDate)) {

                System.out.println(
                        "Date of birth must be before registration date.");
                return;
            }

            String taxpayerStatus;

            while (true) {

                taxpayerStatus = readRequiredText(
                        scanner,
                        "Taxpayer Status (ACTIVE/INACTIVE): ",
                        1,
                        8).toUpperCase();

                if (taxpayerStatus.equals("ACTIVE")
                        || taxpayerStatus.equals("INACTIVE")) {
                    break;
                }

                System.out.println(
                        "Enter ACTIVE or INACTIVE.");
            }

            String district = readRequiredText(
                    scanner, "District: ", 2, 60);

            String sector = readRequiredText(
                    scanner, "Sector: ", 2, 60);

            String taxOffice = readRequiredText(
                    scanner, "Tax Office: ", 2, 60);

            // STEP 3: CORPORATE INFORMATION
            System.out.println(
                    "\n===== STEP 3: CORPORATE INFORMATION =====");

            String companyRegistrationNo;

            while (true) {

                companyRegistrationNo = readRequiredText(
                        scanner,
                        "Company Registration Number: ",
                        5,
                        30);

                if (!companyRegistrationNo.matches(
                        "[A-Z0-9-]{5,30}")) {

                    System.out.println(
                            "Use 5-30 uppercase letters, digits or hyphens.");
                    continue;
                }

                boolean duplicate = false;

                for (int i = 0;
                        i < DataStore.corporateCount; i++) {

                    if (DataStore.corporateTaxpayers[i]
                            .getCompanyRegistrationNo()
                            .equals(companyRegistrationNo)) {

                        duplicate = true;
                        break;
                    }
                }

                if (duplicate) {

                    System.out.println(
                            "Company registration number already exists.");
                    continue;
                }

                break;
            }

            String legalName = readRequiredText(
                    scanner, "Legal Company Name: ", 2, 60);

            LocalDate incorporationDate =
                    readCorporateDate(scanner, "Incorporation Date");

            if (!incorporationDate.isBefore(registrationDate)) {

                System.out.println(
                        "Incorporation date must be before registration date.");
                return;
            }

            String businessSector = readRequiredText(
                    scanner, "Business Sector: ", 2, 60);

            String authorizedRepresentative = readRequiredText(
                    scanner, "Authorized Representative: ", 2, 60);

            BigDecimal annualTurnover;

            while (true) {

                try {

                    System.out.print("Annual Turnover: ");

                    annualTurnover = new BigDecimal(
                            scanner.nextLine().trim());

                    if (annualTurnover.signum() < 0
                            || annualTurnover.scale() > 2
                            || annualTurnover.compareTo(
                                    new BigDecimal("999999999999.99")) > 0) {

                        System.out.println(
                                "Turnover must be non-negative, have at most "
                                + "2 decimal places and not exceed 999999999999.99.");

                        continue;
                    }

                    break;

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Enter a valid monetary amount.");
                }
            }

            // CREATE CORPORATE TAXPAYER
            CorporateTaxpayer taxpayer =
                    new CorporateTaxpayer(
                            firstName,
                            lastName,
                            email,
                            phoneNumber,
                            dateOfBirth,
                            tin,
                            registrationDate,
                            taxpayerStatus,
                            district,
                            sector,
                            taxOffice,
                            companyRegistrationNo,
                            legalName,
                            incorporationDate,
                            businessSector,
                            authorizedRepresentative,
                            annualTurnover
                    );

            taxpayer.validate();

            // STEP 4: REVIEW
            System.out.println(
                    "\n===== STEP 4: REVIEW CORPORATE TAXPAYER =====");

            System.out.println(
                    "Unique Party ID: " + taxpayer.getId());

            System.out.println(
                    "Summary: " + taxpayer.getSummary());

            taxpayer.printDetails();

            System.out.println("\n===== SAVE OR CANCEL =====");
            System.out.println("1. SAVE");
            System.out.println("0. CANCEL");

            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {

                // Recheck duplicates before saving.
                for (int i = 0;
                        i < DataStore.individualCount; i++) {

                    if (DataStore.individualTaxpayers[i]
                            .getTin().equals(tin)) {

                        System.out.println(
                                "TIN already exists. Nothing saved.");
                        return;
                    }
                }

                for (int i = 0;
                        i < DataStore.corporateCount; i++) {

                    CorporateTaxpayer existing =
                            DataStore.corporateTaxpayers[i];

                    if (existing.getTin().equals(tin)
                            || existing.getCompanyRegistrationNo()
                                    .equals(companyRegistrationNo)) {

                        System.out.println(
                                "Duplicate corporate information. Nothing saved.");
                        return;
                    }
                }

                DataStore.corporateTaxpayers[
                        DataStore.corporateCount] = taxpayer;

                DataStore.corporateCount++;

                System.out.println(
                        "\nCorporate taxpayer registered successfully!");

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

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid information: " + e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "Registration failed: " + e.getMessage());
        }
    }


    public static void demonstratePolymorphism() {

        System.out.println(
                "\n========== POLYMORPHISM DEMONSTRATION ==========");

        // Individual taxpayers
        for (int i = 0; i < DataStore.individualCount; i++) {

            TaxParty party = DataStore.individualTaxpayers[i];

            System.out.println("\nIndividual Taxpayer");

            System.out.println(
                    "Name: " + party.getFirstName()
                    + " " + party.getLastName());

            System.out.println("Email: " + party.getEmail());

            System.out.println(
                    "Object Type: "
                    + party.getClass().getSimpleName());
        }

        // Corporate taxpayers
        for (int i = 0; i < DataStore.corporateCount; i++) {

            TaxParty party = DataStore.corporateTaxpayers[i];

            System.out.println("\nCorporate Taxpayer");

            System.out.println(
                    "Name: " + party.getFirstName()
                    + " " + party.getLastName());

            System.out.println("Email: " + party.getEmail());

            System.out.println(
                    "Object Type: "
                    + party.getClass().getSimpleName());
        }

        // Revenue officers
        for (int i = 0; i < DataStore.officerCount; i++) {

            TaxParty party = DataStore.revenueOfficers[i];

            System.out.println("\nRevenue Officer");

            System.out.println(
                    "Name: " + party.getFirstName()
                    + " " + party.getLastName());

            System.out.println("Email: " + party.getEmail());

            System.out.println(
                    "Object Type: "
                    + party.getClass().getSimpleName());
        }

        System.out.println(
                "\n========== END OF DEMONSTRATION ==========");
    }


    private static void readIndividualFields(
            Scanner scanner,
            String[] data,
            int start,
            int end) {

        String[] names = {
            "First Name", "Last Name", "Email", "Phone Number",
            "Date of Birth", "TIN", "Registration Date",
            "Taxpayer Status", "District", "Sector", "Tax Office",
            "National ID", "Occupation", "Business Name",
            "Business Start Date", "Annual Turnover", "Resident"
        };

        for (int i = start; i < end; i++) {

            System.out.println("\n" + (i + 1) + ". " + names[i]);

            data[i] = readIndividualField(
                    scanner, data, i);
        }
    }


    private static String readIndividualField(
            Scanner scanner,
            String[] data,
            int index) {

        String[] prompts = {
            "First Name: ",
            "Last Name: ",
            "Email: ",
            "Phone Number: ",
            "Date of Birth (YYYY-MM-DD): ",
            "TIN (9 digits): ",
            "Registration Date (YYYY-MM-DD): ",
            "Taxpayer Status (ACTIVE/INACTIVE): ",
            "District: ",
            "Sector: ",
            "Tax Office: ",
            "National ID (16 digits): ",
            "Occupation: ",
            "Business Name: ",
            "Business Start Date (YYYY-MM-DD): ",
            "Annual Turnover: ",
            "Is Resident? (YES/NO): "
        };

        while (true) {

            if (data[index] != null) {

                System.out.println(
                        "Current value: " + data[index]);

                System.out.print(
                        "Enter new value or press ENTER to keep: ");

            } else {

                System.out.print(prompts[index]);
            }

            String value = scanner.nextLine().trim();

            if (value.isEmpty() && data[index] != null) {
                return data[index];
            }

            if (value.isEmpty()) {
                System.out.println("This field is required.");
                continue;
            }

            switch (index) {

                case 0:
                case 1:
                case 8:
                case 9:
                case 10:
                case 12:
                case 13:

                    if (value.length() < 2
                            || value.length() > 60) {

                        System.out.println(
                                "Must contain between 2 and 60 characters.");
                        continue;
                    }

                    if ((index == 0 || index == 1)
                            && !value.matches("[a-zA-Z\\s'-]+")) {

                        System.out.println(
                                "Use letters, spaces, hyphens or apostrophes.");
                        continue;
                    }
                    break;

                case 2:

                    if (value.length() > 120
                            || !value.matches(
                                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

                        System.out.println(
                                "Invalid email address.");
                        continue;
                    }
                    break;

                case 3:

                    if (!value.matches("^\\+?[0-9]{10,15}$")) {

                        System.out.println(
                                "Phone must contain 10-15 digits, "
                                + "with an optional leading +.");
                        continue;
                    }
                    break;

                case 4:
                case 6:
                case 14:

                    try {

                        LocalDate date = LocalDate.parse(value);

                        if (date.isAfter(LocalDate.now())) {

                            System.out.println(
                                    "Date cannot be in the future.");
                            continue;
                        }

                        if (index == 6 && data[4] != null
                                && !LocalDate.parse(data[4])
                                        .isBefore(date)) {

                            System.out.println(
                                    "Date of birth must precede registration date.");
                            continue;
                        }

                        if (index == 14 && data[6] != null
                                && !date.isBefore(
                                        LocalDate.parse(data[6]))) {

                            System.out.println(
                                    "Business start date must precede registration date.");
                            continue;
                        }

                    } catch (DateTimeParseException e) {

                        System.out.println(
                                "Invalid date. Use YYYY-MM-DD.");
                        continue;
                    }
                    break;

                case 5:

                    if (!value.matches("[0-9]{9}")) {

                        System.out.println(
                                "TIN must contain exactly 9 digits.");
                        continue;
                    }

                    boolean duplicateTin = false;

                    for (int i = 0;
                            i < DataStore.individualCount; i++) {

                        if (DataStore.individualTaxpayers[i]
                                .getTin().equals(value)) {

                            duplicateTin = true;
                            break;
                        }
                    }

                    for (int i = 0;
                            i < DataStore.corporateCount; i++) {

                        if (DataStore.corporateTaxpayers[i]
                                .getTin().equals(value)) {

                            duplicateTin = true;
                            break;
                        }
                    }

                    if (duplicateTin) {

                        System.out.println(
                                "This TIN is already registered.");
                        continue;
                    }
                    break;

                case 7:

                    if (!value.equalsIgnoreCase("ACTIVE")
                            && !value.equalsIgnoreCase("INACTIVE")) {

                        System.out.println(
                                "Status must be ACTIVE or INACTIVE.");
                        continue;
                    }

                    value = value.toUpperCase();
                    break;

                case 11:

                    if (!value.matches("[0-9]{16}")) {

                        System.out.println(
                                "National ID must contain exactly 16 digits.");
                        continue;
                    }

                    boolean duplicateId = false;

                    for (int i = 0;
                            i < DataStore.individualCount; i++) {

                        if (DataStore.individualTaxpayers[i]
                                .getNationalId().equals(value)) {

                            duplicateId = true;
                            break;
                        }
                    }

                    if (duplicateId) {

                        System.out.println(
                                "This National ID is already registered.");
                        continue;
                    }
                    break;

                case 15:

                    try {

                        BigDecimal amount = new BigDecimal(value);

                        if (amount.signum() < 0
                                || amount.scale() > 2
                                || amount.compareTo(
                                    new BigDecimal("999999999999.99")) > 0) {

                            System.out.println(
                                    "Turnover must be non-negative, "
                                    + "have at most 2 decimal places and "
                                    + "not exceed 999999999999.99.");
                            continue;
                        }

                    } catch (NumberFormatException e) {

                        System.out.println(
                                "Enter a valid monetary amount.");
                        continue;
                    }
                    break;

                case 16:

                    if (!value.equalsIgnoreCase("YES")
                            && !value.equalsIgnoreCase("NO")) {

                        System.out.println("Enter YES or NO.");
                        continue;
                    }

                    value = value.toUpperCase();
                    break;
            }

            return value;
        }
    }


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


    private static LocalDate readCorporateDate(
            Scanner scanner,
            String fieldName) {

        while (true) {

            try {

                System.out.print(
                        fieldName + " (YYYY-MM-DD): ");

                LocalDate date = LocalDate.parse(
                        scanner.nextLine().trim());

                if (date.isAfter(LocalDate.now())) {

                    System.out.println(
                            fieldName + " cannot be in the future.");
                    continue;
                }

                return date;

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Use YYYY-MM-DD.");
            }
        }
    }
}
