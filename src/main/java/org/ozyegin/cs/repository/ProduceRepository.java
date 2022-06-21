package org.ozyegin.cs.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Repository
public class ProduceRepository extends JdbcDaoSupport {

  final String createPS = "INSERT INTO produce (company, product_id, capacity) VALUES(?,?,?)";
  final String getIds = "SELECT id FROM produce";
  final String deleteProduceById = "DELETE FROM produce WHERE id=?";
  final String deleteAll = "DELETE FROM produce";


  @Autowired
  public void setDatasource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  private final RowMapper<Integer> idRowMapper = ((resultSet, i) -> resultSet.getInt(1));

  public Integer produce(String company, int product, int capacity) {

    List<Integer> ids = Objects.requireNonNull(getJdbcTemplate()).query(getIds,idRowMapper);

    Objects.requireNonNull(getJdbcTemplate()).update(createPS,company,product,capacity);

    List<Integer> newIds = Objects.requireNonNull(getJdbcTemplate()).query(getIds,idRowMapper);

    newIds.removeAll(ids);

    return newIds.get(0);
  }

  public void delete(int produceId) throws Exception {
    if (Objects.requireNonNull(getJdbcTemplate()).update(deleteProduceById,produceId) != 1) {
      throw new Exception("not working");
    }
  }

  public void deleteAll() {
    Objects.requireNonNull(getJdbcTemplate()).update(deleteAll);
  }
}
