package org.ozyegin.cs.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class TransactionRepository extends JdbcDaoSupport {

  final String createPS = "INSERT INTO orderProduct (company, product_id, amount, order_date) VALUES(?,?,?,?)";
  final String createTransaction_history_PS = "INSERT INTO transaction_history (company, product_id, amount, order_date) VALUES(?,?,?,?)";

  final String getIds = "SELECT id FROM orderProduct";
  final String deleteProduceById = "DELETE FROM orderProduct WHERE id=?";
  final String deleteAll = "DELETE FROM orderProduct";

  private final RowMapper<Integer> idRowMapper = ((resultSet, i) -> resultSet.getInt(1));

  @Autowired
  public void setDatasource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  public Integer order(String company, int product, int amount, Date createdDate) {
    List<Integer> ids = Objects.requireNonNull(getJdbcTemplate()).query(getIds,idRowMapper);

    Objects.requireNonNull(getJdbcTemplate()).update(createPS,company,product,amount,createdDate);
    Objects.requireNonNull(getJdbcTemplate()).update(createTransaction_history_PS,company,product,amount,createdDate);

    List<Integer> newIds = Objects.requireNonNull(getJdbcTemplate()).query(getIds,idRowMapper);

    newIds.removeAll(ids);

    return newIds.get(0);
  }

  public void delete(int transactionId) throws Exception {
    if (Objects.requireNonNull(getJdbcTemplate()).update(deleteProduceById,transactionId) != 1) {
      throw new Exception("not working");
    }
  }

  public void deleteAll() {
    Objects.requireNonNull(getJdbcTemplate()).update(deleteAll);
  }
}
