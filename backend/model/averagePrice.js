module.exports = {
  async find(conn, keyword) {
    return await conn.execute(
      `
      SELECT
        *
      FROM
        average_price;
      `,
      [keyword]
    );
  },
  async add(conn, keyword, carrot, thunder, joongna) {
    return await conn.execute(
      `
      INSERT INTO
        average_price (keyword, carrot, thunder, joonga)
      VALUES
        (?, ?, ?, ?);
      `,
      [keyword, carrot, thunder, joongna]
    );
  },
};
