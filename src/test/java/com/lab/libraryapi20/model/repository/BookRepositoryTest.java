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

        //cenário
        String isbn ="123";

        Book book = createBook(isbn);

        entityManager.persist(book);

        //execução
        boolean exists = bookRepository.existsByIsbn(isbn);

        //verificação
        assertThat(exists).isTrue();
    }

    private Book createBook(String isbn) {
        return Book.builder().title("As aventuras").author("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnNotExists(){

        //cenário
        String isbn ="123";

        Book book = createBook(isbn);


        //execução
        boolean exists = bookRepository.existsByIsbn("123");

        //verificação
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void findByIdTest(){
        //cenario
        Book book = createBook("123");
        entityManager.persist(book);

        //execução
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();

    }

}
