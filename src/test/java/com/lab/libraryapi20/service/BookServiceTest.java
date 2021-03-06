package com.lab.libraryapi20.service;

import com.lab.libraryapi20.api.exception.BusinessException;
import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.model.repository.BookRepository;
import com.lab.libraryapi20.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar o livro")
    public void saveBookTest(){

        //cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(book)).thenReturn(Book.builder()
                .id(1L)
                .isbn("123")
                .author("Fulano")
                .title("As aventuras")
                .build());

        //execução
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getIsbn()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");

    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar cadastrar um livro com isbn duplicado")
    public void shouldNotSaveBookWithDuplicatedIsbn(){

        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificação
        assertThat(exception).isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdtest(){
        //cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //execução
        Optional<Book> foundBook = service.findById(id);

        //verificações
        assertThat( foundBook.isPresent()).isTrue();
        assertThat( foundBook.get().getId()).isEqualTo(id);
        assertThat( foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat( foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat( foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por Id quando ele não existir na base")
    public void bookNotFoundtest(){
        //cenario
        Long id = 1L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Book> book = service.findById(id);

        //verificações
        assertThat( book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest(){
        //cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.save(book)).thenReturn(book);
        
        //execução
        Book updateBook = service.update(book);

        //verificação
        assertThat(updateBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(updateBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(updateBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(updateBook.getAuthor()).isEqualTo(book.getAuthor());

    }

    @Test
    @DisplayName("Deve lançar um erro para livro nulo")
    public void updateBookExcetionTest(){
        //cenario
        Book book = new Book() ;
        book.setId(null);

        //execução
        //execução
        org.junit.jupiter.api.Assertions.assertThrows( IllegalArgumentException.class, () ->  service.update(book));

        //verificação
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        //cenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.save(book)).thenReturn(book);

        //execução
         org.junit.jupiter.api.Assertions.assertDoesNotThrow( () ->  service.delete(book));

        //verificação
        Mockito.verify(repository, Mockito.atLeastOnce()).delete(book);


    }

    @Test
    @DisplayName("Deve lançar um erro para deletar um livro nulo")
    public void deleteBookExceptionTest(){
        //cenario
        Book book = new Book() ;
        book.setId(null);

        //execução
        org.junit.jupiter.api.Assertions.assertThrows( IllegalArgumentException.class, () ->  service.delete(book));

        //verificação
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades")
    public void findBookTest(){
        //scenario
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(book);
        Page<Book> page = new PageImpl<>(books, pageRequest, 1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        //executor
        Page result =  service.find(book, pageRequest);

        //verification
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(books);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve obter um livro por isbn")
    public void getByIsbntest(){
        //cenario
        String isbn = "1234";
        Mockito.when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

        //execução
        Optional<Book> foundBook = service.getBookByIsbn(isbn);

        //verificações
        assertThat( foundBook.isPresent()).isTrue();
        assertThat( foundBook.get().getIsbn()).isEqualTo(isbn);
        verify(repository, atLeastOnce()).findByIsbn(isbn);
    }

}
