package com.peerlender.lendingengine.domain.service;

import com.peerlender.lendingengine.domain.exception.LoanApplicationNotfoundException;
import com.peerlender.lendingengine.domain.exception.LoanNotFoundException;
import com.peerlender.lendingengine.domain.exception.UserNotFoundException;
import com.peerlender.lendingengine.domain.model.*;
import com.peerlender.lendingengine.domain.repository.LoanApplicationRepository;
import com.peerlender.lendingengine.domain.repository.LoanRepository;
import com.peerlender.lendingengine.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LoanService {

    private final LoanApplicationRepository loanApplicationRepository ;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository ;

    @Autowired
    public LoanService(LoanApplicationRepository loanApplicationRepository, UserRepository userRepository, LoanRepository loanRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional
    public void repayLoan(final Money amountToRepay,
                          final long loanId ,
                          final  User borrower){
        Loan loan = loanRepository.findOneByIdAndBorrower(loanId , borrower)
                .orElseThrow(LoanNotFoundException::new);

        Money actualPaidAmount = amountToRepay.getAmount() > loan.getAmountOwed().getAmount()?
                loan.getAmountOwed() : amountToRepay ;

        loan.repay(actualPaidAmount);

    }

    @Transactional
    public void acceptLoan(final long loanApplicationId , final String lenderUserName){
        User lender = userRepository.findById(lenderUserName).orElseThrow(() -> new UserNotFoundException(lenderUserName));
        LoanApplication loanApplication = findLoanApplication(loanApplicationId);
        loanRepository.save(loanApplication.acceptLoanApplication(lender));
    }

    public List<Loan> findAllBorrowedLoans(final User borrower , final Status status){
        return loanRepository.findAllByBorrowerAndStatus(borrower , status);
    }

    public List<Loan> findAllLentLoans(final User lender ,final Status status){
        return loanRepository.findAllByLenderAndStatus(lender , status);
    }


    private LoanApplication findLoanApplication(long loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId)
                .orElseThrow(() -> new LoanApplicationNotfoundException(loanApplicationId));
    }

    public List<Loan> getLoans(){
        return loanRepository.findAll();
    }
}

