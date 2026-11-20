
package com.auca.methods;

import com.auca.corporatetaxpayer.CorporateTaxpayer;
import com.auca.individualtaxpayer.IndividualTaxpayer;
import com.auca.penalty.Penalty;
import com.auca.revenueofficer.RevenueOfficer;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxclearance.TaxClearance;
import com.auca.taxdeclaration.TaxDeclaration;
import com.auca.taxobligation.TaxObligation;
import com.auca.taxpayment.TaxPayment;
import com.auca.taxrefund.TaxRefund;
import com.auca.taxtype.TaxType;
import java.util.Scanner;

public class SearchMethods {

    public static void searchRecordByIdOrTin(Scanner scanner) {

        System.out.println("\n===== SEARCH RECORD =====");
        System.out.println("1. Search Individual Taxpayer");
        System.out.println("2. Search Corporate Taxpayer");
        System.out.println("3. Search Revenue Officer");
        System.out.println("4. Search Tax Type");
        System.out.println("5. Search Tax Obligation");
        System.out.println("6. Search Tax Declaration");
        System.out.println("7. Search Tax Assessment");
        System.out.println("8. Search Tax Payment");
        System.out.println("9. Search Penalty");
        System.out.println("10. Search Tax Refund");
        System.out.println("11. Search Tax Clearance");

        int choice;

        while (true) {
            try {
                System.out.print("\nSelect record type: ");
                choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice < 1 || choice > 11) {
                    System.out.println("Choose between 1 and 11!");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number!");
            }
        }

        System.out.print("Enter ID / TIN / Reference: ");
        String searchValue = scanner.nextLine().trim();

        if (searchValue.isEmpty()) {
            System.out.println("Search value cannot be empty!");
            return;
        }

        boolean found = false;

        switch (choice) {

            // 1. INDIVIDUAL TAXPAYER
            case 1:

                for (int i = 0; i < DataStore.individualCount; i++) {

                    IndividualTaxpayer taxpayer =
                            DataStore.individualTaxpayers[i];

                    if (taxpayer == null) {
                        continue;
                    }

                    if (taxpayer.getTin().equalsIgnoreCase(searchValue)
                            || taxpayer.getNationalId()
                                    .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== INDIVIDUAL TAXPAYER =====");
                        taxpayer.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 2. CORPORATE TAXPAYER
            case 2:

                for (int i = 0; i < DataStore.corporateCount; i++) {

                    CorporateTaxpayer taxpayer =
                            DataStore.corporateTaxpayers[i];

                    if (taxpayer == null) {
                        continue;
                    }

                    if (taxpayer.getTin().equalsIgnoreCase(searchValue)
                            || taxpayer.getCompanyRegistrationNo()
                                    .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== CORPORATE TAXPAYER =====");
                        taxpayer.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 3. REVENUE OFFICER
            case 3:

                for (int i = 0; i < DataStore.officerCount; i++) {

                    RevenueOfficer officer =
                            DataStore.revenueOfficers[i];

                    if (officer == null) {
                        continue;
                    }

                    if (officer.getStaffNumber()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== REVENUE OFFICER =====");
                        officer.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 4. TAX TYPE
            case 4:

                for (int i = 0; i < DataStore.taxTypeCount; i++) {

                    TaxType type = DataStore.taxTypes[i];

                    if (type == null) {
                        continue;
                    }

                    if (type.getTaxTypeId().equalsIgnoreCase(searchValue)
                            || type.getTaxCode()
                                    .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX TYPE =====");
                        type.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 5. TAX OBLIGATION
            case 5:

                for (int i = 0; i < DataStore.obligationCount; i++) {

                    TaxObligation obligation =
                            DataStore.taxObligations[i];

                    if (obligation == null) {
                        continue;
                    }

                    if (obligation.getObligationId()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX OBLIGATION =====");
                        obligation.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 6. TAX DECLARATION
            case 6:

                for (int i = 0; i < DataStore.declarationCount; i++) {

                    TaxDeclaration declaration =
                            DataStore.taxDeclarations[i];

                    if (declaration == null) {
                        continue;
                    }

                    if (declaration.getDeclarationId()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX DECLARATION =====");
                        declaration.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 7. TAX ASSESSMENT
            case 7:

                for (int i = 0; i < DataStore.assessmentCount; i++) {

                    TaxAssessment assessment =
                            DataStore.taxAssessments[i];

                    if (assessment == null) {
                        continue;
                    }

                    if (assessment.getAssessmentId()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX ASSESSMENT =====");
                        assessment.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 8. TAX PAYMENT
            case 8:

                for (int i = 0; i < DataStore.paymentCount; i++) {

                    TaxPayment payment = DataStore.taxPayments[i];

                    if (payment == null) {
                        continue;
                    }

                    if (payment.getPaymentId()
                            .equalsIgnoreCase(searchValue)
                            || payment.getPaymentReference()
                                    .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX PAYMENT =====");
                        payment.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 9. PENALTY
            case 9:

                for (int i = 0; i < DataStore.penaltyCount; i++) {

                    Penalty penalty = DataStore.penalties[i];

                    if (penalty == null) {
                        continue;
                    }

                    if (penalty.getPenaltyId()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== PENALTY =====");
                        penalty.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 10. TAX REFUND
            case 10:

                for (int i = 0; i < DataStore.refundCount; i++) {

                    TaxRefund refund = DataStore.taxRefunds[i];

                    if (refund == null) {
                        continue;
                    }

                    if (refund.getRefundId()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX REFUND =====");
                        refund.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            // 11. TAX CLEARANCE
            case 11:

                for (int i = 0; i < DataStore.clearanceCount; i++) {

                    TaxClearance clearance =
                            DataStore.taxClearances[i];

                    if (clearance == null) {
                        continue;
                    }

                    if (clearance.getClearanceId()
                            .equalsIgnoreCase(searchValue)) {

                        System.out.println("\n===== TAX CLEARANCE =====");
                        clearance.printDetails();

                        found = true;
                        break;
                    }
                }
                break;

            default:
                System.out.println("Invalid search option!");
                return;
        }

        if (!found) {
            System.out.println("\nNo matching record found!");
        }
    }
}
