package quixotic

import quixotic.QuillContext._
import quixotic.model.Person

import io.getquill._

object Queries:

  enum HumanType:
    case Regular(seg: String, year: Int)
    case Super(side: String, year: Int)

  inline def humanCustomer(inline tpe: HumanType) =
    inline tpe match
      case HumanType.Regular(seg, year) =>
        query[Human]
          .filter(h => h.segment == seg && h.age > year)
          .map(h => Customer(h.id, h.firstName + " " + h.lastName, h.age, h.membership))
      case HumanType.Super(side, year) =>
        query[SuperHuman]
          .filter(h => h.side == side && h.age > year)
          .map(h => Customer(h.id, h.heroName, h.age, "PLAT"))

  inline def customerMembership(inline cs: Query[Customer])(
      inline housesFilter: Houses => Boolean,
      inline membershipFunction: (Customer, PricingYears) => String
  ) =
    for {
      customer <- cs
      h        <- query[Houses].join(h => h.owner == customer.id && housesFilter(h))
      p        <- query[PricingYears].join(p => customer.age > p.startYear && customer.age < p.endYear)
    } yield Record(customer.name, customer.age, customer.membership, customer.id, h.zip)

end Queries
