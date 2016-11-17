package slack.endofthe.marcel

import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

import org.slf4j.{Logger, LoggerFactory}
import slack.endofthe.marcel.Direction._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.HashMap
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class HaliteUnit(location: Location, strength: Int, id: Int)

class MyBot(id: Int, gameMap:GameMap) extends HaliteBot(id, gameMap) {

  val log:Logger = LoggerFactory.getLogger("marcel")

  override def name = "marcel"

  def myUnits(gameMap: GameMap): Stream[HaliteUnit] = {
    for {
      y <- (0 until gameMap.height).toStream
      x <- (0 until gameMap.width).toStream
      location = new Location(x, y)
      site = gameMap.getSite(location)
      if site.owner == id
    } yield HaliteUnit(location, site.strength, id)
  }

  def getObjective(unit: HaliteUnit): Objective = {
    if (unit.strength > 150) {
      return ReinforceBordersObjective
    }
    TakeUnownedTerritoryObjective
  }

  override def takeTurn(turn:BigInt, gameMap:GameMap): MoveList = {

    val start = System.currentTimeMillis()

    val futures = myUnits(gameMap).map(unit => Future {
        getObjective(unit).nextMove(unit, gameMap)
      })

    // one issue with this is that the moves aren't ordered at all, we just churn through as many as we can
    val aggregated = Future.sequence(futures)
    val results = Await.result(aggregated, Duration(900,TimeUnit.MILLISECONDS))
    val moveList = new MoveList()
    results.foreach( move => moveList.add(move) )

    // need nicer profiling impl we're gonna need this kinda thing a lot
    val end = System.currentTimeMillis()
    val time = end - start
    val moveCount = moveList.size()
    log.info(s"Turn took $time :ms, generated $moveCount moves.")

    return moveList
  }
}

object MyBot {

  def main(args:Array[String]):Unit = {

    val log:Logger = LoggerFactory.getLogger("marcel");
    val maker = new HaliteBotMaker() {
      override def makeBot(id:Int, gameMap:GameMap):HaliteBot = new MyBot(id, gameMap)
    }

    log.info("Running...")
    HaliteBot.run(args, maker)
  }
}
