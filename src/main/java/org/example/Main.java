package org.example;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args){
        var coffee = Expense.of("coffee", new BigDecimal("5.50"), Category.FOOD);
        System.out.println(coffee);
    }
}