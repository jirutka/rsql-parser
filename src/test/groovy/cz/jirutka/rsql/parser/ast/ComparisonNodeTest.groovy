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
package cz.jirutka.rsql.parser.ast

import spock.lang.Specification

import static cz.jirutka.rsql.parser.ast.RSQLOperators.*

class ComparisonNodeTest extends Specification {

    def 'throw exception when given multiple arguments for single-argument operator'() {
        when:
            new ComparisonNode(operator, 'sel', ['arg1', 'arg2'])
        then:
            thrown IllegalArgumentException
        where:
            operator << defaultComparisonOperators().findAll{ !it.multiValue }
    }

    def 'should be immutable'() {
        given:
            def args = ['thriller', 'sci-fi']
            def node = new ComparisonNode(IN, 'genres', args)

        when: "modify list of node's arguments"
            node.getArguments() << 'horror'
        then: "node's arguments remain unchanged"
            node.getArguments() == args

        expect: "withX returns copy and doesn't change original node"
            node.withOperator(NOT_IN)   == new ComparisonNode(NOT_IN, 'genres', args)
            node.withSelector('foo')    == new ComparisonNode(IN, 'foo', args)
            node.withArguments(['foo']) == new ComparisonNode(IN, 'genres', ['foo'])
            node == new ComparisonNode(IN, 'genres', args)

        when: 'modify original list of arguments given to node'
            args << 'horror'
        then: "node's arguments remains unchanged"
            node.getArguments() == ['thriller', 'sci-fi']
    }
}
