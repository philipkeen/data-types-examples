package model

sealed abstract case class CardHolder private (firstName: String, lastName: String)

object CardHolder {

  def apply(
    firstNameMaybe: Option[String],
    lastNameMaybe: Option[String]
  ): Either[CardError, Option[CardHolder]] =
    (firstNameMaybe, lastNameMaybe) match {
      case (Some(firstName), Some(lastName)) if firstName.nonEmpty && lastName.nonEmpty =>
        Right(Some(new CardHolder(firstName, lastName) {}))
      case (Some(firstName), Some(lastName)) =>
        Left(InvalidCardHolder(s"Invalid card holder name, given first name '$firstName' and last name '$lastName'"))
      case (Some(firstName), None) =>
        Left(InvalidCardHolder(s"Invalid card holder name, given first name '$firstName' but no last name"))
      case (None, Some(lastName)) =>
        Left(InvalidCardHolder(s"Invalid card holder name, given last name '$lastName' but no first name"))
      case (None, None) =>
        Right(None)
    }

}
