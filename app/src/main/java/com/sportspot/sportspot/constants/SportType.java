package com.sportspot.sportspot.constants;

import java.io.Serializable;

public enum SportType implements Serializable {
    BASKETBALL("Basketball"),
    FOOTBALL("Football"),
    TABLE_TENNIS("Table tennis"),
    TENNIS("Tennis"),
    TEQBALL("Teqball");

    private String name;

    SportType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SportType getSportTypeByName(String name) {
        for (SportType sportType : SportType.values()) {
            if (sportType.getName().equals(name)) {
                return sportType;
            }
        }
        return null;
    }
}
