Feature: Book management

  Scenario: the user creates two books and retrieves both
    Given the user creates the book with title "Clean Code" and author "Robert Martin"
    And the user creates the book with title "TDD by Example" and author "Kent Beck"
    When the user gets all books
    Then the list should contain 2 books