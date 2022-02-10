package com.lab.libraryapi20.model.repository;

import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.api.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static com.lab.libraryapi20.model.repository.BookRepositoryTest.createBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LoanRepository repository;

    @Test
    @DisplayName("deve verificar se o livro esta emprestado")
    public void existsByBookAndNotReturned(){
        Loan loan = createAndPersistLoan(LocalDate.now());

        boolean exists = repository.existsByBookAndNotReturned(loan.getBook());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("deve buscar emprestimos via filtro")
    public void findByBookIsbnOrCustomerTest(){

        //cenario
        Loan loan = createAndPersistLoan(LocalDate.now());

        //execução
        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));

        //verificação
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);

    }

    @Test
    @DisplayName("Deve obter emprestimos cuja data emprestimo foi menor ou igual a tres dias atras e não retornados")
    public void findByLoanDateLessThanAndNotReturnedTest(){
        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).hasSize(1).contains(loan);


    }

    @Test
    @DisplayName("Deve retornar vazio quando nao houver emprestimos atrasados")
    public void notFindByLoanDateLessThanAndNotReturnedTest(){
        Loan loan = createAndPersistLoan(LocalDate.now());

        List<Loan> result = repository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).isEmpty();


    }

    public Loan createAndPersistLoan(LocalDate loanDate){
        Book book = createBook("123");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).loanDate(loanDate).customer("Fulano").build();
        entityManager.persist(loan);

        return loan;
    }



}
