package banking.ui;

public class Account {
    private final String number;
    private final String pin;
    private long balance = 0;

    public Account(final String number, final String pin) {
        this.number = number;
        this.pin = pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

}
