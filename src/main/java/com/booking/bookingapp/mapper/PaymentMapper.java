package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.payment.PaymentCancelDto;
import com.booking.bookingapp.dto.payment.PaymentResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = {BookingMapper.class})
public interface PaymentMapper {
    @Mappings({
            @Mapping(source = "id", target = "paymentId"),
            @Mapping(source = "sessionUrl", target = "paymentUrl"),
            @Mapping(source = "booking.id", target = "bookingId")
    })
    PaymentResponseDto toDto(Payment payment);

    PaymentCancelDto toDto(Payment payment, String message);

    @Mappings({
            @Mapping(source = "id", target = "paymentId"),
            @Mapping(source = "booking.id", target = "bookingId"),
            @Mapping(source = "status", target = "paymentStatus")
    })
    PaymentSuccessDto toSuccessDto(Payment payment);
}
