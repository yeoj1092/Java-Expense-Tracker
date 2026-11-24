package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

    @Test

    void addsExpenseAndReturnsAllExpenses(){
        ExpenseService service = new ExpenseService();
        Expense coffee = new Expense(
                UUID.randomUUID(),
                "Coffee",
                new BigDecimal("5.50"),
                Category.FOOD,
                LocalDate.now()
        );

        service.addExpense(coffee);

        List<Expense> all = service.getAllExpenses();
        assertEquals(1, all.size());
        assertEquals(coffee, all.getFirst());
    }

    @Test

    void rejectsNullExpense(){
        ExpenseService service = new ExpenseService();

        assertThrows(NullPointerException.class, () -> service.addExpense(null));
    }

    @Test

    void returnsUnmodifiableListFromGetAllExpenses(){
        ExpenseService service = new ExpenseService();
        Expense coffee = new Expense(
                UUID.randomUUID(),
                "Coffee",
                new BigDecimal("5.50"),
                Category.FOOD,
                LocalDate.now()
        );
        service.addExpense(coffee);

        List<Expense> all = service.getAllExpenses();

        assertThrows(UnsupportedOperationException.class, all::clear);
    }

    @Test

    void filterAndSumByCategoryAccurately(){
        ExpenseService service = new ExpenseService();
        service.addExpense(new Expense(UUID.randomUUID(), "Coffee", new BigDecimal("5.50"), Category.FOOD, LocalDate.now()));
        service.addExpense(new Expense(UUID.randomUUID(), "Lunch", new BigDecimal("14.50"), Category.FOOD, LocalDate.now()));
        service.addExpense(new Expense(UUID.randomUUID(), "Bus", new BigDecimal("3.20"), Category.TRANSPORT, LocalDate.now()));

        assertEquals(2, service.findByCategory(Category.FOOD).size());
        assertEquals(new BigDecimal("23.20"), service.totalAmount());
        assertEquals(new BigDecimal("20.00"), service.totalByCategory(Category.FOOD));

    }
}
