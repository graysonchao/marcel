package slack.endofthe.marcel

trait HaliteBotMaker {
  def makeBot(id:Int, gameMap:GameMap):HaliteBot = new HaliteBot(id, gameMap)
}
