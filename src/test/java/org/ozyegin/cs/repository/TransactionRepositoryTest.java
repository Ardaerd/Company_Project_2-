package org.ozyegin.cs.repository;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import org.junit.Test;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.entity.Product;

public class TransactionRepositoryTest extends IntegrationTestSuite  {

  @Test
  public void orderSimple() throws Exception {
    List<Product> products = generateProducts(1);
    List<Company> companies = generateCompany(1);
    List<Integer> ids = productRepository.create(products);
    companyRepository.create(companies.get(0));
    produceRepository.produce(companies.get(0).getName(), ids.get(0), 100);
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 100,
        new Date(Instant.now().toEpochMilli()));
  }

  @Test
  public void order100() throws Exception {
    List<Product> products = generateProducts(10);
    List<Company> companies = generateCompany(10);
    List<Integer> ids = productRepository.create(products);
    for (int i=0;i<10;i++) {
      products.get(i).setId(ids.get(i));
    }
    for (Company company: companies) {
      companyRepository.create(company);
    }
    for (Company company: companies) {
      for (int i=0;i<10;i++) {
        products.get(i).setId(ids.get(i));
        produceRepository.produce(company.getName(), products.get(i).getId(), 100);
        transactionRepository.order(company.getName(), products.get(i).getId(), 100,
            new Date(Instant.now().toEpochMilli()));
      }
    }
  }

  @Test
  public void orderDelete() throws Exception {
    List<Product> products = generateProducts(1);
    List<Company> companies = generateCompany(1);
    List<Integer> ids = productRepository.create(products);
    companyRepository.create(companies.get(0));
    Integer produceId = produceRepository.produce(companies.get(0).getName(), ids.get(0), 100);
    Integer order = transactionRepository.order(companies.get(0).getName(), ids.get(0), 50,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.delete(order);
    produceRepository.delete(produceId);
  }

  @Test(expected = Exception.class)
  public void orderNotExist() throws Exception {
    transactionRepository.delete(random(100));
  }

  @Test(expected = Exception.class)
  public void orderCannotExceedCapacity() throws Exception {
    List<Product> products = generateProducts(1);
    List<Company> companies = generateCompany(1);
    List<Integer> ids = productRepository.create(products);
    companyRepository.create(companies.get(0));
    produceRepository.produce(companies.get(0).getName(), ids.get(0), 100);
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 75,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 75,
        new Date(Instant.now().toEpochMilli()));
  }

  @Test
  public void orderCannotExceedCapacity2() throws Exception {
    List<Product> products = generateProducts(1);
    List<Company> companies = generateCompany(1);
    List<Integer> ids = productRepository.create(products);
    companyRepository.create(companies.get(0));
    produceRepository.produce(companies.get(0).getName(), ids.get(0), 100);
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 25,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 25,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 25,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), ids.get(0), 25,
        new Date(Instant.now().toEpochMilli()));
  }
}
