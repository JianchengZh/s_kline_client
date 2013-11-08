database: stockmarket

CREATE TABLE stocklist(
stock_id VARCHAR(6)  PRIMARY KEY,
market_type VARCHAR(1) ,
start INT,
last INT,
name VARCHAR(10),
quick VARCHAR(5)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;;

CREATE TABLE zhishulist(
stock_id VARCHAR(6)  PRIMARY KEY,
market_type VARCHAR(1) ,
start INT,
last INT,
name VARCHAR(10),
quick VARCHAR(5)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;;


/**
 *  table name: data_kline_600031_1
 *  create table if not exists stock_kline_<stock_id>_<market_type>
 * 
 */
 
CREATE TABLE IF NOT EXISTS data_kline_<stock_id>_<market_type>(
date INT  PRIMARY KEY,
open INT,
high INT,
low INT,
close INT,
vol INT,
cje INT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

REPLACE INTO `data_kline_XXX` VALUES (20121012, 958, 936, 941, 314393, 30009),(...);


/**
 *  table name: data_exrights_600031_1
 *  create table if not exists data_exrights_<stock_id>_<market_type>
 * 
 */
 
CREATE TABLE IF NOT EXISTS data_exrights_<stock_id>_<market_type>(
date INT  PRIMARY KEY,
multi_num INT,
add_num INT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
 