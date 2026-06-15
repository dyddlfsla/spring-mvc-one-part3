# Thymeleaf Basic Concept

## 1) Thymeleaf란?

Thymeleaf는 서버 사이드 템플릿 엔진(Server Side Template Engine)이다.

Spring MVC에서 컨트롤러가 모델 데이터를 담아 뷰 이름을 반환하면, Thymeleaf가 HTML 템플릿에 모델 데이터를 적용하여 최종 HTML을 생성한다.

```text
Controller
    ↓
Model 데이터 저장
    ↓
View 이름 반환
    ↓
Thymeleaf 템플릿 처리
    ↓
HTML 생성
    ↓
Browser 응답
```

예를 들어 다음과 같은 컨트롤러가 있다고 가정하자.

```java
@GetMapping("/items")
public String items(Model model) {
    model.addAttribute("items", itemRepository.findAll());
    return "basic/items";
}
```

컨트롤러는 데이터를 준비하고 뷰 이름만 반환한다.

```java
return "basic/items";
```

그러면 Thymeleaf가 다음 템플릿을 찾아 처리한다.

```text
src/main/resources/templates/basic/items.html
```

---

## 2) Thymeleaf의 역할

Thymeleaf는 View 기술이다.

즉, 비즈니스 로직을 처리하는 기술이 아니라 화면을 생성하는 기술이다.

MVC 구조에서 역할은 다음과 같이 구분된다.

| 계층         | 역할              |
| ---------- | --------------- |
| Controller | 요청 처리, Model 구성 |
| Service    | 비즈니스 로직         |
| Repository | 데이터 접근          |
| Thymeleaf  | 화면 렌더링          |

좋은 설계는 다음과 같다.

```java
@GetMapping("/items")
public String items(Model model) {

    List<Item> items = itemService.findItems();

    model.addAttribute("items", items);

    return "basic/items";
}
```

Thymeleaf는 전달받은 데이터를 화면에 출력하는 역할만 담당한다.

---

## 3) Thymeleaf의 특징

### Natural Template

Thymeleaf의 가장 큰 특징은 Natural Template이다.

템플릿 파일을 서버 없이 브라우저에서 직접 열어도 어느 정도 화면 확인이 가능하다.

예시:

```html
<a href="item.html"
   th:href="@{/items/{id}(id=${item.id})}"
   th:text="${item.itemName}">
    샘플 상품명
</a>
```

브라우저에서 HTML 파일만 열면:

```html
<a href="item.html">
    샘플 상품명
</a>
```

처럼 동작한다.

반면 Spring MVC + Thymeleaf를 거치면:

```html
<a href="/items/1">
    itemA
</a>
```

로 변환된다.

---

### HTML 친화적 문법

JSP는 다음과 같은 문법을 사용한다.

```jsp
<%= item.getName() %>
```

반면 Thymeleaf는 HTML 태그에 `th:*` 속성을 추가하는 구조를 가지고 있어, HTML 구조를 그대로 유지한다.

```html
<span th:text="${item.itemName}">상품명</span>
```

HTML을 크게 훼손하지 않는다.

---

## 4) 브라우저는 `th:*`를 모른다

```html
<span th:text="${item.itemName}">상품명</span>
```

브라우저는 `th:text`를 해석하지 않는다.

브라우저 입장에서는 단순히 모르는 HTML 속성일 뿐이다.

따라서 서버를 거치지 않으면 무시된다.

```html
<span th:text="${item.itemName}">상품명</span>
```

↓

브라우저가 실제로 사용하는 것

```html
<span>상품명</span>
```

즉 Thymeleaf 처리는 브라우저가 아니라 서버에서 수행된다.

```text
HTML Template
    ↓
Thymeleaf Engine
    ↓
Pure HTML
    ↓
Browser
```

---

## 5) Thymeleaf 처리 과정

### 템플릿

```html
<tr th:each="item : ${items}">
    <td th:text="${item.id}">1</td>
    <td th:text="${item.itemName}">상품명</td>
</tr>
```

### Model

```java
model.addAttribute("items", List.of(
    new Item(1L, "itemA", 10000, 10),
    new Item(2L, "itemB", 20000, 20)
));
```

### 렌더링 결과

```html
<tr>
    <td>1</td>
    <td>itemA</td>
</tr>

<tr>
    <td>2</td>
    <td>itemB</td>
</tr>
```

Thymeleaf는 모델 데이터를 템플릿에 적용하여 최종 HTML을 생성한다.

---

## 6) Spring Boot 기본 설정

Spring Boot는 Thymeleaf를 기본적으로 지원한다.

의존성만 추가하면 바로 사용할 수 있다.

### 의존성 추가

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
}
```

---

### 템플릿 위치

기본 위치:

```text
src/main/resources/templates
```

예시:

```text
templates
 ├─ index.html
 ├─ basic
 │   ├─ items.html
 │   └─ addForm.html
```

---

### 정적 리소스 위치

기본 위치:

```text
src/main/resources/static
```

예시:

```text
static
 ├─ css
 │   └─ bootstrap.min.css
 ├─ js
 │   └─ app.js
 └─ images
     └─ logo.png
```

---

## 7) Thymeleaf Namespace

HTML 문서 최상단에는 보통 다음과 같이 선언한다.

```html
<html xmlns:th="http://www.thymeleaf.org">
```

예시:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="UTF-8">
    <title>Items</title>
</head>

<body>

</body>

</html>
```

이는 `th:*` 속성이 Thymeleaf 문법이라는 것을 명시한다.

---

## 8) View Resolver와 Thymeleaf

컨트롤러:

```java
@GetMapping("/items")
public String items() {
    return "basic/items";
}
```

반환값:

```java
"basic/items"
```

Spring MVC는 View Resolver를 통해 실제 파일을 찾는다.

기본 설정:

```text
prefix = classpath:/templates/
suffix = .html
```

따라서:

```java
return "basic/items";
```

는 실제로 다음 파일을 찾는다.

```text
classpath:/templates/basic/items.html
```

---
