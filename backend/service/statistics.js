const { pool, searchModel } = require("../model");
const Platform = require("../domain/platform");

module.exports = {
  async getLatest(keyword, size) {
    const conn = await pool.getConnection();

    try {
      const statistics = await searchModel.find(conn, keyword, size);
      return statistics;
    } finally {
      conn.release();
    }
  },
  async make(keyword, [carrot, thunder, joongna]) {
    const conn = await pool.getConnection();

    try {
      conn.beginTransaction();

      await searchModel.add(
        conn,
        keyword,
        new Platform(carrot).average,
        new Platform(thunder).average,
        new Platform(joongna).average
      );

      // Optimistic locking for throttling
      const statistics = await searchModel.find(conn, keyword, "2");
      if (statistics.length > 1) {
        if (
          Date.parse(statistics[0].collected_at) -
            Date.parse(statistics[1].collected_at) <
          1_000 * 60 * 60 // Throttling based on 1 hour
        )
          throw Error("throttle");
      }

      conn.commit();
      return;
    } catch (err) {
      conn.rollback();

      if (err.message == "throttle") return;
      throw err;
    } finally {
      conn.release();
    }
  },
};
