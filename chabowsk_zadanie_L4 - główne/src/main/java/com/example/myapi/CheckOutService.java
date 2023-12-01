package com.example.myapi;

import java.util.List;

public interface CheckOutService {
    List<CheckOut> getAllCheckOuts();

    CheckOut getCheckOutById(Long id);

    CheckOut createCheckOut(CheckOut checkOut);

    CheckOut updateCheckOut(Long id, CheckOut checkOut);

    boolean deleteCheckOut(Long id);

    //CheckOutDTO checkOutConvertToDTO(CheckOut checkOut);
}
