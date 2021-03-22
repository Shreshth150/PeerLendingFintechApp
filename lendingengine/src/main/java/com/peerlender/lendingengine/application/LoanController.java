package com.peerlender.lendingengine.application;

import com.peerlender.lendingengine.application.model.LoanRepaymentRequest;
import com.peerlender.lendingengine.application.model.LoanRequest;
import com.peerlender.lendingengine.application.service.Impl.TokenValidationService;
import com.peerlender.lendingengine.domain.model.Loan;
import com.peerlender.lendingengine.domain.model.LoanApplication;
import com.peerlender.lendingengine.domain.model.Status;
import com.peerlender.lendingengine.domain.model.User;
import com.peerlender.lendingengine.domain.repository.LoanApplicationRepository;
import com.peerlender.lendingengine.domain.service.LoanApplicationAdapter;
import com.peerlender.lendingengine.domain.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LoanController {


    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationAdapter loanApplicationAdapter;
    private final LoanService loanService;
    private final TokenValidationService tokenValidationService;

    @Autowired
    public LoanController(LoanApplicationRepository loanApplicationRepository, LoanApplicationAdapter loanApplicationAdapter, LoanService loanService, TokenValidationService tokenValidationService) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.loanApplicationAdapter = loanApplicationAdapter;
        this.loanService = loanService;
        this.tokenValidationService = tokenValidationService;
    }


    @PostMapping(value = "/loan/request")
    public void requstLoan(@RequestBody final LoanRequest loanRequest , HttpServletRequest request){
//        LoanApplication loanApplication = loanApplicationAdapter.transform(loanRequest);
//        loanReApplicationRepository.save(loanApplication);
        User borrower = tokenValidationService.validateTokenAndGetUser(request.getHeader(HttpHeaders.AUTHORIZATION));
        loanApplicationRepository.save(loanApplicationAdapter.transform(loanRequest , borrower));
    }

    @GetMapping(value = "/loan/requests")
        public List<LoanApplication> findAllLoanApplications(HttpServletRequest request){
        tokenValidationService.validateTokenAndGetUser(request.getHeader(HttpHeaders.AUTHORIZATION));
        List<LoanApplication> list = loanApplicationRepository.findAllByStatusEquals(Status.ONGOING);
        return list;
        }

    @GetMapping(value = "/loans")
    public List<Loan> getLoans(){
        return loanService.getLoans();
    }

    @PostMapping("/loan/repay")
    public void repayLoan(@RequestBody LoanRepaymentRequest request
                          , @RequestHeader String authorization){
        User borrower = tokenValidationService.validateTokenAndGetUser(authorization);
        loanService.repayLoan(request.getAmount() , request.getLoanId() , borrower);

    }

    @GetMapping(value = "/loan/{status}/borrowed")
    public List<Loan> findBorrowedLoans(@RequestHeader String authorization,
                                        @PathVariable Status status){
        User borrow = tokenValidationService.validateTokenAndGetUser(authorization);
        return loanService.findAllBorrowedLoans(borrow , status);
    }

    @GetMapping(value = "/loan/{status}/lent")
    public List<Loan> findLentLoans(@RequestHeader String authorization ,
                                    @PathVariable Status status){
        User lender = tokenValidationService.validateTokenAndGetUser(authorization);
        return loanService.findAllLentLoans(lender , status);
    }

    @PostMapping(value = "/loan/accept/{loanApplicationId}")
    public void acceptLoan(@PathVariable final String loanApplicationId , HttpServletRequest request){
        User lender = tokenValidationService.validateTokenAndGetUser(request.getHeader(HttpHeaders.AUTHORIZATION));
        loanService.acceptLoan(Long.parseLong(loanApplicationId)
                , lender.getUsername());

    }


}
