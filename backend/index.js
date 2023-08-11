require("dotenv").config();
const logger = require("morgan");
const createError = require("http-errors");
const express = require("express");
const app = express();

app.use(logger("common"));
app.use(express.json());

app.use("/statistics", require("./routes/statistics"));
app.use("/keywords", require("./routes/keywords"));

// catch 404
app.use((_req, _res, next) => {
  return next(createError(404, "Not Found"));
});

// error handler
app.use((err, _req, res, _next) => {
  console.error(err.message);

  return res
    .status(err.status ?? 500)
    .json({ error: err.message ?? "Internal Server Error" });
});

app.listen(3000, () => console.log("express listenning on port 3000"));
