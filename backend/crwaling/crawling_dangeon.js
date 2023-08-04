const { Builder, By } = require("selenium-webdriver");

//최근 100개의 물건 가격을 가져옵니다 *return Array
const getLatest100Prices = async (searchName) => {
  let driver = await new Builder().forBrowser("chrome").build();
  const returnArr = new Array(); //Int Array
  try {
    driver.get(`https://m.bunjang.co.kr/search/products?q=%${searchName}`);
    //`#root > div > div > div:nth-child(4) > div > div:nth-child(4) > div > div:nth-child(${idx}) > a > div:nth-child(2) > div:nth-child(2) > div`
    for (let i = 1; i <= 100; i++) {
      const result = await driver.findElement(
        By.css(
          `#root > div > div > div:nth-child(4) > div > div:nth-child(4) > div > div:nth-child(${i}) > a > div:nth-child(2) > div:nth-child(2) > div`
        )
      );
      returnArr.push(+(await result.getText()).toString().replace(/,/g, ""));
    }
  } catch (err) {
    console.log(err);
    return returnArr;
  } finally {
    driver.close();
  }
  //console.log(returnArr.length + "\n"+ returnArr);

  return returnArr;
};

module.exports = {
  getLatest100Prices,
};
