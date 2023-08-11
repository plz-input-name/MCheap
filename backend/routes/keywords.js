const express = require("express");
const router = express.Router();
const { asyncWrapper } = require("./helper");

const { getLatestKeywords } = require("../controller/keywords");
router.get("/", asyncWrapper(getLatestKeywords));

module.exports = router;
