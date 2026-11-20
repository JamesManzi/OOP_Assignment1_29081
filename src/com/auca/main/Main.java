package com.auca.main;
import com.auca.exceptions.ValidationException;
import com.auca.methods.AssessmentMethods;
import com.auca.methods.CalculateMethods;
import com.auca.methods.PaymentMethods;
import com.auca.methods.RegisterMethods;
import com.auca.methods.RequestMethods;
import com.auca.methods.SearchMethods;
import com.auca.methods.TaxDeclarationMethods;
import com.auca.methods.TaxPayerMethods;
import com.auca.methods.UpdateMethod;
import com.auca.methods.ViewRecordsMethods;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
public class Main {

public static LocalDate readDate(Scanner scanner, String message) {
    while (true) {
        try {
            System.out.print(message + " (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();

            return LocalDate.parse(input);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date! Please use YYYY-MM-DD.");
        }
    }
}

    public static void displayMenu(){
        System.out.println("================ TAX ADMINISTRATION MANAGEMENT ================");
            System.out.println("1.Register Individual Taxpayer");
            System.out.println("2.Register Corporate Taxpayer");
            System.out.println("3.Register Revenue Officer");
            System.out.println("4.Register Tax Type");
            System.out.println("5.Create Tax Obligation");
            System.out.println("6.File Tax Declaration");
            System.out.println("7.Assess Declaration");
            System.out.println("8.Confirm Assessment");
            System.out.println("9.View All Records");
            System.out.println("10.View Declaration History");
            System.out.println("11.View Assessment History");
            System.out.println("12.Record Tax Payment");
            System.out.println("13.Calculate / Record Penalty");
            System.out.println("14.Request / Approve Refund");
            System.out.println("15.Request / Issue Tax Clearance");
            System.out.println("16.Search Record by ID / TIN");
            System.out.println("17.Update Eligible Record");
            System.out.println("18.View Taxpayer Statement");
            System.out.println("19.View Outstanding Balances");
            System.out.println("20.View Tax Reports");
            System.out.println("21.Demonstrate Polymorphism");
            System.out.println("0.Exit");
    }

    public static int getValidOption(Scanner scanner){
        int option=-1;
        do {
                System.out.println("Select option: ");
                try {
                option=Integer.parseInt(scanner.nextLine());
                if(option<0||option>21){
                    throw new ValidationException("Invalid Option Must be between (0-21)");
                }

            } catch (NumberFormatException e) {
    System.out.println("Invalid input! Please enter a valid number.");
    option = -1;

} catch (ValidationException e) {
    System.out.println(e.getMessage());
    option = -1;
}
            } while (option<0||option>21);
            return option;
    }
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int option;
        do {
            displayMenu();
            option=getValidOption(scanner);
            switch (option) {

    case 0:
        System.out.println("Thank You for Using Our System");
        break;

    case 1:
        TaxPayerMethods.registerIndividualTaxpayer(scanner);
        break;

    case 2:
        TaxPayerMethods.registerCorporateTaxpayer(scanner);
        break;

    case 3:
        RegisterMethods.registerRevenueOfficer(scanner);
        break;

    case 4:
        RegisterMethods.registerTaxType(scanner);
        break;

    case 5:
        RegisterMethods.createTaxObligation(scanner);
        break;

    case 6:
        TaxDeclarationMethods.fileTaxDeclaration(scanner);
        break;

    case 7:
        AssessmentMethods.assessDeclaration(scanner);
        break;

    case 8:
        AssessmentMethods.confirmAssessment(scanner);
        break;

    case 9:
        ViewRecordsMethods.viewAllRecords();
        break;

    case 10:
    AssessmentMethods.viewDeclarationHistory();
    break;

case 11:
    AssessmentMethods.viewAssessmentHistory();
    break;
    case 12:
        PaymentMethods.recordTaxPayment(scanner);
        break;

    case 13:
        CalculateMethods.calculateAndRecordPenalty(scanner);
        break;

    case 14:
        RequestMethods.requestApproveRefund(scanner);
        break;

    case 15:
        RequestMethods.requestIssueTaxClearance(scanner);
        break;

    case 16:
        SearchMethods.searchRecordByIdOrTin(scanner);
        break;

    case 17:
        UpdateMethod.updateEligibleRecord(scanner);
        break;

    case 18:
        ViewRecordsMethods.viewTaxpayerStatement(scanner);
        break;

    case 19:
        ViewRecordsMethods.viewOutstandingBalances();
        break;

    case 20:
        ViewRecordsMethods.viewTaxReports();
        break;

    case 21:
        TaxPayerMethods.demonstratePolymorphism();
        break;

    default:
        System.out.println("Invalid Option Try Again");
        break;
}
        } while (option!=0);
        
    }
    

}
