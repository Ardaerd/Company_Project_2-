package org.ozyegin.cs.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.ozyegin.cs.Homework;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.entity.Product;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Homework.class)
public abstract class IntegrationTestSuite {
  @Resource
  protected SampleRepository sampleRepository;

  @Resource
  protected CompanyRepository companyRepository;

  @Resource
  protected ProductRepository productRepository;

  @Resource
  protected ProduceRepository produceRepository;

  @Resource
  protected TransactionRepository transactionRepository;

  @ClassRule
  public static PostgreSQLContainer postgreSQLContainer =
      HomeworkPostgresqlContainer.getInstance();

  @Before
  public void cleanAll() throws Exception {
    sampleRepository.deleteAll();
    produceRepository.deleteAll();
    transactionRepository.deleteAll();
    companyRepository.deleteAll();
    productRepository.deleteAll();
  }

  @After
  public void cleanAfter() {
    sampleRepository.deleteAll();
    produceRepository.deleteAll();
    transactionRepository.deleteAll();
    companyRepository.deleteAll();
    productRepository.deleteAll();
  }

  public <T> void assertTwoListEqual(Collection<T> expectedList, Collection<T> actualList) {
    Assert.assertEquals(expectedList.size(), actualList.size());
    Set<T> set = new HashSet<>(actualList);
    for (T t : expectedList) {
      Assert.assertTrue(set.contains(t));
    }
    Set<T> set2 = new HashSet<>(expectedList);
    for (T t : actualList) {
      Assert.assertTrue(set2.contains(t));
    }
  }

  protected int random(int limit) {
    return (int) (Math.random() * limit);
  }

  List<Product> generateProducts(int size) {
    List<Product> products = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Product product = new Product();
      product.setName(RandomStringUtils.random(10+random(10), true, true));
      product.setDescription(RandomStringUtils.random(random(10), true, true));
      product.setBrandName(RandomStringUtils.random(random(10), true, true));
      products.add(product);
    }
    return products;
  }



  protected List<Company> generateCompany(int size) {
    ArrayList<Company> companies = new ArrayList<>();

    HashMap<Integer, String> zip2city = new HashMap<>();

    for (int i = 0; i < 99999; i++) {
      zip2city.put(i, RandomStringUtils.random(random(10)));
    }

    HashSet<String> phones = new HashSet<>();

    for (int i = 0; i < size; i++) {
      Company company = new Company();
      company.setName(RandomStringUtils.random(10+random(10), true, true));
      company.setCountry(RandomStringUtils.random(random(10), true, true));
      int random = random(99999);
      company.setZip(random);
      company.setCity(zip2city.get(random));
      company.setStreetInfo(RandomStringUtils.random(random(20), true, true));
      String phoneNumber;
      do {
        phoneNumber = RandomStringUtils.random(11, false, true);
      } while(phones.contains(phoneNumber));
      phones.add(phoneNumber);
      company.setPhoneNumber(phoneNumber);
      int emailNumber = random(5);
      company.setE_mails(new ArrayList<>());
      for (int j = 0; j < emailNumber; j++) {
        company.getE_mails().add(RandomStringUtils.random(20, true, true));
      }
      companies.add(company);
    }

    return companies;
  }
}
