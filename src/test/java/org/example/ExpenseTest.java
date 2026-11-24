package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {
    @Test
    // check all data can be stored
    void creatValidExpenseWithExpectedFields(){
        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026,11,24);
        BigDecimal amount = new BigDecimal("5.50");

        Expense expense = new Expense(id, "Coffee", amount, Category.FOOD, date);

        assertEquals(id, expense.id());
        assertEquals("Coffee", expense.description());
        assertEquals(amount, expense.amount());
        assertEquals(Category.FOOD, expense.category());
        assertEquals(date, expense.date());
    }

    @Test
    // test blank/bad description
    void rejectsBlankDescription(){
        assertThrows(IllegalArgumentException.class, () ->
                new Expense(
                        UUID.randomUUID(),
                        "   ",
                        new BigDecimal("5.50"),
                        Category.FOOD,
                        LocalDate.now()
                )
        );
    }

    @Test
    // test -
    void rejectsNonPositiveAmount(){
        assertThrows(IllegalArgumentException.class, () ->
                new Expense(
                        UUID.randomUUID(),
                        "Coffee",
                        BigDecimal.ZERO,
                        Category.FOOD,
                        LocalDate.now()
                )
        );
    }

    @Test
    void allowsNegativeFiveAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                new Expense(
                        UUID.randomUUID(),
                        "Refund or adjustment",
                        new BigDecimal("-5.00"),
                        Category.FOOD,
                        LocalDate.now()
                )
        );
    }
}