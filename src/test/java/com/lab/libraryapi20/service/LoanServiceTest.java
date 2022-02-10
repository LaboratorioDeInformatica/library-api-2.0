package com.lab.libraryapi20.service;

import com.lab.libraryapi20.api.dto.LoanFilterDTO;
import com.lab.libraryapi20.api.exception.BusinessException;
import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.api.model.entity.Loan;
import com.lab.libraryapi20.model.repository.LoanRepository;
import com.lab.libraryapi20.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    LoanRepository repository;

    LoanService service;

    @BeforeEach
    public void setUp(){
        this.service = new LoanServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest(){

        String customer = "Fulano";

        Book book = Book.builder().id(1L).build();
        Loan loanToSave = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();


        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .customer(customer)
                .book(book)
                .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);

        Mockito.when(repository.save(loanToSave)).thenReturn(savedLoan);

        Loan loan = service.save(loanToSave);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());

    }

    @Test
    @DisplayName("Deve lançar um erro de negovio ao salvar um emprestiomo com livro já emprestado")
    public void loanedBookSaveTest(){

        //cenário
        Long id = 1L;

        Loan loan = createLoan();
        loan.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));

        //execução
        Optional<Loan> result = service.getById(id);

        //verificação

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        verify(repository).findById(id);

    }

    @Test
    @DisplayName("Deve atualçiza um emrpestimo")
    public void updateLoanTest(){
        //cenario
        Loan loan = createLoan();
        loan.setId(1l);
        loan.setReturned(true);

        when(repository.save(loan)).thenReturn(loan);

        //execução
        Loan updateLoan = service.update(loan);

        //verificação
        assertThat(updateLoan.getReturned()).isTrue();
        verify(repository).save(loan);
    }

    public static  Loan createLoan(){
        String customer = "Fulano";

        Book book = Book.builder().id(1L).build();
        Loan loanToSave = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        return loanToSave;
    }

    @Test
    @DisplayName("Deve filtrar emprestimos pelas propriedades")
    public void findLoanTest(){

        //cenario
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().isbn("321").customer("Fulano").build();

        Loan loan = createLoan();
        loan.setId(1L);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Loan> loans = Arrays.asList(loan);

        Page<Loan> page = new PageImpl<>(loans, pageRequest, 1);

        when( repository.findByBookIsbnOrCustomer(Mockito.anyString(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        //execução
        Page<Loan> result = service.find(loanFilterDTO, pageRequest);

        //verificação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(loans);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
