package org.ozyegin.cs.entity;

import com.google.common.base.Objects;

public class Sample {
  private int id;
  private String name;
  private String data;
  private int value;

  public Sample() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  public Sample id(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Sample name(String name) {
    this.name = name;
    return this;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Sample data(String data) {
    this.data = data;
    return this;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Sample value(int value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sample sample = (Sample) o;
    return getValue() == sample.getValue() &&
        Objects.equal(getName(), sample.getName()) &&
        Objects.equal(getData(), sample.getData());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName(), getData(), getValue());
  }
}
