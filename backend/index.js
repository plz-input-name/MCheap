require("dotenv").config();
const logger = require("morgan");
const createError = require("http-errors");
const express = require("express");
const app = express();

app.use(logger("common"));
app.use(express.json());

// catch 404
app.use((_req, _res, next) => {
  console.log("test");
  return next(createError(404, "Not Found"));
});

// error handler
app.use((err, req, res, _next) => {
  return res.status(err.status).json({ error: err.message });
});

app.listen(3000, () => console.log("express listenning on port 3000"));
