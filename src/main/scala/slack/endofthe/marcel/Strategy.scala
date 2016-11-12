package slack.endofthe.marcel

import slack.endofthe.marcel.Direction._

/**
  * Created by grayson on 11/12/16.
  */
object Strategy {
  case class HaliteUnit(location: Location, strength: Int)

  def siteInDirection(unit: HaliteUnit, direction: Direction, gameMap: GameMap): Site = {

    val x = unit.location.x
    val y = unit.location.y
    if (direction == WEST) {
      if (x == 0) {
        return gameMap.getSite(new Location(gameMap.width - 1, y))
      }
      return gameMap.getSite(new Location(x - 1, y))
    }

    if (direction == EAST) {
      if (x == gameMap.width - 1) {
        return gameMap.getSite(new Location(0, y))
      }

      return gameMap.getSite(new Location(x + 1, y))
    }

    if (direction == NORTH) {
      if (y == gameMap.width - 1) {
        return gameMap.getSite(new Location(x, 0))
      }

      return gameMap.getSite(new Location(x, y + 1))
    }
    if (direction == SOUTH) {
      if (y == 0) {
        return gameMap.getSite(new Location(x, gameMap.width - 1))
      }
      return gameMap.getSite(new Location(x, y - 1))
    }

    // Direction is STILL
    gameMap.getSite(new Location(x, y))
  }

  def surroundingSquares(unit: HaliteUnit, gameMap: GameMap): List[(Site, Direction)] = {
    List(
      (siteInDirection(unit, Direction.NORTH, gameMap), Direction.NORTH),
      (siteInDirection(unit, Direction.WEST, gameMap), Direction.WEST),
      (siteInDirection(unit, Direction.SOUTH, gameMap), Direction.SOUTH),
      (siteInDirection(unit, Direction.EAST, gameMap), Direction.EAST)
    )
  }
}
