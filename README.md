# 홀로서기

자취 생활을 시작하거나 독립을 준비하는 사람들이 스스로의 삶을 당당히 꾸려가고, 그 과정에서 어려움과 고충을 이겨내기를 응원하는 마음을 담아 프로젝트 진행하는 팀입니다.


---

## 프로젝트 소개 📝


'홀로서기'는 자취를 처음 시작하는 이들이 불안감을 줄이고, 효율적이고 즐거운 독립 생활을 할 수 있도록 돕는 커뮤니티입니다. 이를 통해 자취 생활자들이 성장하고, 함께 어려움을 극복하는 장을 만들고자 합니다.


---

## 팀원 👩‍💻👨‍💻👩‍💻

| **역할** | **이름** |
| -------- | -------- |
| Backend  | 오엘리사  |
| Backend  | 이치연     |


# ERD 🛠️

<img src="https://cdn.discordapp.com/attachments/1270746914585055327/1307349065511604275/image.png?ex=6777f1b2&is=6776a032&hm=84ede431cb65aeac402d7c4541254344b3afade9c1c1676c480e5009014c22a4&" alt="ERD" with="100"/>

---

## 프로젝트 기술스택 💡

| 이미지                                                                                                       | 설명                 |
| ------------------------------------------------------------------------------------------------------------ | -------------------- |
| <img src="https://upload.wikimedia.org/wikipedia/en/thumb/3/30/Java_programming_language_logo.svg/40px-Java_programming_language_logo.svg.png" alt="java" width="50"/> | 프로그래밍 언어      |
| <img src="https://miro.medium.com/v2/resize:fit:160/0*pEXv3r4OrSFKe6Bs.png" alt="gradle" width="100"/>        | 빌드 도구           |
| <img src="https://media.licdn.com/dms/image/v2/D4D12AQFscCu_T0xB3A/article-cover_image-shrink_600_2000/article-cover_image-shrink_600_2000/0/1688794846091?e=2147483647&v=beta&t=UAzceqpsA588kvnVbHm01O35qL8lnK6eYus5DTDKR8M" alt="spring boot" width="100"/> | 프레임워크           |
| <img src="https://images.velog.io/images/park9910/post/dc139b53-7a2c-4973-ba88-23fb96b06b2e/image.png" alt="Spring Security" width="100"/>       | 보안                 |
| <img src="https://velog.velcdn.com/images/bini/post/55264881-14dd-4c1c-988f-7cfde499c41a/image.png" alt="mysql" width="100"/>                   | 데이터베이스         |
| <img src="https://velog.velcdn.com/images/pak4184/post/49d37a62-b4f3-4432-8326-7d2c3059543d/image.svg" alt="h2" width="100"/>                   | 인메모리 DB          |
| <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJrUIPz92r6o3cC76UZAYuU1vb7tmpCPmZAw&s" alt="redis" width="100"/>               | 캐시 DB              |
| <img src="https://velog.velcdn.com/images/hithae_hothae/post/6f31a620-0707-4a74-82be-5b2dbf6c0184/image.png" alt="aws ec2" width="100"/>        | 서버 호스팅          |
| <img src="https://images.velog.io/images/doohyunlm/post/6e997033-ec22-4718-80ac-78353b9a0854/s3.png" alt="aws s3" width="100"/>                 | 파일 스토리지        |
| <img src="https://blog.kakaocdn.net/dn/cWEmBD/btqH7bsQfhx/msmwQuacwVIIFNjmomkoc1/img.png" alt="aws rds" width="100"/>                           | 데이터베이스 관리    |
| <img src="https://img1.daumcdn.net/thumb/R1280x0.fpng/?fname=http://t1.daumcdn.net/brunch/service/user/dkta/image/mZyUgQW1H1vk_TFaK2FZbvZqyBM.png" alt="jwt" width="100"/> | 인증 토큰           |
| <img src="https://github.blog/wp-content/uploads/2024/07/github-logo.png" alt="github actions" width="100"/>                                     | CI/CD 도구           |
| <img src="https://gdsc-university-of-seoul.github.io/assets/images/post-WebSocket/websocket.png" alt="websocket" width="100"/>                 | 실시간 통신          |
| <img src="https://miro.medium.com/v2/resize:fit:854/1*QR_zc6t4O2Mq7Nfkh47HPQ.png" alt="stomp" width="100"/>                                      | 메시징 프로토콜      |
| <img src="https://blog.kakaocdn.net/dn/bkJzw6/btrRi1aoXRC/PkBOHUKckXsYOsV8jEawqK/img.webp" alt="smtp" width="100"/>                             | 이메일 전송          |

---

# 프로젝트 아키텍처 🏛️

<img src="https://cdn.discordapp.com/attachments/1270746914585055327/1313366412022255627/width1383.png?ex=67781509&is=6776c389&hm=bc31b244202e83110b31aec0fb2fbc9fd17bb0a91aa56a9dd182492f04ed0499&" alt="Architecture Diagram" with="100"/>

## 백엔드 배포 과정


### 서버
AWS 코드 파이프라인을 이용한 CI/CD

---

# 코딩 컨벤션 🖋️

## 1) 네이밍 규칙
1. 변수나 함수, 클래스명은 `camelCase`를 사용한다.
   - 예: `getInfo()`
2. 함수의 경우 동사 + 명사를 사용한다.
   - 예: `getInfo()`
3. DB에 저장되는 컬럼명은 `snake_case`를 사용한다.
   - 예: `member_id`
4. URL 명은 `kebab-case`를 사용하며, 명사와 소문자로 구성한다.
   - 예: `www.example.com/user`
5. 구분자로 하이픈(-)을 사용하며, 뒤로도 이어지면 구분자 없이 구성한다.

## 2) 빌더
- 가독성 향상을 위해 생성자 대신 빌더를 필수적으로 사용한다.

# 전체 API 로직 구현 진척도 및 담당자

| 기능                                 | 담당자    | 구현 여부 |
| ------------------------------------ | --------- | --------- |
| **1. 회원검증**                      |           |           |
| **POST** 아이디 찾기 - 이메일        | 오엘리사  | ✅         |
| **POST** 비밀번호 찾기 - 이메일      | 오엘리사  | ✅         |
| **POST** 일반 로그인                 | 오엘리사  | ✅         |
| **POST** 로그아웃                    | 오엘리사  | ✅         |
| **POST** 비밀번호 검증               | 오엘리사  | ✅         |
| **POST** 초기 엑세스 토큰 인증       | 오엘리사  | ✅         |
| **POST** 관리자 검증                 | 오엘리사  | ✅         |
| **2. 회원관리**                      |           |           |
| [POST] 회원가입                      | 오엘리사  | ✅         |
| [DELETE] 회원탈퇴                   | 오엘리사  | ✅         |
| [PATCH] [관리자] 회원 상태 변경      | 오엘리사  | ✅         |
| [PATCH] 회원 이메일 변경             | 오엘리사  | ✅         |
| [PATCH] 회원 닉네임 변경             | 오엘리사  | ✅         |
| [PATCH] 회원 성별 변경               | 오엘리사  | ✅         |
| [PATCH] 회원 주소 변경               | 오엘리사  | ✅         |
| [PATCH] 회원 생일 변경               | 오엘리사  | ✅         |
| [PATCH] 회원 비밀번호 변경           | 오엘리사  | ✅         |
| [POST] 회원 연락처 변경 (문자 전송)  | 오엘리사  | ✅         |
| [PATCH] 회원 연락처 변경 (저장)      | 오엘리사  | ✅         |
| [GET] 유저 회원 정보 조회            | 오엘리사  | ✅         |
| [POST] 회원가입 인증코드 문자 전송   | 오엘리사  | ✅         |
| [POST] 회원가입 인증코드 확인        | 오엘리사  | ✅         |
| [GET] 회원가입 시 아이디 중복 확인   | 오엘리사  | ✅         |
| [PATCH] 유저 설정 ON/OFF             | 오엘리사  | ✅         |
| [GET] 유저 설정 조회                 | 오엘리사  | ✅         |
| [PUT] 사용자 프로필 사진 수정        | 오엘리사  | ✅         |
| [PATCH] 사용자 프로필 설명 수정      | 오엘리사  | ✅         |
| [GET] 사용자 프로필 조회             | 오엘리사  | ✅         |
| [GET] 헤더 프로필 조회               | 오엘리사  | ✅         |
| **3. 게시글 관리**                   |           |           |
| [POST] 게시글 작성                   | 오엘리사  | ✅         |
| [PUT] 게시글 수정                    | 오엘리사  | ✅         |
| [DELETE] 게시글 삭제                 | 오엘리사  | ✅         |
| [GET] 게시글 전체 조회               | 오엘리사  | ✅         |
| [GET] 게시글 목록 조회               | 오엘리사  | ✅         |
| [GET] 게시글 상세 조회               | 오엘리사  | ✅         |
| [GET] 메인 페이지: 인기 게시글 조회  | 오엘리사  | ✅         |
| [GET] 사용자가 작성한 게시글 목록 조회| 오엘리사  | ✅         |
| [GET] 사용자가 추천한 게시글 목록 조회| 오엘리사  | ✅         |
| [POST] 게시글 추천                   | 오엘리사  | ✅         |
| [DELETE] 게시글 추천 취소            | 오엘리사  | ✅         |
| **4. 댓글 관리**                     |           |           |
| [POST] 댓글 작성                     | 이치연  | ✅         |
| [POST] 대댓글 작성                   | 이치연  | ✅         |
| [DELETE] 댓글 삭제                   | 이치연  | ✅         |
| [PATCH] 댓글 수정                    | 이치연  | ✅         |
| [POST] 댓글 추천                     | 이치연  | ✅         |
| [DELETE] 댓글 추천 취소              | 이치연  | ✅         |
| [GET] 댓글 조회(게시글)              | 이치연  | ✅         |
| [GET] 댓글 조회(유저)                | 이치연  | ✅         |
| [POST] AI 댓글 작성                   | 이치연  | ✅         |
| **5. 팔로우 관리**                   |           |           |
| [POST] 팔로우 요청                   | 이치연  | ✅         |
| [DELETE] 팔로우 끊기                 | 이치연  | ✅         |
| [GET] 본인이 팔로우 하고 있는 유저 조회| 이치연  | ✅         |
| [GET] 본인을 팔로잉 하고 있는 유저 조회| 이치연  | ✅         |
| **6. 차단 관리**                     |           |           |
| [POST] 차단 하기                     | 오엘리사  | ✅         |
| [DELETE] 차단 해제하기               | 오엘리사  | ✅         |
| [GET] 차단 목록 조회                 | 오엘리사  | ✅         |
| **7. 알림 관리**                     |           |           |
| [GET] 알림 리스트 조회               | 이치연  | ✅         |
| [PATCH] 알림 읽음 처리               | 이치연  | ✅         |
| [DELETE] 알림 삭제                   | 이치연  | ✅         |
| [GET] 알림 개수 조회                 | 이치연  | ✅         |
| **8. 알림**                          |           |           |
| [GET] 알림 목록 조회                 | 이치연  | ✅         |
| [DELETE] 알림 삭제                   | 이치연  | ✅         |
| [GET] 새로운 알림 유무 조회          | 이치연  | ✅         |
| **8. 신고 관리**                     |           |           |
| [POST] 신고 하기                     | 오엘리사  | ✅         |
| [DELETE] [관리자] 신고 기록 삭제     | 오엘리사  | ✅         |
| [PATCH] [관리자] 신고 상태 수정      | 오엘리사  | ✅         |
| [GET] [관리자] 신고 목록 조회        | 오엘리사  | ✅         |
| [GET] [관리자] 신고 개수 조회        | 오엘리사  | ✅         |
| **9. 게시글 통계**                   |           |           |
| [GET] 게시글 로그 목록 조회          | 오엘리사  | ✅         |
| [GET] 전체 게시글 수 및 오늘 작성된 게시글 수 조회 | 오엘리사  | ✅         |
| [GET] 최근 30일 동안 날짜별 게시글 작성 수 조회 | 오엘리사  | ✅         |
| [GET] 최근 30일 동안 날짜별 및 카테고리별 게시글 작성 수 조회 | 오엘리사 | ✅         |
| [PATCH] [관리자] 게시글 상태 변경    | 오엘리사  | ✅         |
| **10. 사용자 활동 로그**             |           |           |
| [GET] 사용자 행동 로그 타임별 조회   | 오엘리사  | ✅         |
| [GET] 오늘 기준 오늘 사용했던 사람들 수 조회 | 오엘리사  | ✅         |
| [GET] 현재 기준 단일 회원가입한 사람들 수 조회 | 오엘리사 | ✅         |
| [GET] 현재 접속자 수 (최근 5분 내에 활동한 사용자) 조회 | 오엘리사 | ✅         |
| [GET] 최근 한달간 일일 방문자 수 조회 | 오엘리사  | ✅         |
| [GET] 최근 한달간 일일 신규 가입자 수 조회 | 오엘리사 | ✅         |
| [GET] 오늘 하루간 시간대별 접속자 수 조회 | 오엘리사 | ✅         |
