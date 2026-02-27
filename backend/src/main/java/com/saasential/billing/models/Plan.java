package com.saasential.billing.models;

import com.saasential.billing.enums.BillingCycle;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    @Column(nullable = false)
    private Integer apiLimit;

    @Column(nullable = false)
    private Integer storageLimit;

    @Column(nullable = false)
    private BigDecimal extraApiCost;

    @Column(nullable = false)
    private BigDecimal extraStorageCost;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
    
    @Column(nullable = false)
    @Builder.Default
    private int trialDays = 0;
}
