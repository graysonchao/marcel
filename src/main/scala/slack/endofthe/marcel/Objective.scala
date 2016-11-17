package slack.endofthe.marcel

import org.slf4j.{Logger, LoggerFactory}
import slack.endofthe.marcel.Direction._

/**
  * Created by grayson on 11/12/16.
  */
trait Objective {
  // Return the next move to make
  def nextMove(unit: HaliteUnit, gameMap: GameMap):Move

  // Override this if your objective has a better idea.
  // In general, needs to stay fast so we don't time out.
  def defaultMove(unit: HaliteUnit, gameMap: GameMap):Move = {
    new Move(unit.location, STILL)
  }
}

object TakeUnownedTerritoryObjective extends Objective {

  val log:Logger = LoggerFactory.getLogger("marcel.TakeUnownedTerritoryObjective")

  def nextMove(unit: HaliteUnit, gameMap: GameMap): Move = {
    val easyTargets = List[Direction](NORTH, EAST, SOUTH, WEST)
      .map(direction => (direction, gameMap.getSite(unit.location, direction)))
      .filter(site => site._2.owner == 0)
      .filter(site => unit.strength > site._2.strength)
      .sortWith((s1, s2) => s1._2.strength > s2._2.strength)

    if (easyTargets.nonEmpty) {
      val move = new Move(unit.location, easyTargets.head._1)
      log.info(s"Moving the unit at ${unit.location.x}, ${unit.location.y} in ${move.dir}")
      return move
    }

    new Move(unit.location, STILL)
  }
}

object ReinforceBordersObjective extends Objective {

  val log:Logger = LoggerFactory.getLogger("marcel.ReinforceBordersObjective")

  def directionOfNearestBorder(unit: HaliteUnit, gameMap: GameMap): (Direction, Int) = {
    List[Direction](NORTH, EAST, SOUTH, WEST)
      .map(direction => new Move(gameMap.getLocation(unit.location, direction), direction))
      .map(move => {
        var distance = 0
        while (gameMap.getSite(move.loc).owner == unit.id) {
          move.loc = gameMap.getLocation(move.loc, move.dir)
          distance += 1
        }
        log.info(s"The nearest border is to the ${move.dir}, $distance steps away.")
        (move.dir, distance)
      })
      .minBy(_._2)
  }

  def nextMove(unit: HaliteUnit, gameMap: GameMap): Move = {
    val nearestBorder:(Direction, Int) = directionOfNearestBorder(unit, gameMap)
    if (nearestBorder._2 == 0) {
      return TakeUnownedTerritoryObjective.nextMove(unit, gameMap)
    }
    new Move(unit.location, nearestBorder._1)
  }
}
