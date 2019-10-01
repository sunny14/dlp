package kafka

import kafka.Kafka.Companion.IBAN_TOPIC
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scanners.scanForIban

class IbanConsumer (kafka: Kafka): Consumer(kafka, IBAN_TOPIC)

fun main() {
    val log: Logger = LoggerFactory.getLogger("IbanConsumer")

    val kafka = Kafka(Kafka.BROCKERS)

    val consumer = CreditCardConsumer(kafka)
    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
        consumer.stop()
    }))
    consumer.consume {
        val found = scanForIban(it)
        if (found.isNotEmpty())   {
            log.error("found IBAN details: \n $found")
        }
        else    {
            log.debug("this message was scanned, no IBAN details found:\n$it")
        }
    }
}