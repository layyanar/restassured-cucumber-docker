Feature: Validate User API

  @TestGetUser
  Scenario: Get users
    Given Verify get users

  @TestPostUser
  Scenario: Post users
    Given Verify post users

  @TestUpdateUser
  Scenario: Update users
    Given Verify update users

  @TestReadExcel
  Scenario: Excel sheet scenarios using Apache POI
    Given Get excel data rows
    Then Write a new excel of empDetail