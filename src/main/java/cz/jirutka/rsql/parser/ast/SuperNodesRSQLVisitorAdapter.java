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
 * An adapter for the {@link RSQLVisitor} interface that delegates handling of
 * all the concrete nodes to {@link #visit(LogicalNode, A)} and
 * {@link #visit(ComparisonNode, A)}.
 *
 * <p>This class is useful when you don't want to handle all of the nodes (i.e.
 * implement <tt>visit()</tt> methods), but just few of them and take care of
 * the rest in the superclass nodes' handler.
 *
 * @param <R> Return type of the visitor's method.
 * @param <A> Type of an optional parameter passed to the visitor's method.
 */
public abstract class SuperNodesRSQLVisitorAdapter<R, A> implements RSQLVisitor<R, A> {

    //////// Superclass nodes ////////

    public abstract R visit(LogicalNode node, A param);

    public abstract R visit(ComparisonNode node, A param);


    //////// Logical nodes ////////

    @Override public R visit(AndNode node, A param) {
        return visit((LogicalNode) node, param);
    }

    @Override public R visit(OrNode node, A param) {
        return visit((LogicalNode) node, param);
    }


    //////// Comparison nodes ////////

    @Override public R visit(EqualNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(InNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(GreaterThanOrEqualNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(GreaterThanNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(LessThanOrEqualNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(LessThanNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(NotEqualNode node, A param) {
        return visit((ComparisonNode) node, param);
    }

    @Override public R visit(NotInNode node, A param) {
        return visit((ComparisonNode) node, param);
    }
}
