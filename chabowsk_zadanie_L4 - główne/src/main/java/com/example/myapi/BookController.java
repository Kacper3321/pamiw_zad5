package com.example.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("${api.book.base.url}")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookDTO> booksDTO = books.stream().map(BookServiceImpl::bookConvertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(booksDTO, HttpStatus.OK);
    }

    @GetMapping("${api.book.by.id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        BookDTO bookDTO = BookServiceImpl.bookConvertToDTO(book);
        if (book != null) {
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
     * @PostMapping("${api.book.base.url}")
     * public ResponseEntity<?> addBook(@Valid @RequestBody Book book, BindingResult
     * bindingResult) {
     * if (bindingResult.hasErrors()) {
     * List<String> errors = new ArrayList<>();
     * for (FieldError error : bindingResult.getFieldErrors()) {
     * errors.add(error.getField() + ": " + error.getDefaultMessage());
     * }
     * return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     * }
     * 
     * bookService.addBook(book);
     * return new ResponseEntity<>(HttpStatus.CREATED);
     * }
     */
    @PostMapping("${api.book.base.url}")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Book book = BookServiceImpl.bookDTOConvertToEntity(bookDTO);
        Book savedBook = bookService.addBook(book);
        BookDTO savedBookDTO = BookServiceImpl.bookConvertToDTO(savedBook);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }

    /*
     * @PutMapping("${api.book.by.id}")
     * public ResponseEntity<?> updateBook(@PathVariable Long
     * id, @Valid @RequestBody Book book,
     * BindingResult bindingResult) {
     * if (bindingResult.hasErrors()) {
     * List<String> errors = new ArrayList<>();
     * for (FieldError error : bindingResult.getFieldErrors()) {
     * errors.add(error.getField() + ": " + error.getDefaultMessage());
     * }
     * return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     * }
     * 
     * bookService.updateBook(id, book);
     * return new ResponseEntity<>(HttpStatus.OK);
     * }
     */

    @PutMapping("${api.book.by.id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Book book = BookServiceImpl.bookDTOConvertToEntity(bookDTO);
        Book updatedBook = bookService.updateBook(id, book);
        BookDTO updatedBookDTO = BookServiceImpl.bookConvertToDTO(updatedBook);
        return ResponseEntity.ok(updatedBookDTO);
    }

    @DeleteMapping("${api.book.by.id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean exists = bookService.deleteBook(id);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
