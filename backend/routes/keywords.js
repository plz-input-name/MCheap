const express = require("express");
const router = express.Router();
const { asyncWrapper } = require("./helper");
const { keywordsService } = require("../service");

router.get(
  "/",
  asyncWrapper(async (req, res, _next) => {
    const size = req.query.size;
    const latestKeywords = await keywordsService.getLatest(size);
    return res.status(200).json(latestKeywords);
  })
);

module.exports = router;
