package org.ozyegin.cs.repository;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.ozyegin.cs.entity.Product;

public class ProductRepositoryTest extends IntegrationTestSuite {

  @Test
  public void createSimple() {
    List<Product> products = generateProducts(4);
    List<Integer> ids = productRepository.create(products);
    List<Product> productList = productRepository.findMultiple(ids);
    for (int i=0;i<4;i++) {
      products.get(i).setId(ids.get(i));
    }
    assertTwoListEqual(products, productList);
  }

  @Test(expected = Exception.class)
  public void createNotNull() {
    List<Product> products = generateProducts(4);
    products.get(3).setName(null);
    productRepository.create(products);
  }

  @Test
  public void createReturnIds() {
    List<Product> products = generateProducts(4);
    List<Integer> ids = productRepository.create(products);
    List<Product> products2 = generateProducts(4);
    List<Integer> ids2 = productRepository.create(products2);
    assertTwoListEqual(Arrays.asList(4+ids.get(0),4+ids.get(1),4+ids.get(2),4+ids.get(3)), ids2);
    List<Product> products3 = generateProducts(4);
    List<Integer> ids3 = productRepository.create(products3);
    assertTwoListEqual(Arrays.asList(4+ids2.get(0),4+ids2.get(1),4+ids2.get(2),4+ids2.get(3)), ids3);
  }

  @Test
  public void find() {
    List<Product> products = generateProducts(4);
    List<Integer> ids = productRepository.create(products);
    Product product = productRepository.find(ids.get(0));
    products.get(0).setId(ids.get(0));
    Assert.assertEquals(products.get(0), product);
  }

  @Test
  public void findMultiple() {
    List<Product> products = generateProducts(4);
    List<Integer> ids = productRepository.create(products);
    List<Product> dbProducts = productRepository.findMultiple(ids);
    for (int i=0;i<4;i++) {
      products.get(i).setId(ids.get(i));
    }
    assertTwoListEqual(products, dbProducts);
  }

  @Test
  public void findByBrandName() {
    List<Product> products = generateProducts(44);
    for(int i=0;i<22;i++) {
      products.get(i).setBrandName("acme");
    }
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<44;i++) {
      products.get(i).setId(ids.get(i));
    }
    List<Product> dbProducts = productRepository.findByBrandName("acme");

    assertTwoListEqual(products.subList(0,22), dbProducts);
  }

  @Test
  public void updateSimple() {
    List<Product> products = generateProducts(4);
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<4;i++) {
      products.get(i).setId(ids.get(i));
      products.get(i).setBrandName("brand");
    }
    productRepository.update(products);
    List<Product> dbProducts = productRepository.findMultiple(ids);
    assertTwoListEqual(products, dbProducts);
  }

  @Test(expected = Exception.class)
  public void updateNameNotNull() {
    List<Product> products = generateProducts(4);
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<4;i++) {
      products.get(i).setId(ids.get(i));
      products.get(i).setName(null);
    }
    productRepository.update(products);
  }

  @Test
  public void deleteSimple() {
    List<Product> products = generateProducts(44);
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<44;i++) {
      products.get(i).setId(ids.get(i));
    }
    productRepository.delete(ids.subList(11,33));
    products.removeAll(products.subList(11,33));
    List<Product> dbProducts = productRepository.findMultiple(ids);

    assertTwoListEqual(products, dbProducts);
  }
}
