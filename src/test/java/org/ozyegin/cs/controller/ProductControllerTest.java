package org.ozyegin.cs.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import org.ozyegin.cs.entity.Product;

public class ProductControllerTest extends IntegrationTestSuite {
  // 10 points
  @Test
  public void create() {
    List<Product> products = generateProducts(44);
    List<Integer> ids = Arrays.asList(
        postMethod("/product", Integer[].class, products, new ArrayList<>(), new ArrayList<>()));
    List<Product> productList = productRepository.findMultiple(ids);
    for (int i=0;i<44;i++) {
      products.get(i).setId(ids.get(i));
    }
    assertTwoListEqual(products, productList);
  }

  // 10 points
  @Test
  public void createFail() {
    List<Product> products = generateProducts(44);
    products.get(43).setName(null);
    List<Integer> ids = Arrays.asList(
        postMethod("/product", Integer[].class, products, new ArrayList<>(), new ArrayList<>()));
    Assert.assertTrue(ids.isEmpty());
  }

  // 5 points
  @Test
  public void update() {
    List<Product> products = generateProducts(44);
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<44;i++) {
      products.get(i).setId(ids.get(i));
      products.get(i).setDescription("111");
    }
    putMethod("/product", Integer[].class, products, new ArrayList<>(), new ArrayList<>());
    List<Product> productList = productRepository.findMultiple(ids);
    assertTwoListEqual(products, productList);
  }

  // 10 points
  @Test
  public void updateFailed() {
    List<Product> products = generateProducts(44);
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<44;i++) {
      products.get(i).setId(ids.get(i));
    }
    List<Product> products2update = products.stream().map(a -> {
      Product product = new Product();
      product.setId(a.getId());
      product.setName(a.getName());
      product.setDescription("aaa");
      product.setBrandName(a.getBrandName());
      return product;
    }).collect(Collectors.toList());
    products2update.get(43).setName(null);
    putMethod("/product", Integer[].class, products2update, new ArrayList<>(), new ArrayList<>());
    List<Product> productList = productRepository.findMultiple(ids);
    assertTwoListEqual(products, productList);
  }
}
