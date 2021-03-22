package com.peerlender.lendingengine.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public final class LoanApplication {

    @Id
    @GeneratedValue
    private long id;
    private int amount ;
    @ManyToOne
    private User borrower ;
    private int repaymentTermInDays;
    private double interstRate ;
    private Status status ;

    public LoanApplication( int amount, User borrower, int repaymentTermInDays, double interstRate) {
        this.amount = amount;
        this.borrower = borrower;
        this.repaymentTermInDays = repaymentTermInDays;
        this.interstRate = interstRate;
        this.status = Status.ONGOING;
    }

    protected LoanApplication(){
    }

    public Loan acceptLoanApplication(final User lender){
        lender.withdraw(getAmount());
        borrower.topUp(getAmount());
        status = Status.COMPLETED;

        return new Loan(lender , this);
    }


    public Money getAmount() {
        return new Money(amount , Currency.USD);
    }

    public User getBorrower() {
        return borrower;
    }

    public int getrepaymentTermInDays() {
        return repaymentTermInDays;
    }

    public double getInterstRate() {
        return interstRate;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanApplication that = (LoanApplication) o;
        return amount == that.amount && Double.compare(that.interstRate, interstRate) == 0 && Objects.equals(borrower, that.borrower) && Objects.equals(repaymentTermInDays, that.repaymentTermInDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, borrower, repaymentTermInDays, interstRate);
    }

    @Override
    public String toString() {
        return "LoanRequest{" +
                "id = " + id +
                "amount=" + amount +
                ", borrower=" + borrower +
                ", repaymentTerm=" + repaymentTermInDays +
                ", interstRate=" + interstRate +
                '}';
    }
}



