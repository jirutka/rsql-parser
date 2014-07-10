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
package cz.jirutka.rsql.parser.ast;

import java.util.ArrayList;
import java.util.List;

import static cz.jirutka.rsql.parser.ast.StringUtils.join;

/**
 * Superclass of all the comparison nodes that represents a specific comparison
 * operator, a selector and an arguments.
 */
public final class ComparisonNode extends AbstractNode {

    private final ComparisonOperator operator;

    private final String selector;

    private final List<String> arguments;


    public ComparisonNode(ComparisonOperator operator, String selector, List<String> arguments) {
        Assert.notNull(operator, "operator must not be null");
        Assert.notBlank(selector, "selector must not be blank");
        Assert.notEmpty(arguments, "arguments list must not be empty");
        Assert.isTrue(operator.isMultiValue() || arguments.size() == 1,
                "operator %s expects single argument, but multiple values given", operator);

        this.operator = operator;
        this.selector = selector;
        this.arguments = new ArrayList<>(arguments);
    }


    public <R, A> R accept(RSQLVisitor<R, A> visitor, A param) {
        return visitor.visit(this, param);
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public ComparisonNode withOperator(ComparisonOperator newOperator) {
        return new ComparisonNode(newOperator, selector, arguments);
    }

    public String getSelector() {
        return selector;
    }

    public ComparisonNode withSelector(String newSelector) {
        return new ComparisonNode(operator, newSelector, arguments);
    }

    public List<String> getArguments() {
        return new ArrayList<>(arguments);
    }

    public ComparisonNode withArguments(List<String> newArguments) {
        return new ComparisonNode(operator, selector, newArguments);
    }


    @Override
    public String toString() {
        String args = arguments.size() > 1
                ? "('" + join(arguments, "','") + "')"
                : "'" + arguments.get(0) + "'";
        return selector + operator + args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparisonNode)) return false;

        ComparisonNode that = (ComparisonNode) o;

        if (!arguments.equals(that.arguments)) return false;
        if (!operator.equals(that.operator)) return false;
        if (!selector.equals(that.selector)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = selector.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
}
