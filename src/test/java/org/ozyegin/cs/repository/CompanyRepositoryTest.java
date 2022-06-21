package org.ozyegin.cs.repository;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.ozyegin.cs.entity.Company;

public class CompanyRepositoryTest extends IntegrationTestSuite {
  @Test
  public void createSimple() throws Exception {
    List<Company> companies = generateCompany(1);
    companyRepository.create(companies.get(0));
    Company company = companyRepository.find(companies.get(0).getName());
    Assert.assertEquals(companies.get(0), company);
  }

  @Test
  public void create100() throws Exception {
    List<Company> companies = generateCompany(100);
    for (Company company: companies) {
      companyRepository.create(company);
    }
    for (Company company : companies) {
      Company dbCompany = companyRepository.find(company.getName());
      Assert.assertEquals(company, dbCompany);
    }
  }

  @Test(expected = Exception.class)
  public void primaryKey() throws Exception {
    List<Company> companies = generateCompany(2);
    companies.get(1).setName(companies.get(0).getName());
    companyRepository.create(companies.get(0));
    companyRepository.create(companies.get(1));
  }

  @Test(expected = Exception.class)
  public void uniquePhone() throws Exception {
    List<Company> companies = generateCompany(2);
    companies.get(1).setPhoneNumber(companies.get(0).getPhoneNumber());
    companyRepository.create(companies.get(0));
    companyRepository.create(companies.get(1));
  }

  @Test(expected = Exception.class)
  public void notNullPhone() throws Exception {
    List<Company> companies = generateCompany(1);
    companies.get(0).setPhoneNumber(null);
    companyRepository.create(companies.get(0));
  }

  @Test
  public void zip2city() throws Exception {
    List<Company> companies = generateCompany(2);
    companies.get(1).setZip(companies.get(0).getZip());
    companies.get(1).setCity(companies.get(0).getCity());
    companyRepository.create(companies.get(0));
    companyRepository.create(companies.get(1));
    Company company = companyRepository.find(companies.get(0).getName());
    Assert.assertEquals(companies.get(0), company);
    Company company2 = companyRepository.find(companies.get(1).getName());
    Assert.assertEquals(companies.get(1), company2);
  }

  @Test(expected = Exception.class)
  public void zip2cityFail() throws Exception {
    List<Company> companies = generateCompany(2);
    companies.get(1).setZip(companies.get(0).getZip());
    companies.get(1).setCity(companies.get(0).getCity()+ "different");
    companyRepository.create(companies.get(0));
    companyRepository.create(companies.get(1));
  }

  @Test
  public void findSimple() throws Exception {
    List<Company> companies = generateCompany(1);
    companyRepository.create(companies.get(0));
    Company company = companyRepository.find(companies.get(0).getName());
    Assert.assertEquals(companies.get(0), company);
  }

  @Test
  public void find100() throws Exception {
    List<Company> companies = generateCompany(100);
    for (Company company: companies) {
      companyRepository.create(company);
    }
    Company company = companies.get(random(100));
    Company dbCompany = companyRepository.find(company.getName());
    Assert.assertEquals(company, dbCompany);
  }

  @Test(expected = Exception.class)
  public void findFail() throws Exception {
    List<Company> companies = generateCompany(100);
    companyRepository.create(companies.get(0));
    companyRepository.find("NotExist");
  }

  @Test
  public void findByCountry() throws Exception {
    List<Company> companies = generateCompany(100);
    for(int i=0;i<50;i++) {
      companies.get(i).setCountry("turkey");
    }
    for (Company company: companies) {
      companyRepository.create(company);
    }
    List<Company> dbCompany = companyRepository.findByCountry("turkey");
    assertTwoListEqual(companies.subList(0, 50), dbCompany);
  }

  @Test(expected = Exception.class)
  public void delete() throws Exception {
    List<Company> companies = generateCompany(1);
    companyRepository.create(companies.get(0));
    companyRepository.delete(companies.get(0).getName());
    companyRepository.find(companies.get(0).getName());
  }

  @Test
  public void complex() throws Exception {
    List<Company> companies = generateCompany(100);
    for(int i=0;i<50;i++) {
      companies.get(i).setCountry("turkey");
    }
    for (Company company: companies) {
      companyRepository.create(company);
    }

    for(int i=0;i<10;i++) {
      companyRepository.delete(companies.get(i).getName());
    }

    List<Company> dbCompany = companyRepository.findByCountry("turkey");

    assertTwoListEqual(companies.subList(10, 50), dbCompany);
  }
}
