create table if not exists sample(
      id serial PRIMARY KEY,
      name VARCHAR(64) NOT NULL,
      data text,
      value int default 0
  );

create table if not exists zip_city(
    zip INT,
    city VARCHAR(100),
    PRIMARY KEY(zip)
);

create table if not exists company(
      name VARCHAR(100) PRIMARY KEY,
      zip int,
      country VARCHAR(100),
      streetInfo VARCHAR(100),
      phoneNumber VARCHAR(120) NOT NULL UNIQUE,
      FOREIGN KEY(zip) REFERENCES zip_city(zip)
      );

create table if not exists e_mails(
    name VARCHAR(100),
    e_mail VARCHAR(100),
    PRIMARY KEY(name,e_mail),
    FOREIGN KEY(name) REFERENCES company(name)
    );

create table if not exists product(
    id serial PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description text,
    brand_name VARCHAR(100)
);

create table if not exists produce(
    id serial PRIMARY KEY,
    company VARCHAR(100) NOT NULL,
    product_id int NOT NULL,
    capacity int,
    FOREIGN KEY (company) REFERENCES company(name),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

create table if not exists orderProduct(
    id serial PRIMARY KEY,
    company VARCHAR(100) NOT NULL,
    product_id int NOT NULL,
    amount int,
    order_date timestamp with time zone,
    FOREIGN KEY (company) REFERENCES company(name),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

create table if not exists transaction_history(
      company VARCHAR(100),
      product_id int,
      amount int,
      order_date timestamp with time zone
      );

create FUNCTION sample_trigger() RETURNS TRIGGER AS
'
    BEGIN
        IF (SELECT value FROM sample where id = NEW.id ) > 1000
           THEN
           RAISE SQLSTATE ''23503'';
           END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

create TRIGGER sample_value AFTER insert ON sample
    FOR EACH ROW EXECUTE PROCEDURE sample_trigger();

CREATE FUNCTION ordered_trigger() RETURNS TRIGGER AS
    '
    BEGIN
        IF (Select capacity
             FROM produce p2,company c1, product p1
             WHERE p2.product_id=p1.id AND p2.company=c1.name AND
             p2.capacity < (SELECT SUM(amount)
                            FROM orderProduct o1
                            WHERE o1.company=c1.name AND o1.product_id=p1.id
        ))
            THEN
            RAISE SQLSTATE ''23503'';
            END IF;
        RETURN NEW;
    END;
    ' LANGUAGE plpgsql;

CREATE TRIGGER order_amount AFTER insert ON orderProduct
    FOR EACH ROW EXECUTE PROCEDURE ordered_trigger();
