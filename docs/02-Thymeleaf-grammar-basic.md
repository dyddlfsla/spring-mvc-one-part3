# Thymeleaf 기본 문법

## 1. Thymeleaf 문법의 구조

Thymeleaf는 HTML 태그에 **Thymeleaf 속성(Attribute)** 을 추가하여 동작한다.

HTML 자체를 완전히 다른 문법으로 바꾸는 방식이 아니라, 기존 HTML 태그에 `th:*` 속성을 붙여서 서버에서 동적으로 처리하는 방식이다.

예를 들어:

```html
<span th:text="${item.itemName}">상품명</span>
```

위 코드는 다음과 같이 구성된다.

| 구성 요소         | 값                  | 설명                               |
| ------------- | ------------------ | -------------------------------- |
| HTML 태그       | `span`             | 화면에 텍스트를 표시하는 HTML 태그            |
| Thymeleaf 속성  | `th:text`          | 태그 내부 텍스트를 바꾸는 Thymeleaf 속성      |
| Thymeleaf 속성값 | `${item.itemName}` | 모델에서 `item.itemName` 값을 조회하는 표현식 |
| 기본 텍스트        | `상품명`              | 서버 없이 HTML을 열었을 때 보이는 샘플 값       |

Thymeleaf는 서버에서 HTML 템플릿을 읽은 뒤 `th:*` 속성을 해석하여 최종 HTML을 생성한다.

```text
HTML Template
    ↓
Thymeleaf Engine
    ↓
Pure HTML
    ↓
Browser
```

즉 브라우저가 `th:text`를 해석하는 것이 아니다.

브라우저에 도착하기 전에 서버에서 이미 다음처럼 순수 HTML로 바뀐다.

```html
<span>
    itemA
</span>
```

---

## 2. Thymeleaf 속성(Attribute)

### 2.1 Thymeleaf 속성(Attribute)

Thymeleaf 속성은 HTML 태그에 붙는 `th:*` 형태의 문법이다.

예를 들어:

```html
<span th:text="${item.itemName}">
```

여기서 `th:text`가 Thymeleaf 속성이다.

Thymeleaf 속성은 HTML 태그가 어떻게 바뀔지를 결정한다.

대표적인 Thymeleaf 속성은 다음과 같다.

각 속성의 역할은 다음과 같다.

| 속성         | 역할                 |
| ---------- | ------------------ |
| `th:text`  | 태그 내부 텍스트를 바꾼다     |
| `th:href`  | 링크 주소를 바꾼다         |
| `th:each`  | 컬렉션을 반복 출력한다       |
| `th:if`    | 조건이 참일 때만 태그를 출력한다 |
| `th:field` | 폼 필드를 객체와 바인딩한다    |

---

### 2.2 `th:text`

`th:text`는 태그 내부 텍스트를 모델 데이터로 바꾼다.

가장 기본적이고 가장 자주 사용하는 Thymeleaf 속성이다.

컨트롤러에서 다음처럼 모델에 데이터를 담았다고 하자.

```java
model.addAttribute("item", item);
```

템플릿:

```html
<span th:text="${item.itemName}">상품명</span>
```

그리고 `item.itemName` 값이 `itemA`라면 최종 HTML은 다음처럼 된다.

```html
<span>itemA</span>
```

기존에 작성해둔 `상품명`은 서버 렌더링 후 사라진다.

`상품명`은 서버 없이 HTML 파일을 직접 열었을 때 보이는 샘플 텍스트이다.

---

#### 2.2.1 `th:text`는 HTML escape 가 작동한다.

`th:text`는 기본적으로 HTML escape를 수행한다.

예를 들어 모델에 다음 값이 들어 있다고 하자.

```java
model.addAttribute("content", "<b>Hello</b>");
```

템플릿:

```html
<span th:text="${content}"></span>
```

브라우저에는 `<b>Hello</b>`가 굵은 글씨로 실행되지 않고 문자 그대로 출력된다.

브라우저 렌더링 결과:

```text
<b>Hello</b>
```

즉 내부적으로는 다음처럼 HTML escape 처리된다.

```html
&amp;lt;b&amp;gt;Hello&amp;lt;/b&amp;gt;
```

이 동작은 보안상 중요하다.

사용자 입력값을 그대로 HTML로 실행하면 XSS 공격 위험이 있기 때문이다.

---

### 2.3 `th:utext`

`th:utext`는 HTML escape 없이 값을 그대로 출력한다.

예를 들어 모델에 다음 값이 들어 있다고 하자.

```java
model.addAttribute("content", "<b>Hello</b>");
```

템플릿:

```html
<span th:utext="${content}"></span>
```

escape 없이 렌더링되면, 브라우저는 `<b>` 태그를 실제 HTML 태그로 해석한다.

브라우저 렌더링 결과:

**Hello**

즉 브라우저에서 HTML 코드가 실행되고 Hello가 굵게 표시된다.

하지만 실무에서는 매우 조심해야 한다.

사용자가 입력한 게시글 본문, 댓글, 닉네임 등에 `th:utext`를 무심코 사용하면 악성 스크립트가 실행될 수 있다.

기본 원칙은 다음과 같다.

```text
- 일반 출력 = th:text
- HTML을 의도적으로 허용해야 하는 경우 = th:utext

🚨 사용자 입력값에는 th:utext를 조심해서 사용해야 한다.
```

---

### 2.4 `th:href`

`th:href`는 HTML 태그의 `href` 속성을 동적으로 만든다.

```html
<a th:href="@{/items}">상품 목록</a>
```

렌더링 결과:

```html
<a href="/items">상품 목록</a>
```

여기서 `th:href`는 Thymeleaf 속성이고, `@{/items}`는 URL 표현식이다.

```text
th:href = href 속성을 바꾼다.
@{/items} = /items URL을 생성한다.
```

---

### 2.5 `th:src`

`th:src`는 이미지, 스크립트 등의 `src` 속성을 동적으로 만든다.

```html
<img th:src="@{/images/logo.png}">
```

렌더링 결과:

```html
<img src="/images/logo.png">
```

정적 리소스가 다음 위치에 있을 때:

```text
src/main/resources/static/images/logo.png
```

브라우저에서는 다음 URL로 접근한다.

```text
/images/logo.png
```

---

### 2.6 `th:action`

`th:action`은 form의 전송 URL을 만든다.

```html
<form th:action="@{/items/add}" method="post">
```

렌더링 결과:

```html
<form action="/items/add" method="post">
```

폼 전송은 링크 이동과 다르다.

`a` 태그는 보통 GET 요청을 만들고, `form`은 `method`에 따라 GET 또는 POST 요청을 만든다.

---

### 2.7 `th:each`

`th:each`는 컬렉션을 반복 출력한다.

```html
<tr th:each="item : ${items}">
    <td th:text="${item.id}">상품아이디</td>
    <td th:text="${item.itemName}">상품명</td>
    <td th:text="${item.price}">상품가격</td>
</tr>
```

이 문법은 자바의 enhanced for문과 비슷하게 이해하면 된다.

```java
for (Item item : items) {
  // 반복
}
```

즉:

```html
item : ${items}
```

는 다음 의미다.

```text
${items} 컬렉션에서 요소를 하나씩 꺼내 item이라는 이름으로 사용한다.
```

---

#### 2.7.1 어떤 컬렉션을 반복할 수 있는가?

전달된 `items`는 `List` 인터페이스를 충족시키면 된다.

구현체가 반드시 `ArrayList`일 필요는 없다.

또한, 더 넓게는 반복 가능한 객체라면 사용할 수 있다.

```text
List
Set
배열
Iterable
Map
```

Map은 일반 컬렉션과 다르게 entry 단위로 순회한다.

---

#### 2.7.2 반복 상태 변수

반복문에서는 상태 변수를 사용할 수 있다.

```html
<tr th:each="item, stat : ${items}">
    <td th:text="${stat.count}">1</td>
    <td th:text="${item.itemName}">상품명</td>
</tr>
```

`stat`은 반복 상태 객체다.

| 속성           | 의미           |
| ------------ | ------------ |
| `stat.index` | 0부터 시작하는 인덱스 |
| `stat.count` | 1부터 시작하는 순번  |
| `stat.size`  | 전체 요소 개수     |
| `stat.first` | 첫 번째 요소 여부   |
| `stat.last`  | 마지막 요소 여부    |
| `stat.even`  | 짝수 여부        |
| `stat.odd`   | 홀수 여부        |

예를 들어 화면에 1번부터 번호를 출력하고 싶으면 `stat.count`를 사용한다.

```html
<td th:text="${stat.count}">1</td>
```

배열 인덱스처럼 0부터 시작하는 값이 필요하면 `stat.index`를 사용한다.

```html
<td th:text="${stat.index}">0</td>
```

---

### 2.8 `th:if`

`th:if`는 조건이 참일 때만 해당 태그를 출력한다.

```html
<span th:if="${item.quantity > 0}">재고 있음</span>
```

`item.quantity > 0`이 참이면 태그가 출력된다.

거짓이면 태그 자체가 최종 HTML에 포함되지 않는다.

---

### 2.9 `th:unless`

`th:unless`는 `th:if`의 반대다.

조건이 거짓일 때만 해당 태그를 출력한다.

```html
<span th:unless="${item.quantity > 0}">품절</span>
```

즉 위 코드는 재고가 0 이하일 때만 출력된다.

---

### 2.10 조건에 따른 텍스트 변경

단순히 출력 여부가 아니라 텍스트만 바꾸고 싶다면 삼항 연산자를 사용할 수 있다.

```html
<span th:text="${item.quantity > 0 ? '재고 있음' : '품절'}"></span>
```

이 경우 태그는 항상 출력되고, 내부 텍스트만 조건에 따라 달라진다.

---

### 2.11 `th:value`

`th:value`는 input의 value 값을 설정한다.

```html
<input type="text" th:value="${item.itemName}">
```

렌더링 결과:

```html
<input type="text" value="itemA">
```

단순 조회 화면에서는 `th:value`를 사용할 수 있다.

폼 바인딩에서는 보통 `th:field`를 더 많이 사용한다.

---

### 2.12 `th:class`

`th:class`는 class 속성을 동적으로 변경한다.

```html
<div th:class="${active} ? 'active' : 'inactive'"></div>
```

`active`가 참이면:

```html
<div class="active"></div>
```

거짓이면:

```html
<div class="inactive"></div>
```

---

### 2.13 `th:classappend`

`th:classappend`는 기존 class에 값을 추가한다.

```html
<div class="item" th:classappend="${selected} ? ' selected'"></div>
```

`selected`가 참이면:

```html
<div class="item selected"></div>
```

기존 class를 유지하면서 조건부 class만 추가하고 싶을 때 유용하다.

---

### 2.14 `th:onclick`

`th:onclick`은 onclick 속성을 동적으로 만든다.

```html
<button type="button" th:onclick="|location.href='@{/items/add}'|">
    등록
</button>
```

렌더링 결과:

```html
<button type="button" onclick="location.href='/items/add'">
    등록
</button>
```

---

### 2.15 `th:object`

`th:object`는 현재 태그 내부에서 사용할 **선택 객체**를 지정한다.

```html
<form th:object="${item}">
```

이후 form 내부에서는 `item`을 기준으로 필드를 작성할 수 있다.

---

### 2.16 `th:field`

`th:field`는 폼 필드를 객체의 필드와 바인딩한다.

```html
<input th:field="*{itemName}">
```

렌더링 시 다음 속성들이 자동으로 생성된다.

```html
id="itemName"
name="itemName"
value="..."
```

예를 들어 `item.itemName = itemA`라면:

```html
<input
    id="itemName"
    name="itemName"
    value="itemA">
```

폼에서는 `th:value`보다 `th:field`를 사용하는 경우가 많다.

왜냐하면 `th:field`가 `id`, `name`, `value`를 함께 처리해주기 때문이다.

---

### 2.17 Form 전체 예시

```html
<form th:action="@{/items/add}" 
      th:object="${item}"
      method="post">

    <div>
        <label for="itemName">상품명</label>
        <input type="text" th:field="*{itemName}">
    </div>

    <div>
        <label for="price">가격</label>
        <input type="text" th:field="*{price}">
    </div>

    <div>
        <label for="quantity">수량</label>
        <input type="text" th:field="*{quantity}">
    </div>

    <button type="submit">등록</button>
</form>
```

---

## 3. Thymeleaf 표현식(Expression)

### 3.1 Thymeleaf 표현식(Expression)

Thymeleaf 표현식은 Thymeleaf 속성값 안에 들어가는 문법이다.

예를 들어:

```html
<span th:text="${item.itemName}">
```

여기서 `${item.itemName}`이 표현식이다.

표현식은 데이터를 조회하거나 URL을 만들거나 문자열을 조립하는 역할을 한다.

대표적인 표현식은 다음과 같다.

| 표현식 | 이름 | 역할 |
|---------|---------|---------|
| `${...}` | 변수 표현식 | Model 데이터 조회 |
| `*{...}` | 선택 변수 표현식 | 선택 객체 기준 데이터 조회 |
| `@{...}` | URL 표현식 | URL 생성 |
| `#{...}` | 메시지 표현식 | 메시지(국제화) 조회 |
| `~{...}` | Fragment 표현식 | Fragment 참조 |
| `\|...\|` | 리터럴 치환 | 문자열 안에 표현식 삽입 |

정리하면 다음과 같다.

```html
<a th:href="@{/items/{id}(id=${item.id})}">
```

| 구분            | 값                               |
| ------------- | ------------------------------- |
| Thymeleaf 속성  | `th:href`                       |
| Thymeleaf 표현식 | `@{/items/{id}(id=${item.id})}` |
| 표현식 내부 변수     | `${item.id}`                    |

---

### 3.2 `${...}`

`${...}`는 모델(Model)에 저장된 데이터를 조회하는 표현식이다.

가장 많이 사용하는 Thymeleaf 표현식이며, 컨트롤러에서 전달한 데이터를 화면에 출력할 때 사용한다.

컨트롤러:

```java
model.addAttribute("item", item);
model.addAttribute("message", "Hello");
```

템플릿:

```html
<span th:text="${item.itemName}"></span>
<span th:text="${message}"></span>
```

`item.itemName = itemA`이고 `message = Hello`라면 결과는 다음과 같다.

```html
<span>itemA</span>
<span>Hello</span>
```

`${...}`는 Model에 저장된 객체, 컬렉션, 문자열, 숫자 등을 조회할 수 있다.

```html
${item}
${item.itemName}
${item.price}
${items}
${message}
```

---

#### 3.2.1 `${...}`와 getter 호출

```html
${item.itemName}
```

여기서 `${item.itemName}`은 모델에 있는 `item` 객체의 `itemName` 프로퍼티를 조회한다.

즉 Thymeleaf에서:

```html
${item.itemName}
```

는 다음의 자바 코드를 호출하게 된다.

```java
item.getItemName();
```

따라서 getter가 존재해야 프로퍼티를 정상적으로 조회할 수 있다.

---

### 3.3 `*{...}`

`*{...}`는 선택 객체 기준 표현식이다.

```html
<form th:object="${item}">
    <input th:field="*{itemName}">
</form>
```

여기서 `*{itemName}`은 현재 선택 객체인 `item`의 `itemName`을 의미한다.

즉 다음과 비슷하다.

```html
${item.itemName}
```

---

### 3.4 `@{...}`

`@{...}`는 URL을 생성할 때 사용하는 표현식이다.

단순 URL부터 변수가 포함된 URL까지 여러 형태의 URL 을 표현할 수 있다.

---

#### 3.4.1 경로 변수

상세 페이지 링크처럼 URL에 id가 들어가야 하는 경우가 많다.

예를 들어 원하는 URL이 다음과 같다고 하자.

```text
/items/1
```

Thymeleaf에서는 다음처럼 작성한다.

```html
<a th:href="@{/items/{id}(id=${item.id})}">상세</a>
```

구조는 다음과 같다.

```text
URL 패턴 : /items/{id}
변수 매핑 : id = ${item.id}
```

`item.id` 값이 `1`이면 결과는 다음과 같다.

```text
/items/1
```

중요한 점은 `{id}`와 `(id=...)`의 이름이 같아야 한다는 것이다.

✅ 올바른 예:

```html
th:href="@{/items/{id}(id=${item.id})}"
```

❗잘못된 예:

```html
th:href="@{/items/{id}(itemId=${item.id})}"
```

위처럼 이름이 다르면 `{id}`를 채우지 못하고, 남은 값이 쿼리 파라미터로 붙을 수 있다.

---

#### 3.4.2 쿼리 파라미터

쿼리 파라미터가 필요한 경우는 다음처럼 작성한다.

```html
<a th:href="@{/items(page=${page}, size=${size})}">상품 목록</a>
```

`page = 1`, `size = 10`이면 결과는 다음과 같다.

```text
/items?page=1&size=10
```

---

#### 3.4.3 경로 변수와 쿼리 파라미터를 함께 사용

```html
<a th:href="@{/items/{id}(id=${item.id}, page=${page}, size=${size})}">
    상세
</a>
```

결과:

```text
/items/1?page=1&size=10
```

여기서 `id`는 경로 변수로 사용되고, `page`, `size`는 쿼리 파라미터로 사용된다.

---

### 3.5 `#{...}`

`#{...}`는 메시지를 조회하는 표현식이다.

주로 다음과 같은 문자열을 한 곳에서 관리하기 위해 사용한다.

- 화면 문구
- 버튼 이름
- 메뉴 이름
- 국제화(i18n)

`messages.properties`

```properties
label.item=상품
label.itemName=상품명
label.price=가격

button.save=저장
button.cancel=취소
```

템플릿:

```html
<label th:text="#{label.itemName}"></label>
<button th:text="#{button.save}"></button>
<button th:text="#{button.cancel}"></button>
```

렌더링 결과:

```html
<label>상품명</label>
<button>저장</button>
<button>취소</button>
```

이렇게 하면 화면 곳곳에 문자열을 직접 작성하지 않고 한 곳에서 관리할 수 있다.

---

### 3.6 `~{...}`

`~{...}`는 Fragment를 참조하는 표현식이다.

Fragment 문법은 마지막 부분에서 따로 다룬다.

---

### 3.7 `|...|` 문자열 치환 표현식

`|...|`는 문자열과 표현식을 조합하는 경우에 자주 사용한다.

```html
<span th:text="|상품명 : ${item.itemName}|"></span>
```

은

```text
상품명 : itemA
```

으로 표현된다.

---

#### 3.7.1 왜 문자열 치환을 사용할까?

표현식에 문자열을 추가하는 경우, 다음과 같이 작성하면 에러가 발생한다.

```html
<span th:text="상품명 : ${item.itemName}"></span>
```

문자열과 변수 표현식이 섞여 있기 때문이다.

이때는 `|...|`를 사용해야 한다.

```html
<span th:text="|상품명 : ${item.itemName}|"></span>
```

---

#### 3.7.2 JavaScript 속성에서 사용

```html
<button th:onclick="|location.href='@{/items/add}'|">등록</button>
```

결과:

```html
<button onclick="location.href='/items/add'">등록</button>
```

---

#### 3.7.3 문자열 치환(`|...|`)으로도 URL을 표현할 수 있다.

다음처럼 작성해도 동작한다.

```html
<a th:href="|/items/${item.id}|">상세</a>
```

결과:

```text
/items/1
```

⚠️하지만 URL을 생성할 때에는 보통 `@{...}`를 사용하는 편이 좋다.

```html
<a th:href="@{/items/{id}(id=${item.id})}">상세</a>
```

그 이유는 다음과 같다.

- URL 생성 의도가 명확하다.
- 경로 변수와 쿼리 파라미터를 구분할 수 있다.
- 컨텍스트 패스를 자동으로 반영할 수 있다.
- 파라미터가 많아져도 구조가 명확하다.

---

## 4. Fragment 문법

### 4.1 Fragment란?

Fragment는 공통 HTML 조각이다.

예를 들어 모든 페이지에 공통으로 들어가는 header, footer, navigation을 따로 분리할 수 있다.

---

### 4.2 Fragment 정의

먼저 페이지에 공통적으로 들어가는 헤더 부분을 HTML 문서로 분리시키자.

```html
<header th:fragment="header">
    <h1>My Shop</h1>
    <nav>
        <a th:href="@{/items}">상품 목록</a>
        <a th:href="@{/items/add}">상품 등록</a>
    </nav>
</header>
```

HTML 문서는 `templates/fragments/header.html` 경로에 위치시킨다.

---

### 4.3 Fragment 삽입하기

이제 만들어진 HTML 조각을 다른 HTML 문서에 넣을 수 있다.

#### 4.3.1 `th:replace` 을 통한 fragment 삽입

`th:replace`는 현재 태그를 지정된 fragment로 교체한다.

```html
<html>
  <div th:replace="~{fragments/header :: header}"></div>
</html>
```

위 코드는 다음과 같은 의미를 가진다.

```text
templates/fragments/header.html 파일에서

th:fragment="header" 로 정의된 Fragment를 가져와 교체시킨다.
```

결과:

```html
<html>
  <header>
      <h1>My Shop</h1>
      <nav>
      ...
      </nav>
  </header>
</html>
```

결과적으로 `div`는 사라지고 `th:fragment="header"`로 정의된 HTML 조각이 삽입된다.

---

#### 4.3.2 `th:insert` 을 통한 fragment 삽입

`th:insert`는 현재 태그의 하위 요소로 지정된 fragment를 삽입한다.

```html
<html>
  <div th:insert="~{fragments/header :: header}"></div>
</html>
```

결과:

```html
<html>
  <div>
      <header>
          <h1>My Shop</h1>
          <nav>
           ...
          </nav>
      </header>
  </div>
</html>
```

`th:replace`와 달리 `th:insert`는 `<div>` 태그가 상위 태그로 남아있다.

실무에서는 보통 `th:replace`를 더 자주 사용한다.

---
