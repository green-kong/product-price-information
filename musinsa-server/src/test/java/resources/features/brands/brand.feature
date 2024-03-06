Feature: 모든 브랜드를 조회한다.

  Background:
    Given "brandA" 브랜드를 생성한다.
    Given "brandB" 브랜드를 생성한다.
    Given "brandC" 브랜드를 생성한다.
    Given "brandD" 브랜드를 생성한다.

  Scenario: 저장된 모든 브랜드를 조회한다.
    When 모든 브랜드를 조회한다.
    Then 응답은 다음의 정보를 포함하고 있다.
      | brandA |
      | brandB |
      | brandC |
      | brandD |
