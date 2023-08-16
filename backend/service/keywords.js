const { pool, searchModel } = require("../model");

module.exports = {
  async getLatest(size) {
    const conn = await pool.getConnection();
    try {
      const keywords = await searchModel.latestKeywords(conn, size);
      return keywords;
    } finally {
      conn.release();
    }
  },
};
