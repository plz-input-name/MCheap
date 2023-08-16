# MCheap

MZ likes Cheap!

## About Project

MZ세대는 영리하며 효율을 중시합니다. 물건을 고를 때도 가능한 좋은 물건을 최대한 저렴하게 구매하려 하기 때문에 **중고거래 플랫폼**이 폭발적으로 성장하고 있습니다. 하지만 플랫폼이 다양해진 만큼 매물을 한눈에 파악하기 어려워졌습니다. 그래서 저희는 중고거래 플랫폼을 **중개**하는 서비스를 기획했습니다.

## 기능

### 목차

- [최근 검색어](https://github.com/plz-input-name/MCheap#%EC%B5%9C%EA%B7%BC-%EA%B2%80%EC%83%89%EC%96%B4)
- [매물 모아보기](https://github.com/plz-input-name/MCheap#%EB%A7%A4%EB%AC%BC-%EB%AA%A8%EC%95%84%EB%B3%B4%EA%B8%B0)
- [매물 추이/통계](https://github.com/plz-input-name/MCheap#%EB%A7%A4%EB%AC%BC-%EC%B6%94%EC%9D%B4%ED%86%B5%EA%B3%84)
- [즐겨찾기](https://github.com/plz-input-name/MCheap#%EC%A6%90%EA%B2%A8%EC%B0%BE%EA%B8%B0)

### 최근 검색어

- 최근 검색 키워드를 확인할 수 있습니다. 실시간으로 업데이트되는 키워드로 대세 품목을 확인할 수 있습니다.

### 매물 모아보기

- 사용자가 관심 있는 제품을 검색하면 [플랫폼](https://github.com/plz-input-name/MCheap#%EC%A7%80%EC%9B%90-%ED%94%8C%EB%9E%AB%ED%8F%BC)마다 매물을 찾고 한데 모아 사용자에게 보여줍니다. 사용자는 scroll view로 편하게 제품을 구경하다가 관심 있는 제품을 발견하면 클릭 한 번으로 해당 매물의 페이지로 이동할 수 있습니다.

- 매물 정보는 Client에서 CSS selector와 API hooking을 활용해 Web Scraping으로 수집합니다.

![img](https://github.com/plz-input-name/MCheap/blob/main/images/001.jpeg?raw=true)

### 매물 추이/통계

- 검색마다 결과의 평균 가격을 플랫폼별로 기록하여 가격 변동 추이를 제공합니다. 최근 가격이 하락 중이라면 제품 구매를 조금 더 미루는 신중한 소비가 가능합니다. 검색마다 데이터를 기록하기 때문에 인기가 많은 품목일수록 더 정확하게 추이를 파악할 수 있습니다.

- [낙관적 락을 활용한 1h 단위 throttling](https://github.com/plz-input-name/MCheap/blob/fb9c584ef05b9b37aef764a4e693baeac69d1f44/backend/service/statistics.js#L29C7-L38C8)으로 Data를 조절합니다.

- IQR를 통한 이상치(Outlier) 제거로 더 정확한 통계를 제공합니다.

![img](https://github.com/plz-input-name/MCheap/blob/main/images/002.jpeg?raw=true)

### 즐겨찾기

- 관심 품목의 가격 변동을 한 눈에 확인할 수 있습니다.

## 지원 플랫폼

- [중고나라](https://web.joongna.com/)
- [번개장터](https://m.bunjang.co.kr/)
- [당근마켓](https://www.daangn.com/)

## Tech Stack

- Androiod (Native - Kotlin)
- expressjs- AWS EC2
- mySQL - AWS RDS

### Collaborating

- [Postman](https://www.postman.com/gold-resonance-779096/workspace/mcheap)
- Github Flow
- Git hook (Husky & lint-staged)
- Discord

## License

[Apache 2.0](https://github.com/plz-input-name/MCheap/blob/main/LICENSE)

## References

frontend:

- https://bumptech.github.io/glide/

- https://developer.android.com/reference

- https://stackoverflow.com/questions/56893945/how-to-use-okhttp-to-make-a-post-request-in-kotlin

backend:

- https://devhwi.tistory.com/11

- https://expressjs.com/en/4x/api.html

- https://www.mysqltutorial.org/

- https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/EC2_GetStarted.html?icmpid=docs_ec2_console

- https://www.npmjs.com/package/forever

## Contributors

| 이름   | 영문이름        | 이메일                    | github                         | 역할     |
| ------ | --------------- | ------------------------- | ------------------------------ | -------- |
| 우이산 | E-San Woo       | startoflateness@gmail.com | https://github.com/goldentrash | backend  |
| 양석준 | SUKJOOON YANG   | ysj7191@naver.com         | https://github.com/yangjjune   | frontend |
| 성정현 | JEONGHYEON SUNG | roy0625@naver.com         | https://github.com/Roysung0625 | backend  |
