package com.h2;

import java.text.DecimalFormat;

public class MortgageCalculator {

     private long loanAmount ;
    private int termInYears  ;
    private float annualRate ;
    private double monthlyPayment;

    public MortgageCalculator(long loanAmount, int termInYears, float annualRate)
    {
        this.loanAmount = loanAmount;
        this.termInYears = termInYears;
        this.annualRate = annualRate;
    }

    private int getNumberOfPayments() {
        return this.termInYears * 12;
    }

    /*
Inside `getMonthlyInterestRate()`, create a new `float` variable
called `interestRate`, and assign to it the result of dividing
`annualRate` by `100`. Then, divide the `interestRate` by `12`
to calculate the monthly interest rate. Finally return the result of the calculation by replacing `0.0f`.
 */
    private float getMonthlyInterestRate() {
        float interestRate = this.annualRate/100;
        return interestRate / 12;
    }

    /*
    P of type long, and assign it loanAmount
r of type float, and initialize it to the return value of getMonthlyInterestRate()
n of type int, and initialize the value by calling getNumberOfPayments()
*/
    public void calculateMonthlyPayment()
    {
        long P = this.loanAmount;
        float r = getMonthlyInterestRate();
        int n = getNumberOfPayments();
        double M = P * (((r * Math.pow(1 + r, n))) / ((Math.pow((1 + r), n)) - 1));
        this.monthlyPayment = M;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("####0.00");
        return "monthlyPayment: " + df.format(monthlyPayment);
    }

    public static void main(String[] args) {
        /*
        A variable called loanAmount of type long. Initialize it by parsing the args[0] value (which
        is of type String) to long using Long.parseLong().
A variable called termInYears of type int and initialize by parsing args[1] using Integer.parseInt().
A variable called annualRate of type float and initialize by calling Float.parseFloat().
         */

        long loanAmount = Long.parseLong(args[0]);
        int termInYears = Integer.parseInt(args[1]);
        float annualRate = Float.parseFloat(args[2]);

        MortgageCalculator calculator = new MortgageCalculator(loanAmount, termInYears,annualRate);
        calculator.calculateMonthlyPayment();

        //System.out.println("Monthly Payment: " + calculator.toString());
        System.out.println(calculator.toString());
    }
}
