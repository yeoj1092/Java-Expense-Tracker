package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record Expense(
    UUID id,
    String description,
    BigDecimal amount,
    Category category,
    LocalDate date
){
    public Expense {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(category, "category cannot be null");
        Objects.requireNonNull(date, "date cannot be null");

        if(description == null || description.isBlank()){
            throw new IllegalArgumentException("Description cannot be blank");
        }
        if ((amount == null) || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");

        }
    }
    public static Expense of(String description, BigDecimal amount, Category category){
        return new Expense(UUID.randomUUID(), description.trim(), amount, category, LocalDate.now());
    }
}
