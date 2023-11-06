# Team10_BE
10조
![뽀득뽀득](https://member-images.githubusercontent.com/104883910/273441051-28dc9814-84e5-4828-abcb-c5e67d3deee4.png)

## 뽀득뽀득

뽀득뽀득은 `셀프세차장 예약 서비스`입니다. 셀프세차장에 예약이라는 시스템을 더해 유저들에게 보장된 시간동안 여유롭게 즐기는 세차 경험을 주는 것을 목표로 합니다. 이를 통해 기다리는 뒷사람 눈치보지 않고, 개인 세차용품을 마음껏 사용하며 세차를 즐기는 환경을 만들어 나가고자 합니다.

## 주요 기능

사용자의 위치 기반으로 주변 셀프세차장을 검색해 시간 단위로 예약하는 기능을 제공합니다.

<p align="center">
  <img src="https://member-images.githubusercontent.com/104883910/273441056-05c43463-5bd5-4656-95fb-f6b135d64659.png" align="center" width="24%">
  <img src="https://member-images.githubusercontent.com/104883910/273441057-8feb9154-acc2-499d-9b1a-833f59a0cebc.png" align="center" width="24%">
  <img src="https://member-images.githubusercontent.com/104883910/273441058-d923cc94-5b53-4ab2-9f52-67ebb8a6454c.png" align="center" width="24%">
  <img src="https://member-images.githubusercontent.com/104883910/273443450-10a46190-e2b8-427d-803d-ba2001291a68.png" align="center" width="24%">
</p>
또한 가맹 여부에 관련없이 서비스에 세차장을 입점 시킬 수 있고, 편하게 관리할 수 있는 기능을 사장님에게 제공합니다.

## 개발 문서

[주차별 개발 일지](https://www.notion.so/6cedabdbf1e343ab9bd64354ee45515f?pvs=4)<br>
[ERD 설계서](https://www.notion.so/ERD-984ec51ccd7e435f8331857a325d1516?pvs=4)<br>
[API 명세서](https://www.notion.so/API-67efa4eea535426b89649a8c311b80a0?pvs=4)<br>
[와이어프레임](https://www.figma.com/file/raidVFqnBM3KgJY4KFCoB1/%EB%BD%80%EB%93%9D%EB%BD%80%EB%93%9D-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?type=design&node-id=1832%3A6899&mode=design&t=X4E2jm08WA3gzqba-1)<br>

## 기술 스택

| 개발 환경 & 언어 | 웹 프레임워크    | 데이터베이스 | 테스팅 & 보안   | 기타         |
| ---------------- | ---------------- | ------------ | --------------- | ------------ |
| intelliJ         | Spring Boot      | H2 Database  | JUnit 5         | kakaomap api |
| Java 11          | Spring Framework | MySQL        | Spring Security | Swagger      |
| Gradle Build     |                  | AWS S3       | JWT             |              |

## 시작 가이드

```
$ git clone https://github.com/Step3-kakao-tech-campus/Team10_BE.git
$ cd Team10_BE
$ ./gradlew build
$ cd build/libs
$ java -jar bdbd-0.0.1-SNAPSHOT.jar
```

<details>
  <summary><b> 📝 커밋 컨벤션</b></summary>

### 커밋 단위

- **한 줄로 설명할 수 있는 행동**이어야 한다.
- 일관성이 유지되는 단위로 **최대한 작게 쪼개서** 되어야 한다.
- 어떤 커밋으로 revert 하더라도 프로그램이 오류 없이 작동해야 한다.

### 제목 규칙

- 태그는 `영소문자`로 작성
- 내용은 `한국어`로 작성
- 50자 이내로 제한
- 완전한 서술형 문장이 아니라, 간결하고 요점적인 서술을 의미.

#### 태그 예시:

- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `docs`: 문서 수정
- `refactor`: 코드 리팩토링
- `comment`: 필요한 주석 추가 및 변경
- `remove`: 파일을 삭제하는 작업만 수행한 경우
- `rename`: 파일 혹은 폴더명 수정하거나 옮기는 경우
- `style`: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- `test`: 테스트(테스트 코드 추가, 수정, 삭제, 비즈니스 로직에 변경이 없는 경우)
- `chore`: 위에 걸리지 않는 기타 변경사항 (빌드 스크립트 수정, assets image, 패키지 매니저 등)
- `design`: CSS 등 사용자 UI 디자인 변경
- `init`: 프로젝트 초기 생성

### 본문 규칙 (선택)

- 무엇을, 왜 변경했는지 상세히 글로 작성

### 꼬리말 규칙 (선택)

- 해당 커밋과 관련된 **Github Issue 번호(#)** 를 첨부하여 작성

#### 태그 예시 :

- `fix`: 이슈 수정중 (아직 해결되지 않은 경우)
- `resolves`: 이슈 수정 완료 (이슈 해결했을때 사용)
- `ref`: 참고할 이슈가 존재할 때 사용
- `related to`: 해당 커밋에 관련된 이슈번호 (아직 해결되지 않은 경우)

</details>

## 함께한 사람들

|                                                           FE                                                           |                                                         FE                                                          |                                                         FE                                                         |
| :--------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------: |
|                                     [노주영(조장)](https://github.com/juyeongnoh)                                      |                                 [김좌훈(FE 테크리더)](https://github.com/catnofat)                                  |                                   [고민주(리마인더)](https://github.com/minjuko)                                   |
| ![juyeongnoh](https://member-images.githubusercontent.com/104883910/273441208-04b916c7-3d13-437e-b269-6837e6977453.jpeg) | ![catnofat](https://member-images.githubusercontent.com/104883910/273441205-78f72cd1-1c75-495c-9d9a-9fd68ee7f755.png) | ![minjuko](https://member-images.githubusercontent.com/104883910/273441202-5cd106a5-b15c-4b1e-a609-59c1ce2d05ae.png) |

|                                                           BE                                                            |                                                         BE                                                         |                                                        BE                                                         |
| :---------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------: |
|                                 [김명지(BE 테크리더)](https://github.com/Starlight258)                                  |                                   [김철호(기획리더)](https://github.com/Cheoroo)                                   |                                   [이유진(타임키퍼)](https://github.com/2Using)                                   |
| ![Starlight258](https://member-images.githubusercontent.com/104883910/273441204-57ff5077-61b7-46fb-9252-4d07d751c2f7.png) | ![Cheoroo](https://member-images.githubusercontent.com/104883910/273441206-53e3289b-4d54-416c-a4bb-8378b6bdeee5.png) | ![2Using](https://member-images.githubusercontent.com/104883910/273441211-80d28f43-ef45-40cc-893a-f3787823f725.png) |




## 카카오 테크 캠퍼스 3단계 진행 보드

</br>

## 배포와 관련하여

```

최종 배포는 크램폴린으로 배포해야 합니다.

하지만 배포 환경의 불편함이 있는 경우를 고려하여 

임의의 배포를 위해 타 배포 환경을 자유롭게 이용해도 됩니다. (단, 금액적인 지원은 어렵습니다.)

아래는 추가적인 설정을 통해 (체험판, 혹은 프리 티어 등)무료로 클라우드 배포가 가능한 서비스입니다.

ex ) AWS(아마존), GCP(구글), Azure(마이크로소프트), Cloudtype 

```
## Notice

```
필요 산출물들은 수료 기준에 영향을 주는 것은 아니지만, 
주차 별 산출물을 기반으로 평가가 이루어 집니다.

주차 별 평가 점수는 추 후 최종 평가에 최종 합산 점수로 포함됩니다.
```

![레포지토리 운영-001 (1)](https://github.com/Step3-kakao-tech-campus/practice/assets/138656575/acb0dccd-0441-4200-999a-981865535d5f)
![image](https://github.com/Step3-kakao-tech-campus/practice/assets/138656575/b42cbc06-c5e7-4806-8477-63dfa8e807a0)

[git flowchart_FE.pdf](https://github.com/Step3-kakao-tech-campus/practice/files/12521045/git.flowchart_FE.pdf)


</br>

## 필요 산출물
<details>
<summary>Step3. Week-1</summary>
<div>

✅**1주차**

```
    - 5 Whys
    - 마켓 리서치
    - 페르소나 & 저니맵
    - 와이어 프레임
    - 칸반보드
```

</div>
</details>

---

<details>
<summary>Step3. Week-2</summary>
<div>

✅**2주차**

```
    - ERD 설계서
    
    - API 명세서
```

</div>
</details>

---

<details>
<summary>Step3. Week-3</summary>
<div>

✅**3주차**

```
    - 최종 기획안
```

</div>
</details>

---

<details>
<summary>Step3. Week-4</summary>
<div>

✅**4주차**

```
    - 4주차 github
    
    - 4주차 노션
```

</div>
</details>

---
<details>
<summary>Step3. Week-5</summary>
<div>

✅**5주차**

```
    - 5주차 github
    
    - 5주차 노션
```

</div>
</details>

---

<details>
<summary>Step3. Week-6</summary>
<div>

✅**6주차**

```
    - 6주차 github
    
    - 중간발표자료
    
    - 피어리뷰시트
```

</div>
</details>

---

<details>
<summary>Step3. Week-7</summary>
<div>

✅**7주차**

```
    - 7주차 github
    
    - 7주차 노션
```

</div>
</details>

---

<details>
<summary>Step3. Week-8</summary>
<div>

✅**8주차**

```
    - 중간고사
    
```

</div>
</details>

---

<details>
<summary>Step3. Week-9</summary>
<div>

✅**9주차**

```
    - 9주차 github
    
    - 9주차 노션
```

</div>
</details>

---

<details>
<summary>Step3. Week-10</summary>
<div>

✅**10주차**

```
    - 10주차 github
    
    - 테스트 시나리오 명세서
    
    - 테스트 결과 보고서
```

</div>
</details>

---

<details>
<summary>Step3. Week-11</summary>
<div>

✅**11주차**

```
    - 최종 기획안
    
    - 배포 인스턴스 링크
```

</div>
</details>

---

## **과제 상세 : 수강생들이 과제를 진행할 때, 유념해야할 것**

```
1. README.md 파일은 동료 개발자에게 프로젝트에 쉽게 랜딩하도록 돕는 중요한 소통 수단입니다.
해당 프로젝트에 대해 아무런 지식이 없는 동료들에게 설명하는 것처럼 쉽고, 간결하게 작성해주세요.

2. 좋은 개발자는 디자이너, 기획자, 마케터 등 여러 포지션에 있는 분들과 소통을 잘합니다.
UI 컴포넌트의 명칭과 이를 구현하는 능력은 필수적인 커뮤니케이션 스킬이자 필요사항이니 어떤 상황에서 해당 컴포넌트를 사용하면 좋을지 고민하며 코드를 작성해보세요.

```

</br>

## **코드리뷰 관련: review branch로 PR시, 아래 내용을 포함하여 코멘트 남겨주세요.**

**1. PR 제목과 내용을 아래와 같이 작성 해주세요.**

> PR 제목 : 부산대_0조_아이템명_0주차
>

</br>

</div>

---


