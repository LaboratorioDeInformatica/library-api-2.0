package com.lab.libraryapi20.service.impl;

import com.lab.libraryapi20.api.exception.BusinessException;
import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.model.repository.BookRepository;
import com.lab.libraryapi20.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {


    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return null;
    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public Book update(Book book) {
return null;
    }
}
