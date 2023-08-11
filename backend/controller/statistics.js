const createError = require("http-errors");
const { pool, search } = require("../model");

exports.getStatistics = async (req, res, next) => {
  const size = req.query.size;
  const keyword = req.params.keyword;
  const conn = await pool.getConnection();

  try {
    const result = await search.find(conn, keyword, size);
    return res.status(200).json(result[0]);
  } catch (err) {
    console.error(err.message);
    // error 원인에 따른 code, message 세분화 필요
    return next(createError(500, "Internal Server Error"));
  } finally {
    conn.release();
  }
};

exports.makeStatistics = async (req, res, next) => {
  const keyword = req.params.keyword;
  const { carrot, thunder, joongna } = req.body;
  const conn = await pool.getConnection();

  try {
    // 이탈값 제거 후 평균값 계산 필요
    search.add(conn, keyword, carrot[0], thunder[0], joongna[0]);
    return res.status(204).send();
  } catch (err) {
    console.error(err.message);
    // error 원인에 따른 code, message 세분화 필요
    return next(createError(500, "Internal Server Error"));
  } finally {
    conn.release();
  }
};
