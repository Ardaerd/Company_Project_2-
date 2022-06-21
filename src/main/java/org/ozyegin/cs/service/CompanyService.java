package org.ozyegin.cs.service;

import java.util.List;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
  @Autowired
  private CompanyRepository companyRepository;


  public void create(List<Company> companies) throws Exception {
    for (Company a : companies) {
      companyRepository.create(a);
    }
  }

  public void delete(String name) {
    companyRepository.delete(name);
  }

  public Company find(String name) {
    return companyRepository.find(name);
  }
}
