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

import net.jcip.annotations.Immutable;

/**
 * Common interface of the AST nodes.
 */
@Immutable
public interface Node {

    /**
     * Accepts the visitor, calls its visit() method and returns the result.
     *
     * <p>Each implementation must implement this methods exactly as listed:
     * <pre>{@code
     * public <R, A> R accept(RSQLVisitor<R, A> visitor, A param) {
     *     return visitor.visit(this, param);
     * }
     * }</pre>
     *
     * @param visitor The visitor whose appropriate method will be called.
     * @param param An optional parameter to pass to the visitor.
     * @param <R> Return type of the visitor's method.
     * @param <A> Type of an optional parameter passed to the visitor's method.
     * @return An object returned by the visitor (may be null).
     */
    <R, A> R accept(RSQLVisitor<R, A> visitor, A param);

    /**
     * Accepts the visitor, calls its visit() method and returns the result.
     *
     * This method should just call {@link #accept(RSQLVisitor, Object)} with
     * <tt>null</tt> as the second argument.
     */
    <R, A> R accept(RSQLVisitor<R, A> visitor);
}
