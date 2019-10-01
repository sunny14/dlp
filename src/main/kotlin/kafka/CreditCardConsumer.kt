package kafka

import kafka.Kafka.Companion.CREDIT_CARDS_TOPIC
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import scanners.scanForCreditCards

class CreditCardConsumer (kafka: Kafka): Consumer(kafka, CREDIT_CARDS_TOPIC)

fun main() {
    val log: Logger = LoggerFactory.getLogger("CreditCardConsumer")

    val kafka = Kafka(Kafka.BROCKERS)

    val consumer = CreditCardConsumer(kafka)
    Runtime.getRuntime().addShutdownHook(Thread(Runnable {
        consumer.stop()
    }))
    consumer.consume {
        val found = scanForCreditCards(it)
        if (found.isNotEmpty())   {
            log.error("found credit card details: \n $found")
        }
        else    {
            log.debug("this message was scanned, no credit card details found:\n$it")
        }
    }
}