package quixotic.rest

import zhttp.service.Server
import zhttp.http._
import zio._
import zio.json._
import quixotic.Record
import quixotic.DataService
import quixotic.DataServiceLive
import quixotic.QuillContext

object RestService extends ZIOAppDefault:
  implicit val encoder: JsonEncoder[Record] = DeriveJsonEncoder.gen[Record]

  override def run =
    ZIO.service[DataService].flatMap { dss =>
      Server.start(
        8088,
        Http.collectZIO[Request] {
          case Method.GET -> !! / "customers" =>
            dss.getCustomers.map(cs => Response.json(cs.toJson))
        }
      )
    }.provide(QuillContext.dataSourceLayer, DataService.live).exitCode

end RestService
