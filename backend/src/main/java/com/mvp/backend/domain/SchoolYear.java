package com.mvp.backend.domain;

public enum SchoolYear {
    YEAR_FIVE("Year Five", 5),
    YEAR_SIX("Year Six", 6),
    YEAR_SEVEN("Year Seven", 7),
    YEAR_EIGHT("Year Eight", 8),
    YEAR_NINE("Year Nine", 9),
    YEAR_TEN("Year Ten", 10),
    YEAR_ELEVEN("Year Eleven", 11),
    YEAR_TWELVE("Year Twelve", 12),
    YEAR_THIRTEEN("Year Thirteen", 13);

    private final String label;
    private final int number;

    SchoolYear(String label, int number) {
        this.label = label;
        this.number = number;
    }

    public String getLabel() {
        return label;
    }

    public int getNumber() {
        return number;
    }
}
