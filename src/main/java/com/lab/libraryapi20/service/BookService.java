package com.lab.libraryapi20.service;

import com.lab.libraryapi20.api.model.entity.Book;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> findById(Long id);

    void delete(Book book);

    Book update(Book book);

    Object find(Book filter, Pageable pageRequest);
}
