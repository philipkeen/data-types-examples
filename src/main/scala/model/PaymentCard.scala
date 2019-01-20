package model

import cats.Eq

final case class PaymentCard(
  number: PaymentCardNumber,
  expiryDate: ExpiryDate,
  cardSecurityCode: CardSecurityCode,
  holderMaybe: Option[CardHolder]
)

object PaymentCard {

  implicit val eq: Eq[PaymentCard] = Eq.fromUniversalEquals
}
