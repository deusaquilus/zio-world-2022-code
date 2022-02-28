package quixotic.rest

import zhttp.service.Server
import zhttp.http._
import zio._
import zio.json._
import zio.Console.printLine
import quixotic.Record
import quixotic.DataService
import quixotic.DataServiceLive
import quixotic.QuillContext

object RestService extends ZIOAppDefault:
  given JsonEncoder[Record] = DeriveJsonEncoder.gen[Record]
  override def run =
    Server.start(
      8088,
      Http.collectZIO[Request] {
        case req @ Method.GET -> !! / "customers" =>
          ZIO.environment[DataService].flatMap { dsl =>
            val lastParams = req.url.queryParams.map((k, v) => (k, v.head))
            dsl.get.getCustomers(lastParams, List("name", "age", "membership")).map(cs => Response.json(cs.toJson))
          }
        case req @ Method.GET -> !! / "customersDebug" =>
          ZIO.environment[DataService].flatMap { dsl =>
            val lastParams = req.url.queryParams.map((k, v) => (k, v.head))
            for {
              plan <- dsl.get.getCustomersPlan(lastParams, List("name", "age", "membership"))
              _    <- printLine("============= Plan =============\n" + plan.mkString("\n"))
              cs   <- dsl.get.getCustomers(lastParams, List("name", "age", "membership"))
            } yield Response.json(cs.toJson)
          }
      }
    ).provide(QuillContext.dataSourceLayer, DataService.live, zio.Console.live).exitCode

end RestService
