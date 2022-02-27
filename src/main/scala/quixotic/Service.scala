package quixotic

import zio.IO
import java.sql.SQLException
import javax.sql.DataSource
import zio.Console.printLine
import io.getquill._

import QuillContext._
import Queries._
import io.getquill.context.ZioJdbc.DataSourceLayer
import zio._

object QuillContext extends PostgresZioJdbcContext(Literal):
  val dataSourceLayer: ULayer[DataSource] =
    DataSourceLayer.fromPrefix("database").orDie

trait DataService:
  def getCustomers: IO[SQLException, List[Record]]

object DataService:
  val live = (DataServiceLive.apply _).toLayer[DataService]

final case class DataServiceLive(dataSource: DataSource) extends DataService:
  def getCustomers: IO[SQLException, List[Record]] =
    run {
      customerMembership {
        humanCustomer(HumanType.Regular("h", 1982))
        ++
        humanCustomer(HumanType.Super("g", 1856))
      }(_ => true, (c, p) => if p.pricing == "sane" then c.membership else p.insaneMembership)
    }.provideService(dataSource)

object RunLayer extends zio.App:
  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (for {
      dsl <- (DataService.live).build.useNow
      _   <- dsl.get.getCustomers.tap(cs => printLine(cs.toString))
    } yield ()).provideCustomLayer(QuillContext.dataSourceLayer).exitCode
