Feature: 저장된 모든 프로덕트를 조회한다.

  Background:
    Given "바지" 카테고리를 생성한다.
    Given "가방" 카테고리를 생성한다.
    Given "양말" 카테고리를 생성한다.

    Given "brandA" 브랜드를 생성한다.
    Given "brandB" 브랜드를 생성한다.
    Given "brandC" 브랜드를 생성한다.

  Scenario: 저장된 모든 프로덕트를 조회한다.
    Given "brandA" 브랜드의 "바지" 카테고리에 10000원 짜리 프로덕트를 생성한다.
    Given "brandA" 브랜드의 "바지" 카테고리에 20000원 짜리 프로덕트를 생성한다.
    Given "brandB" 브랜드의 "양말" 카테고리에 30000원 짜리 프로덕트를 생성한다.
    Given "brandB" 브랜드의 "양말" 카테고리에 40000원 짜리 프로덕트를 생성한다.
    Given "brandC" 브랜드의 "가방" 카테고리에 50000원 짜리 프로덕트를 생성한다.
    Given "brandC" 브랜드의 "바지" 카테고리에 60000원 짜리 프로덕트를 생성한다.
    When 모든 프로덕트를 조회한다.
    Then 응답은 아래 목록의 프로덕트를 모두 포함한다.
      | brandA | 바지 | 10000 |
      | brandA | 바지 | 20000 |
      | brandB | 양말 | 30000 |
      | brandB | 양말 | 40000 |
      | brandC | 가방 | 50000 |
      | brandC | 바지 | 60000 |
