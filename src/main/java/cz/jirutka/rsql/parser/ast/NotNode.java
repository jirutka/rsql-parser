package cz.jirutka.rsql.parser.ast;

import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

/**
 * Node representing the logical NOT operation, which negates a single child node.
 */
@Immutable
public final class NotNode extends LogicalNode {

    /**
     * Creates a new NOT node with a single child.
     *
     * @param child The child node to negate; must not be <tt>null</tt>.
     */
    public NotNode(Node child) {
        super(LogicalOperator.NOT, Collections.singletonList(child));
    }

    /**
     * Returns a copy of this node with the specified child node.
     *
     * @param children Must contain exactly one child node.
     * @throws IllegalArgumentException If the list doesn't contain exactly one child.
     */
    @Override
    public NotNode withChildren(List<? extends Node> children) {
        if (children.size() != 1) {
            throw new IllegalArgumentException("NOT operator requires exactly one child");
        }
        return new NotNode(children.get(0));
    }

    /**
     * Returns the single child node being negated.
     */
    public Node getChild() {
        return getChildren().get(0);
    }

    public <R, A> R accept(RSQLVisitor<R, A> visitor, A param) {
        return visitor.visit(this, param);
    }

    @Override
    public String toString() {
        // NOT operator should be displayed before its operand
        return getOperator() + super.toString();
    }
}
