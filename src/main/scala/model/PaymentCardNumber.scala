package model

sealed abstract case class PaymentCardNumber private (value: String)

object PaymentCardNumber {

  private val cardNumberRegex = """^\d{16}$""".r

  def apply(value: String): Either[CardError, PaymentCardNumber] =
    cardNumberRegex.findFirstIn(value) match {
      case Some(number) => Right(new PaymentCardNumber(number) {})
      case None => Left(InvalidCardNumber)
    }
}
