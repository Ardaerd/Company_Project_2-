Company(name:String, zip:int, country:String, streetInfo:String, phoneNumber:String),
Product(id:Int,name:String,description:String,brandName:String),
orderProduce(id:Int,company:String,product_id:Int,capacity:Int),
Produce(id:Int,company:String,product_id:Int,capacity:Int),
transaction_history(company:String,product:id,amount:int,order_date:date)

PRIMARY Key (Company) = <name>
PRIMARY Key (Product) = <id>
PRIMARY Key (Produce) = <id>
PRIMARY Key (orderProduce) = <id>

FOREIGN KEY Company(company) REFERENCES company(name),
FOREIGN KEY Produce(product_id) REFERENCES product(id),
FOREIGN KEY orderProduce(company) REFERENCES company(name),
FOREIGN KEY orderProduce(product_id) REFERENCES product(id)
