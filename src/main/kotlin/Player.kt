class Player (val name: String, val deck: MutableList<Card> = mutableListOf(), val playPile: MutableList<Card> = mutableListOf()){
    val card: Card
    get() = playPile.last()

    /**
     * @return Boolean false if player ran out of cards
     * This will remove a card from the deck and place
     * it into the player's playPile
     */
    fun playCard(visible: Boolean = true): Boolean {
        if ( deck.size > 0 )
            playPile.add(deck.removeFirst())
            else return false

        return true
    }

    fun takeCardsFrom(pile: MutableList<Card>) {
        println("$name won ${pile.size + playPile.size} cards!")
        // Add own play pile to deck
        deck.addAll(playPile)
        playPile.clear()

        // Add other play pile to deck
        deck.addAll(pile)
        pile.clear()

        println("$name now has ${deck.size} cards!\n")
    }
}