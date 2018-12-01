import com.google.inject.AbstractModule
import domain.repositories.ReceptionHistoryRepository
import services._
import javax.inject._
import net.codingwell.scalaguice.ScalaModule
import play.api.{ Configuration, Environment }
import infrastracture.repositories._

/**
 * Sets up custom components for Play.
 *
 * https://www.playframework.com/documentation/latest/ScalaDependencyInjection
 */
class Module(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule {

  override def configure(): Unit = {
    bind[ReceptionHistoryRepository].to[ReceptionHistoryRepositoryImpl].in[Singleton]
  }
}