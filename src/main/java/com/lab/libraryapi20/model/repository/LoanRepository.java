package com.lab.libraryapi20.model.repository;

import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.api.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {


    @Query(value="select case when ( count (l.id) > 0 ) then true else false end " +
            "from Loan l where l.book = :book and ( l.returned is null or l.returned is not true)")
    boolean existsByBookAndNotReturned(@Param("book") Book book);

    @Query(value="SELECT l FROM Loan as l JOIN l.book as b WHERE b.isbn= :isbn OR l.customer= :customer")
    Page<Loan> findByBookIsbnOrCustomer(@Param("isbn") String isbn, @Param("customer") String customer, Pageable page);

    Page<Loan> findByBook(Book book, Pageable page);

    @Query("select l from Loan l where l.loanDate <= :threeDaysAgo and ( l.returned is null or l.returned is not true ) ")
    List<Loan> findByLoanDateLessThanAndNotReturned(@Param("threeDaysAgo") LocalDate threeDaysAgo);
}
