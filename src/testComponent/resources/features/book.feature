Feature: Book management

  Scenario: the user creates two books and retrieves both
    Given the user creates the book with title "Clean Code" and author "Robert Martin"
    And the user creates the book with title "TDD by Example" and author "Kent Beck"
    When the user gets all books
    Then the list should contain 2 books

  Scenario: the user reserves a book successfully
    Given the user creates the book with title "Clean Code" and author "Robert Martin"
    When the user gets all books
    And the user reserves the first book
    Then the first book should be reserved

  Scenario: the user cannot reserve an already reserved book
    Given the user creates the book with title "Clean Code" and author "Robert Martin"
    When the user gets all books
    And the user reserves the first book
    And the user tries to reserve the first book again
    Then the reservation should fail with status 400