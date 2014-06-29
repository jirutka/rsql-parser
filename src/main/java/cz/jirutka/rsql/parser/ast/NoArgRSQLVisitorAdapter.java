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

/**
 * An adapter for the {@link RSQLVisitor} interface with a simpler contract
 * that omits an optional second argument.
 *
 * @param <R> Return type of the visitor's method.
 */
public abstract class NoArgRSQLVisitorAdapter<R> implements RSQLVisitor<R, Void> {


    //////// Logical nodes ////////

    public abstract R visit(AndNode node);

    public abstract R visit(OrNode node);


    //////// Comparison nodes ////////

    public abstract R visit(EqualNode node);

    public abstract R visit(InNode node);

    public abstract R visit(GreaterThanOrEqualNode node);

    public abstract R visit(GreaterThanNode node);

    public abstract R visit(LessThanOrEqualNode node);

    public abstract R visit(LessThanNode node);

    public abstract R visit(NotEqualNode node);

    public abstract R visit(NotInNode node);



    //////// Delegates ////////

    @Override public R visit(AndNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(OrNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(EqualNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(InNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(GreaterThanOrEqualNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(GreaterThanNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(LessThanOrEqualNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(LessThanNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(NotEqualNode node, Void param) {
        return visit(node);
    }

    @Override public R visit(NotInNode node, Void param) {
        return visit(node);
    }
}
