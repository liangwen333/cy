package com.lw.db;

public interface Filter<T> {
    public boolean filterData(T t);
}
