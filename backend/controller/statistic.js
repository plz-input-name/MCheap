const pool = require("../model/index");
const statisticsModel = require("../model/averagePrice");

exports.getStatisticData = async (req, res, _next) => {
  const size = req.query.size;
  const keyword = req.params.keyword;

  try {
    const dbCon = await pool;
    //console.log(`size : ${size} / keyword : ${keyword}`);

    const result = await statisticsModel.find(dbCon, keyword, size);
    //console.log(result);
    res.status(200).json(result[0]);
  } catch (err) {
    console.log(err);
    res.status(404);
  }
};

exports.makeStatistic = async (req, res, _next) => {
  const keyword = req.params.keyword;
  const { carrot, thunder, joongna } = req.body.price;
  const conn = await pool.getConnection();

  statisticsModel.add(conn, keyword, carrot[0], thunder[0], joongna[0]);

  conn.release();
  res.status(200).json({ message: "Ok" });
};
