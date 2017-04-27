Feature: API para consultar el balance de la cuenta en un momento determinado
  Como consumidor del API
  quiero conocer el balance de la cuenta en un momento determinado
  para poder comparar el resultado con el balance del banco

  Background:
    Given que existe una cuenta en el sistema
    And la cuenta tiene los siguientes gastos
      | paymentType | amount | date       |
      | Agua        | 30.00  | 2010-01-01 |
      | Agua        | 30.00  | 2010-02-01 |
      | Seguro      | 150.00 | 2010-06-01 |
      | Seguro      | 150.00 | 2011-06-01 |
      | Comunidad   | 50.00  | 2011-04-15 |
      | Comunidad   | 30.00  | 2011-11-15 |
      | Comunidad   | 45.00  | 2012-04-01 |
    And la cuenta tiene los siguientes ingresos
      | person  | amount | date       |
      | Antonia | 150.00 | 2010-01-01 |
      | Beli    | 50.00  | 2010-01-21 |
      | Paco    | 100.00 | 2010-01-15 |
      | Antonia | 75.00  | 2011-01-01 |

  Scenario: Balance positivo
    When consulto el balance a Enero del 2010
    Then el balance es 270.00

  Scenario: Balance negativo
    When consulto el balance a Enero del 2013
    Then el balance es -110.00