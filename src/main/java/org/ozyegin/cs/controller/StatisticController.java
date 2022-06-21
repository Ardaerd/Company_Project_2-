package org.ozyegin.cs.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.ozyegin.cs.entity.Pair;
import org.ozyegin.cs.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
@CrossOrigin
public class StatisticController {
  @Autowired
  private TransactionHistoryRepository transactionHistoryRepository;
  @Autowired
  private PlatformTransactionManager transactionManager;



  @RequestMapping(value = "/query1", produces = "application/json", method = RequestMethod.GET)
  public ResponseEntity getQuery1() {
    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);

    try {
      List<Pair> list = transactionHistoryRepository.query1();
      transactionManager.commit(txStatus);

      return new ResponseEntity<>(list, null, HttpStatus.OK);
    }
    // if there is an error, return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_ACCEPTABLE);
    }



  }

  @RequestMapping(value = "/query2/{start}/{end}", produces = "application/json", method = RequestMethod.GET)
  public ResponseEntity getQuery2(@PathVariable("start") String start,
                                  @PathVariable("end")  String end) throws ParseException {
    TransactionDefinition txDef = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDef);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    try {
      List<String> list = transactionHistoryRepository.query2(
              new Date(simpleDateFormat.parse(start).getTime()),
              new Date(simpleDateFormat.parse(end).getTime()));
      transactionManager.commit(txStatus);

      return new ResponseEntity<>(list, null, HttpStatus.OK);
    }
    // if there is an error, return new ResponseEntity<>(null, null, HttpStatus.NOT_ACCEPTABLE);
    catch (Exception e) {
      transactionManager.rollback(txStatus);
      return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_ACCEPTABLE);
    }


  }
}
