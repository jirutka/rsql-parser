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
package cz.jirutka.rsql.parser.ast;

import java.util.ArrayList;
import java.util.List;

import static cz.jirutka.rsql.parser.ast.StringUtils.isNotBlank;
import static cz.jirutka.rsql.parser.ast.StringUtils.join;

/**
 * Superclass of all the comparison nodes that represents a specific comparison
 * operator, a selector and an arguments.
 */
public abstract class ComparisonNode implements Node {

    private final String selector;

    private final List<String> arguments;


    protected ComparisonNode(String selector, List<String> arguments) {
        assert isNotBlank(selector) : "selector must not be blank";
        assert arguments.size() > 0 : "arguments list must not be empty";

        this.selector = selector;
        this.arguments = arguments;
    }


    /**
     * Returns a operator that is represented by this node (ex.: =gt=, ==, ...).
     * Must not return null or empty string!
     */
    public abstract String getOperator();

    public String getSelector() {
        return selector;
    }

    /**
     * Return a copy of the arguments list.
     */
    public List<String> getArguments() {
        return new ArrayList<>(arguments);
    }


    @Override
    public String toString() {
        String args = arguments.size() > 1
                ? "('" + join(arguments, "','") + "')"
                : "'" + arguments.get(0) + "'";
        return selector + getOperator() + args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparisonNode)) return false;

        ComparisonNode that = (ComparisonNode) o;

        if (!arguments.equals(that.arguments)) return false;
        if (!getOperator().equals(getOperator())) return false;
        if (!selector.equals(that.selector)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = selector.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + getOperator().hashCode();
        return result;
    }
}
