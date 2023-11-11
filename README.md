## 프로젝트 소개

뽀득뽀득은 `셀프세차장 예약 및 관리 서비스`를 제공합니다.
이 서비스는 예약 관리를 자동화함으로써 사용자와 세차장 관리자 모두에게 편의성을 제공합니다. 사용자들은 원하는 시간에 세차장을 예약할 수 있고, 관리자는 자신의 세차장을 손쉽게 홍보하고 예약을 관리하여 매출을 증대시킬 수 있습니다.

## 서비스 주요 기능

- 예약 시스템
  - 예약 생성, 조회, 수정, 삭제 기능 구현
  - 등록을 하면 자동으로 관리할 수 있는 방법을 예약시스템의 도입으로 해소
  - 세차장을 영업시간과 가격을 등록하고 세차장에서 가용가능한 베이를 지정한 하면 세차장 예약관리의 자동화가 가능
- 세차장 검색 시스템
  - 위치기반 검색, 키워드 기반 검색 기능 구현
  - 세차장을 찾을때 한눈에 들어오지 않던 키포인트들 -> 키워드 검색을 통해 각 세차장마다
    어떤 키포인트들이 있고, 이를 검색할 수 있음
  - 위치기반한 세차장 찾기 시스템 도입으로, 세차장과 나와의 거리를 파악할 수 있도록 작성
- 리뷰 시스템
  - 각 세차 예약 건수마다 리뷰를 작성할 수 있음. 해당 리뷰에 대한 키워드로 해당 세차장에 대한 사용자 입장의 평가가 어떠한지 한눈에 파악 가능
  - 별점 시스템을 통해 해당 세차장의 평균 평점이 어떤지 확인할 수 있음
- 세차장 관리 시스템
  - 세차장을 등록을 손쉽게 하고, 보유하고 있는 모든 세차장들의 매출을 한눈에 확인할 수 있고, 개별 세차장의 예약 현황과 매출 관리를 쉽게 확인할 수 있도록 만듦
- 결제하기
  - 카카오페이 api를 사용하여 mock 결제 절차를 구현
  - 결제 금액에 관련한 신뢰성있는 로직을 구현하여 결제 신뢰성 확보

## 프로젝트 문서 링크 모음

### 개발 문서

| 문서 종류          | 링크                                                                               |
| ------------------ | ---------------------------------------------------------------------------------- |
| API 문서           | [API 문서](https://www.notion.so/API-67efa4eea535426b89649a8c311b80a0?pvs=4)       |
| ERD                | [ERD](https://www.notion.so/ERD-984ec51ccd7e435f8331857a325d1516?pvs=4)            |
| 에러코드 정의서    | [에러코드 정의서](https://www.notion.so/004bbaf9938443bd92e86c0f51bb70f0?pvs=4)    |
| 코딩 컨벤션        | [코딩 컨벤션](https://www.notion.so/37636b2e131048acb4e238472496b246?pvs=4)        |
| Git 작업 단위      | [Git 작업 단위](https://www.notion.so/git-70b0a49482c8434abab83b06c0bc94c1?pvs=4)  |
| 커밋 컨벤션        | [커밋 컨벤션](https://www.notion.so/f88b7fb79bf94fdca5099a70d971983b?pvs=4)        |
| 테스트 시나리오    | [테스트 시나리오](https://www.notion.so/c55b2e5aff9b443a90d0799d3b84f8a5?pvs=4)    |
| 테스트 결과 보고서 | [테스트 결과 보고서](https://www.notion.so/5db4232de5c047a7b3eac6b4b214dfed?pvs=4) |

### 기획 문서

| 문서 종류     | 링크                                                                                                                                                                                                                           |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 기획 자료     | [기획 자료](https://www.notion.so/as_is_to_be-8ab0b5dab76a4a608343200301ac5008?pvs=4)                                                                                                                                          |
| 기획 발표자료 | [기획 발표자료](https://www.notion.so/Weekly-01cdc0b6e98a418db0d7ee886e8b7594?pvs=4#64122c5a98cb4426b4302c94fdbe57a7)                                                                                                          |
| 와이어 프레임 | [와이어 프레임](https://www.figma.com/file/raidVFqnBM3KgJY4KFCoB1/%EB%BD%80%EB%93%9D%EB%BD%80%EB%93%9D-%EC%99%80%EC%9D%B4%EC%96%B4%ED%94%84%EB%A0%88%EC%9E%84?type=design&node-id=478%3A3616&mode=design&t=tW0gdxACISuQbhdX-1) |

### 배포 주소

| 서비스          | 링크                                                            |
| --------------- | --------------------------------------------------------------- |
| 뽀득뽀득 사용자 | [사용자 페이지](https://k923062c3c512a.user-app.krampoline.com) |
| 뽀득뽀득 관리자 | [관리자 페이지](https://kae2e326be433a.user-app.krampoline.com) |

## API문서

| 권한                     | 기능              | 설명                          |
| ------------------------ | ----------------- | ----------------------------- |
| 공통                     |                   |                               |
|                          | 🚗 세차장 조회    | 공개 세차장 정보 조회         |
|                          | 📝 리뷰 조회      | 공개 리뷰 조회                |
|                          | 🔍 검색           | 세차장, 리뷰 검색             |
|                          | 📖 예약 정보 조회 | 공개 예약 정보 조회           |
| 일반 회원, 세차장 관리자 |                   |                               |
|                          | 📅 예약 관리      | 예약 생성, 수정, 취소         |
|                          | 💬 리뷰 관리      | 리뷰 작성, 수정, 삭제         |
|                          | 🚗 세차장 조회    | 세차장 상세 정보 조회         |
|                          | 📈 통계           | 사용자 통계 조회              |
| 세차장 관리자            |                   |                               |
|                          | 🚗 세차장 관리    | 세차장 정보 등록, 수정, 삭제  |
|                          | 📅 예약 관리      | 예약 승인, 거절               |
|                          | 💬 리뷰 응답      | 리뷰에 대한 응답 작성         |
|                          | 📈 통계           | 세차장 예약 및 매출 통계 조회 |

## 예약 신뢰성

- 사용자가 요청한 예약이 운영 시간을 준수하고 중복 예약을 방지하는 등 예약 오류를
  최소화하였습니다.
  - **`운영 시간` 내 예약 검증** : 운영시간 내에 예약이 이루어지는지 확인합니다.
  - **`중복 예약` 검증** : 기존 시간과 겹치지 않는지 확인합니다.
  - **`예약 시간` 배수 검증** : 예약 시간이 30분의 배수인지 확인합니다.
  - **`최소 예약`** **시간 검증** **:** 예약 시간이 최소 30분 이상인지 검사합니다.
- 예약 권한별 가이드라인
  - \***\*예약 조회\*\***
    - 사용자는 자신의 예약 내역만 조회할 수 있습니다.
  - **예약 수정**
    - 사용자는 자신의 예약만 수정할 수 있습니다.
  - \***\*예약 취소\*\***
    - 사용자는 자신의 예약만 취소할 수 있습니다.

## **결제 신뢰성**

### **카카오페이 기반 결제 시스템**

- `카카오페이 결제 API`를 사용하여 결제 준비 및 결제 승인을 처리합니다.

### **금액 검증 메커니즘**

- **프론트엔드 금액 검증**: 프론트엔드에서 요청한 결제 금액(**`total_amount`**)과 서버 측에서 계산한 금액을 비교하여 결제 오류를 미연에 방지합니다.
- 프론트엔드와 서버 측의 계산 금액이 다를 경우, **`BadRequestError`**(`400`) 예외 (**`1001 VALIDATION_FAILED`** 에러 코드)를 발생시킵니다.

## 검증

- **AOP를 사용한 유효성 검증**
  - **`GlobalValidationHandler`에서** `POST`와 `PUT` 요청에 대한 공통적인 유효성 검증 로직을 정의했습니다.
  - 유효성 검증 오류가 있을 경우 **`BadRequestError`**(`400`) 예외를 발생시켰습니다.
- **DTO 내부의 `@Valid` 어노테이션을 사용한 검증 : `@NotBlank`**, **`@Size`**, **`@NotNull`** 등의 어노테이션을 사용하여 각 필드의 유효성 규칙을 생성하고 검증했습니다.

```java
@Getter
@Setter
public static class FileUpdateDTO {
    @NotBlank(message = "File URL is required")
    private String url;

    @PastOrPresent(message = "Uploaded time must be in the past or present")
    private LocalDateTime uploadedAt;
}
```

## 예외 처리

### **명확한 예외 처리 및 상태코드 할당**

- `HTTP 상태 코드`에 따라 예외 클래스를 정의하여 예외를 분류할 수 있습니다.

![Untitled](README%20md%204002eaae140145a59993ba950ce3fe3e/Untitled.png)

- 상태 코드 내에서 `사용자 정의 에러 코드`를 정의하여 명확하게 오류의 세부 사항을 쉽게 파악할 수 있습니다.

```java
public enum ErrorCode implements ApiException.ErrorCode {
    VALIDATION_FAILED(1001, "Request Validation Failed"),
    WRONG_REQUEST_TRANSMISSION(1002, "Wrong Request Transmission"),
    MISSING_PART(1003, "Missing essential part"),
    DUPLICATE_RESOURCE(1004, "Duplicate Resource");
    ...
}
```

- 예외 발생시 `status`(상태 코드), `code`(사용자 정의 에러코드), `message`(에러 메세지) 형식을 지켜 일관된 응답이 전송되도록 만들었습니다.
- **`GlobalExceptionHandler`** 에서 다양한 유형의 예외를 잡고 적절한 HTTP 응답을 반환하도록 했습니다.
- `**DefaultErrorAttributes**` 클래스를 확장한 **`CustomErrorAttributes` 를** 구현하여 핸들러가 잡지 못한 모든 예외에 대해서도 일관된 응답 형식을 보장했습니다.
- 새로운 예외 유형을 추가할 수 있도록 확장 가능하게 설계했습니다.

## 보안

### 파일 신뢰성

- `파일 확장자` 검증 : 지정 파일 형식만 업로드를 허용하여 보안 및 데이터 무결성을 강화합니다.

```java
static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");
```

- `고유 파일명`: UUID를 통해 고유한 파일 이름을 생성하여 원본 파일명이 노출되지 않고 파일명 충돌을 방지합니다.

```java
static final List<String uniqueFilename = UUID.randomUUID().toString() + extension;
        String keyName = "uploads/" + uniqueFilename;> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");
```

### CORS 설정 환경별 분리

- 와일드 카드(`*`)를 사용하지 않고 `특정 출처`만 허용하도록 설정했습니다.
- 개발 환경과 운영 환경에 따라 CORS 설정을 구분했습니다.
  - `개발 환경`: 로컬 url로 지정된 특정 출처만을 허용했습니다.
  - `운영 환경`: 특정 패턴을 설정하여 허용되는 출처를 엄격히 제한했습니다.

```java
@Bean
    @Profile("!prod")
    public CorsConfigurationSource devCorsConfigurationSource() {
        // 개발 환경용 CORS 설정
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin(frontlocalurl);
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    @Profile("prod")
    public CorsConfigurationSource prodCorsConfigurationSource() {
        // 운영 환경용 CORS 설정
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        // USER, OWNER 배포 주소 (React)
        configuration.addAllowedOriginPattern(prodfrontuserurl);
        configuration.addAllowedOriginPattern(prodfrontownerurl);
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
```

### 환경 변수 설정 및 **Kubernetes Secret 사용**

![Untitled](README%20md%204002eaae140145a59993ba950ce3fe3e/Untitled%201.png)

- 보안을 강화하기위해 **`@Value` 을 통해** 구성값을 소스 코드 내에 하드코딩하지 않고 외부 구성 파일과 Kubernetes의 Secret을 활용하여 관리합니다.
- 변경 가능성이 있는 key들을 환경 변수로 관리하여 재사용성을 높였습니다.

## \***\*권한 중심의 사용자 및 관리자 경험 최적화\*\***

### **사용자 권한과 보안 강화**

- `역할 기반` 접근 권한 분리와 `토큰의 유무`에 따른 권한 분리를 제공합니다.
- `역할 기반` 권한관리 :\*\* Role에 따른 권한 분리를 스프링 시큐리티의 `SecurityConfig`에서 제공합니다.

```java
http.authorizeRequests(authorize -> authorize
                .antMatchers("/api/open/**").permitAll()
                .antMatchers("/api/user/**").access("hasAnyRole('USER', 'OWNER')")
                .antMatchers("/api/owner/**").access("hasRole('OWNER')")
                .anyRequest().authenticated());
```

- **`토큰 유무` 권한 관리 :** 서비스 가입 전 둘러보기에서, open 컨트롤러에 있는 기능을 권한없이 접근할 수 있도록 합니다.

```java
private boolean isNonProtectedUrl(HttpServletRequest request) {
        AntPathRequestMatcher openMatcher = new AntPathRequestMatcher("/api/open/**");
        return openMatcher.matches(request);
    }
```

- **`권한`에 따른 컨트롤러 구조 :** 권한에 따라 Controller를 분리하여 권한 관리를 명확하게 했습니다.

![Untitled](README%20md%204002eaae140145a59993ba950ce3fe3e/Untitled%202.png)

## 리팩토링

- **재사용성** : 반복되어 사용되는 메서드들을 추출해 utils로 분리하여 코드 재사용성을 높였습니다.

![Untitled](README%20md%204002eaae140145a59993ba950ce3fe3e/Untitled%203.png)

- **코딩 컨벤션** : 코딩 컨벤션에 따라 클래스, 메서드명, 변수명등을 작성하고 띄어쓰기 및 중괄호
  스타일등을 지켜 코드 통일성을 높였습니다.

## 프로젝트 구조

```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┣ 📂bdbe
 ┃ ┃ ┃ ┣ 📂bdbd
 ┃ ┃ ┃ ┃ ┣ 📂_core
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AWSConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestTemplateConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BadGatewayError.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BadRequestError.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomErrorAttributes.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ForbiddenError.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜InternalServerError.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotFoundError.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UnAuthorizedError.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GlobalValidationHandler.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetails.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JWTProvider.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂utils
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ApiUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DateUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FileUploadUtil.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FilterResponseUtils.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Haversine.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KeywordTypeConverter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberUtils.java
 ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommonMemberController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂open
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenCarwashController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenMemberController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OpenReservationController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OpenReviewController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂owner
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OwnerBayRestController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OwnerCarwashController.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserPayController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserReservationController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserReviewController.java
 ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┣ 📂bay
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BayRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BayResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂carwash
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CarwashRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CarwashResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂file
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FileRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FileResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂owner
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OwnerResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂pay
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PayRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PayResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂reservation
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReservationRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReservationResponse.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂review
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ReviewRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewResponse.java
 ┃ ┃ ┃ ┃ ┣ 📂model
 ┃ ┃ ┃ ┃ ┃ ┣ 📂bay
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Bay.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂carwash
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Carwash.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂file
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜File.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂keyword
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂carwashKeyword
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CarwashKeyword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂reviewKeyword
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewKeyword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Keyword.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂location
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Location.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Member.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂optime
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Optime.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂reservation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Reservation.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Review.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜Code.java
 ┃ ┃ ┃ ┃ ┣ 📂pay
 ┃ ┃ ┃ ┃ ┃ ┗ 📜.DS_Store
 ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┣ 📂bay
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BayJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂carwash
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CarwashJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂file
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FileJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂keyword
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂carwashKeyword
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CarwashKeywordJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂reviewKeyword
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewKeywordJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KeywordJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂location
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LocationJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂optime
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜OptimeJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂reservation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReservationJPARepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂review
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewJPARepository.java
 ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┣ 📂bay
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BayService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂carwash
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CarwashService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂file
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FileService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OwnerService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂pay
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PayService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂reservation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReservationService.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📂review
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ReviewService.java
 ┃ ┃ ┃ ┃ ┣ 📜.DS_Store
 ┃ ┃ ┃ ┃ ┗ 📜BdbdApplication.java
 ┃ ┃ ┃ ┗ 📜.DS_Store
 ┃ ┃ ┗ 📜.DS_Store
 ┃ ┣ 📂resources
 ┃ ┃ ┣ 📜application-local.yml
 ┃ ┃ ┣ 📜application-prod.yml
 ┃ ┃ ┗ 📜application.yml
 ┃ ┗ 📜.DS_Store
```

## 커밋 **컨벤션**

프로젝트의 유지보수 및 협업 효율성을 높이기 위해, 다음과 같은 커밋 컨벤션을 준수합니다.

### 커밋 단위

- **한 줄로 설명할 수 있는 행동**이어야 한다.
- 일관성이 유지되는 단위로 **최대한 작게 쪼개져**야 한다.
- 어떤 커밋으로 revert 하더라도 프로그램이 오류 없이 작동해야 한다.

### **제목 규칙**

- 태그는 `영소문자`로 작성
- 내용은 `한국어`로 작성
- 50자 이내로 제한
- 완전한 서술형 문장이 아니라, 간결하고 요점적인 서술을 의미.

`feat`: 새로운 기능 추가

`fix`: 버그 수정

`docs`: 문서 수정

`refactor`: 코드 리팩토링

`comment`: 필요한 주석 추가 및 변경

`wip` : 미완성된 코드일 경우(상의가 필요한 부분일때, 권장 X)

`remove`: 파일을 삭제하는 작업만 수행한 경우

`rename`: 파일 혹은 폴더명 수정하거나 옮기는 경우

`style`: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우

`test`: 테스트(테스트 코드 추가, 수정, 삭제, 비즈니스 로직에 변경이 없는 경우)

`chore`: 위에 걸리지 않는 기타 변경사항 (빌드 스크립트 수정, assets image, 패키지 매니저 등)

`design`: CSS 등 사용자 UI 디자인 변경

`init`: 프로젝트 초기 생성

> **# FE 예시**

feat: 버튼 컴포넌트 구현
design: font-size를 16px에서 14px로 조정

### 본문 규칙 (선택)

- 무엇을, 왜 변경했는지 상세히 작성

> **# FE 예시**

Card 컴포넌트 내에서 텍스트가 영역 밖으로 벗어났기 때문에 font-size를 16px 에서 14px로 변경

### 꼬리말 규칙 (선택)

- 해당 커밋과 관련된 Github 이슈 번호를 첨부

`fix`: 이슈 수정 중(아직 해결되지 않은 경우)

    `resolves`: 이슈를 해결했을 때 사용

    `ref`: 참고할 이슈가 있을 때 사용

    `related to`: 해당 커밋에 관련된 이슈번호 (아직 해결되지 않은 경우)

이 컨벤션을 따름으로써, 프로젝트 참여자는 커밋의 목적과 범위를 쉽게 이해하고,
이력을 효율적으로 추적할 수 있습니다.

## 중간 피드백 반영도

### 이미지 파일 관리

- 파일 저장 시스템을 개선하여, 모든 이미지 파일은 지정된 경로에 저장
- 파일 전송의 정확성을 검증하기 위한 로직을 구현하여, 파일 전달 과정에서의 오류 최소화

### 예약 시스템 최적화

- 예약 데이터에 대한 날짜 및 상태 관리를 철저히 하여 시스템의 정확석을 향상
- 예약 시스템의 로직을 구조화하여, 관리 및 유지보수의 용이성을 강화

### 지도 통합 및 상호작용

- 카카오 지도 API와의 연동 상태를 지속적으로 모니터링하고, 지도 표시 기능의 정확성을 개선
- 사용자 인터페이스를 통해 지도 상호작용을 더욱 직관적이고 효율적으로 만듦

### 보안 및 권한 관리

- Spring Security를 사용하여 사용자 권한 관리를 강화
- 보안 프로토콜을 업데이트하고, 접근 제어 메커니즘을 통해 시스템의 안전을 보장

### 검색 기능 향상

- 검색 알고리즘을 최적화하고, 인덱싱 메커니즘을 통해 검색 속도와 정확성을 개선
- 사용자의 검색 경험을 향상시키기 위해 검색 인터페이스를 재구성

### 에러 코드 관리

- 시스템에서 발생할 수 있는 에러를 식별하고, 필요한 에러 모드를 정의하여 사용자에게 명확한 피드백을 제공
- 에러 핸들링 절차를 간소화하여, 개발자가 에러를 더 빠르게 식별하고 해결할 수 있도록 지원

위 각 항목은 사용자 경험을 중심으로, 안정적이고 신뢰할 수 있는 서비스 제공을 목표로 함

## ERD 설계서

![Untitled](README%20md%204002eaae140145a59993ba950ce3fe3e/Untitled%204.png)

### 연관관계

---

### Carwash **- Bay**

`1:N`관계

> 세차장은 여러 베이(예약 공간)들을 가질 수 있다.

### Carwash **- Optime**

`1:N`관계

> 세차장은 여러 운영시간(평일, 주말)을 가질 수 있다.

### Carwash **- File**

`1:N`관계

> 세차장은 여러 사진을 가질 수 있다.

### Carwash **- CarwashKeyword**

`1:N`관계

> 세차장은 여러 키워드(`세차장 키포인트`)를 가질 수 있다.

### Carwash **- Location**

`1:1`관계

> 세차장은 위치 정보를 가질 수 있다.

### Member **- Carwash**

`1:N` 관계

> 멤버 중 사장님(owner)는 여러 세차 매장을 가질 수 있다.

### Bay **- Reservation**

`1:N` 관계

> 세차장 베이(예약 공간)은 여러 예약을 가질 수 있다.

### Reservation **- Review**

`1:1` 관계

> 예약은 리뷰 한개를 가질 수 있다.

### Member **- Reservation**

`1:N` 관계

> 멤버(사용자, 사장님)은 여러 예약을 가질 수 있다.

### Member **- Review**

`1:N` 관계

> 멤버(사용자, 사장님)은 여러 예약 리뷰를 가질 수 있다.

### Review **- ReviewKeyword**

`1:N` 관계

> 작성한 리뷰은 여러 리뷰 키워드를 가질 수 있다.

### Keyword **- ReviewKeyword**

`1:N` 관계

> 키워드 하나는 여러 리뷰 키워드를 가질 수 있고, 리뷰와 키워드는 `리뷰 키워드` 테이블로 매핑된다.

### Keyword **- CarwashKeyword**

`1:N` 관계

> 키워드 하나는 여러 세차장 키워드를 가질 수 있고, 세차장과 키워드는 `세차장 키워드` 테이블로 매핑된다.

## 시퀀스 다이어그램\_사용자 관점

![Untitled](README%20md%204002eaae140145a59993ba950ce3fe3e/Untitled%205.png)
