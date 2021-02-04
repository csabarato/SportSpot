package com.sportspot.sportspot.constants;

public enum ActivityFilter {

    ALL_ACTIVITIES("all"),
    MY_ACTIVITIES("my"),
    SIGNED_UP_ACTIVITIES("signedUp"),
    OPEN_ACTIVITIES("open");

    private String filterValue;

    ActivityFilter(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public static ActivityFilter getActivityFilterByValue(String filterValue) {
        for (ActivityFilter activityFilter : ActivityFilter.values()) {
            if (activityFilter.getFilterValue().equals(filterValue)) {
                return activityFilter;
            }
        }
        return null;
    }
}
