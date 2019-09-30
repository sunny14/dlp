import scanners.CreditCardScannerKt
import spock.lang.Specification


class CreditCardScanTest extends Specification {

    def withCreditCardMsg = "my credit card is 4539093950304573 here is it!\n" +
            "also can write it down as 4539-0939-5030-4573 or 4539 0939 5030 4573\n" +
            "and here is a credit card of my wife: 349250804014372!"
    def noCreditCardMsg = "no way I am telling you my credit card number!\n" +
            "probably it is 5678-9087 8976 0000 but I am lying"
    def invalidCreditCardMsg = "my credit card is 5678908787655656 here is it!\n" +
            "also can write it down as 5678-9087-8765-5656 or 5678 9087 8765 5656\n" +
            "and here is a credit card of my wife: 5678987656438!"

    def "test scan for credit card"()   {
        when:
        def found = CreditCardScannerKt.scanForCreditCards(withCreditCardMsg)

        then:
        found.size() == 4
        found[0].contains("4539093950304573")
        found[1].contains("4539-0939-5030-4573")
        found[2].contains("4539 0939 5030 4573")
        found[3].contains("349250804014372")
    }

    def "test scan for no credit card"()   {
        when:

        def found = CreditCardScannerKt.scanForCreditCards(noCreditCardMsg)

        then:
        found.size() == 0
    }


    def "test not valid by Luhn"()  {
        when:
        def found = CreditCardScannerKt.scanForCreditCards(invalidCreditCardMsg)

        then:
        found.size() == 0
    }
}
