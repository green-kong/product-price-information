Feature: 프로덕트의 가격 정보를 수정한다.

  Background: 프로덕트를 생성한다.
    Given "바지" 카테고리를 생성한다.
    Given "brandA" 브랜드를 생성한다.
    When "brandA" 브랜드의 "바지" 카테고리에 20000원 짜리 프로덕트를 생성한다.

  Scenario: 프로덕트를 삭제한다.
    When "product1"을 삭제한다.
    Then 204을 응답한다.

  Scenario: 존재하지 않는 프로덕트를 삭제할 수 없다.
    When "존재하지 않는 프로덕트"을 삭제한다.
    Then 400을 응답한다.
