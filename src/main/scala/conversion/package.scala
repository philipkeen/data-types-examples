import cats.data.ValidatedNel
import cats.implicits._
import logging._
import model._

package object conversion {

  type ConversionErrorsOr[A] = ValidatedNel[ConversionError, A]

  def toPaymentCard(
    cardNumber: String,
    expiryDate: String,
    securityCode: String,
    cardHolderFirstNameMaybe: Option[String],
    cardHolderLastNameMaybe: Option[String]
  ): ConversionErrorsOr[PaymentCard] =
    (
      validateCardNumber(cardNumber),
      validateExpiryDate(expiryDate),
      validateSecurityCode(securityCode),
      validateCardHolder(cardHolderFirstNameMaybe, cardHolderLastNameMaybe)
    ).mapN { (number, expiry, csc, holderMaybe) =>
      PaymentCard(
        number = number,
        expiryDate = expiry,
        cardSecurityCode = csc,
        holderMaybe = holderMaybe
      )
    }

  def validateCardNumber(number: String): ConversionErrorsOr[PaymentCardNumber] =
    PaymentCardNumber(number) match {
      case Right(cardNumber) => cardNumber.validNel
      case Left(_) => PaymentCardNumberConversionError(s"Received invalid card number $number").invalidNel
    }

  def validateExpiryDate(expiryDate: String): ConversionErrorsOr[ExpiryDate] =
    ExpiryDate(expiryDate) match {
      case Right(expiry) => expiry.validNel
      case Left(_) => ExpiryDateConversionError(s"Received invalid expiry date $expiryDate").invalidNel
    }

  def validateSecurityCode(securityCode: String): ConversionErrorsOr[CardSecurityCode] =
    CardSecurityCode(securityCode) match {
      case Right(csc) => csc.validNel
      case Left(_) => SecurityCodeConversionError(s"Received invalid security code $securityCode").invalidNel
    }

  def validateCardHolder(
    firstNameMaybe: Option[String],
    lastNameMaybe: Option[String]
  ): ConversionErrorsOr[Option[CardHolder]] =
    CardHolder(firstNameMaybe, lastNameMaybe) match {
      case Right(holderMaybe) =>
        holderMaybe.validNel
      case Left(InvalidCardHolder(message)) =>
        CardHolderConversionError(s"Received invalid card holder details: $message").invalidNel
      case Left(error) =>
        CardHolderConversionError(s"Error during card holder conversion: $error").invalidNel
    }
}
