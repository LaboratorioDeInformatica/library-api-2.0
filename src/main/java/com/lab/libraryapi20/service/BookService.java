package com.lab.libraryapi20.service;

import com.lab.libraryapi20.api.model.entity.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> findById(Long id);
}
