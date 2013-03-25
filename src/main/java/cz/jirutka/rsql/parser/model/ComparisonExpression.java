/*
 * The MIT License
 *
 * Copyright 2013 Jakub Jirutka <jakub@jirutka.cz>.
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
package cz.jirutka.rsql.parser.model;

/**
 * Represents a comparison expression (constraint) which consists of triplet: 
 * selector - operator - argument. When processed, constraint yields Boolean 
 * value.
 * 
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class ComparisonExpression extends Expression {
    
    private final String selector;
    private final String argument;
    private final Comparison operator;

    
    /**
     * Construct a new Comparison expression.
     * 
     * @param selector identifies some element of an entry's content
     * @param operator comparison operator
     * @param argument
     */
    public ComparisonExpression(String selector, Comparison operator, String argument) {
        this.selector = selector;
        this.argument = argument;
        this.operator = operator;
    }

    
    /**
     * Get selector which identifies some element of an entry's content.
     * 
     * @return selector
     */
    public String getSelector() {
        return selector;
    }
    
    /**
     * @return argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * @return comparison operator
     */
    public Comparison getOperator() {
        return operator;
    }
    
    
    @Override
    public String toString() {
        return selector + " " + operator + " " + argument;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComparisonExpression other = (ComparisonExpression) obj;
        if ((this.selector == null) ? (other.selector != null) : !this.selector.equals(other.selector)) {
            return false;
        }
        if ((this.argument == null) ? (other.argument != null) : !this.argument.equals(other.argument)) {
            return false;
        }
        if (this.operator != other.operator) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.selector != null ? this.selector.hashCode() : 0);
        hash = 47 * hash + (this.argument != null ? this.argument.hashCode() : 0);
        hash = 47 * hash + (this.operator != null ? this.operator.hashCode() : 0);
        return hash;
    }
    
}
