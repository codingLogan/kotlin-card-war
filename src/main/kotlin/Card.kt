class Card (val value: Int, val suit: String){
    val display: String
    get(): String = "${getValueDisplay()}$suit"

    private fun getValueDisplay(): String {
        if ( value in 2..10 ) return value.toString()

        return when (value) {
            11 -> "J"
            12 -> "Q"
            13 -> "K"
            14 -> "A"
            else -> "Joker"
        }
    }
}