package com.booking.bookingapp.repository.accommodation;

import com.booking.bookingapp.model.Accommodation;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)

    @NonNull
    Optional<Accommodation> findById(@NonNull Long id);

    @EntityGraph(attributePaths = {"location", "amenities"})
    @NonNull
    Page<Accommodation> findAll(@NonNull Pageable pageable);
}
