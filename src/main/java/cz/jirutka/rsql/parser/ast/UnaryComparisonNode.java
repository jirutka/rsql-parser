package cz.jirutka.rsql.parser.ast;

import java.util.Objects;

public class UnaryComparisonNode extends AbstractNode {

    private final UnaryComparisonOperator operator;
    private final String selector;

    /**
     * @param operator Must not be <tt>null</tt>.
     * @param selector ust not be <tt>null</tt> or blank.
     *
     * @throws IllegalArgumentException If one of the conditions specified above it not met.
     */
    public UnaryComparisonNode(UnaryComparisonOperator operator, String selector){
        Assert.notNull(operator, "operator must not be null");
        Assert.notBlank(selector, "selector must not be blank");

        this.operator = operator;
        this.selector = selector;
    }

    public <R, A> R accept(RSQLVisitor<R, A> visitor, A param) {
        return visitor.visit(this, param);
    }

    public UnaryComparisonOperator getOperator(){
        return operator;
    }

    /**
     * Returns a copy of this node with the specified operator.
     *
     * @param newOperator Must not be <tt>null</tt>.
     */
    public UnaryComparisonNode withOperator(UnaryComparisonOperator newOperator) {
        return new UnaryComparisonNode(newOperator, selector);
    }

    public String getSelector() {
        return selector;
    }

    /**
     * Returns a copy of this node with the specified selector.
     *
     * @param newSelector Must not be <tt>null</tt> or blank.
     */
    public UnaryComparisonNode withSelector(String newSelector) {
        return new UnaryComparisonNode(operator, newSelector);
    }

    @Override
    public String toString() {
        return selector + operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnaryComparisonNode unaryNode = (UnaryComparisonNode) o;
        return Objects.equals(operator, unaryNode.operator) &&
            Objects.equals(selector, unaryNode.selector);
    }

    @Override
    public int hashCode() {
        int result = selector.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
}
