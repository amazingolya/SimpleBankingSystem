package banking.storage;

import java.util.Random;

public class CardInfoGenerator {
    Random random = new Random();

    public String generateNumber() {
        String cardId = "400000" + getCardId();
        return cardId + findChecksum(cardId);
    }

    public String generatePin() {
        StringBuilder builder = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public String getCardId() {
        StringBuilder builder = new StringBuilder(9);
        for (int i = 0; i < 9; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public int findChecksum(final String cardIdentifier) {
        long number = Long.parseLong(cardIdentifier);
        int[] digits = new int[15];
        for (int i = 14; i >= 0; i--) {
            digits[i] = (int) (number % 10);
            number /= 10;
        }
        for (int i = 0; i < 15; i += 2) {
            digits[i] *= 2;
            if (digits[i] > 9) {
                digits[i] -= 9;
            }
        }
        int sum = 0;
        for (int digit : digits) {
            sum += digit;
        }
        return sum % 10 == 0 ? 0 : 10 - sum % 10;
    }
}
