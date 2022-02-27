package quixotic.rest

import zhttp.service.Server
import zhttp.http._
import zio._
import zio.json._
import quixotic.Record
import quixotic.DataService
import quixotic.DataServiceLive
import quixotic.QuillContext

object RestService extends zio.App:
  implicit val encoder: JsonEncoder[Record] = DeriveJsonEncoder.gen[Record]

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (QuillContext.dataSourceLayer >>> DataService.live).build.useNow.flatMap(dsl =>
      Server.start(
        8088,
        Http.collectM[Request] {
          case Method.GET -> Root / "customers" =>
            dsl.get.getCustomers.map(cs => Response.jsonString(cs.toJson))
        }
      ).forever.exitCode
    )

end RestService
