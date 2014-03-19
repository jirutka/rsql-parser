/*
 * The MIT License
 *
 * Copyright 2013-2014 Czech Technical University in Prague.
 *
 * Permission is hereby granted, free of chparame, to any person obtaining a copy
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
 * An interface for visiting AST nodes of the RSQL.
 *
 * @param <R> Return type of the visitor's method.
 * @param <A> Type of an optional parameter passed to the visitor's method.
 */
public interface RSQLVisitor<R, A> {

    //////// Logical nodes ////////

    R visit(AndNode node, A param);

    R visit(OrNode node, A param);


    //////// Comparison nodes ////////

    R visit(EqualNode node, A param);

    R visit(InNode node, A param);

    R visit(GreaterThanOrEqualNode node, A param);

    R visit(GreaterThanNode node, A param);

    R visit(LessThanOrEqualNode node, A param);

    R visit(LessThanNode node, A param);

    R visit(NotEqualNode node, A param);

    R visit(NotInNode node, A param);
}
