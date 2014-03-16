/*
 * The MIT License
 *
 * Copyright 2013-2014 Czech Technical University in Prague.
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
package cz.jirutka.rsql.parser;

import cz.jirutka.rsql.parser.ast.Node;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Parser of the RSQL (RESTful Service Query Language).
 *
 * <p>RSQL is query language for a parametrized filtering of entries in
 * RESTful APIs. It's a superset of
 * <a href="http://tools.ietf.org/html/draft-nottingham-atompub-fiql-00">
 * FIQL</a> (Feed Item Query Language), so it can be used for parsing FIQL
 * as well.</p>
 *
 * <p><b>Grammar in EBNF notation:</b>
 * <pre>
 * input          = or, EOF;
 * or             = and, { "," , and };
 * and            = constraint, { ";" , constraint };
 * constraint     = ( group | comparison );
 * group          = "(", or, ")";
 *
 * comparison     = selector, comparator, arguments;
 * selector       = unreserved-str;
 *
 * comparator     = comp-fiql | comp-alt;
 * comp-fiql      = ( ( "=", { ALPHA } ) | "!" ), "=";
 * comp-alt       = ( ">" | "<" ), [ "=" ];
 *
 * arguments      = ( "(", value, { "," , value }, ")" ) | value;
 * value          = unreserved-str | double-quoted | single-quoted;
 *
 * unreserved-str = unreserved, { unreserved }
 * single-quoted  = "'", { all-chars - "'" }, "'";
 * double-quoted  = '"', { all-chars - '"' }, '"';
 *
 * reserved       = '"' | "'" | "(" | ")" | ";" | "," | "=" | "!" | "~" | "<" | ">" | " ";
 * unreserved     = all-chars - reserved;
 * all-chars      = ? all unicode characters ?;
 * </pre></p>
 *
 * @version 2.0
 */
public final class RSQLParser {

    private static final Charset ENCODING = Charset.forName("UTF-8");

    private RSQLParser() {}

    /**
     * Parses the RSQL2 expression and returns AST.
     *
     * @param query The query expression to parse.
     * @return A root of the AST.
     * @throws RSQLParserException This exception wraps {@link ParseException}
     *         and {@link TokenMgrError}.
     */
    public static Node parse(String query) throws RSQLParserException {
        if (query == null) {
            throw new IllegalArgumentException("query must not be null");
        }
        InputStream is = new ByteArrayInputStream(query.getBytes(ENCODING));
        Parser parser = new Parser(is, ENCODING.name());

        try {
            return parser.Input();

        } catch (ParseException | TokenMgrError ex) {
            throw new RSQLParserException(ex);
        }
    }
}
