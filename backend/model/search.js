module.exports = {
  async find(conn, keyword, size) {
    return await conn.execute(
      `
      SELECT
        *
      FROM
        search
      WHERE
        keyword = ?
      ORDER BY
        collected_at DESC
      LIMIT ?;
      `,
      [keyword, size]
    );
  },
  async add(conn, keyword, carrot, thunder, joongna) {
    return await conn.execute(
      `
      INSERT INTO
        search (keyword, carrot, thunder, joongna)
      VALUES
        (?, ?, ?, ?);
      `,
      [keyword, carrot, thunder, joongna]
    );
  },
  async latestKeywords(conn, size) {
    return await conn.execute(
      `
      SELECT
        keyword,
        MAX(collected_at) as last_collected_at
      FROM
        search
      GROUP BY
        keyword
      ORDER BY
        last_collected_at DESC
      LIMIT
        ?;  
      `,
      [size]
    );
  },
};
