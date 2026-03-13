PRAGMA foreign_keys = ON;



CREATE TABLE IF NOT EXISTS user(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_name TEXT NOT NULL,
    e_mail TEXT NOT NULL,
    password_hash TEXT NOT NULL
);

CREATE TABLE stocksPerUser(
    symbol TEXT PRIMARY KEY,
    purchase_price DECIMAL(19,3) NOT NULL,
    current_price DECIMAL(19,3) NOT NULL,
    quantity INTEGER NOT NULL
);

CREATE TABLE transactions(
    stock TEXT PRIMARY KEY,
    transaction_type TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(19, 3) NOT NULL

);

