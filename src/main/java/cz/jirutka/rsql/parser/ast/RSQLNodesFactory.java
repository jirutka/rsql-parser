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

import cz.jirutka.rsql.parser.UnknownOperatorException;
import net.jcip.annotations.Immutable;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Factory that creates {@link Node} instances for the parser.
 *
 * <p>If you want to define custom operators, then extend this class and
 * override {@link #createComparisonNode(String, String, List) createComparisonNode()}
 * method.</p>
 */
@Immutable
public class RSQLNodesFactory {

    /**
     * Creates a specific {@link LogicalNode} instance for the specified
     * operator and with the given children nodes.
     *
     * @param operator The logical operator to create a node for.
     * @param children An arguments of the operation.
     * @return A subclass of the {@link LogicalNode} according to the
     *         specified operator.
     */
    public LogicalNode createLogicalNode(LogicalOp operator, List<Node> children) {
        switch (operator) {
            case AND : return new AndNode(children);
            case OR  : return new OrNode(children);

            // this normally can't happen
            default  : throw new IllegalStateException("Unknown operator: " + operator);
        }
    }

    /**
     * @see #createComparisonNode(String, String, List)
     */
    public ComparisonNode createComparisonNode(
            String operator, String selector, String argument) throws UnknownOperatorException {

        return createComparisonNode(operator, selector, asList(argument));
    }

    /**
     * Creates a specific {@link ComparisonNode} instance for the specified
     * operator and with the given selector and arguments.
     *
     * <p>If you want to define a custom FIQL-like operators (i.e.
     * <tt>=[a-z]*=</tt>, ex.: <tt>=foo=</tt>), then override this method,
     * handle your custom operators at top and then call {@code super} to
     * handle the built-in operators.</p>
     *
     * @param operator The comparison operator to create a node for.
     * @param selector The selector that specifies a left side of the comparison.
     * @param arguments A list of arguments that specifies a right side of the
     *                  comparison.
     * @return A subclass of the {@link ComparisonNode} according to the
     *         specified operator.
     * @throws UnknownOperatorException
     */
    public ComparisonNode createComparisonNode(
            String operator, String selector, List<String> arguments) throws UnknownOperatorException {

        switch (ComparisonOp.parse(operator)) {
            case EQ  : return new EqualNode(selector, arguments);
            case IN  : return new InNode(selector, arguments);
            case GE  : return new GreaterThanOrEqualNode(selector, arguments);
            case GT  : return new GreaterThanNode(selector, arguments);
            case LE  : return new LessThanOrEqualNode(selector, arguments);
            case LT  : return new LessThanNode(selector, arguments);
            case NE  : return new NotEqualNode(selector, arguments);
            case OUT : return new NotInNode(selector, arguments);

            // this normally can't happen, validation is done in ComparisonOp.parse()
            default  : throw new UnknownOperatorException("Unknown operator: " + operator);
        }
    }
}
