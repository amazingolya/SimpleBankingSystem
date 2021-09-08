package banking.controller;

import banking.storage.AccountDataBase;
import banking.storage.CardInfoGenerator;
import banking.ui.*;

public class ProcessControl {
    private Account currentAccount = null;
    private final CardInfoGenerator generator = new CardInfoGenerator();
    private final AccountDataBase dataBase = new AccountDataBase();

    public void createDataBase(final String filename) {
        dataBase.createNewDatabase(filename);
    }

    public Account manageCreation() {
        String number = generator.generateNumber();
        String pin = generator.generatePin();
        dataBase.addEntry(number, pin, 0);
        return new Account(number, pin);
    }

    public boolean logIn(final Account account) {
        boolean contains = dataBase.contains(account.getNumber(), account.getPin());
        if (contains) {
            currentAccount = account;
        }
        return contains;
    }

    public long getBalance() {
        return currentAccount.getBalance();
    }

    public void addIncome(final int income) {
        currentAccount.setBalance(income);
        dataBase.updateBalance(income, currentAccount.getNumber());
    }

    public void logOut() {
        currentAccount = null;
    }

    public void closeAccount() {
        dataBase.deleteEntry(currentAccount.getNumber());
        logOut();
    }

    public int checkAccount(final String number) {
        int checksum = generator.findChecksum(number.substring(0, number.length() - 1));
        if (checksum != Integer.parseInt(number.substring(number.length() - 1))) {
            return -1;
        }
        return dataBase.accountExists(number) ? 1 : 0;
    }

    public boolean transferMoney(final String number, final int money) {
        boolean transferred = dataBase.makeTransfer(currentAccount.getNumber(), number, money);
        if (transferred) {
            currentAccount.setBalance(currentAccount.getBalance() - money);
        }
        return transferred;
    }
}
