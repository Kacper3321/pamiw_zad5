package com.example.myapi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {
    // Tutaj możesz dodać niestandardowe metody zapytań, jeśli są potrzebne
}
