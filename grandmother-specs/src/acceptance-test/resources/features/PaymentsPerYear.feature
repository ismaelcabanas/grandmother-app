Feature: API para consultar los pagos realizados por año sobre la cuenta de la abuela
  Como cosumidor del API
  quiero consultar los pagos anuales realizados sobre una cuenta
  para saber los gastos de la cuenta en un año determinado

  Background:
    Given una cuenta bancaria con los pagos
      | paymentType | amount | date       |
      | Agua        | 30.00  | 2010-01-01 |
      | Agua        | 30.00  | 2010-02-01 |
      | Seguro      | 150.00 | 2010-06-01 |
      | Seguro      | 150.00 | 2011-06-01 |
      | Comunidad   | 50.00  | 2011-04-15 |
      | Comunidad   | 30.00  | 2011-11-15 |
      | Comunidad   | 45.00  | 2012-04-01 |

  Scenario: Un usuario consulta los pagos Cuenta con pagos para el año consultado
    When consulto los pagos del año 2011
    Then los pagos realizados en el año son
      | paymentType | amount | date       |
      | Seguro      | 150.00 | 2011-06-01 |
      | Comunidad   | 30.00  | 2011-11-15 |
      | Comunidad   | 50.00  | 2011-04-15 |
    And la cantidad total de los pagos del año es 230.00

  Scenario: Cuenta sin pagos para el año consultado
    When consulto los pagos del año 2001
    Then no hay pagos realizados en el año
    And la cantidad total de los pagos del año es 0