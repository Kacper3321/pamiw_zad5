package com.example.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CheckOutController {

    private final CheckOutService checkOutService;
    private final BookService bookService;

    @Autowired
    public CheckOutController(CheckOutService checkOutService, BookService bookService) {
        this.checkOutService = checkOutService;
        this.bookService = bookService;
    }

    @GetMapping("${api.checkout.base.url}")
    public ResponseEntity<List<CheckOutDTO>> getAllCheckOuts() {
        List<CheckOut> checkOuts = checkOutService.getAllCheckOuts();
        List<CheckOutDTO> checkOutsDTO = checkOuts.stream().map(CheckOutServiceImpl::checkOutConvertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(checkOutsDTO, HttpStatus.OK);
    }

    @GetMapping("${api.checkout.by.id}")
    public ResponseEntity<CheckOutDTO> getCheckOutById(@PathVariable Long id) {
        try {
            CheckOut checkOut = checkOutService.getCheckOutById(id);
            CheckOutDTO checkOutDTO = CheckOutServiceImpl.checkOutConvertToDTO(checkOut);
            return new ResponseEntity<>(checkOutDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("${api.checkout.base.url}")
    public ResponseEntity<?> createCheckOut(@Valid @RequestBody CheckOutDTO checkOutDTO, BindingResult bindingResult) {
        if(checkOutDTO.getReturnDays() < 0) {
            String error = "Liczba dni na zwrot nie moze byc mniejsza niz 0";
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Book book = bookService.getBookById(checkOutDTO.getBookId());
            if (book == null) {
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
            BookDTO bookDTO = BookServiceImpl.bookConvertToDTO(book);
            checkOutDTO.setBook(bookDTO);
            CheckOut checkOut = CheckOutServiceImpl.checkOutDTOConvertToEntity(checkOutDTO);
            CheckOut savedCheckOut = checkOutService.createCheckOut(checkOut);
            CheckOutDTO savedCheckOutDTO = CheckOutServiceImpl.checkOutConvertToDTO(savedCheckOut);
            return new ResponseEntity<>(savedCheckOutDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("${api.checkout.by.id}")
    public ResponseEntity<?> updateCheckOut(@PathVariable Long id, @Valid @RequestBody CheckOutDTO checkOutDTO, BindingResult bindingResult) {

        if(checkOutDTO.getReturnDays() < 0) {
            String error = "Liczba dni na zwrot nie moze byc mniejsza niz 0";
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Pobierz książkę na podstawie bookId z DTO
            Book book = bookService.getBookById(checkOutDTO.getBookId());
            if (book == null) {
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
    
            // Ustaw BookDTO w CheckOutDTO
            BookDTO bookDTO = BookServiceImpl.bookConvertToDTO(book);
            checkOutDTO.setBook(bookDTO);
    
            // Konwertuj CheckOutDTO na encję i zaktualizuj w bazie danych
            CheckOut checkOut = CheckOutServiceImpl.checkOutDTOConvertToEntity(checkOutDTO);
            CheckOut updatedCheckOut = checkOutService.updateCheckOut(id, checkOut);
    
            // Konwertuj zaktualizowaną encję na DTO i zwróć w odpowiedzi
            CheckOutDTO updatedCheckOutDTO = CheckOutServiceImpl.checkOutConvertToDTO(updatedCheckOut);
            return new ResponseEntity<>(updatedCheckOutDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("${api.checkout.by.id}")
    public ResponseEntity<Void> deleteCheckOut(@PathVariable Long id) {
        boolean exists = checkOutService.deleteCheckOut(id);
        if (exists) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
