package com.lab.libraryapi20.service;

import com.lab.libraryapi20.api.dto.LoanFilterDTO;
import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.api.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO dto, Pageable page);

    Page<Loan> getLoansByBook(Book book, Pageable page);

    List<Loan> getAllLateLoans();
}
