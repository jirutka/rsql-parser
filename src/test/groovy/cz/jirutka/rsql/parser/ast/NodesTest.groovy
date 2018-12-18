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

import cz.jirutka.rsql.parser.RSQLParser
import spock.lang.Specification

import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL
import static cz.jirutka.rsql.parser.ast.RSQLOperators.IS_NULL

class NodesTest extends Specification {

    def 'nodes should define toString method'() {
        setup:
            def rootNode = new RSQLParser().parse(input)
        expect:
            rootNode.toString() == output
        where:
            input                            | output
            'genres=in=(sci-fi,action)'      | "genres=in=('sci-fi','action')"
            'name=="Kill Bill";year=gt=2003' | "(name=='Kill Bill';year=gt='2003')"
            'a<=1;b!=2;c>3'                  | "(a=le='1';b!='2';c=gt='3')"
            'a=gt=1,b==2;c!=3,d=lt=4'        | "(a=gt='1',(b=='2';c!='3'),d=lt='4')"
            'a=isnull=;b==2,c!=4'             | "((a=isnull=;b=='2'),c!='4')"
    }

    def 'nodes should accept visitor'() {
        setup:
            def visitor = Mock(RSQLVisitor)
            def param = Stub(Object)
            def expected = Stub(Object)

        when: 'accept method with parameter'
            def result = node.accept(visitor, param)
        then:
            1 * visitor.visit({ it.class == node.class }, param) >> expected
            0 * visitor._
        and:
            result == expected

        when: 'accept method without parameter'
            result = node.accept(visitor)
        then:
            1 * visitor.visit({ it.class == node.class }, null) >> expected
            0 * visitor._
        and:
            result == expected
        where:
            node << [new AndNode([]), new OrNode([]), new ComparisonNode(EQUAL, 'x', ['y']), new UnaryComparisonNode(IS_NULL,'x')]
    }
}
