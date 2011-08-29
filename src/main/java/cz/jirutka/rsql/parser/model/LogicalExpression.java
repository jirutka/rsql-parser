/*
 * Copyright (c) 2011 Jakub Jirutka <jakub@jirutka.cz>
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the  GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
