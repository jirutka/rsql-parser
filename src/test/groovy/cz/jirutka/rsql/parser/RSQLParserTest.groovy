/*
 * The MIT License
 *
 * Copyright 2013-2014 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.jirutka.rsql.parser

import cz.jirutka.rsql.parser.ast.*
import spock.lang.Specification
import spock.lang.Unroll

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*

@Unroll
class RSQLParserTest extends Specification {

    static final RESERVED = ['"', "'", '(', ')', ';', ',', '=', '<', '>', '!', '~', ' ']

    def factory = new NodesFactory(defaultComparisonOperators(), defaultUnaryOperator())


    def 'throw exception when created with null or empty set of comparison operators'() {
        when:
           new RSQLParser(operators as Set, defaultUnaryOperator() as Set )
        then:
            thrown IllegalArgumentException
        where:
            operators << [null, []]
    }

    def 'throw exception when created with null or empty set of unary operators'() {
        when:
            new RSQLParser(defaultComparisonOperators() as Set, operators as Set )
        then:
            thrown IllegalArgumentException
        where:
            operators << [null, []]
    }

    def 'throw exception when input is null'() {
        when:
            parse(null)
        then:
            thrown IllegalArgumentException
    }


    def 'parse comparison operator: #op'() {
        given:
            def expected = factory.createComparisonNode(op, 'sel', ['val'])
        expect:
            parse("sel${op}val") == expected
        where:
            op << defaultComparisonOperators()*.symbols.flatten()
    }

    def 'parse unary operator: #op'(){
        given:
            def expected = factory.createUnaryComparisonNode(op, 'sel')
        expect:
            parse("sel${op}") == expected
        where:
            op << defaultUnaryOperator()*.symbols.flatten()
    }

    def 'throw exception for deprecated short equal operator: ='() {
        when:
            parse('sel=val')
        then:
            thrown RSQLParserException
    }


    def 'parse selector: #input'() {
        expect:
            parse("${input}==val") == eq(input, 'val')
        where:
            input << [
                'allons-y', 'l00k.dot.path', 'look/XML/path', 'n:look/n:xml', 'path.to::Ref', '$doll_r.way' ]
    }

    def 'throw exception for selector with reserved char: #input'() {
        when:
            parse("${input}==val")
            System.out.println("${input}")
        then:
            thrown RSQLParserException
        where:
            input << RESERVED.collect{ ["ill${it}", "ill${it}ness"] }.flatten() - ['ill ']
    }

    def 'throw exception for empty selector with comparison operator'() {
        when:
            parse("==val")
        then:
            thrown RSQLParserException
    }

    def 'throw exception for empty selector with unary comparison operator'() {
        when:
            parse(input)
        then:
            thrown RSQLParserException
        where:
            input << ['=isnull=', '=notnull=']
    }

    def 'parse unquoted argument: #input'() {
        given:
            def expected = eq('sel', input)
        expect:
            parse("sel==${input}") == expected
        where:
            input << [ '«Allons-y»', 'h@llo', '*star*', 'čes*ký', '42', '0.15', '3:15' ]
    }

    def 'throw exception for unquoted argument with reserved char: #input'() {
        when:
            parse("sel==${input}")
        then:
            thrown RSQLParserException
        where:
            input << RESERVED.collect{ ["ill${it}", "ill${it}ness"] }.flatten() - ['ill ']
    }

    def 'parse quoted argument with any chars: #input'() {
        given:
            def expected = eq('sel', input[1..-2])
        expect:
            parse("sel==${input}") == expected
        where:
            input << [ '"hi there!"', "'Pěkný den!'", '"Flynn\'s *"', '"o)\'O\'(o"', '"6*7=42"' ]
    }


    def 'parse escaped single quoted argument: #input'() {
        expect:
            parse("sel==${input}") == eq('sel', parsed)
        where:
            input                   | parsed
            "'10\\' 15\"'"          | "10' 15\""
            "'10\\' 15\\\"'"        | "10' 15\""
            "'w\\\\ \\'Flyn\\n\\''" | "w\\ 'Flynn'"
            "'\\\\(^_^)/'"          | "\\(^_^)/"
    }

    def 'parse escaped double quoted argument: #input'() {
        expect:
            parse("sel==${input}") == eq('sel', parsed)
        where:
            input                   | parsed
            '"10\' 15\\""'          | '10\' 15"'
            '"10\\\' 15\\""'        | '10\' 15"'
            '"w\\\\ \\"Flyn\\n\\""' | 'w\\ "Flynn"'
            '"\\\\(^_^)/"'          | '\\(^_^)/'
    }

    def 'parse arguments group: #input'() {
        setup: 'strip quotes'
            def values = input.collect { val ->
                val[0] in ['"', "'"] ? val[1..-2] : val
            }
        expect:
            parse("sel=in=(${input.join(',')})") == new ComparisonNode(IN, 'sel', values)
        where:
            input << [ ['chunky', 'bacon', '"ftw!"'], ["'hi!'", '"how\'re you?"'], ['meh'], ['")o("'] ]
    }


    def 'parse logical operator: #op'() {
        given:
            def expected = factory.createLogicalNode(op, [eq('sel1', 'arg1'), isnull('sel2')])
        expect:
            parse("sel1==arg1${op.toString()}sel2=isnull=") == expected
        where:
            op << LogicalOperator.values()
    }

    def 'parse alternative logical operator: "#alt"'() {
        given:
            def expected = factory.createLogicalNode(op, [eq('sel1', 'arg1'), isnull('sel2')])
        expect:
            parse("sel1==arg1${alt}sel2=isnull=") == expected
        where:
            op << LogicalOperator.values()
            alt = op == LogicalOperator.AND ? ' and ' : ' or ';
    }

    def 'parse queries with default operators priority: #input'() {
        expect:
            parse(input) == expected
        where:
            input                                    | expected
            's0==a0;s1==a1;s2==a2'                   | and(eq('s0','a0'), eq('s1','a1'), eq('s2','a2'))
            's0==a0,s1=out=(a10,a11),s2==a2'         | or(eq('s0','a0'), out('s1','a10', 'a11'), eq('s2','a2'))
            's0==a0,s1==a1;s2==a2,s3==a3'            | or(eq('s0','a0'), and(eq('s1','a1'), eq('s2','a2')), eq('s3','a3'))
            's0=notnull=,s1=isnull=;s2=notnull='     | or(notnull('s0'), and(isnull('s1'), notnull('s2')))
    }

    def 'parse queries with parenthesis: #input'() {
        expect:
            parse(input) == expected
        where:
            input                                            | expected
            '(s0==a0,s1==a1);s2==a2'                         | and(or(eq('s0','a0'), eq('s1','a1')), eq('s2','a2'))
            '(s0==a0,s1=out=(a10,a11));s2==a2,s3==a3'        | or(and(or(eq('s0','a0'), out('s1','a10', 'a11')), eq('s2','a2')), eq('s3','a3'))
            '((s0==a0,s1==a1);s2==a2,s3==a3);s4==a4'         | and(or(and(or(eq('s0','a0'), eq('s1','a1')), eq('s2','a2')), eq('s3','a3')), eq('s4','a4'))
            '((s0==a0,s1=isnull=);s2==a2,s3=isnull=);s4==a4' | and(or(and(or(eq('s0','a0'), isnull('s1')), eq('s2','a2')), isnull('s3')), eq('s4','a4'))
            '(s0==a0)'                                       | eq('s0', 'a0')
            '((s0==a0));s1==a1'                              | and(eq('s0', 'a0'), eq('s1','a1'))
            '((s0==a0));s1=isnull='                          | and(eq('s0', 'a0'), isnull('s1'))
            '((s0==a0),s1=notnull=);s2=isnull='              | and(or(eq('s0','a0'), notnull('s1')), isnull('s2'))
    }

    def 'throw exception for unclosed parenthesis: #input'() {
        when:
            parse(input)
        then:
            thrown RSQLParserException
        where:
            input << [ '(s0==a0;s1!=a1', 's0==a0)', 's0==a;(s1=in=(b,c),s2!=d', "(s0=isnull=;s0==a0", "(s0=notnull=;s0==a0" ]
    }


    def 'use parser with custom set of operators'() {
        setup:
            def allOperator = new ComparisonOperator('=all=', true)
            def notOperator = new UnaryComparisonOperator('=not=')
            def parser = new RSQLParser([EQUAL, allOperator] as Set, [IS_NULL, notOperator] as Set)
            def expected = and(eq('name', 'TRON'), new ComparisonNode(allOperator, 'genres', ['sci-fi', 'thriller']))

        expect:
            parser.parse('name==TRON;genres=all=(sci-fi,thriller)') == expected

        when: 'unsupported operator used'
            parser.parse('name==TRON;year=ge=2010')
        then:
            def ex = thrown(RSQLParserException)
            ex.cause instanceof UnknownOperatorException
    }


    //////// Helpers ////////

    def parse(String rsql) { new RSQLParser().parse(rsql) }

    def and(Node... nodes) { new AndNode(nodes as List) }
    def or(Node... nodes) { new OrNode(nodes as List) }
    def eq(sel, arg) { new ComparisonNode(EQUAL, sel, [arg as String]) }
    def isnull(sel){ new UnaryComparisonNode(IS_NULL,sel) }
    def notnull(sel){ new UnaryComparisonNode(NOT_NULL, sel) }
    def out(sel, ...args) { new ComparisonNode(NOT_IN, sel, args as List) }
}
