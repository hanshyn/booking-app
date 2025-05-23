package com.booking.bookingapp.service.booking;

import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import com.booking.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.booking.bookingapp.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface BookingService {
    BookingResponseDto save(BookingRequestDto requestDto, UriComponentsBuilder uri, User user);

    List<BookingResponseDto> search(BookingSearchParameters searchParameters, Pageable pageable);

    List<BookingResponseDto> getByUser(Pageable pageable);

    BookingResponseDto getById(Long id);

    BookingResponseDto updateById(Long id, UpdateBookingRequestDto requestDto,
                                  UriComponentsBuilder urlBuilder);

    void deleteById(Long id);
}
