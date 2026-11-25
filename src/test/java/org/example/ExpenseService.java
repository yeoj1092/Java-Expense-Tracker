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

    @Test
    void updateAmountForExistingExpense(){
        ExpenseService service = new ExpenseService();
        UUID id = UUID.randomUUID();
        service.addExpense(new Expense(id, "coffee", new BigDecimal("5.50"), Category.FOOD, LocalDate.now()));

        boolean updated = service.updateAmount(id, new BigDecimal("6.25"));

        assertTrue(updated);
        assertEquals(new BigDecimal("6.25"), service.getAllExpenses().getFirst().amount());
        assertEquals(new BigDecimal("6.25"), service.totalAmount());

    }

    @Test
    void returnFalseWhenUpdatingMissingExpense(){
        ExpenseService service = new ExpenseService();

        boolean updated = service.updateAmount(UUID.randomUUID(), new BigDecimal("6.25"));

        assertFalse(updated);
    }

    @Test
    void removeExistingExpenseById(){
        ExpenseService service = new ExpenseService();
        UUID id = UUID.randomUUID();
        service.addExpense(new Expense(id, "coffee", new BigDecimal("5.50"),Category.FOOD, LocalDate.now()));

        boolean removed = service.removeExpense(id);

        assertTrue(removed);
        assertTrue(service.getAllExpenses().isEmpty());
        assertEquals(BigDecimal.ZERO, service.totalAmount());
    }

    @Test
    void returnFalseWhenRemovingMissingExpense(){
        ExpenseService service = new ExpenseService();

        assertFalse(service.removeExpense((UUID.randomUUID())));
    }
}
