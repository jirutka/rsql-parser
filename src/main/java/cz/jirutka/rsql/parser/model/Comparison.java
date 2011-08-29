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
 * Comparison operator which is used in {@link ComparisonExpression}.
 * 
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public enum Comparison {

    /** Equal to : "==" or "=" */
    EQUAL ("="),
    
    /** Not equal to : "!=" */
    NOT_EQUAL ("!="),
    
    /** Greater than operator : "=gt=" or ">" */
    GREATER_THAN (">"),
    
    /** Greater than or equal to: "=ge=" or ">=" */
    GREATER_EQUAL (">="),
    
    /** Less than : "=lt=" or "<" */
    LESS_THAN ("<"),
    
    /** Less than or equal to : "=le=" or "<=" */
    LESS_EQUAL ("<=");

    
    private final String symbol;
    
    private Comparison(String symbol) {
        this.symbol = symbol;
    }

    
    @Override
    public String toString() {
        return symbol;
    }
    
}
