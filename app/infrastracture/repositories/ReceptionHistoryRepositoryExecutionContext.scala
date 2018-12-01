package infrastracture.repositories

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

class ReceptionHistoryRepositoryExecutionContext @Inject() (actorSystem: ActorSystem)
  extends CustomExecutionContext(actorSystem, "receptionHistory.repository.dispatcher")
