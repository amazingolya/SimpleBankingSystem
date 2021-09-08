package banking.ui;

import banking.controller.ProcessControl;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    State state = State.ENTER;
    final ProcessControl control = new ProcessControl();

    public void printMainMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    public void printCreationResult(final String cardNumber, final String pin) {
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
    }

    public void printClientMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    public void printTransferMoney() {
        System.out.println("Transfer");
        System.out.println("Enter card number");
        String cardNumber = scanner.next();
        switch (control.checkAccount(cardNumber)) {
            case -1: {
                System.out.println("Probably you made a mistake in the card number. Please try again!");
                printClientMenu();
                break;
            }
            case 0: {
                System.out.println("Such a card does not exist.");
                printClientMenu();
                break;
            }
            case 1: {
                System.out.println("Enter how much money you want to transfer:");
                if (!control.transferMoney(cardNumber, scanner.nextInt())) {
                    System.out.println("Not enough money");
                } else {
                    System.out.println("Success!");
                }
                printClientMenu();
                break;
            }
        }
    }

    public void interact(final String fileName) {
        control.createDataBase(fileName);
        printMainMenu();
        while (state != State.EXIT) {
            switch (scanner.nextInt()) {
                case 1: {
                    if (state == State.ENTER) {
                        Account account = control.manageCreation();
                        printCreationResult(account.getNumber(), account.getPin());
                        printMainMenu();
                    } else {
                        System.out.printf("Balance: %s\n", control.getBalance());
                        printClientMenu();
                    }
                    break;
                }
                case 2: {
                    if (state == State.ENTER) {
                        System.out.println("Enter your card number:");
                        String number = scanner.next();
                        System.out.println("Enter your PIN:");
                        String pin = scanner.next();
                        Account account = new Account(number, pin);
                        if (control.logIn(account)) {
                            System.out.println("You have successfully logged in!");
                            state = State.IN;
                            printClientMenu();
                        } else {
                            System.out.println("Wrong card number or PIN!\n");
                            printMainMenu();
                        }
                    } else {
                        System.out.println("Enter income:");
                        control.addIncome(scanner.nextInt());
                        System.out.println("Income was added!");
                        printClientMenu();
                    }
                    break;
                }
                case 3: {
                    printTransferMoney();
                    break;
                }
                case 4: {
                    control.closeAccount();
                    System.out.println("The account has been closed!");
                    state = State.ENTER;
                    printMainMenu();
                    break;
                }
                case 5: {
                    control.logOut();
                    state = State.ENTER;
                    printMainMenu();
                    break;
                }
                case 0: {
                    control.logOut();
                    state = State.EXIT;
                    break;
                }
            }
        }
    }
}
