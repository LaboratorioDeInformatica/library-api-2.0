package com.lab.libraryapi20.model.repository;

import com.lab.libraryapi20.api.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists(){

        //scenario
        String isbn ="123";

        Book book = createBook(isbn);

        entityManager.persist(book);

        //executor
        boolean exists = bookRepository.existsByIsbn(isbn);

        //verification
        assertThat(exists).isTrue();
    }

    public static Book createBook(String isbn) {
        return Book.builder().title("As aventuras").author("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnNotExists(){

        //scenario
        String isbn ="123";

        Book book = createBook(isbn);


        //executor
        boolean exists = bookRepository.existsByIsbn("123");

        //verification
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void findByIdTest(){
        //scenario
        Book book = createBook("123");
        entityManager.persist(book);

        //executor
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        //verification
        assertThat(foundBook.isPresent()).isTrue();

    }

    @Test
    @DisplayName("Deve salvar um livro por Id")
    public void saveBookTest(){
        //scenario
        Book book = createBook("123");

        //executor
        Book savedBook = bookRepository.save(book);

        //verification
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){

        //scenario
        Book book = createBook("123");
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());

        //executor
         bookRepository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());

        //verification
        assertThat(deletedBook).isNull();

    }

}
