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
