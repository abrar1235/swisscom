package com.swisscom.operations.model;

import lombok.Getter;

@Getter
public class Result<T, V> {

    private T success;
    private V failure;
    private String status;

    private static class Success<T, V> extends Result<T, V> {
        private final T t;

        public Success(T success) {
            this.t = success;
        }
        @Override
        public T getSuccess() {
            return t;
        }
        @Override
        public V getFailure() {
            return null;
        }
        @Override
        public String getStatus() {
            return "success";
        }
    }

    private static class Failure<T, V> extends Result<T, V> {
        private final V v;

        public Failure(V failure) {
            this.v = failure;
        }

        @Override
        public T getSuccess() {
            return null;
        }
        @Override
        public V getFailure() {
            return v;
        }

        @Override
        public String getStatus() {
            return "failure";
        }
    }

    public static <T, V> Result<T, V> success(T success) {
        return new Success<>(success);
    }

    public static <T, V> Result<T, V> failure(V failure) {
        return new Failure<>(failure);
    }

    private Result() {
    }
}
