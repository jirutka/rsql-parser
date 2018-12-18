package cz.jirutka.rsql.parser.ast;

import static cz.jirutka.rsql.parser.ast.StringUtils.isBlank;

import java.util.regex.Pattern;
import net.jcip.annotations.Immutable;

@Immutable
public final class UnaryComparisonOperator {

    private static final Pattern SYMBOL_PATTERN = Pattern.compile("=[a-zA-Z]+=");

    private final String[] symbols;

    /**
     * @param symbols Textual representation of this operator (e.g. <tt><null></></tt>); the first item
     *          is primary representation, any others are alternatives. Must match
     *          <tt><[a-zA-Z]+></tt>.
     *
     * @throws IllegalArgumentException If the {@code symbols} is either <tt>null</tt>, empty,
     *          or contain illegal symbols.
     */
    public UnaryComparisonOperator(String[] symbols){
        Assert.notEmpty(symbols, "symbols must not be null or empty");
        for (String sym : symbols) {
            Assert.isTrue(isValidOperatorSymbol(sym), "symbol must match: %s", SYMBOL_PATTERN);
        }
        this.symbols = symbols.clone();
    }

    /**
     * @see #UnaryComparisonOperator(String[])
     */
    public UnaryComparisonOperator(String symbol) {
        this(new String[]{symbol});
    }

    /**
     * @see #UnaryComparisonOperator(String[])
     */
    public UnaryComparisonOperator(String symbol, String altSymbol) {
        this(new String[]{symbol, altSymbol});
    }

    /**
     * Returns the primary representation of this operator.
     */
    public String getSymbol() {
        return symbols[0];
    }

    /**
     * Returns all representations of this operator. The first item is always the primary
     * representation.
     */
    public String[] getSymbols() {
        return symbols.clone();
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
        if (o == null || getClass() != o.getClass()) return false;
        UnaryComparisonOperator that = (UnaryComparisonOperator) o;
        return getSymbol().equals(that.getSymbol());
    }

    @Override
    public int hashCode() {
        return getSymbol().hashCode();
    }
}
