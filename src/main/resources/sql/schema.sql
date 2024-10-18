CREATE SCHEMA IF NOT EXISTS CRYPTO;

CREATE TABLE IF NOT EXISTS CRYPTO.USERS (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  usdt_balance DECIMAL(19,2) DEFAULT 0,
  ethusdt_balance DECIMAL(19,8) DEFAULT 0,
  btcusdt_balance DECIMAL(19,8) DEFAULT 0,
  dt_created TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS CRYPTO.TRADE_TRANSACTION (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  crypto_pair VARCHAR(20) NOT NULL,
  txn_type VARCHAR(10) NOT NULL,
  amount DECIMAL(19,8) NOT NULL,
  price DECIMAL(19,8) NOT NULL,
  dt_created TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES USERS(id)
);

CREATE TABLE IF NOT EXISTS CRYPTO.market_price (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  crypto_pair VARCHAR(20) NOT NULL,
  bid_price DECIMAL(19,8),
  ask_price DECIMAL(19,8),
  dt_received TIMESTAMP NOT NULL
);
