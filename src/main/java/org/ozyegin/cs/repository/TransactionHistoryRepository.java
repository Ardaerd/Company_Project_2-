package org.ozyegin.cs.repository;

import java.sql.Date;
import java.util.*;
import javax.sql.DataSource;

import org.ozyegin.cs.entity.Company;
import org.ozyegin.cs.entity.Pair;
import org.ozyegin.cs.entity.Product;
import org.ozyegin.cs.entity.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionHistoryRepository extends JdbcDaoSupport {

  int batchSize = 10;
  final String Query1="SELECT h.company, h.product_id " +
                       "FROM transaction_history h " +
                       "GROUP BY h.product_id, h.company  " +
                       "HAVING SUM(h.amount)>(SELECT SUM(h1.amount)  " +
                                             "FROM transaction_history h1 " +
                                             "WHERE h.product_id<>h1.product_id AND h.company=h1.company)";

  final String Query2 = "SELECT  c.name " +
                        "FROM transaction_history h ,company c " +
                        "GROUP BY  c.name " +
                        "EXCEPT " +
                        "SELECT  h1.company " +
                        "FROM transaction_history h1, company c1 " +
                        "WHERE  c1.name=h1.company AND h1.order_date >= ? and h1.order_date <= ? " +
                        "GROUP BY  h1.company";

  private final RowMapper<Pair> pairMapper = (resultSet, i) -> new Pair(
      resultSet.getString(1),
      resultSet.getInt(2)
  );

  private final RowMapper<String> stringMapper = (resultSet, i) -> resultSet.getString(1);

  @Autowired
  public void setDatasource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  public List<Pair> query1() {
    List<Pair> pairs = Objects.requireNonNull(getJdbcTemplate()).query(Query1,pairMapper);

    return pairs;
  }



  public List<String> query2(Date start, Date end) {
    List<String> companies = Objects.requireNonNull(getJdbcTemplate().query(Query2, new Object[]{start, end}, stringMapper));

    return companies;
  }

  public void deleteAll() {
    Objects.requireNonNull(getJdbcTemplate()).update("DELETE FROM transaction_history");
  }
}
