package org.ozyegin.cs.controller;

import java.util.ArrayList;
import java.util.List;
import org.ozyegin.cs.entity.Product;
import org.ozyegin.cs.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
  @Autowired
  private ProductService productService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @RequestMapping(produces = "application/json", method = RequestMethod.POST)
  public ResponseEntity create(@RequestBody List<Product> products) {
    // if successful, return new ResponseEntity<>(idlist, HttpStatus.OK)

    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);

    try {
      productService.create(products);
      transactionManager.commit(txStatus);

      return new ResponseEntity<>(productService.create(products),HttpStatus.OK);
    }
    // if there is an error, return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @RequestMapping(produces = "application/json", method = RequestMethod.PUT)
  public ResponseEntity update(@RequestBody List<Product> products) {
    // if successful, return new ResponseEntity<>(HttpStatus.OK)

    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);

    try {
      productService.update(products);
      transactionManager.commit(txStatus);

      return new ResponseEntity<>(HttpStatus.OK);
    }
    // if there is an error, return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

  }
}
