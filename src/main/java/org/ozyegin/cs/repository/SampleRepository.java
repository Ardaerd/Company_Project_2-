package org.ozyegin.cs.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;
import org.ozyegin.cs.entity.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class SampleRepository extends JdbcDaoSupport {
  final int batchSize = 10;

  final String createPS = "INSERT INTO sample (name, data, value) VALUES(?,?,?)";
  final String updatePS = "UPDATE sample SET name=?, data=?, value=? WHERE id=?";
  final String getPS = "SELECT * FROM sample WHERE id IN (:ids)";
  final String getAllPS = "SELECT * FROM sample";
  final String getSinglePS = "SELECT * FROM sample WHERE id=?";
  final String deleteAllPS = "DELETE FROM sample";
  final String deletePS = "DELETE FROM sample WHERE id=?";

  // Mandatory for all repository
  @Autowired
  public void setDatasource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  /*
   * Another repository can be used in here as follows:
   * */
  @Autowired
  private ProductRepository productRepository;

  private final RowMapper<Sample> sampleRowMapper = (resultSet, i) -> new Sample()
      .id(resultSet.getInt("id"))
      .name(resultSet.getString("name"))
      .data(resultSet.getString("data"))
      .value(resultSet.getInt("value"));

  /**
   * Inserts the given samples into sample table.
   *
   * @param samples the sample list to insert into sample table
   */
  public void create(List<Sample> samples) {
    Objects.requireNonNull(getJdbcTemplate()).batchUpdate(createPS, samples,
        batchSize,
        (ps, sample) -> {
          ps.setString(1, sample.getName());
          ps.setString(2, sample.getData());
          ps.setInt(3, sample.getValue());
        });
  }

  /**
   * Updates a given sample.
   *
   * @param sample the sample to update
   */
  public void update(Sample sample) throws Exception {
    if (Objects.requireNonNull(getJdbcTemplate()).update(updatePS,
        sample.getName(), sample.getData(), sample.getValue(), sample.getId()) != 1) {
      throw new Exception("Sample Update is failed!");
    }
  }

  /**
   * Deletes a given sample.
   *
   * @param id the sample id to delete
   */
  public void delete(int id) throws Exception {
    if (Objects.requireNonNull(getJdbcTemplate()).update(deletePS,
        id) != 1) {
      throw new Exception("Sample Update is failed!");
    }
  }

  /**
   * Fetches the samples given by a list of ids.
   *
   * @param ids a list of sample ids
   * @return A list of samples
   */
  public List<Sample> get(List<Integer> ids) {
    if (ids == null || ids.isEmpty()) {
      return new ArrayList<>();
    } else {
      Map<String, List<Integer>> params = new HashMap<>() {
        {
          this.put("ids", new ArrayList<>(ids));
        }
      };
      var template = new NamedParameterJdbcTemplate(Objects.requireNonNull(getJdbcTemplate()));
      return template.query(getPS, params, sampleRowMapper);
    }
  }

  /**
   * Fetches a sample by a given id..
   *
   * @param id sample id
   * @return Samples
   */
  public Sample get(int id) throws Exception {
    Sample sample;
    try {
      sample = Objects.requireNonNull(getJdbcTemplate()).queryForObject(getSinglePS,
          new Object[] {id},
          sampleRowMapper);
    } catch (EmptyResultDataAccessException e) {
      throw new Exception("Sample not found!");
    }
    return sample;
  }

  public List<Sample> getAll() {
    return Objects.requireNonNull(getJdbcTemplate()).query(getAllPS, sampleRowMapper);
  }

  /**
   * BEWARE: TEST ONLY.
   */
  public void deleteAll() {
    Objects.requireNonNull(getJdbcTemplate()).update(deleteAllPS);
  }
}
