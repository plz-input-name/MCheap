const express = require("express");
const router = express.Router();
const { asyncWrapper } = require("./helper");
const { statisticsService } = require("../service");

router.get(
  "/:keyword",
  asyncWrapper(async (req, res, _next) => {
    const size = req.query.size;
    const keyword = req.params.keyword;

    const statistics = await statisticsService.getLatest(keyword, size);
    return res.status(200).json(statistics);
  })
);

router.post(
  "/:keyword",
  asyncWrapper(async (req, res, _next) => {
    const keyword = req.params.keyword;
    const { carrot, thunder, joongna } = req.body;

    await statisticsService.make(keyword, [carrot, thunder, joongna]);
    return res.status(204).send();
  })
);

module.exports = router;
