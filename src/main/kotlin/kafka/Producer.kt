package kafka

import kafka.Kafka.Companion.BROCKERS
import kafka.Kafka.Companion.CREDIT_CARDS_TOPIC
import kafka.Kafka.Companion.IBAN_TOPIC
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.*

class Producer(kafka: Kafka, private val topic: String) {
    private val kafkaProducer: KafkaProducer<String, String>

    init {
        val config = Properties()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
        config[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        config[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        kafkaProducer = KafkaProducer(config)
    }

    fun send(msg: String) {
        kafkaProducer.send(ProducerRecord(topic, msg))
    }

    fun flush() = kafkaProducer.flush()


}

fun main() {
    val log: Logger = LoggerFactory.getLogger("Producer")

    val kafka = Kafka(BROCKERS)

    val credProducer = Producer(kafka, CREDIT_CARDS_TOPIC)
    val ibanProducer = Producer(kafka, IBAN_TOPIC)

    (1..10).forEach {
        println("Please enter the text...\n")
        val input = readLine()
        val msg = "test message $input ${LocalDateTime.now()}"
        log.debug("sending $msg")
        credProducer.send(msg)
        ibanProducer.send(msg)
    }
    credProducer.flush()

}
