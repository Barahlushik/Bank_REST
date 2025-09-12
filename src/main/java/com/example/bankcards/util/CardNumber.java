package com.example.bankcards.util;

import com.example.bankcards.entity.enums.CardType;
import lombok.experimental.UtilityClass;
import java.security.SecureRandom;

@UtilityClass
public class CardNumber {

    private static final SecureRandom random = new SecureRandom();

    public static String generate(CardType type) {
        StringBuilder sb = new StringBuilder(type.getPrefix());
        while (sb.length() < type.getLength() - 1) {
            sb.append(random.nextInt(10));
        }
        int checkDigit = calculateLuhnCheckDigit(sb.toString());
        sb.append(checkDigit);

        return sb.toString();
    }

    private static int calculateLuhnCheckDigit(String numberWithoutCheck) {
        int sum = 0;
        boolean doubleIt = true;
        for (int i = numberWithoutCheck.length() - 1; i >= 0; i--) {
            int digit = numberWithoutCheck.charAt(i) - '0';
            if (doubleIt) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            doubleIt = !doubleIt;
        }

        return (10 - (sum % 10)) % 10;
    }
}
