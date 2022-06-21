package org.ozyegin.cs.controller;

import java.util.List;
import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {
  @Autowired
  private CompanyService companyService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @RequestMapping(produces = "application/json", method = RequestMethod.POST)
  public ResponseEntity create(@RequestBody List<Company> companies) {
    // if successful, return new ResponseEntity<>( HttpStatus.OK)

    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);

    try {
      companyService.create(companies);
      transactionManager.commit(txStatus);

      return new ResponseEntity<>(HttpStatus.OK);
    }
    // if there is an error, return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);
    }


  }

  @RequestMapping(value = "/{name}", produces = "application/json", method = RequestMethod.DELETE)
  public ResponseEntity delete(@PathVariable("name") String name) {
    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);

    try {
      companyService.delete(name);
      transactionManager.commit(txStatus);

      return new ResponseEntity<>(HttpStatus.OK);
    }
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @RequestMapping(value = "/{name}", produces = "application/json", method = RequestMethod.GET)
  public ResponseEntity get(@PathVariable("name") String name) {
    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);

    try {
      companyService.find(name);
      transactionManager.commit(txStatus);
      return new ResponseEntity<>(companyService.find(name),HttpStatus.OK);
    }
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(null, null, HttpStatus.OK);
    }
  }
}
