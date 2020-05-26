package com.h2;

import java.text.DecimalFormat;

public class MortgageCalculator {
    private long loanAmount;
    private int termInYears;
    private float annualRate;
    private double monthlyPayment;

    public MortgageCalculator(long loanAmount, int termInYears, float annualRate) {
        this.loanAmount = loanAmount;
        this.termInYears = termInYears;
        this.annualRate = annualRate;
    }

    private int getNumberOfPayments() {
        return termInYears * 12;
    }

    private float getMonthlyInterestRate() {
        float interestRate = annualRate / 100;
        return interestRate / 12;
    }

    public void calculateMonthlyPayment() {
        long P = loanAmount;
        float r = getMonthlyInterestRate();
        int n = getNumberOfPayments();

        double M = P * (((r * Math.pow(1 + r, n))) / ((Math.pow((1 + r), n)) - 1));

        this.monthlyPayment = M;
    }

    public static void main(String[] args) {
        final long loanAmount = Utilities.getLongValue(args[0]);
        final int termInYears = Utilities.getIntValue(args[1]);
        final float annualRate = Utilities.getFloatValue(args[2]);
        final MortgageCalculator c = new MortgageCalculator(loanAmount, termInYears, annualRate);

        c.calculateMonthlyPayment();

        System.out.println(c.toString());
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("####0.00");
        return "monthlyPayment: " + df.format(monthlyPayment);
    }
}
