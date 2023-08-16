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

  const total = [...carrot, ...thunder, ...joongna].sort((a, b) => a - b);
  const q1 = total[Math.floor(total.length * 0.25)];
  const q3 = total[Math.ceil(total.length * 0.75)];
  const iqr = q3 - q1;
  const THRESHOLD = 1.5;
  const max = q3 + iqr * THRESHOLD;
  const min = q1 - iqr * THRESHOLD;

  const calcAvg = (arr) => {
    arr = arr.map((i) => {
      return Number(i);
    });
    const filtered = arr.filter((price) => price < max && price > min);
    const sum = filtered.reduce((sum, curr) => (sum += curr));

    return Math.ceil(sum / filtered.length);
  };

  try {
    conn.beginTransaction();

    search.add(
      conn,
      keyword,
      calcAvg(carrot),
      calcAvg(thunder),
      calcAvg(joongna)
    );
    const result = await search.find(conn, keyword, "2");
    if (result[0].length > 1) {
      if (
        Date.parse(result[0][0].collected_at) -
          Date.parse(result[0][1].collected_at) <
        1000 * 60 * 60
      )
        throw Error("throttle");
    }

    conn.commit();
    return res.status(204).send();
  } catch (err) {
    console.error(err.message);
    // error 원인에 따른 code, message 세분화 필요

    conn.rollback();
    return next(createError(500, "Internal Server Error"));
  } finally {
    conn.release();
  }
};
