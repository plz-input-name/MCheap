const createError = require("http-errors");
const { pool, search } = require("../model");

exports.getLatestKeywords = async (req, res, next) => {
  const size = req.query.size;
  const conn = await pool.getConnection();

  try {
    const result = await search.latestKeywords(conn, size);
    return res.status(200).json(result[0]);
  } catch (err) {
    console.error(err.message);
    // error에 따른 code, message 세분화 필요
    return next(createError(500, "Internal Server Error"));
  } finally {
    conn.release();
  }
};
