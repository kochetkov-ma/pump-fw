Feature: Debug - 3

  Scenario: Test scenario - 1
    * Page - open 'Главная страница'
    * Page - get element 'Заголовок' and save text to 'result'
    * Verify - contains string expected 'Гл' to actual '${result}'
