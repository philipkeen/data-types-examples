package conversion

import model._
import org.scalatest.FunSuite

class ConversionTest extends FunSuite {

  test("conversion should return valid card") {
    val actual =
      toPaymentCard(
        cardNumber = "1234567890123456",
        expiryDate = "0123",
        securityCode = "123",
        cardHolderFirstNameMaybe = Some("Joe"),
        cardHolderLastNameMaybe = Some("Bloggs")
      )

    assert(actual.isValid)
    assert(actual.toEither.right.get.number.value == "1234567890123456")
    assert(actual.toEither.right.get.expiryDate.month == "01")
    assert(actual.toEither.right.get.expiryDate.year == "23")
    assert(actual.toEither.right.get.cardSecurityCode.value == "123")
    assert(actual.toEither.right.get.holderMaybe.nonEmpty)
    assert(actual.toEither.right.get.holderMaybe.get.firstName == "Joe")
    assert(actual.toEither.right.get.holderMaybe.get.lastName == "Bloggs")
  }

  test("conversion should return valid card with no customer name") {
    val actual =
      toPaymentCard(
        cardNumber = "1234567890123456",
        expiryDate = "0123",
        securityCode = "123",
        cardHolderFirstNameMaybe = None,
        cardHolderLastNameMaybe = None
      )

    assert(actual.isValid)
    assert(actual.toEither.right.get.number.value == "1234567890123456")
    assert(actual.toEither.right.get.expiryDate.month == "01")
    assert(actual.toEither.right.get.expiryDate.year == "23")
    assert(actual.toEither.right.get.cardSecurityCode.value == "123")
    assert(actual.toEither.right.get.holderMaybe.isEmpty)
  }

  test("conversion should report bad card number") {
    val actual =
      toPaymentCard(
        cardNumber = "123456789012345",
        expiryDate = "0123",
        securityCode = "123",
        cardHolderFirstNameMaybe = Some("Joe"),
        cardHolderLastNameMaybe = Some("Bloggs")
      )

    assert(actual.isInvalid)
    assert(actual.toEither.left.get.size == 1)
    assert(actual.toEither.left.get.head.message == "Received invalid card number 123456789012345")
  }

  test("conversion should report bad expiry date") {
    val actual =
      toPaymentCard(
        cardNumber = "1234567890123456",
        expiryDate = "3210",
        securityCode = "123",
        cardHolderFirstNameMaybe = Some("Joe"),
        cardHolderLastNameMaybe = Some("Bloggs")
      )

    assert(actual.isInvalid)
    assert(actual.toEither.left.get.size == 1)
    assert(actual.toEither.left.get.head.message == "Received invalid expiry date 3210")
  }

  test("conversion should report bad security code") {
    val actual =
      toPaymentCard(
        cardNumber = "1234567890123456",
        expiryDate = "0123",
        securityCode = "1234",
        cardHolderFirstNameMaybe = Some("Joe"),
        cardHolderLastNameMaybe = Some("Bloggs")
      )

    assert(actual.isInvalid)
    assert(actual.toEither.left.get.size == 1)
    assert(actual.toEither.left.get.head.message == "Received invalid security code 1234")
  }

  test("conversion should report missing surname") {
    val actual =
      toPaymentCard(
        cardNumber = "1234567890123456",
        expiryDate = "0123",
        securityCode = "123",
        cardHolderFirstNameMaybe = Some("Joe"),
        cardHolderLastNameMaybe = None
      )

    assert(actual.isInvalid)
    assert(actual.toEither.left.get.size == 1)
    assert(actual.toEither.left.get.head.message ==
      "Received invalid card holder details: Invalid card holder name, given first name 'Joe' but no last name")
  }

  test("conversion should report missing first name") {
    val actual =
      toPaymentCard(
        cardNumber = "1234567890123456",
        expiryDate = "0123",
        securityCode = "123",
        cardHolderFirstNameMaybe = None,
        cardHolderLastNameMaybe = Some("Bloggs")
      )

    assert(actual.isInvalid)
    assert(actual.toEither.left.get.size == 1)
    assert(actual.toEither.left.get.head.message ==
      "Received invalid card holder details: Invalid card holder name, given last name 'Bloggs' but no first name")
  }
}
