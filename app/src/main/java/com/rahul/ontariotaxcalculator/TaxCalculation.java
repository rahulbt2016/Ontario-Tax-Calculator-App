package com.rahul.ontariotaxcalculator;

public class TaxCalculation {

    private static final double RRSP_LIMIT_2022 = 29210;
    private static final double RRSP_LIMIT_2023 = 30780;

    private double income;
    private double rrspContribution;

    public TaxCalculation(double income, double rrspContribution) {
        this.income = income;
        this.rrspContribution = rrspContribution;
    }

    public TaxCalculation() {
        this(0, 0);
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getRrspContribution() {
        return rrspContribution;
    }

    public void setRrspContribution(double rrspContribution) {
        this.rrspContribution = rrspContribution;
    }

    public double getFederalTax() {
        double federalTax = 0;
        double grossIncome = income - rrspContribution;

        //https://www.canada.ca/en/revenue-agency/services/tax/individuals/frequently-asked-questions-individuals/canadian-income-tax-rates-individuals-current-previous-years.html
        double federalTaxBracketAmount[] = {50197, 50195, 55233, 66083};
        double federalTaxBracketPercent[] = {15, 20.5, 26, 29};

        double maxFederalTax = 33;

        int i = 0;
        while (grossIncome > 0 && i < federalTaxBracketAmount.length) {

            if(grossIncome > federalTaxBracketAmount[i]){
                federalTax += federalTaxBracketAmount[i] * (federalTaxBracketPercent[i] / 100);
                grossIncome -= federalTaxBracketAmount[i];
            }
            else {
                federalTax += grossIncome * (federalTaxBracketPercent[i] / 100);
                grossIncome -= grossIncome;
            }
            //System.out.println(i +  " => Federal Tax: " + federalTax + " & Gross Income: " + grossIncome);
            i+=1;
        }

        if (grossIncome > 0) {
            federalTax += grossIncome * (maxFederalTax / 100);
        }

        return federalTax;
    }

    public double getProvincialTax() {

        double provincialTax = 0;
        double grossIncome = income - rrspContribution;

        //https://www.taxtips.ca/taxrates/on.htm
        double provincialTaxBracketAmount[] = {46226,
                                            (92454 - 46226),
                                            (150000 - 92454),
                                            (220000 - 150000)};
        double provincialTaxBracketPercent[] = {5.05, 9.15, 11.16, 12.16};

        double maxProvincialTax = 13.16;

        int i = 0;
        while (grossIncome > 0 && i < provincialTaxBracketAmount.length) {

            if(grossIncome > provincialTaxBracketAmount[i]){
                provincialTax += provincialTaxBracketAmount[i] * (provincialTaxBracketPercent[i] / 100);
                grossIncome -= provincialTaxBracketAmount[i];
            }
            else {
                provincialTax += grossIncome * (provincialTaxBracketPercent[i] / 100);
                grossIncome -= grossIncome;
            }

            i+=1;
        }

        if (grossIncome > 0) {
            provincialTax += grossIncome * (maxProvincialTax / 100);
        }

        return provincialTax;

    }

    public double getTotalTax() {
        return getFederalTax() + getProvincialTax();
    }

    public  double getAfterTaxIncome() {
        return this.income - getTotalTax();
    }

    public double getNextYearRrsp() {

        double unusedRrrp = RRSP_LIMIT_2022 - rrspContribution;
        double nextYearRrsp = Math.min(RRSP_LIMIT_2023,  (unusedRrrp + (income * 18 / 100)));

        return nextYearRrsp;
    }
}
