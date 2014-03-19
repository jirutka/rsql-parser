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

/**
 * An abstract implementation of the {@link RSQLVisitor} interface that
 * delegates handling of all the concrete nodes to {@link #visit(LogicalNode)}
 * and {@link #visit(ComparisonNode)}.
 *
 * <p>This class is useful when you don't want to handle all of the nodes (i.e.
 * implement <tt>visit()</tt> methods), but just few of them and take care of
 * the rest in the superclass nodes' handler.
 *
 * @param <T> Return type of the visitor's method.
 */
public abstract class AbstractSuperNodesVisitor<T> implements RSQLVisitor<T> {

    //////// Superclass nodes ////////

    public abstract T visit(LogicalNode node);

    public abstract T visit(ComparisonNode node);


    //////// Logical nodes ////////

    @Override public T visit(AndNode node) {
        return visit((LogicalNode) node);
    }

    @Override public T visit(OrNode node) {
        return visit((LogicalNode) node);
    }


    //////// Comparison nodes ////////

    @Override public T visit(EqualNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(InNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(GreaterThanOrEqualNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(GreaterThanNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(LessThanOrEqualNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(LessThanNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(NotEqualNode node) {
        return visit((ComparisonNode) node);
    }

    @Override public T visit(NotInNode node) {
        return visit((ComparisonNode) node);
    }
}
