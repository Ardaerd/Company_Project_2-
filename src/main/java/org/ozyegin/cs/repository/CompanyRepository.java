package org.ozyegin.cs.repository;

import org.ozyegin.cs.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CompanyRepository extends JdbcDaoSupport {

  int batchSize = 10;
  final String createPS = "INSERT INTO company (name, zip, country, streetInfo, phoneNumber) VALUES(?,?,?,?,?)";
  final String createZip_city = "INSERT INTO zip_city (zip,city) VALUES(?,?)";
  final String createPS_e_mail = "INSERT INTO e_mails (name, e_mail) VALUES(?,?)";
  final String getNameByCountry =  "SELECT name FROM company WHERE country=?";
  final String getCity = "SELECT city FROM zip_city WHERE zip=?";
  final String getE_mail = "SELECT e_mail FROM e_mails WHERE name=?";
  final String getSinglePS = "SELECT * FROM company WHERE name=?";
  final String deleteEmailsByName = "DELETE FROM e_mails WHERE name=?";
  final String deleteCompanyByName = "DELETE FROM company WHERE name=?";
  final String deleteCompany = "DELETE FROM company";
  final String deleteEmail = "DELETE FROM e_mails";
  final String deleteZip_city = "DELETE FROM zip_city";


  @Autowired
  public void setDatasource(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  private final RowMapper<Company> companyRowMapper = (resultSet, i) -> new Company()
          .name(resultSet.getString("name"))
          .zip(resultSet.getInt("zip"))
          .country(resultSet.getString("country"))
          .streetInfo(resultSet.getString("streetInfo"))
          .phoneNumber(resultSet.getString("phoneNumber"));


  private final RowMapper<Company> zip_cityRowMapper = ((resultSet, i) -> new Company()
          .zip(resultSet.getInt("zip"))
          .city(resultSet.getString("city"))
  );

  private final RowMapper<String> stringRowMapper = (resultSet, i) -> resultSet.getString(1);

  public Company find(String name) {
    Company company = null;

    company = Objects.requireNonNull(getJdbcTemplate()).queryForObject(getSinglePS,
            new Object[] {name},
            companyRowMapper);

    String city = null;
    if (company != null) {
      city = Objects.requireNonNull(getJdbcTemplate()).queryForObject(getCity,
              new Object[] {company.getZip()},
              stringRowMapper);

      company.setCity(city);

      company.setE_mails(Objects.requireNonNull(getJdbcTemplate()).query(getE_mail,
              new Object[] {company.getName()},
              stringRowMapper));
    }

    return company;
  }

  public List<Company> findByCountry(String country) {

    ArrayList<Company> companies = new ArrayList<>();
    List<String> companyNames = Objects.requireNonNull(getJdbcTemplate())
            .query(getNameByCountry,
                    new Object[] {country},
                    stringRowMapper);

    for (String name : companyNames) {
      companies.add(find(name));
    }
    return companies;
  }

  public String create(Company company) throws Exception {

    try {

      String zip_city = Objects.requireNonNull(getJdbcTemplate()).queryForObject(getCity,
              new Object[] {company.getZip()},
              stringRowMapper);

      if (!zip_city.equals(company.getCity())) {
        throw new RuntimeException("zip_city contradiction!");
      }

    } catch (EmptyResultDataAccessException e) {
      Objects.requireNonNull(getJdbcTemplate()).update(createZip_city,
              (ps -> {
                ps.setInt(1,company.getZip());
                ps.setString(2,company.getCity());
              }));
    }

    Objects.requireNonNull(getJdbcTemplate()).update(createPS, (PreparedStatement ps) -> {
      ps.setString(1,company.getName());
      ps.setInt(2,company.getZip());
      ps.setString(3,company.getCountry());
      ps.setString(4,company.getStreetInfo());
      ps.setString(5,company.getPhoneNumber());
    });

    List<String> e_mails = company.getE_mails();

    Objects.requireNonNull(getJdbcTemplate()).batchUpdate(createPS_e_mail, e_mails,
            batchSize,
            (ps, e_mail) -> {
              ps.setString(1, company.getName());
              ps.setString(2, e_mail);
            });

    return company.getName();
  }

  public String delete(String name) {
    Objects.requireNonNull(getJdbcTemplate()).update(deleteEmailsByName,name);
    Objects.requireNonNull(getJdbcTemplate()).update(deleteCompanyByName,name);
    return name;
  }

  public void deleteAll() {
    Objects.requireNonNull(getJdbcTemplate()).update(deleteEmail);
    Objects.requireNonNull(getJdbcTemplate()).update(deleteCompany);
    Objects.requireNonNull(getJdbcTemplate()).update(deleteZip_city);
  }
}
