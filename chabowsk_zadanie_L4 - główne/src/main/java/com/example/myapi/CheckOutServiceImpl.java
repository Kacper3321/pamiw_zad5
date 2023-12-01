package com.example.myapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

@Service
public class CheckOutServiceImpl implements CheckOutService {

    private final CheckOutRepository checkOutRepository;

    @Autowired
    public CheckOutServiceImpl(CheckOutRepository checkOutRepository) {
        this.checkOutRepository = checkOutRepository;
    }

    @Override
    public List<CheckOut> getAllCheckOuts() {
        return checkOutRepository.findAll();
    }

    @Override
    public CheckOut getCheckOutById(Long id) {
        Optional<CheckOut> checkOutOptional = checkOutRepository.findById(id);
        if (!checkOutOptional.isPresent()) {
            throw new EntityNotFoundException("CheckOut not found for id: " + id);
        }
        return checkOutOptional.get();
    }

    @Override
    @Transactional
    public CheckOut createCheckOut(CheckOut checkOut) {
        return checkOutRepository.save(checkOut);
    }

    @Override
    @Transactional
    public CheckOut updateCheckOut(Long id, CheckOut updatedCheckOut) {
        CheckOut checkOut = getCheckOutById(id);
        checkOut.setBook(updatedCheckOut.getBook());
        checkOut.setCheckoutDate(updatedCheckOut.getCheckoutDate());
        checkOut.setReturnDays(updatedCheckOut.getReturnDays());
        checkOut.setBorrowerFullName(updatedCheckOut.getBorrowerFullName());
        return checkOutRepository.save(checkOut);
    }

    @Override
    @Transactional
    public boolean deleteCheckOut(Long id) {
        if (checkOutRepository.existsById(id)) {
            checkOutRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    //@Override
    public static CheckOutDTO checkOutConvertToDTO(CheckOut checkOut) {
        CheckOutDTO dto = new CheckOutDTO();
        dto.setId(checkOut.getId());
        dto.setBook(BookServiceImpl.bookConvertToDTO(checkOut.getBook())); // konwersja książki na DTO
        dto.setBorrowerFullName(checkOut.getBorrowerFullName());
        dto.setCheckoutDate(checkOut.getCheckoutDate());
        dto.setReturnDays(checkOut.getReturnDays());
        dto.setBookId(checkOut.getBook().getId());
        return dto;
    }

    public static CheckOut checkOutDTOConvertToEntity(CheckOutDTO dto) {
        CheckOut checkOut = new CheckOut();
        checkOut.setId(dto.getId());
        checkOut.setBook(BookServiceImpl.bookDTOConvertToEntity(dto.getBook()));
        checkOut.setBorrowerFullName(dto.getBorrowerFullName());
        checkOut.setCheckoutDate(dto.getCheckoutDate());
        checkOut.setReturnDays(dto.getReturnDays());
        return checkOut;
    }
}
