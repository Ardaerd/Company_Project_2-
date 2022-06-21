package org.ozyegin.cs.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.ozyegin.cs.entity.Company;
import org.springframework.dao.EmptyResultDataAccessException;

public class CompanyControllerTest extends IntegrationTestSuite {
  // 5 points
  @Test
  public void create() {
    List<Company> companies = generateCompany(44);
    postMethod("/company", String.class, companies, new ArrayList<>(), new ArrayList<>());
    for(Company company: companies) {
      Assert.assertEquals(company, companyRepository.find(company.getName()));
    }
  }

  // 10 points
  @Test
  public void createFail() {
    List<Company> companies = generateCompany(44);
    int random = random(42);
    companies.get(random).setName(companies.get(random+1).getName());
    postMethod("/company", String.class, companies, new ArrayList<>(), new ArrayList<>());
    for(Company company: companies) {
      try{
        companyRepository.find(company.getName());
        Assert.fail();
      } catch (EmptyResultDataAccessException e) {
      }
    }
  }

  // 10 points
  @Test
  public void createFail2() {
    List<Company> companies = generateCompany(44);
    int random = random(42);
    companies.get(random).setName(companies.get(random+1).getName());
    postMethod("/company", String.class, companies, new ArrayList<>(), new ArrayList<>());
    companies.remove(random);
    postMethod("/company", String.class, companies, new ArrayList<>(), new ArrayList<>());
    for(Company company: companies) {
      Assert.assertEquals(company, companyRepository.find(company.getName()));
    }
  }

  // 5 points
  @Test
  public void delete() throws Exception {
    List<Company> companies = generateCompany(44);
    for(Company company: companies) {
      companyRepository.create(company);
    }
    for(Company company: companies) {
      deleteMethod("/company/"+company.getName(), String.class, new ArrayList<>(), new ArrayList<>());
    }
    for(Company company: companies) {
      try{
        companyRepository.find(company.getName());
        Assert.fail();
      } catch (EmptyResultDataAccessException e) {
      }
    }
  }

  // 5 points
  @Test
  public void find() throws Exception {
    List<Company> companies = generateCompany(44);
    for(Company company: companies) {
      companyRepository.create(company);
    }
    int random = random(42);
    Company company = companies.get(random);
    Company dbCompany = getMethod("/company/" + company.getName(), Company.class, new ArrayList<>(),
        new ArrayList<>());
    Assert.assertEquals(company, dbCompany);
  }
}
