package com.lab.libraryapi20.api.resource;

import com.lab.libraryapi20.api.exception.ApiErrors;
import com.lab.libraryapi20.api.exception.BusinessException;
import com.lab.libraryapi20.api.model.entity.Book;
import com.lab.libraryapi20.api.resource.dto.BookDTO;
import com.lab.libraryapi20.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/books")
public class BookController {

    private BookService service;

    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto){

        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id){
       Book book = service.findById(id).get();
        return modelMapper.map(book, BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException ex){
        BindingResult bindResult = ex.getBindingResult();
        return new ApiErrors(bindResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException ex){
        return new ApiErrors(ex);
    }

}
