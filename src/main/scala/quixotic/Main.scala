package quixotic

import zio._
import io.getquill._
//import scala.reflect.Selectable.reflectiveSelectable

trait HumanLike { def id: Int; def age: Int }
case class Human(id: Int, firstName: String, lastName: String, age: Int, membership: String, segment: String) extends HumanLike
case class SuperHuman(id: Int, heroName: String, age: Int, side: String)                                      extends HumanLike

trait RobotLike { def id: Int; def assemblyYear: Int }
case class Robot(id: Int, model: String, assemblyYear: Int)                       extends RobotLike
case class KillerRobot(id: Int, model: String, assemblyYear: Int, series: String) extends RobotLike

case class Customer(id: Int, name: String, age: Int, membership: String)

case class Yetti(id: Int, uniqueGruntingSound: String, age: Int)

case class Houses(zip: Int, owner: Int, origin: String, hasChargingPort: Boolean)
case class PricingYears(startYear: Int, endYear: Int, pricing: String, insaneMembership: String, voltage: Int)

val ctx = new SqlMirrorContext(PostgresDialect, Literal)
import ctx._

// object Yxt1:
//   import Membership._
//   // format: off
//   extension (inline q: Query[Yetti])
//     inline def toCustomer = quote {
//       q.map(y => Customer(y.id, y.uniqueGruntingSound, y.age, "YETT"))
//     }
//   // format: on
//   @main
//   def y12: Unit = println(run {
//     customerMembership {
//       query[Yetti].toCustomer
//     }(h => h.origin == "Canada" || h.origin == "Russia", (c, p) => c.membership)
//   })

// object Rxt1:
//   import Membership._
//   // format: off
//   extension [R <: RobotLike](inline q: Query[R])
//     inline def toCustomer = quote {
//       (name: R => String, assemblyYearMin: Int) =>
//           q.filter(r => r.assemblyYear > assemblyYearMin)
//             .map(r => Customer(r.id, name(r), infix"today()".pure.as[Int] - r.assemblyYear, "AUTO"))
//     }
//   // format: on
//   @main
//   def r1: Unit = println(run {
//     customerMembership {
//       query[Robot].toCustomer(r => r.model, 1990)
//       ++
//       query[KillerRobot].toCustomer(r => r.series + "-" + r.model, 1992)
//     }(_.hasChargingPort == true, (c, p) => if p.voltage == 120 then "US" else "EU")
//   })

// // Scala2-Quill
// object Ext0:
//   // format: off
//   implicit class Ops[H <: HumanLike](q: Query[H]) {
//     def toCustomer = quote {
//       (name: H => String, membership: H => String, age: Int) =>
//           q.filter(h => h.age > age)
//             .map(h => Customer(h.id, name(h), h.age, membership(h)))
//     }
//   }
//   // format: on
//   @main
//   def m0: Unit = println(run {
//     query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership, 1982)
//     ++
//     query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT", 1992)
//   })

// // Can't use regular functions, need this quoted thing since the macro has to evaluate the method
// object Ext1:
//   import Membership._
//   // format: off
//   extension [H <: HumanLike](inline q: Query[H])
//     inline def toCustomer = quote {
//       (name: H => String, membership: H => String, age: Int) =>
//           q.filter(h => h.age > age)
//             .map(h => Customer(h.id, name(h), h.age, membership(h)))
//     }
//   // format: on
//   @main
//   def m1: Unit = println(run {
//     query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership, 1982)
//     ++
//     query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT", 1992)
//   })
//   def m12: Unit = println(run {
//     customerMembership {
//       query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership, 1982)
//       ++
//       query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT", 1992)
//     }(_ => true, (c, p) => if p.pricing == "sane" then c.membership else p.insaneMembership)
//   })

// object Ext2:
//   extension [H <: HumanLike](inline q: Query[H])
//     inline def toCustomer =
//       (name: H => String, membership: H => String, age: Int) =>
//         q.filter(h => h.age > age)
//           .map(h => Customer(h.id, name(h), h.age, membership(h)))

//   @main
//   def m2: Unit = println(run {
//     query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership, 1982)
//     ++
//     query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT", 1992)
//   })

// object Ext3:
//   extension [H <: HumanLike](inline q: Query[H])
//     inline def toCustomer(inline name: H => String, inline membership: H => String, inline minAge: Int) =
//       q.filter(h => h.age > minAge)
//         .map(h => Customer(h.id, name(h), h.age, membership(h)))
//   @main
//   def m3: Unit = println(run {
//     query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership, 1982)
//     ++
//     query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT", 1992)
//   })

// // TODO typeclass example

// object Ext5:
//   import Membership._

//   trait HumanLikeFilter[H]:
//     extension (h: Query[H]) def toCustomer: Query[Customer]

//   inline given HumanLikeFilter[Human] with
//     extension (h: Query[Human])
//       inline def toCustomer: Query[Customer] =
//         query[Human]
//           .filter(h => h.segment == "h" && h.age > 1982)
//           .map(h => Customer(h.id, h.firstName + " " + h.lastName, h.age, h.membership))

//   inline given HumanLikeFilter[SuperHuman] with
//     extension (h: Query[SuperHuman])
//       inline def toCustomer: Query[Customer] =
//         query[SuperHuman]
//           .filter(h => h.side == "h" && h.age > 1992)
//           .map(h => Customer(h.id, h.heroName, h.age, "PLAT"))

//   @main
//   def m5: Unit = println(run {
//     customerMembership {
//       query[Human].toCustomer
//       ++
//       query[SuperHuman].toCustomer
//     }(_ => true, (c, p) => if p.pricing == "sane" then c.membership else p.insaneMembershiptt)
//   })

// end Ext5

object Ext4:
  import Membership._

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

  @main
  def m4: Unit = println(run { ////
    customerMembership {
      humanCustomer(HumanType.Regular("h", 1982))
      ++
      humanCustomer(HumanType.Super("g", 1856))
    }(_ => true, (c, p) => if p.pricing == "sane" then c.membership else p.insaneMembership)
  })

object Membership:
  inline def customerMembership(inline cs: Query[Customer])(
      inline housesFilter: Houses => Boolean,
      inline membershipFunction: (Customer, PricingYears) => String
  ) =
    for {
      customer <- cs
      h        <- query[Houses].join(h => h.owner == customer.id && housesFilter(h))
      p        <- query[PricingYears].join(p => customer.age > p.startYear && customer.age < p.endYear)
    } yield (customer.name, customer.age, customer.membership, customer.id, h.zip)
