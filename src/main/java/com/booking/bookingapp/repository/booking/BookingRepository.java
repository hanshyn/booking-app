package com.booking.bookingapp.repository.booking;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface BookingRepository extends JpaRepository<Booking, Long>,
        JpaSpecificationExecutor<Booking> {

    @EntityGraph(attributePaths = {"accommodation", "status", "accommodation.location",
            "accommodation.amenities"})
    @NonNull
    Optional<Booking> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"accommodation", "user", "status", "accommodation.location",
            "accommodation.amenities"})
    @NonNull
    Page<Booking> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"accommodation", "user", "status", "accommodation.location",
            "accommodation.amenities"})
    @NonNull
    Page<Booking> findAll(@NonNull Specification<Booking> specification,
                          @NonNull Pageable pageable);

    //List<Booking> findBookingByUserId(Long id);

    @EntityGraph(attributePaths = {"accommodation", "user", "status", "accommodation.location",
            "accommodation.amenities"})
    List<Booking> findBookingsByUser(User user, Pageable pageable);

    //List<Booking> findBookingsByUser(User user);

    //List<Booking> findAllByAccommodation_Id(Long id);

    //Long countAllByAccommodation_IdAndStatusIn(Long id, Collection<Booking.Status> status);

    @Query(value = "SELECT count(booking.accommodation.id) from Booking booking "
            + "where booking.accommodation.id = :accommodationId "
            + "and booking.status in (:statuses)"
            + "group by booking.accommodation.id"
    )
    Optional<Long> countAllByAccommodationIdAndStatuses(
            @Param("accommodationId") Long id,
            @Param("statuses") List<Booking.Status> statuses
    );

    Optional<Long> countAllByStatusAndUserId(Booking.Status status, Long id);

    List<Booking> findAllByCheckOutDateLessThanEqualAndStatusIn(LocalDate date,
                                                         List<Booking.Status> statuses);
}
