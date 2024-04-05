package com.booking.bookingapp.service.booking;

import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponseDto save(BookingRequestDto requestDto);

    List<BookingResponseDto> search(BookingSearchParameters searchParameters);

    List<BookingResponseDto> getByUser(Pageable pageable);

    BookingResponseDto getById(Long id);

    BookingResponseDto updateById(Long id, BookingRequestDto requestDto);

    void deleteById(Long id);
}
