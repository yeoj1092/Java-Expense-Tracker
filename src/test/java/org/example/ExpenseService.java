package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
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
    void findsExpenseWithinInclusiveDataRange(){
        ExpenseService service = new ExpenseService();
        UUID id = UUID.randomUUID();
        service.addExpense(new Expense(UUID.randomUUID(),"Early", new BigDecimal("4.00"),Category.FOOD, LocalDate.of(2026, 10, 31)));
        service.addExpense(new Expense(id, "Coffee", new BigDecimal("5.50"),Category.FOOD, LocalDate.of(2026,11,10)));
        service.addExpense(new Expense(UUID.randomUUID(),"Late", new BigDecimal("8.00"),Category.FOOD, LocalDate.of(2026, 11, 20)));

        List<Expense> results = service.findByDateRange(LocalDate.of(2026,11,1),LocalDate.of(2026,11,10));

        assertEquals(1, results.size());
        assertEquals(id, results.getFirst().id());
    }

    @Test
    void rejectsEndDateBeforeStartDate(){
        ExpenseService service = new ExpenseService();

        assertThrows(IllegalArgumentException.class, () ->
                service.findByDateRange(LocalDate.of(2026, 11,15), LocalDate.of(2026, 11,10)));
    }

    @Test
    void calculateTotalByMonth(){
        ExpenseService service = new ExpenseService();
        service.addExpense((new Expense(UUID.randomUUID(), "Coffee", new BigDecimal("5.50"), Category.FOOD,LocalDate.of(2026,11,5))));
        service.addExpense((new Expense(UUID.randomUUID(), "Lunch", new BigDecimal("14.50"), Category.FOOD,LocalDate.of(2026,11,10))));
        service.addExpense((new Expense(UUID.randomUUID(), "Oat Snack", new BigDecimal("9.00"), Category.FOOD,LocalDate.of(2026,10,25))));

        assertEquals(new BigDecimal("20.00"), service.totalByMonth(YearMonth.of(2026,11)));
        assertEquals(BigDecimal.ZERO, service.totalByMonth(YearMonth.of(2026,12)));
    }
}
