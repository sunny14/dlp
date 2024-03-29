package kafka

data class Kafka(val bootstrapServers: String)  {

    companion object    {
        const val CREDIT_CARDS_TOPIC = "creditcard"
        const val IBAN_TOPIC = "iban"
        const val BROCKERS = "localhost:9092"
    }
}

