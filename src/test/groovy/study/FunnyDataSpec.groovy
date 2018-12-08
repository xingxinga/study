package study

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class FunnyDataSpec extends Specification implements DomainUnitTest<FunnyData> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
