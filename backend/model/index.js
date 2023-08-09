const mysql = require("mysql2/promise");

const pool = mysql.createPool({
  host: process.env.DB_HOST ?? "127.0.0.1",
  user: process.env.DB_USER ?? "root",
  password: process.env.DB_USER
    ? process.env.DB_PASSWORD
    : process.env.DB_ROOT_PASSWORD,
  database: "testDB",
  dateStrings: true,
});
module.exports = pool;
