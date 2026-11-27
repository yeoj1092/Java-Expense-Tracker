package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.Scanner;


public class Main {
    private static final ExpenseService service = new ExpenseService();
        public static void main(String[] args){
            try(Scanner scanner = new Scanner(System.in)){
                boolean running = true;
                while(running){
                    printMenu();
                    String choice = scanner.nextLine().trim();

                    switch(choice){
                        case "1" -> handleAddExpense(scanner);
                        case "2" -> handleListExpense();
                        case "0" -> {
                            running = false;
                            System.out.println("Goodbye!");
                        }
                        default -> System.out.println("Invalid option. Please choose 0-6.");
                    }
                    System.out.println();
                }
            }
        }
        private static void printMenu(){
            System.out.println("=== Expense Tracker ===");
            System.out.println("1. Add expense");
            System.out.println("2. View all expenses");
            System.out.println("3. Filter by category");
            System.out.println("4. View totals (overall / category / month)");
            System.out.println("5. Update expense amount");
            System.out.println("6. Delete expense");
            System.out.println("0. Exit");
            System.out.print("Select an option. ");
       }
       private static void handleAddExpense(Scanner scanner){
            System.out.println("Description:  ");
            String description = scanner.nextLine().trim();

            BigDecimal amount = readBigDecimal(scanner, "Amount (e.g. 5.50): ");
            Category category = readCategory(scanner);
            LocalDate date = readOptionalDate(scanner, "Date (YYYY-MM-DD, blank for today): ");

            Expense expense = new Expense(UUID.randomUUID(), description, amount, category, date);
            service.addExpense(expense);
            System.out.println("Added: "+ expense);
       }
       private static void handleListExpense(){
            List<Expense> expenses = service.getAllExpenses();
            if(expenses.isEmpty()){
                System.out.println("No expenses recorded yet.");
                return;
            }
            expenses.forEach(System.out::println);
       }


    private static BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        while(true){
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try{
                return new BigDecimal(input);
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static Category readCategory(Scanner scanner) {
        while(true){
            System.out.print("Category (FOOD, TRANSPORT, UTILITIES, ENTERTAINMENT, OTHER): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try{
                return Category.valueOf(input);
            }catch(IllegalArgumentException e){
                System.out.println("Invalid category. Try again.");
            }
        }
    }
    private static LocalDate readOptionalDate(Scanner scanner, String prompt){
            while(true){
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if(input.isEmpty()){
                    return LocalDate.now();
                }
                try{
                    return LocalDate.parse(input);
                }catch(DateTimeParseException e ){
                    System.out.println("Invalid date format. Use YYYY-MM-DD or leave blank.");
                }
            }
    }
}