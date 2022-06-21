package org.ozyegin.cs.controller;

import java.util.List;
import org.ozyegin.cs.entity.Sample;
import org.ozyegin.cs.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
@CrossOrigin
public class SampleController {
  @Autowired
  private SampleService sampleService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @RequestMapping(produces = "application/json", method = RequestMethod.POST)
  public ResponseEntity create(@RequestBody List<Sample> samples) {
    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);
    try {
      sampleService.create(samples);
      transactionManager.commit(txStatus);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
