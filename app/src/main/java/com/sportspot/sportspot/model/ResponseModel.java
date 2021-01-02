package com.sportspot.sportspot.model;

import java.util.Collections;
import java.util.List;

public class ResponseModel<T> {

    private T data;
    private List<String> errors = Collections.emptyList();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
