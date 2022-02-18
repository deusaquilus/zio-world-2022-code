package quixotic

import zio._
import io.getquill._
//import scala.reflect.Selectable.reflectiveSelectable

trait HumanLike { def id: Int; def age: Int }
case class Human(id: Int, firstName: String, lastName: String, age: Int, membership: String, segment: String) extends HumanLike
case class SuperHuman(id: Int, heroName: String, age: Int, side: String)                                      extends HumanLike

case class Customer(id: Int, name: String, age: Int, membership: String)

val ctx = new SqlMirrorContext(PostgresDialect, Literal)
import ctx._

// Scala2-Quill
object Ext0:
  // format: off
  implicit class Ops[H <: HumanLike](q: Query[H]) {
    def toCustomer = quote {
      (name: H => String, membership: H => String) =>
        (age: Int) =>
          q.filter(h => h.age > age)
            .map(h => Customer(h.id, name(h), h.age, membership(h)))
    }
  }
  // format: on
  @main
  def m0: Unit = println(run {
    query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
    ++
    query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)
  })

// Can't use regular functions, need this quoted thing since the macro has to evaluate the method
object Ext1:
  // format: off
  extension [H <: HumanLike](inline q: Query[H])
    inline def toCustomer = quote {
      (name: H => String, membership: H => String) =>
        (age: Int) =>
          q.filter(h => h.age > age)
            .map(h => Customer(h.id, name(h), h.age, membership(h)))
    }
  // format: on
  @main
  def m1: Unit = println(run {
    query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
    ++
    query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)
  })

object Ext2:
  extension [H <: HumanLike](inline q: Query[H])
    inline def toCustomer =
      (name: H => String, membership: H => String) =>
        (age: Int) =>
          q.filter(h => h.age > age)
            .map(h => Customer(h.id, name(h), h.age, membership(h)))

  @main
  def m2: Unit = println(run {
    query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
    ++
    query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)
  })

object Ext3:
  extension [H <: HumanLike](inline q: Query[H])
    inline def toCustomer(
        inline name: H => String,
        inline membership: H => String
    )(
        inline minAge: Int
    ) =
      q.filter(h => h.age > minAge)
        .map(h => Customer(h.id, name(h), h.age, membership(h)))
  @main
  def m3: Unit = println(run {
    query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
    ++
    query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)
  })

// TODO typeclass example

object Ext4:
  enum HumanType:
    case Regular(seg: String, year: Int)
    case Super(side: String, year: Int)

  inline def humanCustomer(inline tpe: HumanType) =
    inline tpe match
      case HumanType.Regular(seg, year) =>
        query[Human].filter(h => h.segment == seg && h.age > year).map(h => Customer(h.id, h.firstName + " " + h.lastName, h.age, h.membership))
      case HumanType.Super(side, year) =>
        query[SuperHuman].filter(h => h.side == side && h.age > year).map(h => Customer(h.id, h.heroName, h.age, "PLAT"))

  @main
  def m4: Unit = println(run {
    humanCustomer(HumanType.Regular("h", 1982))
    ++
    humanCustomer(HumanType.Super("g", 1856))
  })
