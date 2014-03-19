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
import java.util.Iterator;
import java.util.List;

import static cz.jirutka.rsql.parser.ast.StringUtils.join;

/**
 * Superclass of all the logical nodes that represents a logical operation
 * that connects a children nodes.
 */
public abstract class LogicalNode extends AbstractNode implements Iterable<Node> {

    private final List<Node> children = new ArrayList<>();

    private final LogicalOp operator;


    protected LogicalNode(LogicalOp operator, List<? extends Node> children) {
        assert operator != null : "operator must not be null";
        assert children != null : "children must not be null";

        this.operator = operator;
        this.children.addAll(children);
    }


    public Iterator<Node> iterator() {
        return children.iterator();
    }

    public LogicalOp getOperator() {
        return operator;
    }


    @Override
    public String toString() {
        return "(" + join(children, operator.toString()) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalNode)) return false;

        LogicalNode nodes = (LogicalNode) o;
        if (!children.equals(nodes.children)) return false;
        if (operator != nodes.operator) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = children.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
}
