package model

sealed abstract case class CardSecurityCode private (value: String)

object CardSecurityCode {

  private val cscRegex = """^\d{3}$""".r

  def apply(value: String): Either[CardError, CardSecurityCode] =
    cscRegex.findFirstIn(value) match {
      case Some(csc) => Right(new CardSecurityCode(csc) {})
      case None => Left(InvalidCardSecurityCode)
    }
}
