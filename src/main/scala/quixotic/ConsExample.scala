package quixotic

import io.getquill._

// val ctx = new SqlMirrorContext(PostgresDialect, Literal)
// import ctx._

// Causes same error as seen before
// object ConsExample:
//   case class Person(name: String, age: Int)

//   enum MyCons:
//     case NameFilter(value: String, next: MyCons)
//     case Done

//   inline def filterN(inline p: Person, inline cons: MyCons): Boolean =
//     inline cons match
//       case MyCons.NameFilter(value, next) =>
//         p.name == "Joe" || filterN(p, next)
//       case MyCons.Done => true

//   println(
//     run(query[Person].filter(p => filterN(p, MyCons.NameFilter("Joe", MyCons.NameFilter("Jack", MyCons.Done)))))
//   )

// Doesn't work either
// sealed trait MyCons
// object MyCons:
//   case class NameFilter[C <: MyCons](value: String, next: C) extends MyCons
//   case object Done                                           extends MyCons

// object ConsExample:
//   case class Person(name: String, age: Int)

//   inline def filterN(inline p: Person, inline cons: MyCons): Boolean =
//     inline cons match
//       case MyCons.NameFilter(value, next) =>
//         p.name == "Joe" || filterN(p, next)
//       case MyCons.Done => true

//   println(
//     run(query[Person].filter(p => filterN(p, MyCons.NameFilter("Joe", MyCons.NameFilter("Jack", MyCons.Done)))))
//   )
