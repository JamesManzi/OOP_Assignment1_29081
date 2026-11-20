package com.auca.methods;

import com.auca.individualtaxpayer.IndividualTaxpayer;
import com.auca.corporatetaxpayer.CorporateTaxpayer;
import com.auca.revenueofficer.RevenueOfficer;
import com.auca.taxtype.TaxType;
import com.auca.taxobligation.TaxObligation;
import com.auca.taxdeclaration.TaxDeclaration;
import com.auca.taxassessment.TaxAssessment;
import com.auca.taxpayment.TaxPayment;
import com.auca.penalty.Penalty;
import com.auca.taxrefund.TaxRefund;
import com.auca.taxclearance.TaxClearance;

public class DataStore {

    public static IndividualTaxpayer[] individualTaxpayers =
            new IndividualTaxpayer[100];
    public static int individualCount = 0;

    public static CorporateTaxpayer[] corporateTaxpayers =
            new CorporateTaxpayer[100];
    public static int corporateCount = 0;

    public static RevenueOfficer[] revenueOfficers =
            new RevenueOfficer[100];
    public static int officerCount = 0;

    public static TaxType[] taxTypes = new TaxType[100];
    public static int taxTypeCount = 0;

    public static TaxObligation[] taxObligations =
            new TaxObligation[100];
    public static int obligationCount = 0;

    public static TaxDeclaration[] taxDeclarations =
            new TaxDeclaration[100];
    public static int declarationCount = 0;

    public static TaxAssessment[] taxAssessments =
            new TaxAssessment[100];
    public static int assessmentCount = 0;

    public static TaxPayment[] taxPayments =
            new TaxPayment[100];
    public static int paymentCount = 0;

    public static Penalty[] penalties = new Penalty[100];
    public static int penaltyCount = 0;

    public static TaxRefund[] taxRefunds = new TaxRefund[100];
    public static int refundCount = 0;

    public static TaxClearance[] taxClearances =
            new TaxClearance[100];
    public static int clearanceCount = 0;
}