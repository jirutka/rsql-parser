package cz.jirutka.rsql.parser.ast

import spock.lang.Specification

import static cz.jirutka.rsql.parser.ast.RSQLOperators.IS_NULL

class UnaryComparisonNodeTest extends Specification {

    def 'throws exception when given a null operator'() {
        when:
            new UnaryComparisonNode(null, 'genres')
        then:
            thrown IllegalArgumentException

    }

    def 'throws exception when given a null selector'(){
        when:
            new UnaryComparisonNode(IS_NULL, null)
        then:
            thrown IllegalArgumentException
    }

    def 'throws exception when given an empty selector'(){
        when:
            new UnaryComparisonNode(IS_NULL, '')
        then:
            thrown IllegalArgumentException
    }
}
