package org.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.UUID;

/// Stores expenses in memory and provides filtering and total calculations.
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();
    /**
     * Adds a non-null expense to the list.
     *
     * @param expense the expense to record
     * @throws NullPointerException if {@code expense} is null
     */
    public void addExpense(Expense expense){
        Objects.requireNonNull(expense, "expense cannot be null");
        expenses.add(expense);
    }
    /**
     * Returns an unmodifiable view of all recorded expenses.
     *
     * @return immutable list of expenses
     */
    public List<Expense> getAllExpenses() {
        return Collections.unmodifiableList(expenses);
    }
    /**
     * Finds all expenses matching the given category.
     *
     * @param category target expense category
     * @return list of matching expenses (empty if none match)
     * @throws NullPointerException if {@code category} is null
     */
    public List<Expense> findByCategory(Category category){
        Objects.requireNonNull(category, "category cannot be null");
        return expenses.stream()
                .filter(e -> e.category() == category)
                .toList();
    }
    /**
     * Calculates the combined amount of all recorded expenses.
     *
     * @return total amount across all expenses, or {@link BigDecimal#ZERO} if empty
     */
    public BigDecimal totalAmount(){
        return expenses.stream()
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    /**
     * Calculates the combined amount for a specific category.
     *
     * @param category target expense category
     * @return total amount for the category, or {@link BigDecimal#ZERO} if none match
     * @throws NullPointerException if {@code category} is null
     */
    public BigDecimal totalByCategory(Category category){
        Objects.requireNonNull(category, "category cannot be null");
        return expenses.stream()
                .filter(e -> e.category() == category)
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean updateAmount(UUID id, BigDecimal newAmount) {
        Objects.requireNonNull(id, "id cannot be null");
        for (int i = 0; i < expenses.size(); i++){
            Expense current = expenses.get(i);
            if (current.id().equals(id)){
                Expense updated = new Expense(
                        current.id(),
                        current.description(),
                        newAmount,
                        current.category(),
                        current.date()
                );
                expenses.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean removeExpense(UUID id){
        Objects.requireNonNull(id, "id cannot be null");
        return expenses.removeIf(expense -> expense.id().equals(id));
    }
}