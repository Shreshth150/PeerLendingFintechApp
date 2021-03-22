package com.peerlender.lendingengine.domain.repository;

import com.peerlender.lendingengine.domain.model.LoanApplication;
import com.peerlender.lendingengine.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>{

    List<LoanApplication> findAllByStatusEquals(Status status);
}
