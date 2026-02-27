package com.saasential.billing.repositories;

import com.saasential.billing.enums.SubscriptionStatus;
import com.saasential.billing.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    
    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    List<Subscription> findByStatusAndEndDateBefore(SubscriptionStatus status, LocalDateTime dateTime);
}
