package model

sealed abstract case class ExpiryDate private (month: String, year: String)

object ExpiryDate {

  private val expiryDateRegex ="""^(0[1-9]|10|11|12)(\d\d)$""".r

  def apply(expiryDate: String): Either[CardError, ExpiryDate] =
    expiryDate match {
      case expiryDateRegex(month, year) => Right(new ExpiryDate(month, year) {})
      case  _ => Left(InvalidExpiryDate)
    }
}
