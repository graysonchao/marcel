package slack.endofthe.marcel

import org.slf4j.{Logger, LoggerFactory}
import slack.endofthe.marcel.Direction._

import scala.collection.mutable.HashMap

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
    val moves = new MoveList()
    myUnits(gameMap).foreach(unit => {
      moves.add(getObjective(unit).nextMove(unit, gameMap))
    })
    moves
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
