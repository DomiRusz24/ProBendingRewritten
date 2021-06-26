package com.probending.probending.util;

public class Pair<T, U> {

    private final T key;
    private final U value;

    public Pair(T key, U value) {
        this.key =key;
        this.value = value;
    }

    public U getValue() {
        return value;
    }

    public T getKey() {
        return key;
    }
}
