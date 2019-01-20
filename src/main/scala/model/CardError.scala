package model

sealed trait CardError

final case class InvalidCardHolder(message: String) extends CardError

case object InvalidCardNumber extends CardError

case object InvalidCardSecurityCode extends CardError

case object InvalidExpiryDate extends CardError
