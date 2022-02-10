package com.lab.libraryapi20.api.resource;

import com.lab.libraryapi20.api.dto.LoanFilterDTO;
import com.lab.libraryapi20.api.dto.ReturnedLoanDTO;
import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.api.model.entity.Loan;
import com.lab.libraryapi20.api.resource.dto.BookDTO;
import com.lab.libraryapi20.api.resource.dto.LoanDTO;
import com.lab.libraryapi20.service.BookService;
import com.lab.libraryapi20.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO dto){
        Book book = bookService.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for isbn"));
        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();

        entity= service.save(entity);
        return entity.getId();
    }

    @PatchMapping("{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto){

        Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());
        service.update(loan);

    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageable){
        Page<Loan> loans = service.find(dto, pageable);
        List<LoanDTO> loanDTOS = loans.getContent().stream().map(
                entity -> {
                    Book book = entity.getBook();
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                    LoanDTO loanDto = modelMapper.map(entity, LoanDTO.class);
                    loanDto.setBook(bookDTO);
                    return loanDto;
                }
        ).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(loanDTOS, pageable, loans.getTotalElements());
    }
}
