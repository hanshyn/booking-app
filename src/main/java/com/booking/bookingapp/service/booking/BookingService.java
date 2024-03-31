package com.booking.bookingapp.service.booking;

import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import java.util.List;

public interface BookingService {
    BookingResponseDto save(BookingRequestDto requestDto);

    List<BookingResponseDto> search();

    List<BookingResponseDto> getByUser();

    BookingResponseDto getById(Long id);

    BookingResponseDto updateById(Long id, BookingRequestDto requestDto);

    void deleteById(Long id);
}
