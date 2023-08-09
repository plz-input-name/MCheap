require("dotenv").config();
const logger = require("morgan");
const createError = require("http-errors");
const express = require("express");
const app = express();

const statisticRouter = require("./routes/statistic");

app.use(logger("common"));
app.use(express.json());

app.use("/statistics", statisticRouter);

// catch 404
app.use((_req, _res, next) => {
  return next(createError(404, "Not Found"));
});

// error handler
app.use((err, _req, res, _next) => {
  return res.status(err.status).json({ error: err.message });
});

app.listen(3000, () => console.log("express listenning on port 3000"));
