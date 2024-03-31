package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.service.booking.BookingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto create(@RequestBody BookingRequestDto requestDto) {
        return bookingService.save(requestDto);
    }

    @GetMapping()
    public List<BookingResponseDto> search() {
        return bookingService.search();
    }

    @GetMapping("/my")
    public List<BookingResponseDto> getByUser() {
        return bookingService.getByUser();
    }

    @GetMapping("/{id}")
    public BookingResponseDto getById(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @PutMapping("/{id}")
    public BookingResponseDto updateById(@PathVariable Long id,
                                         @RequestBody BookingRequestDto requestDto) {
        return bookingService.updateById(id, requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteById(id);
    }
}
