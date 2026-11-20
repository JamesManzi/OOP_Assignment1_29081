
package com.auca.methods;

import com.auca.corporatetaxpayer.CorporateTaxpayer;
import com.auca.exceptions.ValidationException;
import com.auca.individualtaxpayer.IndividualTaxpayer;
import com.auca.revenueofficer.RevenueOfficer;
import com.auca.taxtype.TaxType;
import java.math.BigDecimal;
import java.util.Scanner;

public class UpdateMethod {

    public static void updateEligibleRecord(Scanner scanner) {

        System.out.println("\n===== UPDATE ELIGIBLE RECORD =====");
        System.out.println("1. Update Individual Taxpayer");
        System.out.println("2. Update Corporate Taxpayer");
        System.out.println("3. Update Revenue Officer");
        System.out.println("4. Update Tax Type");

        int choice = readInteger(scanner, "Select record type: ", 1, 4);

        System.out.print("Enter record ID / TIN: ");
        String searchValue = scanner.nextLine().trim();

        if (searchValue.isEmpty()) {
            System.out.println("Search value cannot be empty!");
            return;
        }

        boolean found = false;

        try {

            switch (choice) {

                // =====================================
                // 1. UPDATE INDIVIDUAL TAXPAYER
                // =====================================
                case 1:

                    for (int i = 0; i < DataStore.individualCount; i++) {

                        IndividualTaxpayer taxpayer =
                                DataStore.individualTaxpayers[i];

                        if (taxpayer == null) {
                            continue;
                        }

                        if (taxpayer.getTin().equalsIgnoreCase(searchValue)) {

                            found = true;

                            System.out.println(
                                    "\n===== INDIVIDUAL TAXPAYER =====");

                            taxpayer.printDetails();

                            System.out.println("\nFields to update:");
                            System.out.println("1. First Name");
                            System.out.println("2. Last Name");
                            System.out.println("3. Email");
                            System.out.println("4. Phone Number");
                            System.out.println("5. Occupation");
                            System.out.println("6. Business Name");
                            System.out.println("7. Annual Turnover");
                            System.out.println("8. District");
                            System.out.println("9. Sector");
                            System.out.println("10. Tax Office");

                            int field = readInteger(
                                    scanner, "Select field: ", 1, 10);

                            String oldValue = null;
                            String newValue = null;

                            switch (field) {

                                case 1:
                                    oldValue = taxpayer.getFirstName();
                                    System.out.print("New first name: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setFirstName(newValue);
                                    break;

                                case 2:
                                    oldValue = taxpayer.getLastName();
                                    System.out.print("New last name: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setLastName(newValue);
                                    break;

                                case 3:
                                    oldValue = taxpayer.getEmail();
                                    System.out.print("New email: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setEmail(newValue);
                                    break;

                                case 4:
                                    oldValue = taxpayer.getPhoneNumber();
                                    System.out.print("New phone number: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setPhoneNumber(newValue);
                                    break;

                                case 5:
                                    oldValue = taxpayer.getOccupation();
                                    System.out.print("New occupation: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setOccupation(newValue);
                                    break;

                                case 6:
                                    oldValue = taxpayer.getBusinessName();
                                    System.out.print("New business name: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setBusinessName(newValue);
                                    break;

                                case 7: {

                                    BigDecimal oldTurnover =
                                            taxpayer.getAnnualTurnover();

                                    BigDecimal newTurnover =
                                            readMoney(scanner,
                                                    "New annual turnover: ");

                                    taxpayer.setAnnualTurnover(newTurnover);

                                    try {
                                        taxpayer.validate();
                                    } catch (ValidationException e) {
                                        taxpayer.setAnnualTurnover(oldTurnover);
                                        throw e;
                                    }

                                    break;
                                }

                                case 8:
                                    oldValue = taxpayer.getDistrict();
                                    System.out.print("New district: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setDistrict(newValue);
                                    break;

                                case 9:
                                    oldValue = taxpayer.getSector();
                                    System.out.print("New sector: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setSector(newValue);
                                    break;

                                case 10:
                                    oldValue = taxpayer.getTaxOffice();
                                    System.out.print("New tax office: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setTaxOffice(newValue);
                                    break;

                                default:
                                    System.out.println("Invalid field!");
                                    return;
                            }

                            if (field != 7) {
                                try {
                                    taxpayer.validate();
                                } catch (ValidationException e) {

                                    restoreIndividualField(
                                            taxpayer, field, oldValue);

                                    throw e;
                                }
                            }

                            break;
                        }
                    }
                    break;


                // =====================================
                // 2. UPDATE CORPORATE TAXPAYER
                // =====================================
                case 2:

                    for (int i = 0; i < DataStore.corporateCount; i++) {

                        CorporateTaxpayer taxpayer =
                                DataStore.corporateTaxpayers[i];

                        if (taxpayer == null) {
                            continue;
                        }

                        if (taxpayer.getTin().equalsIgnoreCase(searchValue)) {

                            found = true;

                            System.out.println(
                                    "\n===== CORPORATE TAXPAYER =====");

                            taxpayer.printDetails();

                            System.out.println("\nFields to update:");
                            System.out.println("1. Legal Name");
                            System.out.println("2. Email");
                            System.out.println("3. Phone Number");
                            System.out.println("4. Business Sector");
                            System.out.println("5. Authorized Representative");
                            System.out.println("6. Annual Turnover");
                            System.out.println("7. District");
                            System.out.println("8. Sector");
                            System.out.println("9. Tax Office");

                            int field = readInteger(
                                    scanner, "Select field: ", 1, 9);

                            String oldValue = null;
                            String newValue = null;

                            switch (field) {

                                case 1:
                                    oldValue = taxpayer.getLegalName();
                                    System.out.print("New legal name: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setLegalName(newValue);
                                    break;

                                case 2:
                                    oldValue = taxpayer.getEmail();
                                    System.out.print("New email: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setEmail(newValue);
                                    break;

                                case 3:
                                    oldValue = taxpayer.getPhoneNumber();
                                    System.out.print("New phone: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setPhoneNumber(newValue);
                                    break;

                                case 4:
                                    oldValue = taxpayer.getBusinessSector();
                                    System.out.print("New business sector: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setBusinessSector(newValue);
                                    break;

                                case 5:
                                    oldValue =
                                            taxpayer.getAuthorizedRepresentative();

                                    System.out.print("New representative: ");
                                    newValue = scanner.nextLine().trim();

                                    taxpayer.setAuthorizedRepresentative(
                                            newValue);
                                    break;

                                case 6: {

                                    BigDecimal oldTurnover =
                                            taxpayer.getAnnualTurnover();

                                    BigDecimal newTurnover =
                                            readMoney(scanner,
                                                    "New annual turnover: ");

                                    taxpayer.setAnnualTurnover(newTurnover);

                                    try {
                                        taxpayer.validate();
                                    } catch (ValidationException e) {
                                        taxpayer.setAnnualTurnover(oldTurnover);
                                        throw e;
                                    }

                                    break;
                                }

                                case 7:
                                    oldValue = taxpayer.getDistrict();
                                    System.out.print("New district: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setDistrict(newValue);
                                    break;

                                case 8:
                                    oldValue = taxpayer.getSector();
                                    System.out.print("New sector: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setSector(newValue);
                                    break;

                                case 9:
                                    oldValue = taxpayer.getTaxOffice();
                                    System.out.print("New tax office: ");
                                    newValue = scanner.nextLine().trim();
                                    taxpayer.setTaxOffice(newValue);
                                    break;

                                default:
                                    System.out.println("Invalid field!");
                                    return;
                            }

                            if (field != 6) {
                                try {
                                    taxpayer.validate();
                                } catch (ValidationException e) {

                                    restoreCorporateField(
                                            taxpayer, field, oldValue);

                                    throw e;
                                }
                            }

                            break;
                        }
                    }
                    break;


                // =====================================
                // 3. UPDATE REVENUE OFFICER
                // =====================================
                case 3:

                    for (int i = 0; i < DataStore.officerCount; i++) {

                        RevenueOfficer officer =
                                DataStore.revenueOfficers[i];

                        if (officer == null) {
                            continue;
                        }

                        if (officer.getStaffNumber()
                                .equalsIgnoreCase(searchValue)) {

                            found = true;

                            System.out.println(
                                    "\n===== REVENUE OFFICER =====");

                            officer.printDetails();

                            System.out.println("\nFields to update:");
                            System.out.println("1. Email");
                            System.out.println("2. Phone Number");
                            System.out.println("3. Department");
                            System.out.println("4. Role");
                            System.out.println("5. Approval Limit");
                            System.out.println("6. Active Status");

                            int field = readInteger(
                                    scanner, "Select field: ", 1, 6);

                            String oldValue = null;
                            String newValue = null;

                            switch (field) {

                                case 1:
                                    oldValue = officer.getEmail();
                                    System.out.print("New email: ");
                                    newValue = scanner.nextLine().trim();
                                    officer.setEmail(newValue);
                                    break;

                                case 2:
                                    oldValue = officer.getPhoneNumber();
                                    System.out.print("New phone: ");
                                    newValue = scanner.nextLine().trim();
                                    officer.setPhoneNumber(newValue);
                                    break;

                                case 3:
                                    oldValue = officer.getDepartment();
                                    System.out.print("New department: ");
                                    newValue = scanner.nextLine().trim();
                                    officer.setDepartment(newValue);
                                    break;

                                case 4:
                                    oldValue = officer.getRole();
                                    System.out.print("New role: ");
                                    newValue = scanner.nextLine().trim();
                                    officer.setRole(newValue);
                                    break;

                                case 5: {

                                    BigDecimal oldLimit =
                                            officer.getApprovalLimit();

                                    BigDecimal newLimit =
                                            readMoney(scanner,
                                                    "New approval limit: ");

                                    officer.setApprovalLimit(newLimit);

                                    try {
                                        officer.validate();
                                    } catch (ValidationException e) {
                                        officer.setApprovalLimit(oldLimit);
                                        throw e;
                                    }

                                    break;
                                }

                                case 6:

                                    oldValue =
                                            String.valueOf(officer.getActive());

                                    System.out.print(
                                            "Active? (true/false): ");

                                    newValue =
                                            scanner.nextLine().trim();

                                    if (!newValue.equalsIgnoreCase("true")
                                            && !newValue.equalsIgnoreCase("false")) {

                                        System.out.println(
                                                "Enter true or false!");
                                        return;
                                    }

                                    officer.setActive(
                                            Boolean.parseBoolean(newValue));
                                    break;

                                default:
                                    System.out.println("Invalid field!");
                                    return;
                            }

                            if (field != 5) {
                                try {
                                    officer.validate();
                                } catch (ValidationException e) {

                                    restoreOfficerField(
                                            officer, field, oldValue);

                                    throw e;
                                }
                            }

                            break;
                        }
                    }
                    break;


                // =====================================
                // 4. UPDATE TAX TYPE
                // =====================================
                case 4:

                    for (int i = 0; i < DataStore.taxTypeCount; i++) {

                        TaxType type = DataStore.taxTypes[i];

                        if (type == null) {
                            continue;
                        }

                        if (type.getTaxTypeId()
                                .equalsIgnoreCase(searchValue)) {

                            found = true;

                            System.out.println("\n===== TAX TYPE =====");

                            type.printDetails();

                            System.out.println("\nFields to update:");
                            System.out.println("1. Tax Name");
                            System.out.println("2. Tax Rate");
                            System.out.println("3. Filing Frequency");
                            System.out.println("4. Active Status");

                            int field = readInteger(
                                    scanner, "Select field: ", 1, 4);

                            String oldValue = null;
                            String newValue = null;

                            switch (field) {

                                case 1:
                                    oldValue = type.getTaxName();
                                    System.out.print("New tax name: ");
                                    newValue = scanner.nextLine().trim();
                                    type.setTaxName(newValue);
                                    break;

                                case 2: {

                                    BigDecimal oldRate = type.getRate();

                                    BigDecimal newRate =
                                            readMoney(scanner,
                                                    "New tax rate (0-100): ");

                                    if (newRate.compareTo(
                                            new BigDecimal("100")) > 0) {

                                        System.out.println(
                                                "Tax rate must be between 0 and 100!");
                                        return;
                                    }

                                    type.setRate(newRate);

                                    try {
                                        type.validate();
                                    } catch (ValidationException e) {
                                        type.setRate(oldRate);
                                        throw e;
                                    }

                                    break;
                                }

                                case 3:

                                    oldValue = type.getFilingFrequency();

                                    System.out.print(
                                            "Filing frequency "
                                            + "(MONTHLY/QUARTERLY/ANNUAL): ");

                                    newValue = scanner.nextLine()
                                            .trim().toUpperCase();

                                    type.setFilingFrequency(newValue);
                                    break;

                                case 4:

                                    oldValue =
                                            String.valueOf(type.getActive());

                                    System.out.print(
                                            "Active? (true/false): ");

                                    newValue =
                                            scanner.nextLine().trim();

                                    if (!newValue.equalsIgnoreCase("true")
                                            && !newValue.equalsIgnoreCase("false")) {

                                        System.out.println(
                                                "Enter true or false!");
                                        return;
                                    }

                                    type.setActive(
                                            Boolean.parseBoolean(newValue));
                                    break;

                                default:
                                    System.out.println("Invalid field!");
                                    return;
                            }

                            if (field != 2) {
                                try {
                                    type.validate();
                                } catch (ValidationException e) {

                                    restoreTaxTypeField(
                                            type, field, oldValue);

                                    throw e;
                                }
                            }

                            break;
                        }
                    }
                    break;
            }

            if (!found) {
                System.out.println("Record not found!");
            } else {
                System.out.println(
                        "\nRecord updated successfully!");
            }

        } catch (ValidationException e) {

            System.out.println(
                    "\nUpdate rejected: " + e.getMessage());

        } catch (NumberFormatException e) {

            System.out.println(
                    "\nInvalid number entered. Record was not updated.");

        } catch (Exception e) {

            System.out.println(
                    "\nUpdate failed: " + e.getMessage());
        }
    }


    // =====================================
    // INPUT HELPERS
    // =====================================

    private static int readInteger(
            Scanner scanner,
            String message,
            int min,
            int max) {

        while (true) {

            try {

                System.out.print(message);

                int value = Integer.parseInt(
                        scanner.nextLine().trim());

                if (value < min || value > max) {
                    System.out.println(
                            "Choose between " + min + " and " + max + ".");
                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println("Enter a valid number.");
            }
        }
    }


    private static BigDecimal readMoney(
            Scanner scanner,
            String message) {

        while (true) {

            try {

                System.out.print(message);

                BigDecimal value = new BigDecimal(
                        scanner.nextLine().trim());

                if (value.signum() < 0
                        || value.compareTo(
                                new BigDecimal("999999999999.99")) > 0
                        || value.scale() > 2) {

                    System.out.println(
                            "Enter a non-negative amount with at most "
                            + "2 decimal places and a maximum of "
                            + "999999999999.99.");

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Enter a valid monetary amount.");
            }
        }
    }


    // =====================================
    // RESTORE OLD VALUES IF VALIDATION FAILS
    // =====================================

    private static void restoreIndividualField(
            IndividualTaxpayer taxpayer,
            int field,
            String oldValue) {

        switch (field) {
            case 1: taxpayer.setFirstName(oldValue); break;
            case 2: taxpayer.setLastName(oldValue); break;
            case 3: taxpayer.setEmail(oldValue); break;
            case 4: taxpayer.setPhoneNumber(oldValue); break;
            case 5: taxpayer.setOccupation(oldValue); break;
            case 6: taxpayer.setBusinessName(oldValue); break;
            case 8: taxpayer.setDistrict(oldValue); break;
            case 9: taxpayer.setSector(oldValue); break;
            case 10: taxpayer.setTaxOffice(oldValue); break;
        }
    }


    private static void restoreCorporateField(
            CorporateTaxpayer taxpayer,
            int field,
            String oldValue) {

        switch (field) {
            case 1: taxpayer.setLegalName(oldValue); break;
            case 2: taxpayer.setEmail(oldValue); break;
            case 3: taxpayer.setPhoneNumber(oldValue); break;
            case 4: taxpayer.setBusinessSector(oldValue); break;
            case 5:
                taxpayer.setAuthorizedRepresentative(oldValue);
                break;
            case 7: taxpayer.setDistrict(oldValue); break;
            case 8: taxpayer.setSector(oldValue); break;
            case 9: taxpayer.setTaxOffice(oldValue); break;
        }
    }


    private static void restoreOfficerField(
            RevenueOfficer officer,
            int field,
            String oldValue) {

        switch (field) {
            case 1: officer.setEmail(oldValue); break;
            case 2: officer.setPhoneNumber(oldValue); break;
            case 3: officer.setDepartment(oldValue); break;
            case 4: officer.setRole(oldValue); break;
            case 6:
                officer.setActive(Boolean.parseBoolean(oldValue));
                break;
        }
    }


    private static void restoreTaxTypeField(
            TaxType type,
            int field,
            String oldValue) {

        switch (field) {
            case 1: type.setTaxName(oldValue); break;
            case 3: type.setFilingFrequency(oldValue); break;
            case 4:
                type.setActive(Boolean.parseBoolean(oldValue));
                break;
        }
    }
}
