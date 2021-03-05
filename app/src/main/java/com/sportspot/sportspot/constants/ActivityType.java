package com.sportspot.sportspot.constants;

public enum ActivityType {

    ALL_ACTIVITIES("all"),
    MY_ACTIVITIES("my"),
    SIGNED_UP_ACTIVITIES("signedUp"),
    OPEN_ACTIVITIES("open");

    private String filterValue;

    ActivityType(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public static ActivityType getActivityTypeByValue(String filterValue) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getFilterValue().equals(filterValue)) {
                return activityType;
            }
        }
        return null;
    }
}
