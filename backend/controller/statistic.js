const app = require("express");
const makeCon = require("../model/index");
const dbExe = require("../model/averagePrice");

exports.getStatisticData = async (req, res, next) => {
  const size = req.query.size;
  const keyword = req.params.keyword;

  try {
    const dbCon = await makeCon;
    //console.log(`size : ${size} / keyword : ${keyword}`);

    const result = await dbExe.find(dbCon, keyword, size);
    //console.log(result);
    res.status(200).json(result[0]).sned();
  } catch (err) {
    console.log(err);
    res.status(404).send();
  }
};
