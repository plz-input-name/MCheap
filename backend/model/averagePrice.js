module.exports = {
  async find(conn, keyword, size) {
    return await conn.execute(
      `
      SELECT
        *
      FROM
        average_price
      WHERE
        keyword = ?
      ORDER BY
        colleted_at
      LIMIT ?;
      `,
      [keyword, size]
    );
  },
  async add(conn, keyword, carrot, thunder, joongna) {
    return await conn.execute(
      `
      INSERT INTO
        average_price (keyword, carrot, thunder, joongna)
      VALUES
        (?, ?, ?, ?);
      `,
      [keyword, carrot, thunder, joongna]
    );
  },
};
