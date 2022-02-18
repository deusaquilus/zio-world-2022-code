package quixotic

import zio._
import io.getquill._
//import scala.reflect.Selectable.reflectiveSelectable

trait HumanLike { def id: Int; def age: Int }
case class Human(id: Int, firstName: String, lastName: String, age: Int, membership: String, segment: String) extends HumanLike
case class Customer(id: Int, name: String, age: Int, membership: String)

val ctx = new SqlMirrorContext(PostgresDialect, Literal)
import ctx._

object Ext1:
  extension [H <: HumanLike](inline q: Query[H])
    inline def toCustomer = quote {
      (name: H => String, membership: H => String) => (age: Int) =>
        q.filter(h => h.age > age)
          .map(h => Customer(h.id, name(h), h.age, membership(h)))
    }
  @main
  def eval: Unit = println(run(query[Human].toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)))


