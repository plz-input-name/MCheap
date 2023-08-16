module.exports = class Platform {
  constructor(originPriceArr) {
    const sortedPriceArr = originPriceArr.map(Number).sort((a, b) => a - b);

    const q1 = sortedPriceArr[Math.floor(sortedPriceArr.length * 0.25)];
    const q3 = sortedPriceArr[Math.floor(sortedPriceArr.length * 0.75)];
    const iqr = q3 - q1;

    const THRESHOLD = 1.5;
    const max = q3 + iqr * THRESHOLD;
    const min = q1 - iqr * THRESHOLD;

    this.priceArr = sortedPriceArr.filter(
      (price) => min <= price && price <= max
    );
  }

  get average() {
    const sum = this.priceArr.reduce((sum, curr) => (sum += curr));
    return Math.ceil(sum / this.priceArr.length);
  }
};
