const express = require("express");

const router = express.Router();
const statisticsCon = require("../controller/statistic");

router.get("/:keyword", statisticsCon.getStatisticData);
router.post("/:keyword", statisticsCon.makeStatistic);

module.exports = router;
