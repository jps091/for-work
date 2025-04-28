#  📑 For-Work - 쇼핑몰

## 📖 목 차
1. [프로젝트 개요](https://github.com/jps091/for-work/blob/dev/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)
2. [프로젝트 기여도](https://github.com/jps091/for-work/blob/dev/README.md#-%EA%B8%B0%EC%97%AC%EB%8F%84)
3. [데이터베이스 모델링](https://github.com/jps091/for-work/tree/dev#-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EA%B8%B0%EB%B0%98-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EB%AA%A8%EB%8D%B8%EB%A7%81)
4. [프로젝트 구조](https://github.com/jps091/for-work/blob/dev/README.md#-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B5%AC%EC%A1%B0)
5. [API 명세서](https://github.com/jps091/for-work/blob/dev/README.md#-api-%EB%AA%85%EC%84%B8%EC%84%9C)
6. [기술 스택](https://github.com/jps091/for-work/blob/dev/README.md#%EC%82%AC%EC%9A%A9-%EA%B8%B0%EC%88%A0-%EC%8A%A4%ED%83%9D)
7. [시스템 아키텍쳐](https://github.com/jps091/for-work/blob/dev/README.md#-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98)
8. [주요 기능 동작 흐름 및 검증](https://github.com/jps091/for-work/blob/dev/README.md#-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EB%8F%99%EC%9E%91-%ED%9D%90%EB%A6%84-%EB%B0%8F-%EA%B2%80%EC%A6%9D)
9. [기능 구현 화면](https://github.com/jps091/for-work/blob/dev/README.md#-%EA%B5%AC%EB%A7%A4%EC%9E%90-%EA%B4%80%EB%A0%A8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84-%ED%99%94%EB%A9%B4)
10. [프로젝트 회고](https://github.com/jps091/for-work/blob/dev/README.md#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0)

---

## 📁 프로젝트 개요
**이 프로젝트는 단순한 CRUD 구현을 넘어 복잡한 비즈니스 로직을 설계, 구현, 검증하는 경험을 쌓기 위해 기획되었습니다.**

**특히, 결제, 환불, 자동 구매 확정, 주문 상태 관리 등 쇼핑몰의 핵심 기능을 효과적으로 설계하고, 성능 최적화 및 가용성을 고려한 개발을 목표로 삼았습니다.**

**또한 요구사항을 분석하여 개념적, 논리적, 물리적 모델링 방식의 데이터베이스 설계 경험을 통해, 비즈니스 로직이 데이터 구조에 적절히 반영되도록 경험을 쌓는데 목표로 하였습니다.**

---

## 👥 기여도
**팀원 총 2명(FE 개발자 1명, 본인 1명), DB 설계 부터 백엔드 개발 및 배포 100% 기여**

- [서비스 배포 링크](https://main--resume-market.netlify.app/)

- [프로젝트 관련 포스팅](https://github.com/jps091/for-work/wiki)

- [Github 칸반보드](https://github.com/users/jps091/projects/2/views/1)를 통한 Git Flow 관리

  <img width="600" height="400" alt="image" src="https://github.com/user-attachments/assets/6022ee5f-290d-4ea6-8f98-91384e6d7aa2" />


---

## 💼 요구사항 기반 데이터베이스 모델링

📌 **주요 요구사항**

1. 판매자는 관리자에게 이력서 판매 신청을 할 수 있다.
2. 관리자는 판매 요청을 검토하고 수락/거절할 수 있다.
3. 판매자는 자신의 이력서 판매 상태 및 판매량을 확인할 수 있다.
4. 구매자는 이력서를 장바구니에 담고 결제할 수 있다.
5. 구매 확정된 이력서는 구매자 이메일로 자동 전송된다.
6. 주문은 일부 취소가 가능하지만, 구매 확정 후 환불은 불가능하다.
7. 주문 및 결제 내역은 사후 관리 목적으로 별도 저장해야 한다.

🏗 **최종 개체 및 관계 목록**

- 개체 : 사용자, 이력서, 주문내역, 판매글, 장바구니, 섬네일, 결제 내역, 매일 내역
- 관계 : 주문,판매,결정, 담기, 관리, 전송, 판매글 등록, 섬네일 등록

⚠️ **최적화**
- `사용자` 개체에서 판매자/구매자/관리자를 구분하는 별도 테이블을 만들지 않고, **속성(Type)으로 구분**  
- `주문내역`의 상태(결제 완료, 환불, 구매 확정 등)도 **별도 테이블 대신 속성(Status)으로 관리**  
- 1:N 관계는 외래 키(FK)로, N:M 관계는 **중간 테이블**을 두어 연결</br> 
</br>

🚀 **ERD (Entity Relationship Diagram)**

<img width="500" height="700" alt="image" src="https://github.com/user-attachments/assets/d6b7139b-d502-4523-b26e-b897ef930a1b" /></br>

</br>

🚀 **관계형 데이터 모델**

> **1:1, 1:N은 FK로 참조, N:M 관계는 별도 테이블 구성** 

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/fa804a6b-7a15-4e85-bab2-45bf468d67c4"></br>

⚠️ **로그 테이블(메일 내역, 주문 내역, 재시도 내역) 외래 키 제거 이유**  
- `파티셔닝(Partitioning)` 적용 시 **외래 키 제약 조건을 사용할 수 없음**  
- 로그성 데이터는 **무결성보다 조회 성능 최적화가 중요**  
- `파티션 프루닝(Partition Pruning)`을 통해 **불필요한 데이터 스캔 최소화** 

---

## 🗂 프로젝트 구조

1. Presentation, Application, Domain, Persistence 4계층으로 프로젝트를 구성 하였습니다.
2. Persistent Object와 Domain Object를 분리 하여 핵심 비지니스 로직은 Domain Object에 구현 하였습니다.
3. Repository는 인터페이스로 설계 하여 서비스 계층과 결합도를 낮췄습니다.
- 더 자세한 설명은 [4계층 프로젝트를 도입한 이유](https://github.com/jps091/for-work/wiki/%EC%A7%80%EC%86%8D-%EC%84%B1%EC%9E%A5-%EA%B0%80%EB%8A%A5%ED%95%9C-%EC%BD%94%EB%93%9C-%EC%84%A4%EA%B3%84-%E2%80%90-4%E2%80%90Tier-Layer,-OOP&%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4)를 확인 해주세요.

<img width="900" height="600" alt="image" src="https://github.com/user-attachments/assets/723f78dc-6196-47ca-b287-988fa17120b5" />

---

## 📝 API 명세서

**[1. 스웨거 API 명세서 ver](http://43.201.73.11:8085/swagger-ui/index.html?urls.primaryName=%EC%A0%84%EC%B2%B4%20%EC%82%AC%EC%9A%A9%EC%9E%90%EB%A5%BC%20%EC%9C%84%ED%95%9C%20For-work%20Service%20%EB%8F%84%EB%A9%94%EC%9D%B8%20API#/OpenController/login)**

- **@ApiResponses를 통해 예외 상황 Docs 코드를 하드코딩하지 않고, 커스텀 어노테이션과 리플렉션을 활용하여 명세서 효율적으로 작성했습니다.(Controller 코드량 평균 20% 감소)**

<img width="700" height="400" alt="image" src="https://github.com/user-attachments/assets/22dc952f-6b34-4a74-8e73-34368cdf4fa3" /></br>
<img width="700" height="400" alt="image" src="https://github.com/user-attachments/assets/c38577c9-ebe2-49e9-9f77-435fc126ea1f" />


**[2. 노션 API 명세서 ver](https://cuboid-sunfish-749.notion.site/API-Notion-125bb83a1adf800ebb04e75115a0e3ac?pvs=4)**

<img width="800" height="400" alt="image" src="https://github.com/user-attachments/assets/90f20877-dd53-4750-b76f-a83180318456" />

---

## ⚒ 사용 기술 스택

- Backend: Java 17, Spring Boot, JPA, Query DSL, MyBatis, RabbitMQ, JWT, Swagger
- Frontend: React
- Test: JUnit5, Spock
- Database: MySQL, Redis, Caffeine
- Load Test & Monitoring: JMeter, PostMan, Scouter
- Infra: EC2, S3, CDN, RDS, ElastiCache, SES
- Containerization: Docker
- CI/CD: Github Actions, ECR, CodeDeploy

---

## 🪃 아키텍처

<img width="850" height="600" alt="image" src="https://github.com/user-attachments/assets/2d5452a8-4289-4d85-a8a4-3fca92988f4f" />

---

## 🚴 주요 기능 동작 흐름 및 검증

</br>

**1. 판매자 이력서 판매글 등록 흐름**

<img width="800" height="400" alt="image" src="https://github.com/user-attachments/assets/f86cfc3c-2a59-4125-99c1-6174077f1fce" /></br>


</br>

**2. 이력서 주문 및 결제 흐름**

<img width="1000" height="500" alt="image" src="https://github.com/user-attachments/assets/a391d5bc-294e-4635-950a-f378c0ff4802" /></br>

**3. 구매자 메일 전송 흐름**

<img width="600" height="350" alt="image" src="https://github.com/user-attachments/assets/6fb5d193-c927-4717-9a1a-2b8888cbabc7" /></br>


**4. 메일 전송 (RabbitMQ 비동기 큐 & Dead Letter 처리)**

<img width="1000" height="450" src="https://github.com/user-attachments/assets/e454b6d0-0356-45fa-9e89-5bbb3a227216"></br>


**✅ 약 150개의 단위 테스트로 검증**

<img width="1000" height="450" alt="image" src="https://github.com/user-attachments/assets/d5a4786b-6c63-4134-bf7a-140fc53b8f3a" />


---

## 🚀 구매자 관련 기능 구현 화면

</br>

**1. 판매글 조회(Pagination Key 방식), 회원가입, 로그인, 관리자 문의**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/478da93c-71fe-4b15-b937-f7d2f8ad7b8d" /></br>

**2. 결제, 주문 전체 내역, 주문 상세 내역**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/4b7a51e1-465d-460c-b88d-3c56eb5fb5da" /></br>

---

## 🌈 판매자 관련 기능 구현 화면

**이력서 판매 요청 작성, 나의 이력서 상태**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/2833d15a-bd28-4f36-8a09-bf041a077b58" />

---

## 🔥 어드민 관련 기능 구현 화면


**요청 온 이력서 전체 조회, 상세 조회**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/c55bf273-59f2-4a33-956f-a19918e2c73f" />

---

## 📓 프로젝트 회고

**이번 프로젝트에서는 결제, 환불, 자동 구매 확정, 주문 상태 관리, 페이징 처리(Pagination Key)와 같은 로직을 간소화하여 구현했으며, 부하 테스트와 모니터링을 통해 성능 최적화를 경험했습니다. 또한, 약 150개의 유닛 테스트를 작성하고 검증하여 코드의 신뢰성을 높일 수 있었습니다.**

**프로젝트 과정에서 API 응답에 엔티티를 직접 노출하거나, 서비스 계층의 비대화, ORM 양방향 관계와 같은 주요 안티 패턴을 식별하고, 4계층 구조, 일급 컬렉션, 디자인 패턴 등을 적용해 더 객체지향적이고 품질 높은 코드에 대해 생각할 수 있었습니다.**

**또한, 이미지 업로드 방식도 Presigned-URL 도입과 AWS Lambda@Edge 기반의 이미지 리사이징 및 WebP 변환을 적용하여 CDN에 캐싱 하는 방식으로 최적화를 진행할 수 있었습니다.**

**마지막으로 프론트엔드 개발자와 협업하면서 커뮤니케이션 방식을 익힐 수 있었고, 데이터베이스 모델링 부터 백엔드 서버 개발, CI/CD 프로세스를 구축 및 인프라 설계까지 경험할 수 있었던 의미 있는 프로젝트였습니다.**
