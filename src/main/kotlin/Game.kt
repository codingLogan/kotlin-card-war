class Game {
    val SUITS = arrayOf("♡", "♤", "♧", "♢")
    val VALUES = arrayOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
    val player1 = Player("Bilbo")
    val player2 = Player("Gandalf")

    fun createDeck(): MutableList<Card> {
        val deck: MutableList<Card> = mutableListOf()
        for (suit in SUITS) {
            for (value in VALUES) deck.add(Card(value, suit))
        }
        return deck
    }

    fun start() {
        val deck = createDeck()
        deck.shuffle()
        deal(deck)

        var round = 0
        do {
            println("Round ${++round}")
            println("------------")
        } while(playRound())

        println("The game lasted ${round} rounds!")
    }

    fun deal(deck: MutableList<Card>, test: Boolean = false) {
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
     * @return Boolean false if any player can't place a card
     * returning false signifies the round and game should end
     */
    fun playCards(faceDown: Boolean = false): Boolean {
        val p1success = player1.playCard()
        val p2success = player2.playCard()

        if ( !p1success ) println("${player1.name} is out of cards!")
        if ( !p2success ) println("${player2.name} is out of cards!")

        // Both players ran out of cards??? This is bad, game breaking logic
        if (!p1success && !p2success) return false

        // See if either player should by default steal cards
        if (!p1success) player2.takeCardsFrom(player1.playPile)
        if (!p2success) player1.takeCardsFrom(player2.playPile)

        if ( faceDown ) {
            printFaceDown()
        } else {
            printCompare(player1.card, player2.card)
        }

        return true
    }

    // return false means the game should end
    // return true means the game should continue
    fun playRound(): Boolean {
        if ( player1.deck.size == 0 || player2.deck.size == 0 ) return false

        if ( !playCards() ) return false // One of the players couldn't place a card

        if ( player1.card.value == player2.card.value ) {
            if ( !playCards(true) ) return false else playRound()
        } else if ( player1.card.value > player2.card.value ) {
            player1.takeCardsFrom(player2.playPile)
        } else if ( player1.card.value < player2.card.value ) {
            player2.takeCardsFrom(player1.playPile)
        }

        if ( player1.deck.size == 0 || player2.deck.size == 0 ) return false

        return true
    }

    fun printCompare(card: Card, card2: Card) =
            println("(${player1.name}) ${card.display} vs ${card2.display} (${player2.name})")

    fun printFaceDown() =
            println("(${player1.name}) ? vs ? (${player2.name})")
}