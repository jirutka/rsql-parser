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

import java.util.regex.Pattern;

import static cz.jirutka.rsql.parser.ast.StringUtils.isBlank;

@Immutable
public final class ComparisonOperator {

    private static final Pattern SYMBOL_PATTERN = Pattern.compile("=[a-zA-Z]*=|[><]=?|!=");

    private final String[] symbols;

    private final boolean multiValue;


    /**
     * @param symbols    Textual representation of this operator (e.g. <tt>=gt=</tt>); the first item
     *                   is primary representation, any others are alternatives. Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param multiValue Whether this operator may be used with multiple arguments. This is then
     *                   validated in {@link NodesFactory}.
     * @throws IllegalArgumentException If the {@code symbols} is either <tt>null</tt>, empty,
     *                                  or contain illegal symbols.
     */
    public ComparisonOperator(String[] symbols, boolean multiValue) {
        Assert.notEmpty(symbols, "symbols must not be null or empty");
        for (String sym : symbols) {
            Assert.isTrue(isValidOperatorSymbol(sym), "symbol must match: %s", SYMBOL_PATTERN);
        }
        this.multiValue = multiValue;
        this.symbols = symbols.clone();
    }

    /**
     * @param symbol     Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param multiValue Whether this operator may be used with multiple arguments. This is then
     *                   validated in {@link NodesFactory}.
     * @see #ComparisonOperator(String[], boolean)
     */
    public ComparisonOperator(String symbol, boolean multiValue) {
        this(new String[]{symbol}, multiValue);
    }

    /**
     * @param symbol     Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param altSymbol  Alternative representation for {@code symbol}.
     * @param multiValue Whether this operator may be used with multiple arguments. This is then
     * @see #ComparisonOperator(String[], boolean)
     */
    public ComparisonOperator(String symbol, String altSymbol, boolean multiValue) {
        this(new String[]{symbol, altSymbol}, multiValue);
    }

    /**
     * @param symbols Textual representation of this operator (e.g. <tt>=gt=</tt>); the first item
     *                is primary representation, any others are alternatives. Must match {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @see #ComparisonOperator(String[], boolean)
     */
    public ComparisonOperator(String... symbols) {
        this(symbols, false);
    }


    /**
     * Returns the primary representation of this operator.
     *
     * @return the primary representation of this operator.
     */
    public String getSymbol() {
        return symbols[0];
    }

    /**
     * Returns all representations of this operator. The first item is always the primary
     * representation.
     *
     * @return all representations of this operator. The first item is always the primary representation.
     */
    public String[] getSymbols() {
        return symbols.clone();
    }

    /**
     * Whether this operator may be used with multiple arguments.
     *
     * @return Whether this operator may be used with multiple arguments.
     */
    public boolean isMultiValue() {
        return multiValue;
    }


    /**
     * Whether the given string can represent an operator.
     * Note: Allowed symbols are limited by the RSQL syntax (i.e. parser).
     */
    private boolean isValidOperatorSymbol(String str) {
        return !isBlank(str) && SYMBOL_PATTERN.matcher(str).matches();
    }


    @Override
    public String toString() {
        return getSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparisonOperator)) return false;

        ComparisonOperator that = (ComparisonOperator) o;
        return getSymbol().equals(that.getSymbol());
    }

    @Override
    public int hashCode() {
        return getSymbol().hashCode();
    }
}
