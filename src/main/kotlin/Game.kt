class Game {
    private val SUITS = arrayOf("♡", "♤", "♧", "♢")
    private val VALUES = arrayOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
    private val player1 = Player("Bilbo")
    private val player2 = Player("Gandalf")

    /**
     * @return MutableList<Card> representing playing cards
     */
    private fun createDeck(): MutableList<Card> {
        val deck: MutableList<Card> = mutableListOf()
        for (suit in SUITS) {
            for (value in VALUES) deck.add(Card(value, suit))
        }
        return deck
    }

    /**
     * @return Boolean true if a winner has been found
     */
    private fun checkForWinner(): Boolean {
        return (player1.deck.size == 0 || player2.deck.size == 0)
    }

    /**
     * Prints winner name to console
     */
    private fun declareWinner() {
        val winner = if (player1.deck.size == 0) player2 else player1
        println("${winner.name} Won!")
    }

    /**
     * Start running the game rounds
     * It will run until a player has no cards left
     */
    fun start() {
        val deck = createDeck()
        deck.shuffle()
        deal(deck)

        var round = 0
        do {
            if(checkForWinner()) break
            println("Round ${++round}")
            println("------------")
        } while(playRound())

        declareWinner()

        println("The game lasted ${round} rounds!")
    }

    /**
     * Deal out the cards 1 by 1 to each player
     */
    private fun deal(deck: MutableList<Card>, test: Boolean = false) {
        // For fun, preload with war
        if ( test ) {
            player1.deck.add(Card(7, "♡"))
            player1.deck.add(Card(3, "♡"))
            player1.deck.add(Card(6, "♡"))

            player2.deck.add(Card(7, "♤"))
            player2.deck.add(Card(2, "♤"))
            player2.deck.add(Card(8, "♤"))

            return
        }

        for ((index, card) in deck.withIndex()) {
            if (index % 2 == 0) player1.deck.add(card) else player2.deck.add(card)
        }
    }

    /**
     * Handle giving cards to winner when a player runs
     * out of cards in their deck
     */
    private fun playerRanOut(p1success: Boolean) {
        val (loser, winner) =
                if ( !p1success ) Pair(player1, player2)
                else Pair(player2, player1)

        println("${loser.name} is out of cards!")
        winner.takeCardsFrom(loser.playPile)
    }

    /**
     * @return Boolean false if any player can't place a card
     * returning false signifies the round and game should end
     */
    private fun playCards(): Boolean {
        val p1success = player1.playCard()
        val p2success = player2.playCard()

        if ( !p1success || !p2success ) {
            playerRanOut(p1success)
        } else printCompare(player1.card, player2.card)

        return (p1success && p2success)
    }

    /**
     * @return Boolean false if any player can't place a card
     * returning false signifies the round and game should end
     */
    private fun playCardFaceDown(): Boolean {
        val p1success = player1.playCard() && player1.deck.size > 0
        val p2success = player2.playCard() && player1.deck.size > 0

        if ( !p1success || !p2success ) {
            playerRanOut(p1success)
        } else printFaceDown()

        return (p1success && p2success)
    }

    /**
     * @return Boolean false means game should end
     */
    private fun playRound(): Boolean {
        if ( player1.deck.size == 0 || player2.deck.size == 0 ) return false

        // Play initial cards
        // End game if a player ran out
        if ( !playCards() ) return false

        when {
            player1.card.value > player2.card.value -> player1.takeCardsFrom(player2.playPile)
            player1.card.value < player2.card.value -> player2.takeCardsFrom(player1.playPile)
            else -> {
                // Start a War
                // Play the faceDown cards and start the next face up round
                // Or if someone ran out of cards end the game
                if ( !playCardFaceDown() ) return false
                playRound()
            }
        }

        return true
    }

    /**
     * Print what cards are in play
     */
    private fun printCompare(card: Card, card2: Card) =
            println("(${player1.name}) ${card.display} vs ${card2.display} (${player2.name})")

    /**
     * Print the war cards
     */
    private fun printFaceDown() =
            println("(${player1.name}) ? vs ? (${player2.name})")
}