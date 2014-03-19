/*
 * The MIT License
 *
 * Copyright 2013-2014 Czech Technical University in Prague.
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

class CustomOperatorsTest extends Specification {


    def 'ensure that parser can be enhanced by custom comparison operator'() {

        setup: 'instantiate parser with our RSQLNodesFactory that handles =foo='
            def parser = new RSQLParser(new CustomRSQLNodesFactory())

        and: 'spy calls to our visitor'
            def visitor = Spy(AbstractCustomRSQLVisitor)

        when: 'parse expression with our custom =foo= operator'
            def rootNode = parser.parse('name==rage;state=foo=FUUuu')

        then: 'expression is parsed to AST'
            rootNode.toString() == "(name=='rage';state=foo='FUUuu')"

        when: 'pass visitor to root node'
            rootNode.accept(visitor)

        then: 'visitor is called for AND and =='
            1 * visitor.visit(_ as AndNode, _)
            1 * visitor.visit(_ as EqualNode, _) >> null

        and: 'most importantly for our =foo= comparison!'
            1 * visitor.visit(_ as FooNode, _) >> null

    }


    static class FooNode extends ComparisonNode {

        protected FooNode(String selector, List<String> arguments) {
            super(selector, arguments)
        }
        public String getOperator() {
            return '=foo='
        }
        public <R, A> R accept(RSQLVisitor<R, A> visitor, A param) {
            return ((CustomRSQLVisitor<R, A>) visitor).visit(this, param)
        }
    }

    static interface CustomRSQLVisitor<R, A> extends RSQLVisitor<R, A> {

        R visit(FooNode node, A param)
    }

    static class CustomRSQLNodesFactory extends RSQLNodesFactory {

        ComparisonNode createComparisonNode(String operator, String selector, List<String> arguments) {
            switch (operator) {
                case '=foo=' : return new FooNode(selector, arguments)
                default      : return super.createComparisonNode(operator, selector, arguments)
            }
        }
    }


    static abstract class AbstractCustomRSQLVisitor implements CustomRSQLVisitor<Void, Void> {

        Void visit(AndNode node, Void param) {
            node.each { child -> child.accept(this, param) }
            return null
        }
        Void visit(OrNode node, Void param) {
            node.each { child -> child.accept(this, param) }
            return null
        }
    }
}
