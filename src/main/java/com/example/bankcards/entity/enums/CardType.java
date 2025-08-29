package com.example.bankcards.entity.enums;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CardType {
    VISA(1, "Visa",           "411111", "4\\d{12,15}",   16),
    MASTERCARD(2, "MasterCard","511111", "5[1-5]\\d{14}", 16),
    AMERICAN_EXPRESS(3, "American Express","371111","3[47]\\d{13}", 15),
    BANK_SPECIFIC(4, "Bank Specific","220220","220[0-2]\\d{12}", 16);

    public static final int GROUP = 4;
    public static final Pattern MASKED_CARD_PATTERN = Pattern.compile("^(\\*{4} ?)*\\d{4}$");

    private final int    typeCode;
    private final String displayName;
    private final String prefixSample;
    private final String panRegex;
    private final int    length;

    private final Pattern pattern;

    CardType(int typeCode, String displayName, String prefixSample, String panRegex, int length) {
        this.typeCode     = typeCode;
        this.displayName  = displayName;
        this.prefixSample = prefixSample;
        this.panRegex     = panRegex;
        this.length       = length;
        this.pattern      = Pattern.compile("^" + panRegex + "$");
    }

    public static int minCode() { return 1; }
    public static int maxCode() { return values().length; }

    public static final Pattern CARD_NUMBER_PATTERN = Pattern.compile(
            "^(" + Stream.of(values()).map(CardType::getPanRegex).collect(Collectors.joining("|")) + ")$"
                                                                     );

    public static Optional<CardType> detect(String pan) {
        if (pan == null) return Optional.empty();
        String digits = pan.replaceAll("\\s+", "");
        return Stream.of(values()).filter(t -> t.pattern.matcher(digits).matches()).findFirst();
    }

    public static final Map<Integer, CardType> BY_CODE =
            Stream.of(values()).collect(Collectors.toUnmodifiableMap(CardType::getTypeCode, t -> t));

    public static String mask(String pan) {
        if (pan == null || pan.length() <= GROUP) return pan;
        String digits = pan.replaceAll("\\s+", "");
        int cut = digits.length() - GROUP;
        String masked = "*".repeat(cut) + digits.substring(cut);
        return masked.replaceAll("(?<=\\G.{4})(?=.)", " ");
    }
}