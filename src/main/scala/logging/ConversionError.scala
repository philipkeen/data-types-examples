package logging

import cats.Eq

trait ConversionError {

  def message: String

  def level: LogLevel
}

final case class PaymentCardNumberConversionError(override val message: String) extends ConversionError {

  override val level: LogLevel = LogLevel.Warning
}

final case class SecurityCodeConversionError(override val message: String) extends ConversionError {

  override val level: LogLevel = LogLevel.Warning
}

final case class ExpiryDateConversionError(override val message: String) extends ConversionError {

  override val level: LogLevel = LogLevel.Warning
}

final case class CardHolderConversionError(override val message: String) extends ConversionError {

  override val level: LogLevel = LogLevel.Warning
}

object ConversionError {

  implicit val eq: Eq[ConversionError] = Eq.fromUniversalEquals
}
