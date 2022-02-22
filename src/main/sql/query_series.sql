



--Initial
SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership FROM Humans h

--Bit more complex
SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership FROM Humans h
WHERE h.memberOf = 'h'

--Bit more complex
SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership FROM Humans h
WHERE h.memberOf = 'h'
UNION
SELECT h.heroName AS name, h.age, 'PLAT' FROM SuperHumans h
WHERE h.side = 'g'

--Bit more complex
SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership FROM Humans h
WHERE h.segment = 'h' AND h.age > 44
UNION
SELECT h.heroName AS name, h.age, 'PLAT' FROM SuperHumans h
WHERE h.side = 'g' AND h.age > 123


-- New Biz Unit
SELECT r.model AS name, today() - r.assemblyYear, 'AUTO' FROM Robots r
WHERE r.assemblyYear > 1990
UNION
SELECT r.series || "-" || r.model AS name, today() - r.assemblyYear, 'AUTO' FROM KillerRobots r
WHERE r.series  = 'T' AND r.assemblyYear > 1992

/*


*/

-- Polymorhpism?

(SELECT Humans h WHERE h.segment = 'h').toCustomer(h.firstName || ' ' || h.lastName, h.membership, 1982)
UNION
(SELECT SuperHumans h WHERE r.side  = 'g').toCustomer(h.heroName, 'PLAT', 1982)


(SELECT Robots r).toCustomer(r.label, 1990)
UNION
(SELECT KillerRobots r WHERE r.series  = 'T').toCustomer(r.series || "-" || r.model, 1992)

/*
case class Human(firstName: String, lastName: String, age: Int, membership: String, segment: String, age: Int)
case class Customer(name: String, age: Int, membership: String)

trait HumanLike { def age: Int }

==== YOU COULD DO THAT ====
query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
union
query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)

==== IF YOU JUST HAVE THIS ====
implicit class Ops(q: Query[HumanLike]) {
  def toCustomer = quote {
    (name: HumanLike => String, membership: HumanLike => String) =>
    (minAge: Int) =>
      q.filter(h => h.age > minAge)
       .map(h => Customer(name(h), h.age, membership(h)))
  }
}

extension (inline q: Query[HumanLike])
  def toCustomer = quote {
    (name: HumanLike => String, membership: HumanLike => String) =>
    (minAge: Int) =>
      q.filter(h => h.age > minAge)
       .map(h => Customer(h.id, name(h), h.age, membership(h)))
  }

extension (inline q: Query[HumanLike])
  inline def toCustomer =
    (name: HumanLike => String, membership: HumanLike => String) =>
    (minAge: Int) =>
      q.filter(h => h.age > minAge)
       .map(h => Customer(h.id, name(h), h.age, membership(h)))

-- show how it progresses to this:

extension (inline q: Query[HumanLike])
  inline def toCustomer(
    inline name: HumanLike => String, inline membership: HumanLike => String)(
    inline minAge: Int
  ) =
    q.filter(h => h.age > minAge)
     .map(h => Customer(h.id, name(h), h.age, membership(h)))

-- by the way, this way you can overload it i.e. toCustomer can be overloaded

query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
union
query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)

// ================

enum HumanType:
  case Regular(seg: String, year: Int)
  case Super(side: String, year: Int)

inline def humanCustomer(inline tpe: HumanType) =
  inline tpe match
    case HumanType.Regular(seg, year) =>
      query[Human].filter(h => h.segment == seg && h.age > year).map(h => Customer(h.id, h.firstName + " " + h.lastName, h.age, h.membership))
    case HumanType.Super(side, year) =>
      query[Human].filter(_.side == side && h.age > year).map(h => Customer(h.id, h.heroName, h.age, "PLAT"))

humanCustomer(HumanType.Regular("h", 1982))
union
humanCustomer(HumanType.Super("g", 1856))

*/



--Bit more complex
SELECT customer.name, customer.age, customer.membership, customer.id, h.zip FROM (
  SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership, h.id FROM Humans h
  WHERE h.segment = 'h' AND h.age > 1982
  UNION
  SELECT h.heroName AS name, h.age, 'PLAT', h.id FROM SuperHumans h
  WHERE h.side = 'g' AND h.age > 1856
) AS customer
JOIN Houses h ON h.OWNER = customer.ID

--Bit more complex (robot)
SELECT customer.name, customer.age, customer.membership, customer.id, h.zip FROM (
  SELECT r.model AS name, today() - r.assemblyYear, 'AUTO' FROM Robots r
  WHERE r.assemblyYear > 1990
  UNION
  SELECT r.series || "-" || r.model AS name, today() - r.assemblyYear, 'AUTO' FROM KillerRobots r
  WHERE r.series  = 'T' AND r.assemblyYear > 1992
) AS customer
JOIN Houses h ON h.OWNER = customer.ID AND h.hasChargingPort=true



--Bit more complex
SELECT customer.name, customer.age, customer.membership, customer.id, h.zip FROM (
  SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership, h.id FROM Humans h
  WHERE h.segment = 'h' AND h.age > 1982
  UNION
  SELECT h.heroName AS name, h.age, 'PLAT', h.id FROM SuperHumans h
  WHERE h.side = 'g' AND h.age > 1856
) AS customer
JOIN Houses h ON h.OWNER = customer.ID AND h.hasChargingPort=true
JOIN PricingYears p ON customer.age > p.startYear && customer.age < p.endYear

--Bit more complex (robot)
SELECT customer.name, customer.age, customer.membership, customer.id, h.zip FROM (
  SELECT r.model AS name, today() - r.assemblyYear, 'AUTO' FROM Robots r
  WHERE r.assemblyYear > 1990
  UNION
  SELECT r.series || "-" || r.model AS name, today() - r.assemblyYear, 'AUTO' FROM KillerRobots r
  WHERE r.series  = 'T' AND r.assemblyYear > 1992
) AS customer
JOIN Pods h ON h.OWNER = customer.ID AND h.hasChargingPort=true
JOIN PodLifetimes l ON l.POD_FK = h.ID AND l.supportedModelsYear >= r.assemblyYear


--Bit more complex
SELECT customer.name, customer.age,
  CASE WHEN p.pricing == 'sane' THEN p. customer.membership ELSE p.insaneMembership END as membership,
  customer.id, h.zip FROM
(
  SELECT h.firstName || ' ' || h.lastName AS name, h.age, h.membership, h.id FROM Humans h
  WHERE h.segment = 'h' AND h.age > 1982
  UNION
  SELECT h.heroName AS name, h.age, 'PLAT', h.id FROM SuperHumans h
  WHERE h.side = 'g' AND h.age > 1856
) AS customer
JOIN Houses h ON h.OWNER = customer.ID AND h.hasChargingPort=true
JOIN PricingYears p ON customer.age > p.startYear && customer.age < p.endYear


--Bit more complex (robot)
SELECT customer.name, customer.age,
  CASE WHEN p.voltage = 120 THEN 'USA' ELSE 'EU' END as membership,
  customer.id, h.zip FROM
(
  SELECT r.model AS name, today() - r.assemblyYear, 'AUTO' FROM Robots r
  WHERE r.assemblyYear > 1990
  UNION
  SELECT r.series || "-" || r.model AS name, today() - r.assemblyYear, 'AUTO' FROM KillerRobots r
  WHERE r.series  = 'T' AND r.assemblyYear > 1992
) AS customer
JOIN Houses h ON h.OWNER = customer.ID AND h.hasChargingPort=true
JOIN PricingYears p ON customer.age > p.startYear && customer.age < p.endYear



SELECT customer.name, customer.age, customer.membership, customer.id, h.zip
FROM
(
  SELECT r.uniqueGruntingSound AS name, y.age, 'YETT'
  FROM Yetti y
) AS customer
JOIN Houses h ON h.OWNER = customer.ID AND (h.origin = 'Canada' OR h.origin = 'Russia')
JOIN PricingYears p ON customer.age > p.startYear && customer.age < p.endYear



--With Polymorphism
customerMembership(
  (SELECT Humans h WHERE h.segment = 'h').toCustomer(h.firstName || ' ' || h.lastName, h.membership, 1982)
  UNION
  (SELECT SuperHumans h WHERE r.side  = 'g').toCustomer(h.heroName, 'PLAT', 1982),
  true
)

-- (robot)
customerMembership(
  (SELECT Robots r).toCustomer(r.label, 1990)
  UNION
  (SELECT KillerRobots r WHERE r.series  = 'T').toCustomer(r.series || "-" || r.model, 1992),
  h.hasChargingPort = true
)

-- (yetti)
customerMembership(
  (SELECT Yetti y).toCustomer(y.uniqueGruntingSound)
  h.origin = 'Canada' OR h.origin = 'Russia'
)


/*

- YOU COULD DO THAT
customerMembership(
  query[Human].filter(_.segment == "h").toCustomer(h => h.firstName + " " + h.lastName, _.membership)(1982)
  union
  query[SuperHuman].filter(_.side == "g").toCustomer(_.heroName, _ => "PLAT")(1992)
)

=== IF YOU HAVE THIS ===
implicit class Ops(q: Query[HumanLike]) {
  def toCustomer = quote {
    (name: HumanLike => String, membership: HumanLike => String) =>
    (minAge: Int) =>
      q.filter(h => h.age > minAge)
       .map(h => Customer(name(h), h.age, membership(h)))
  }
}

=== AND THIS ===
def customerMembership(cs: Query[Customer]) = quote {
  for {
    c <- cs
    h <- query[Houses].join(h => h.owner = c.id)
    p <- query[PricingYears].join(p => c.age > p.startYear && c.age < p.endYear)
  } yield (c.name, c.age, if p.pricing == "sane" then c.membership else p.insaneMembership, c.id, h.zip)
}


-- Let's focus on middle for a second
*/


