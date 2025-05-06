package com.booking.bookingapp.repository.accommodation;

import com.booking.bookingapp.model.Accommodation;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)

    @NonNull
    Optional<Accommodation> findById(@NonNull Long id);

    @NonNull
    @Query("SELECT accom from Accommodation accom WHERE accom.isActive = true")
    Page<Accommodation> findAll(@NonNull Pageable pageable);

    Set<Accommodation> findAccommodationByAmenities_Id(Long amenitiesId);
}
