package scanners

const val SIMPLE_PATTERN = """\b[0-9]{13,16}\b"""
const val PATTERN_WITH_SPACES = """\b[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}\b"""
const val PATTERN_WITH_DASHES = """\b[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}\b"""
const val CREDIT_CARD_PATTERN = """$PATTERN_WITH_SPACES|$PATTERN_WITH_DASHES|$SIMPLE_PATTERN"""



fun scanForCreditCards(data: String): List<String>  {
    return scanForCreditCards(data, listOf(CREDIT_CARD_PATTERN))
}

private fun scanForCreditCards(data: String, rules: List<String>): List<String>  {

    val report = mutableListOf<String>()

    for (rule in rules.map { it.toRegex() }) {

        val found = rule.findAll(data)
        found.map { regex -> regex.value }.forEach {
            if (Luhn.isValid(it.replace('-', ' '))) {
                report.add("for rule $rule found: \n $it")
            } else {
                println("$it does not pass a Luhn test")
            }
        }

    }

    return report.toList()

}

object Luhn {
    fun isValid(input: String): Boolean {
        val sanitizedInput = input.replace(" ", "")

        return when {
            valid(sanitizedInput) -> checksum(sanitizedInput) % 10 == 0
            else -> false
        }
    }

    private fun valid(input: String) = input.all(Char::isDigit) && input.length > 1

    private fun checksum(input: String) = addends(input).sum()

    private fun addends(input: String) = input.digits().mapIndexed { i, j ->
        when {
            (input.length - i + 1) % 2 == 0 -> j
            j >= 5 -> j * 2 - 9
            else -> j * 2
        }
    }

    private fun String.digits() = this.map(Character::getNumericValue)
}

