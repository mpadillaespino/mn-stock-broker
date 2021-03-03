package com.mfpe.broker.persistence.jpa;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "quote")
@Table(name = "quotes", schema = "mn")
@Data
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = SymbolEntity.class)
    @JoinColumn(name = "symbol", referencedColumnName = "value")
    private SymbolEntity symbol;

    private BigDecimal bid;

    private BigDecimal ask;

    @Column(name = "last_price")
    private BigDecimal lastPrice;

    private BigDecimal volume;

}
