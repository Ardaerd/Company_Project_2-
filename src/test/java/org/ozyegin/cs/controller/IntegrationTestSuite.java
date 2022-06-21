package org.ozyegin.cs.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.ozyegin.cs.Pair;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.entity.Product;
import org.ozyegin.cs.repository.CompanyRepository;
import org.ozyegin.cs.repository.HomeworkPostgresqlContainer;
import org.ozyegin.cs.repository.ProduceRepository;
import org.ozyegin.cs.repository.ProductRepository;
import org.ozyegin.cs.repository.SampleRepository;
import org.ozyegin.cs.repository.TransactionHistoryRepository;
import org.ozyegin.cs.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

  @Resource
  protected TransactionHistoryRepository transactionHistoryRepository;

  @ClassRule
  public static PostgreSQLContainer postgreSQLContainer =
      HomeworkPostgresqlContainer.getInstance();

  @LocalServerPort
  protected int port;

  @Autowired
  protected TestRestTemplate testRestTemplate;

  protected final String url = "http://localhost:";

  @Before
  public void cleanAll() throws Exception {
    transactionHistoryRepository.deleteAll();
    sampleRepository.deleteAll();
    produceRepository.deleteAll();
    transactionRepository.deleteAll();
    companyRepository.deleteAll();
    productRepository.deleteAll();
  }

  @After
  public void cleanAfter() {
    transactionHistoryRepository.deleteAll();
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

  protected List<Product> generateProducts(int size) {
    List<Product> products = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Product product = new Product();
      product.setName(RandomStringUtils.random(10 + random(10), true, true));
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
      company.setName(RandomStringUtils.random(10 + random(10), true, true));
      company.setCountry(RandomStringUtils.random(random(10), true, true));
      int random = random(99999);
      company.setZip(random);
      company.setCity(zip2city.get(random));
      company.setStreetInfo(RandomStringUtils.random(random(20), true, true));
      String phoneNumber;
      do {
        phoneNumber = RandomStringUtils.random(11, false, true);
      } while (phones.contains(phoneNumber));
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

  private <T> T genericMethod(String path,
                              Class<T> responseClass,
                              Object body,
                              List<Pair<String, Object>> queryParams,
                              List<Pair<String, String>> headerParams,
                              HttpMethod type) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    for (Map.Entry<String, String> params : headerParams) {
      headers.set(params.getKey(), params.getValue());
    }
    headers.set("jwt", "dummy_token");

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + port + "/" + path);
    for (Map.Entry<String, Object> params : queryParams) {
      builder = builder.queryParam(params.getKey(), params.getValue());
    }

    ResponseEntity<T> responseEntity = testRestTemplate.exchange(builder.build().toUri(),
        type, new HttpEntity<>(body, headers), responseClass);
    //Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    return responseEntity.getBody();
  }

  public <T> T getMethod(String path, Class<T> responseClass,
                         List<Pair<String, Object>> queryParams,
                         List<Pair<String, String>> headerParams) {
    return genericMethod(path, responseClass, null, queryParams, headerParams, HttpMethod.GET);
  }

  public <T> T postMethod(String path,
                          Class<T> responseClass,
                          Object body,
                          List<Pair<String, Object>> queryParams,
                          List<Pair<String, String>> headerParams) {
    return genericMethod(path, responseClass, body, queryParams, headerParams, HttpMethod.POST);
  }

  public <T> T putMethod(String path,
                         Class<T> responseClass,
                         Object body,
                         List<Pair<String, Object>> queryParams,
                         List<Pair<String, String>> headerParams) {
    return genericMethod(path, responseClass, body, queryParams, headerParams, HttpMethod.PUT);
  }

  public <T> T deleteMethod(String path,
                            Class<T> responseClass,
                            List<Pair<String, Object>> queryParams,
                            List<Pair<String, String>> headerParams) {
    return genericMethod(path, responseClass, null, queryParams, headerParams, HttpMethod.DELETE);
  }
}
