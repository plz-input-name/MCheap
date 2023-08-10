const express = require("express");
const router = express.Router();
const { asyncWrapper } = require("./helper");

const {
  getStatistics,
  makeStatistics,
  getLatestKeywords,
} = require("../controller/statistics");

router.get("/keywords", asyncWrapper(getLatestKeywords));
router.get("/:keyword", asyncWrapper(getStatistics));
router.post("/:keyword", asyncWrapper(makeStatistics));

module.exports = router;
