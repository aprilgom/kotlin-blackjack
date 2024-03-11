package blackjack

import blackjack.model.Dealer
import blackjack.model.Deck
import blackjack.model.GameStatistics
import blackjack.model.Participant
import blackjack.model.Player
import blackjack.view.InputView
import blackjack.view.OutputView

class Controller {

    private val deck = Deck()
    fun run() {
        val players = makePlayers()
        val dealer = Dealer()
        initParticipantsCard(dealer, players)
        proceedParticipantsTure(dealer, players)
        printStatistics(dealer, players)
    }

    private fun makePlayers(): List<Player> {
        val names: List<String> = InputView.getNames()
        return names.map { Player(it) }
    }

    private fun initParticipantsCard(
        dealer: Dealer,
        players: List<Player>,
    ) {
        players.forEach { player ->
            player.initCard()
        }
        dealer.initCard()
        OutputView.printInitialResult(dealer, players)
    }

    private fun proceedParticipantsTure(
        dealer: Dealer,
        players: List<Player>,
    ) {
        players.forEach { player ->
            proceedPlayerTurn(player)
        }
        proceedDealerTurn(dealer)
    }

    private fun proceedPlayerTurn(player: Player) {
        if (player.isMaxScore()) {
            OutputView.printBlackJackMessage(player)
            return
        }
        while (player.isHitable() && askPick(player.name)) {
            deck giveCardTo player
            OutputView.printParticipantStatus(player)
        }
        if (player.isBusted()) {
            OutputView.printBustedMessage(player)
        }
    }

    private fun askPick(name: String): Boolean {
        return InputView.askPickAgain(name)
    }

    private fun proceedDealerTurn(dealer: Dealer) {
        while (dealer.isHitable()) {
            deck giveCardTo dealer
            OutputView.printDealerHitMessage()
        }
    }

    private fun printStatistics(dealer: Dealer, players: List<Player>) {
        OutputView.printResult(dealer, players)
        val gameStatistics = GameStatistics(dealer, players)
        OutputView.printDealerStatistics(gameStatistics.dealerStatistics)
        OutputView.printPlayerStatistics(gameStatistics.playerStatistics)
    }

    private fun Participant.initCard() {
        deck giveCardTo this
        deck giveCardTo this
    }
}
