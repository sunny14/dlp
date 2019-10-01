package kafka

data class Kafka(val bootstrapServers: String)  {

    companion object    {
        const val CREDIT_CARDS_TOPIC = "mytopic"
        const val BROCKERS = "localhost:9092"
    }
}

