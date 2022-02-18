package quixotic

import quixotic.QuillContext._
import quixotic.model.Person

import io.getquill._

object Queries {

  final val largeAge = 100

  val personsOlderThan =
    quote { (age: Int) =>
      query[Person].filter(person => person.age > largeAge)
    }
}
