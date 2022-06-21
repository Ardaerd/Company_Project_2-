package org.ozyegin.cs.entity;

import com.google.common.base.Objects;

public class Product {
  private int id;
  private String name;
  private String description;
  private String brandName;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int id_1(int id) {
    this.id = id;
    return id;
  }

  public Product id_2(int id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Product name(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Product description(String description) {
    this.description = description;
    return this;
  }

  public String getBrandName() {
    return brandName;
  }

  public void setBrandName(String brandName) {
    this.brandName = brandName;
  }

  public Product brandName(String brandName) {
    this.brandName = brandName;
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
    Product product = (Product) o;
    return getId() == product.getId() &&
            Objects.equal(getName(), product.getName()) &&
            Objects.equal(getDescription(), product.getDescription()) &&
            Objects.equal(getBrandName(), product.getBrandName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId(), getName(), getDescription(), getBrandName());
  }
}
