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
 * Represents a logical expression which is composed of two 
 * {@link Expression expressions} ({@link ComparisonExpression comparison} or
 * another {@link Logical logical}).
 * 
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class LogicalExpression extends Expression {
    
    private final Expression left;
    private final Expression right;
    private final Logical operator;


    /**
     * Construct a new Logical expression.
     * 
     * @param left left operand
     * @param operator logical operator
     * @param right right operand
     */
    public LogicalExpression(Expression left, Logical operator, Expression right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
    

    /**
     * @return left operand
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * @return right operand
     */
    public Expression getRight() {
        return right;
    }
    
    /**
     * @return logical operator
     */
    public Logical getOperator() {
        return operator;
    }

    
    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LogicalExpression other = (LogicalExpression) obj;
        if (this.left != other.left && (this.left == null || !this.left.equals(other.left))) {
            return false;
        }
        if (this.right != other.right && (this.right == null || !this.right.equals(other.right))) {
            return false;
        }
        if (this.operator != other.operator) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.left != null ? this.left.hashCode() : 0);
        hash = 29 * hash + (this.right != null ? this.right.hashCode() : 0);
        hash = 29 * hash + (this.operator != null ? this.operator.hashCode() : 0);
        return hash;
    }
    
}
