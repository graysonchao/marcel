package slack.endofthe.marcel

import org.slf4j.{Logger, LoggerFactory}

import slack.endofthe.marcel.Strategy._

/**
  * Created by snoe on 7/23/16.
  */
class MyBot(id: Int, gameMap:GameMap) extends HaliteBot(id, gameMap) {

  val log:Logger = LoggerFactory.getLogger("marcel");

  override def name = "marcel"

  def myUnits(gameMap: GameMap): Stream[HaliteUnit] = {
    for {
      y <- (0 until gameMap.height).toStream
      x <- (0 until gameMap.width).toStream
      location = new Location(x, y)
      site = gameMap.getSite(location)
      if site.owner == id
    } yield HaliteUnit(location, site.strength)
  }

  def move(unit: HaliteUnit): Move = {
    val easyTargets = surroundingSquares(unit, gameMap)
      .filter((site) => site._1.strength < unit.strength)
      .filter((site) => site._1.owner != id)

    if (easyTargets.nonEmpty) {
      val target = easyTargets.head
      log.info(s"Found an easy target with strength ${target._1.strength}, mine is ${unit.strength}")
      return new Move(unit.location, target._2)
    }

    new Move(unit.location, Direction.STILL)
  }

  override def takeTurn(turn:BigInt, gameMap:GameMap): MoveList = {
    // Random moves
    val moves = new MoveList()
    myUnits(gameMap).foreach(u => {
      val m = move(u)
      moves.add(m)
      log.info(s"I'm moving the unit at (${u.location.x}, ${u.location.y}) in ${m.dir}")
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
