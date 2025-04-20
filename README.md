#  코문철 Co Moon Chul

> **코드 리뷰를 위한 개발자 커뮤니티 서비스**
> <br>
> 코드 리뷰 커뮤니티
> <br>
> 실시간 코드 공유
> <br>
> AI 코드 리뷰
>
> 프로젝트 기간 : 2025.02.01 ~ 2025.04.30

<br>

## Contributors

| 리더 | 배포/아키 | 알림/SMTP | 실시간 공유 | 리뷰/배틀 | 회원 |
| :---------: | :----------: | :---------: | :---------: | :---------: | :---------: |
| ⚡ 임현우 | ⚡ 심우현 | ⚡ 안승기 | ⚡ 고영성 | ⚡ 박종일 | ⚡ 한성수 |
| [Myunwoo](https://github.com/Myunwoo) | [woohj0702](https://github.com/woohj0702) | [AnSeungGi](https://github.com/AnSeungKi) | [KoYoungSung](https://github.com/KoYoungSung) | [Allday Park](https://github.com/JongIlParks) | [sungsuhan](https://github.com/sungsuhan) |

<br>

## 링크


- 배포 후 입력(서비스)
- 배포 후 입력(스웨거)

<br>

## Development Environment and Library

<p align="left"> <img src="https://img.shields.io/badge/Java-17-dd6620"> <img src="https://img.shields.io/badge/Spring%20Boot-3.3.4-green"> <img src="https://img.shields.io/badge/Gradle-8.5-blue"> </p>

- Framework

|                라이브러리                |        사용 목적         | Version |
|:-----------------------------------:|:--------------------:|:-------:|
|    Spring Boot Starter Data JPA     |   데이터베이스 연동 (JPA)    |  3.3.4  |
|    Spring Boot Starter Security     |       인증 및 인가        |  3.3.4  |
|       Spring Boot Starter AOP       |      AOP 프로그래밍       |  3.3.4  |
|      Spring Boot Starter Mail       |        이메일 전송        |  3.3.4  |
|    Spring Boot Starter WebSocket    |        웹소켓 통신        |  3.3.4  |
|    Spring Boot Starter Thymeleaf    |     이메일 템플릿 렌더링      |  3.3.4  |
|   Spring Boot Starter Data Redis    |       Redis 연동       |  3.3.4  |
| SpringDoc OpenAPI Starter WebMVC UI | API 문서 자동화 (Swagger) |  2.0.3  |

- Library

라이브러리 | 사용 목적 | Version  
:---------:|:---------:|:--------:  
jjwt (api, impl, jackson) | JWT 토큰 발급 및 검증 | 0.11.5  
openapi-generator | OpenAPI 코드 생성기 | 7.8.0
ChatGPT Spring Boot Starter | OpenAI API 연동 (ChatGPT) | 1.0.4
Google API Client | 구글 OAuth 로그인 지원 | 2.2.0 

<br>

## Conventions

<details>
<summary>Commit Convention</summary>
<div markdown="1">

- [HOTFIX] : 🚑️  issue나, QA에서 급한 버그 수정에 사용
- [FIX] : 🔨 버그, 오류 해결
- [ADD] : ➕ Feat 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 시
- [FEAT] ✨ 새로운 기능 구현
- [DEL] : ⚰️ 쓸모없는 코드 삭제
- [DOCS] : 📝 README나 WIKI 등의 문서 개정
- [MOD] :💄 storyboard 파일,UI 수정한 경우
- [CHORE] : ✅ 코드 수정, 내부 파일 수정
- [CORRECT] : ✏️ 주로 문법의 오류나 타입의 변경, 이름 변경 등에 사용합니다.
- [MOVE] : 🚚 프로젝트 내 파일이나 코드의 이동
- [RENAME] : ⏪️  파일 이름 변경이 있을 때 사용합니다.
- [IMPROVE] : ⚡️ 향상이 있을 때 사용합니다.
- [REFACTOR] : ♻️ 전면 수정이 있을 때 사용합니다
- [MERGE] : 🔀 다른브렌치를 merge 할 때 사용합니다.
</div>
</details>

<details>
<summary>Git flow</summary>
<div markdown="1">

- Github issue에서 이슈가 발행되면 issue 별로 번호가 채번됩니다.
- 브랜치 명은 feature/{issue번호}로 생성합니다.

</div>
</details>

<details>
<summary>API</summary>
<div markdown="1">

### HTTP Method

- **GET** : 조회 단건/복수
- **POST** : 리소스 생성/갱신/삭제
- **~~PUT/DELETE~~** : 보안상 사용하지 않음

<aside>
사유

- CSRF (Cross-Site Request Forgery) 취약점
    - `PUT`과 `DELETE` 메서드는 데이터 변경을 수반하는 경우가 많기 때문에, CSRF 공격에 더 민감
- API 관리의 단순화
    - HTTP 메서드의 종류를 줄임으로써 API의 복잡도를 낮추고, 요청 처리 로직을 단순화
- RESTful 표준을 엄격히 따르지 않기로 결정
- 참여 개발자에게 익숙한 환경
</aside>

### GET API의 파라메터 전달시 **@Pathvariable 사용금지**

- URI의 일부로 전달되는 Pathvariable은 보안에 취약함
- @GetMapping("/v1/boards") 의 형태로 URI에는 파라메터를 포함하지 않고 API에 **@RequestParam 을 사용**해서 파라메터를 전달한다.
- @RequestParam 은 Default 로 required=true 이므로 필수값이 아닌 경우 required=false 로 설정해준다.

### api method 명 규칙

- 조회: select
- 수정: update
- 삭제: delete
- 생성: create

로 시작하는, camelCase 규칙을 사용합니다.<br>
조회/수정/삭제/생성으로 표현이 어려운 api의 경우, 상황에 맞는 “동사”를 api method 명의 첫 단어로 사용합니다.
</div>
</details>

<details>
<summary>
Hexagonal
</summary>
<div markdown="1">

[Hexagonal Architecture](https://alistair.cockburn.us)

## 헥사고날 아키텍처

![image](https://github.com/user-attachments/assets/8f4e2ffd-bf41-4bd4-a414-ef124de9a333)


헥사고날 아키텍처는 

- Business core를 담당하는 도메인 영역
- Port라고 불리우는 응용 영역

으로 구성되며 외부 adapter들은 port를 통해 통신하게 됩니다.

Port는 Input port와 Out port로 나누어지고,

- Input port: 도메인를 호출하는 어댑터가 통신하는 port
- Output port: 도메인에 의해 호출되는 어댑터가 통신하는 port

헥사고날 아키텍처의 핵심은 **비즈니스 로직이 있는 '도메인 계층' 이 외부 요소에 의존하지 않고 외부 요소가 '도메인 계층'에 의존하도록 하는 것이 핵심**입니다.

## 계층형 vs 헥사고날

| **영역** | **계층형** | **헥사고날** |
| --- | --- | --- |
| 표현영역 | controller | adapter.in |
| 응용영역 | service | application |
| 도메인영역 |  | domain |
| 인프라스트럭처영역 | dao | adapter.out |

응용 계층은 외부에 제공해야 할 정보를 application.port.in 패키지 영역에 interface로 만들고 application.service 패키지 영역에 구현하여 제공합니다.

DB 조회 및 등록 같이 도메인 영역에서 이용할 정보는 application.port.out 패키지 영역에 interface로 만들고 adapter.out 패키지 영역에서 구현합니다.

## 코문철에서의 활용

| **영역** | **계층형** | **헥사고날** |
| --- | --- | --- |
| 표현영역 | controller | adapter.in.web |
|  | eventListener | adapter.in.event |
| 응용영역 | service | application |
| 도메인영역 |  | domain |
| 인프라스트럭처영역 | dao | adapter.out |

코문철에서는 adapter.in의 방식으로 api controller, event listener가 존재합니다.

따라서,

- api controller에 의해 도메인 영역이 호출
- event listener에 의해 도메인 영역이 호출

위 둘은 도메인 호출 방식이 다를 뿐, 수행해야 할 비즈니스가 같으므로 @Service의 멤버 메서드는 input/output 타입을 해당 도메인의 도메인 객체로 통일합니다.

```jsx
package com.sw.cmc.application.port.in.comment;

import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.domain.comment.CommentListDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.comment
 * fileName       : CommentUseCase
 * author         : ihw
 * date           : 2025. 2. 13.
 * description    : comment usecase
 */
public interface CommentUseCase {
		...

    /**
     * methodName : createComment
     * author : IM HYUN WOO
     * description : 댓글 생성
     *
     * @param comment domain
     * @return comment domain
     * @throws Exception the exception
     */
    CommentDomain createComment(CommentDomain commentDomain) throws Exception;

    /**
     * methodName : deleteComment
     * author : IM HYUN WOO
     * description : 댓글 삭제
     *
     * @param comment domain
     * @return comment domain
     * @throws Exception the exception
     */
    CommentDomain deleteComment(CommentDomain commentDomain) throws Exception;

		...
}
```


</div>
</details>

<br>

## ERD
![cmc_erd](https://github.com/user-attachments/assets/ef4bb2dc-f9c7-4083-a794-19bc1ad9b41c)


## AWS Architecture
![cmc drawio](https://github.com/user-attachments/assets/b3f5cc1b-0d7b-4fab-adda-75828f9f8ef7)



