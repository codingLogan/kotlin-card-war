class Game {
    val SUITS = arrayOf("♡", "♤", "♧", "♢")
    val VALUES = arrayOf(2,3,4,5,6,7,8,9,10,11,12,13,14)
    val player1Deck: MutableList<Card> = mutableListOf()
    val player2Deck: MutableList<Card> = mutableListOf()

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
        playRound(player1Deck, player2Deck)
    }

    fun deal(deck: MutableList<Card>) {
        // Test war
        player1Deck.add(Card(5, "♡"))
        player2Deck.add(Card(5, "♡"))

        for ((index, card) in deck.withIndex()) {
            if (index % 2 == 0) player1Deck.add(card) else player2Deck.add(card)
        }
    }

    fun playRound(deck1: MutableList<Card>, deck2: MutableList<Card>): Boolean {
        val winnersPile: MutableList<Card> = mutableListOf()

        // Check the top cards for each player before adding to list
        while (deck1.first().value == deck2.first().value) {
            // Add the player cards to the pile
            // If one player runs out of cards, the other wins
            printCompare(deck1.first(), deck2.first())
            war(winnersPile, deck1, deck2)
        }

        // Get ready for winners pile of cards
        val player1Card = deck1.removeFirst()
        val player2Card = deck2.removeFirst()
        winnersPile.add(player1Card)
        winnersPile.add(player2Card)

        printCompare(player1Card, player2Card)
        if (player1Card.value > player2Card.value) winRound(winnersPile, player1Deck, "Player 1")
        else winRound(winnersPile, player2Deck, "Player 2")

        // return true means the game should continue
        return true
    }

    fun winRound(pile: MutableList<Card>, playerDeck: MutableList<Card>, playerName: String) {
        println("$playerName wins the round and gains ${pile.size} cards!")
        playerDeck.addAll(pile)
        println("Player 1: ${player1Deck.size} cards")
        println("Player 2: ${player2Deck.size} cards\n")
    }

    fun removeCards(winnersPile: MutableList<Card>, deck1: MutableList<Card>, deck2: MutableList<Card>) {
        // Remove top card from players
        if ( deck1.size == 0 ) endGame("player1 wins because player 2 couldn't war anymore")
        winnersPile.add(deck1.removeFirst())

        if ( deck2.size == 0 ) endGame("player2 wins because player 1 couldn't war anymore")
        winnersPile.add(deck2.removeFirst())
    }

    fun war(winnersPile: MutableList<Card>, deck1: MutableList<Card>, deck2: MutableList<Card>) {
        println("War!!!, each player places a face down card into the pile")
        repeat (2) {removeCards(winnersPile, deck1, deck2)}
    }

    fun printCompare(card: Card, card2: Card) = println("(Player 1) ${card.display} vs ${card2.display} (Player 2)")

    fun endGame(message: String) {
        println(message)
    }
}