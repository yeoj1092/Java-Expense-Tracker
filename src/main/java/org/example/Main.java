package org.example;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args){
        /// testing Expense
        var coffee = Expense.of("coffee", new BigDecimal("5.50"), Category.FOOD);
        System.out.println(coffee);

        var service = new ExpenseService();
        service.addExpense(Expense.of("coffee", new BigDecimal("5.50"), Category.FOOD));
        /// testing ExpenseService
        System.out.println("Count: " + service.getAllExpenses().size());
        System.out.println("Total: " + service.totalAmount());

    }
}