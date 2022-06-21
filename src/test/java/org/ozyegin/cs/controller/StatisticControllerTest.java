package org.ozyegin.cs.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.entity.Pair;
import org.ozyegin.cs.entity.Product;

public class StatisticControllerTest extends IntegrationTestSuite {
  // 10 points
  @Test
  public void query1() throws Exception {
    List<Company> companies = generateCompany(3);
    List<Product> products = generateProducts(2);
    for (Company company : companies) {
      companyRepository.create(company);
    }
    List<Integer> ids = productRepository.create(products);
    for (int i = 0; i < ids.size(); i++) {
      products.get(i).setId(ids.get(i));
    }
    produceRepository.produce(companies.get(0).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(0).getName(), products.get(1).getId(), 100);
    produceRepository.produce(companies.get(1).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(1).getName(), products.get(1).getId(), 100);
    produceRepository.produce(companies.get(2).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(2).getName(), products.get(1).getId(), 100);

    transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));

    transactionRepository.order(companies.get(0).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(0).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));

    transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));

    transactionRepository.order(companies.get(1).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(1).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));

    transactionRepository.order(companies.get(2).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(2).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(2).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli()));

    transactionRepository.order(companies.get(2).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));
    transactionRepository.order(companies.get(2).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli()));

    List<Pair> list = Arrays.asList(getMethod("/stat/query1", Pair[].class, new ArrayList<>(),
        new ArrayList<>()));

    assertTwoListEqual(Arrays.asList(new Pair(companies.get(0).getName(), products.get(0).getId()),
        new Pair(companies.get(1).getName(), products.get(0).getId()),
        new Pair(companies.get(2).getName(), products.get(1).getId())), list);
  }

  // 10 points
  @Test
  public void query1withDeletes() throws Exception {
    List<Company> companies = generateCompany(3);
    List<Product> products = generateProducts(2);
    for (Company company : companies) {
      companyRepository.create(company);
    }
    List<Integer> ids = productRepository.create(products);
    for (int i = 0; i < ids.size(); i++) {
      products.get(i).setId(ids.get(i));
    }
    produceRepository.produce(companies.get(0).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(0).getName(), products.get(1).getId(), 100);
    produceRepository.produce(companies.get(1).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(1).getName(), products.get(1).getId(), 100);
    produceRepository.produce(companies.get(2).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(2).getName(), products.get(1).getId(), 100);

    transactionRepository.delete(transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));

    transactionRepository.delete(transactionRepository.order(companies.get(0).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(0).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));

    transactionRepository.delete(transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));

    transactionRepository.delete(transactionRepository.order(companies.get(1).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(1).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));

    transactionRepository.delete(transactionRepository.order(companies.get(2).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(2).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(2).getName(), products.get(1).getId(), 5,
        new Date(Instant.now().toEpochMilli())));

    transactionRepository.delete(transactionRepository.order(companies.get(2).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));
    transactionRepository.delete(transactionRepository.order(companies.get(2).getName(), products.get(0).getId(), 5,
        new Date(Instant.now().toEpochMilli())));

    List<Pair> list = Arrays.asList(getMethod("/stat/query1", Pair[].class, new ArrayList<>(),
        new ArrayList<>()));

    assertTwoListEqual(Arrays.asList(new Pair(companies.get(0).getName(), products.get(0).getId()),
        new Pair(companies.get(1).getName(), products.get(0).getId()),
        new Pair(companies.get(2).getName(), products.get(1).getId())), list);
  }

  // 10 points
  @Test
  public void query2() throws Exception {
    List<Company> companies = generateCompany(3);
    List<Product> products = generateProducts(2);
    for (Company company : companies) {
      companyRepository.create(company);
    }
    List<Integer> ids = productRepository.create(products);
    for (int i = 0; i < ids.size(); i++) {
      products.get(i).setId(ids.get(i));
    }
    produceRepository.produce(companies.get(0).getName(), products.get(0).getId(), 100);
    produceRepository.produce(companies.get(1).getName(), products.get(0).getId(), 100);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    transactionRepository.order(companies.get(0).getName(), products.get(0).getId(), 50,
        new Date(simpleDateFormat.parse("2000-10-10").getTime()));
    transactionRepository.order(companies.get(1).getName(), products.get(0).getId(), 50,
        new Date(simpleDateFormat.parse("2000-10-10").getTime()));

    List<String> list = Arrays.asList(getMethod("/stat/query2/2000-10-9/2000-10-11",
        String[].class,
        new ArrayList<>(),
        new ArrayList<>()));

    assertTwoListEqual(Collections.singletonList(companies.get(2).getName()), list);
  }
}
