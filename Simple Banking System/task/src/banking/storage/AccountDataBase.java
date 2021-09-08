package banking.storage;

import java.sql.*;

public class AccountDataBase {
    private String url = "jdbc:sqlite:";
    private static final String createTable = "CREATE TABLE IF NOT EXISTS card (id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT, pin TEXT, balance INTEGER DEFAULT 0)";
    private static final String insertEntry = "INSERT INTO card(number, pin, balance) VALUES (?, ?, ?)";
    private static final String checkAccount = "SELECT id FROM card WHERE number = ? AND pin = ?";
    private static final String isAccountExists = "SELECT id FROM card WHERE number = ?";
    private static final String getBalance = "SELECT balance FROM card WHERE number = ?";
    private static final String increaseBalance = "UPDATE card SET balance = balance + ? WHERE number = ?";
    private static final String decreaseBalance = "UPDATE card SET balance = balance - ? WHERE number = ?";
    private static final String deleteEntry = "DELETE FROM card WHERE number = ?";

    public void createNewDatabase(String fileName) {
        url += fileName;
        try (Connection connection = DriverManager.getConnection(url)) {
            createNewTable(connection);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createNewTable(final Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEntry(final String number, final String pin, final int balance) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(insertEntry)) {
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, pin);
            preparedStatement.setInt(3, balance);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(final String number, final String pin) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(checkAccount)) {
            statement.setString(1, number);
            statement.setString(2, pin);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean accountExists(final String number) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(isAccountExists)) {
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateBalance(final int income, final String number) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(increaseBalance)) {
            statement.setInt(1, income);
            statement.setString(2, number);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEntry(final String number) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(deleteEntry)) {
            statement.setString(1, number);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean makeTransfer(final String from, final String to, final int money) {
        try (Connection connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);

            try (PreparedStatement withdraw = connection.prepareStatement(decreaseBalance);
                 PreparedStatement addMoney = connection.prepareStatement(increaseBalance);
                 PreparedStatement checkBalance = connection.prepareStatement(getBalance)) {

                checkBalance.setString(1, from);
                ResultSet set = checkBalance.executeQuery();
                if (set.getInt("balance") < money) {
                    return false;
                }

                withdraw.setInt(1, money);
                withdraw.setString(2, from);
                withdraw.executeUpdate();

                addMoney.setInt(1, money);
                addMoney.setString(2, to);
                addMoney.executeUpdate();

                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}