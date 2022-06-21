package org.ozyegin.cs.repository;

import org.ozyegin.cs.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ProductRepository extends JdbcDaoSupport {

  final int batchSize = 10;

  final String createPS = "INSERT INTO product (name, description, brand_name) VALUES(?,?,?)";
  final String updatePS = "UPDATE product SET name=?, description=?, brand_name=? WHERE id=?";
  final String getIds = "SELECT id FROM product";
  final String getBrandNames = "SELECT * FROM product WHERE brand_name = ?";
  final String getPS = "SELECT * FROM product WHERE id IN (:ids)";
  final String getSinglePS = "SELECT * FROM product WHERE id=?";
  final String deleteAllPS = "DELETE FROM product";
  final String deletePS = "DELETE FROM product WHERE id=?";

  @Autowired
  public void setDatasource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  private final RowMapper<Integer> idsRowMapper = (resultSet, i) -> new Product()
          .id_1(resultSet.getInt("id"));

  private final RowMapper<Product> productRowMapper = ((resultSet, i) -> new Product()
          .id_2(resultSet.getInt("id"))
          .name(resultSet.getString("name"))
          .description(resultSet.getString("description"))
          .brandName(resultSet.getString("brand_name"))
  );

  public Product find(int id) {
    Product product = null;

    product = Objects.requireNonNull(getJdbcTemplate()).queryForObject(getSinglePS,
            new Object[] {id},
            productRowMapper);

    return product;
  }

  public List<Product> findMultiple(List<Integer> ids) {

    if (ids == null || ids.isEmpty())
      return new ArrayList<>();
    else {
      Map<String, List<Integer>> params = new HashMap<>() {
        {
          this.put("ids",new ArrayList<>(ids));
        }
      };

      var template = new NamedParameterJdbcTemplate(Objects.requireNonNull(getJdbcTemplate()));
      return template.query(getPS,params,productRowMapper);
    }
  }

  public List<Product> findByBrandName(String brandName) {

    return Objects.requireNonNull(getJdbcTemplate()).query(getBrandNames,
            new Object[] {brandName},
            productRowMapper);
  }

  public List<Integer> create(List<Product> products) {

    List<Integer> ids = Objects.requireNonNull(getJdbcTemplate()).query(getIds,idsRowMapper);

    Objects.requireNonNull(getJdbcTemplate()).batchUpdate(createPS, products,
            batchSize,
            (ps, product) -> {
              ps.setString(1, product.getName());
              ps.setString(2, product.getDescription());
              ps.setString(3, product.getBrandName());
            });

    List<Integer> newIds = Objects.requireNonNull(getJdbcTemplate()).query(getIds,idsRowMapper);

    newIds.removeAll(ids);
    return newIds;
  }

  public void update(List<Product> products) {

    Objects.requireNonNull(getJdbcTemplate()).batchUpdate(updatePS,
            products, batchSize,
            (ps, product) -> {
              ps.setString(1,product.getName());
              ps.setString(2,product.getDescription());
              ps.setString(3,product.getBrandName());
              ps.setInt(4,product.getId());
            });

  }

  public void delete(List<Integer> ids) {

    Objects.requireNonNull(getJdbcTemplate()).batchUpdate(deletePS,
            ids, batchSize,
            (ps, id) -> {
              ps.setInt(1,id);
            });

  }

  public void deleteAll() {
    Objects.requireNonNull(getJdbcTemplate()).update(deleteAllPS);
  }
}
