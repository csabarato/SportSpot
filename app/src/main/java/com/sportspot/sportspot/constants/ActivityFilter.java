package com.sportspot.sportspot.constants;

public enum ActivityFilter {

    MY_ACTIVITIES("my"),
    SIGNED_UP_ACTIVITIES("signedUp");

    private String filterValue;

    ActivityFilter(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterValue() {
        return filterValue;
    }
}
