package org.ozyegin.cs.repository;

import java.util.List;
import org.junit.Test;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.entity.Product;

public class ProduceRepositoryTest extends IntegrationTestSuite  {

  @Test
  public void produceSimple() throws Exception {
    List<Product> products = generateProducts(1);
    List<Company> companies = generateCompany(1);
    List<Integer> ids = productRepository.create(products);
    companyRepository.create(companies.get(0));
    produceRepository.produce(companies.get(0).getName(), ids.get(0), 100);
  }

  @Test
  public void produce100() throws Exception {
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
      }
    }
  }

  @Test
  public void produceDelete() throws Exception {
    List<Product> products = generateProducts(1);
    List<Company> companies = generateCompany(1);
    List<Integer> ids = productRepository.create(products);
    companyRepository.create(companies.get(0));
    Integer produceId = produceRepository.produce(companies.get(0).getName(), ids.get(0), 100);
    produceRepository.delete(produceId);
  }

  @Test(expected = Exception.class)
  public void produceDeleteNotExist() throws Exception {
    produceRepository.delete(random(100));
  }
}
