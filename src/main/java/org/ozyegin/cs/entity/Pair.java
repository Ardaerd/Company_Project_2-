package org.ozyegin.cs.entity;

import com.google.common.base.Objects;

public class Pair {
    private String key;
    private Integer value;

    public Pair(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {

    }
    public String getKey() {
        return this.key;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equal(getKey(), pair.getKey()) &&
            Objects.equal(getValue(), pair.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getKey(), getValue());
    }
}