package com.peerlender.lendingengine.domain.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Loan {

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private User borrower ;
    @ManyToOne
    private User lender ;
    @OneToOne(cascade = CascadeType.ALL)
    private Money loanAmount;
    private double interestRate ;
    private LocalDate dateLent ;
    private LocalDate dateDue ;
    @OneToOne(cascade = CascadeType.ALL)
    private Money amountRepayed ;
    private Status status ;

    public Loan() {
    }

    public Loan(User lender , LoanApplication loanApplication) {
        this.borrower = loanApplication.getBorrower();
        this.lender = lender;
        this.loanAmount = loanApplication.getAmount();
        this.interestRate = loanApplication.getInterstRate();
        this.dateLent = LocalDate.now();
        this.dateDue = LocalDate.now().plusDays(loanApplication.getrepaymentTermInDays());
        this.amountRepayed = Money.ZERO;
        this.status = Status.ONGOING;
    }

    public void repay(final Money money){
        borrower.withdraw(money);
        lender.topUp(money);
        amountRepayed = amountRepayed.add(money);

        if (getAmountOwed().equals(Money.ZERO)){
            status = Status.COMPLETED;
        }
    }

    public Money getAmountOwed(){
        return loanAmount.times(1 + interestRate/100d).minus(amountRepayed);
    }

    public long getId() {
        return id;
    }

    public User getBorrower() {
        return borrower;
    }

    public User getLender() {
        return lender;
    }

    public Money getAmountRepayed() {
        return amountRepayed;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public LocalDate getDateLent() {
        return dateLent;
    }

    public LocalDate getDateDue() {
        return dateDue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return id == loan.id && Double.compare(loan.interestRate, interestRate) == 0 && Objects.equals(borrower, loan.borrower) && Objects.equals(lender, loan.lender) && Objects.equals(loanAmount, loan.loanAmount) && Objects.equals(dateLent, loan.dateLent) && Objects.equals(dateDue, loan.dateDue) && Objects.equals(amountRepayed, loan.amountRepayed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, borrower, lender, loanAmount, interestRate, dateLent, dateDue, amountRepayed);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", borrower=" + borrower +
                ", lender=" + lender +
                ", loanAmount=" + loanAmount +
                ", interestRate=" + interestRate +
                ", dateLent=" + dateLent +
                ", dateDue=" + dateDue +
                ", amountRepayed=" + amountRepayed +
                '}';
    }
}

