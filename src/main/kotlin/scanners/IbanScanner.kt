package scanners

const val IBAN_PATTERN = """\b[A-Z]{2}[0-9]{2}[[0-9]{4} ]{1,7}[0-9]{1,3}\b"""

//TODO: extend country codes list
val COUNTRY_CODES = arrayOf("AD", "AE", "AL", "AT", "BA", "BE", "BG")

fun scanForIban (data: String): List<String>  {
    return scanForIban(data, listOf(IBAN_PATTERN))
}

private fun scanForIban(data: String, rules: List<String>): List<String>  {

    val report = mutableListOf<String>()

    for (rule in rules.map { it.toRegex() }) {
        val found = rule.findAll(data)

        val validIbans = found.filter { iban ->
                COUNTRY_CODES.contains(iban.value.subSequence(0,2))
            }

        report.add("for rule $rule found: \n $validIbans")
    }

    return report.toList()

}







