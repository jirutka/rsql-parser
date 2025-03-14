package cz.jirutka.rsql.parser.ast;

import spock.lang.Specification

class UnaryComparisonOperatorTest extends Specification{
    def 'construct with valid symbol'() {
        expect:
            new UnaryComparisonOperator(sym)
        where:
            sym << ['=isnull=', '=notnull=', '=not=']
    }

    def 'throw IllegalArgumentException when given invalid symbol'() {
        when:
            new UnaryComparisonOperator((String)sym)
        then:
            thrown IllegalArgumentException
        where:
            sym << [null, '', 'foo', '=123=', '><' , '!=', '>', '<', '=', '=<', '>=', '<=', '=>', '=!', 'a=b=c']
    }

    def 'equals when contains same symbols'() {
        expect:
            new UnaryComparisonOperator('=isnull=', '=nl=') == new UnaryComparisonOperator('=isnull=', '=nl=',)
    }
}
