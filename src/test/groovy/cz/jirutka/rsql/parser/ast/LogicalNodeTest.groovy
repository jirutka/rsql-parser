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
import spock.lang.Unroll

import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL

@Unroll
abstract class LogicalNodeTest extends Specification {

    abstract LogicalNode newNode(List children)


    def 'should be immutable'() {
        given:
            def child1 = new ComparisonNode(EQUAL, 'foo', ['bar'])
            def child2 = new AndNode([])
            def children = [child1, child2]
            def node = newNode(children)

        when:
            def it = node.iterator()
            it.next()
            it.remove()
        then:
            thrown UnsupportedOperationException

        expect: "withChildren() returns copy and doesn't change original node"
            node.withChildren([child1]) == newNode([child1])
            node == newNode(children)

        when: 'modify original list of children given to node'
            children << new OrNode([child1])
        then: "node's children remains unchanged"
            node.children == [child1, child2]
    }
}

class AndNodeTest extends LogicalNodeTest {
    LogicalNode newNode(List children) {
        new AndNode(children)
    }
}

class OrNodeTest extends LogicalNodeTest {
    LogicalNode newNode(List children) {
        new OrNode(children)
    }
}
