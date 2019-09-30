package kafka

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import scanners.scanForCreditCards
import java.time.Duration
import java.util.*

private fun createConsumer(brokers: String): Consumer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = brokers
    props["group.id"] = "person-processor"
    props["key.deserializer"] = StringDeserializer::class.java
    props["value.deserializer"] = StringDeserializer::class.java
    return KafkaConsumer<String, String>(props)
}

val logger = LoggerFactory.getLogger("consumer")


fun main() {
    val consumer = createConsumer("localhost:9092")
    consumer.subscribe(listOf("mytopic"))

    while (true) {
        val records = consumer.poll(Duration.ofSeconds(1))
        for (rec in records)    {
            val found = scanForCreditCards(rec.value())
            if (found.isNotEmpty())   {
                logger.error("found credit card details: \n $found")
            }
            else    {
                logger.info("this message was scanned, no credit card details found:\n$rec")
            }
        }
    }

}
