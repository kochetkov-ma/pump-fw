Feature: Debug - 1

  @NoSkip
  Scenario: Test scenario - 1
    * Page - open 'Главная страница'
    * Verify - equals object expected '1' and actual '2'
    * Verify - equals object expected '1' and actual '2'

  @NoSkip
  Scenario: Test scenario - 2
    * Verify - equals string expected '1' and actual '1'
    * Page - get element 'Заголовок' and save text to 'result'
    * Verify - contains string expected 'Гл' to actual '${result}'

  @NoSkip
  Scenario: Test scenario - 3
    * Verify - equals long expected '2' and actual '1'

  @NoSkip
  Scenario: Test scenario - 4
    * Verify - equals object expected '1,2,3' and actual '1,2,3'

  @NoSkip
  Scenario: Test scenario - 5
    * Verify - equals list expected '1,2,3' and actual '1,2,3'