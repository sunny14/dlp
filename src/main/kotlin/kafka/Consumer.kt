package kafka

import kafka.Kafka.Companion.BROCKERS
import kafka.Kafka.Companion.CREDIT_CARDS_TOPIC
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scanners.scanForCreditCards
import java.util.*

class Consumer(kafka: Kafka, topic: String) {
    private val kafkaConsumer: KafkaConsumer<String, String>

    @Volatile
    var keepGoing = true

    init {
        val config = Properties()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        config[ConsumerConfig.GROUP_ID_CONFIG] = UUID.randomUUID().toString()
        kafkaConsumer = KafkaConsumer<String, String>(config).apply {
            subscribe(listOf(topic))
        }
    }

    fun consume(handler: (value: String) -> Unit) = Thread(Runnable {
        keepGoing = true
        kafkaConsumer.use { kc ->
            while (keepGoing) {
                kc.poll(500)?.forEach {
                    handler(it?.value() ?: "???")
                }
            }
        }
    }).start()

    fun stop() {
        keepGoing = false
    }


}

fun main() {
    val log: Logger = LoggerFactory.getLogger("Consumer")

    val kafka = Kafka(BROCKERS)
    val topic = CREDIT_CARDS_TOPIC

    val consumer = Consumer(kafka, topic)
    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
        consumer.stop()
    }))
    consumer.consume {
                val found = scanForCreditCards(it)
                if (found.isNotEmpty())   {
                    println("found credit card details: \n $found")
                }
                else    {
                    println("this message was scanned, no credit card details found:\n$it")
                }
    }
}

