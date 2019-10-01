import kafka.createConsumer
import kafka.createProducer
import kafka.logger
import org.apache.kafka.clients.producer.ProducerRecord
import scanners.scanForCreditCards
import java.time.Duration
import kotlin.concurrent.thread


const val TOPIC = "mytopic"
const val BROCKERS = "localhost:9092"

fun main() {

    //consumer thread
    thread(start = true) {
        println("KAFKA CONSUMER has run.")
        val consumer = createConsumer(BROCKERS)
        consumer.subscribe(listOf(TOPIC))
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

    //producer thread
    thread(start = true) {
        println("KAFKA PRODUCER has run.")
        val producer = createProducer(BROCKERS)

        while (true) {
            println("Please enter the text...\n")
            val input = readLine()
            println("$input")
            val futureResult = producer.send(ProducerRecord(TOPIC, input))
            futureResult.get()

            println("sent!")
        }

    }


}


