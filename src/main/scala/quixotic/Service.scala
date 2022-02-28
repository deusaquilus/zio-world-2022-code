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
  def getCustomers(params: Map[String, String], columns: List[String]): IO[SQLException, List[Record]]
  def getCustomersPlan(params: Map[String, String], columns: List[String]): IO[SQLException, List[String]]

object DataService:
  val live = (DataServiceLive.apply _).toLayer[DataService]

final case class DataServiceLive(dataSource: DataSource) extends DataService:
  def getCustomersPlan(params: Map[String, String], columns: List[String]) =
    run(customersPlan(params, columns), OuterSelectWrap.Never).provideService(dataSource)
  def getCustomers(params: Map[String, String], columns: List[String]): IO[SQLException, List[Record]] =
    run(customers(params, columns)).provideService(dataSource)

object RunLayer extends zio.App:
  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (for {
      dsl <- (DataService.live).build.useNow
      _   <- dsl.get.getCustomers(Map(), List()).tap(cs => printLine(cs.toString))
    } yield ()).provideCustomLayer(QuillContext.dataSourceLayer).exitCode
