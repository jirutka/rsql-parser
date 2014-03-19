RSQL / FIQL parser
==================
[![Build Status](https://travis-ci.org/jirutka/rsql-parser.png)](https://travis-ci.org/jirutka/rsql-parser) [![Coverage Status](https://coveralls.io/repos/jirutka/rsql-parser/badge.png)](https://coveralls.io/r/jirutka/rsql-parser)

RSQL is a query language for parametrized filtering of entries in RESTful APIs. It’s based on
[FIQL](http://tools.ietf.org/html/draft-nottingham-atompub-fiql-00) (Feed Item Query Language) – an URI-friendly syntax
for expressing filters across the entries in an Atom Feed. FIQL is great for use in URI; there are no unsafe characters,
so URL encoding is not required. On the other side, FIQL’s syntax is not very intuitive and URL encoding isn’t always
that big deal, so RSQL also provides a friendlier syntax for logical operators and some of the comparison operators.

For example, you can query your resource like this: `/movies?query=name=="Kill Bill";year=gt=2003` or
`/movies?query=director.lastName==Nolan and year>=2000`. See [examples](#examples) below.

This is a complete and thoroughly tested parser for RSQL written in [JavaCC](http://javacc.java.net/) and Java. Since
RSQL is a superset of the FIQL, it can be used for parsing FIQL as well.

RSQL-parser can be used with [RSQL-hibernate](https://github.com/jirutka/rsql-hibernate) library to translate RSQL
expression to Hibernate’s Criteria query (it’s written for 1.x version of the parser though).


Grammar and semantic
--------------------
_The following grammar specification is written in EBNF notation ([ISO 14977])._

RSQL expression is composed of one or more comparisons, related to each other with logical operators:

* Logical AND : `;` or ` and `
* Logical OR : `,` or ` or `

By default, the AND operator takes precedence (i.e. it’s evaluated before any OR operators are). However, a
parenthesized expression can be used to change the precedence, yielding whatever the contained expression yields.

    input          = or, EOF;
    or             = and, { "," , and };
    and            = constraint, { ";" , constraint };
    constraint     = ( group | comparison );
    group          = "(", or, ")";

Comparison is composed of a selector, an operator and an argument.

    comparison     = selector, comparison-op, arguments;

Selector identifies a field (or attribute, element, …) of the resource representation to filter by. It can be any non
empty Unicode string that doesn’t contain reserved characters (see below) or a white space. The specific syntax of the
selector is not enforced by this parser.

    selector       = unreserved-str;

Comparison operators are in FIQL notation and some of them has an alternative syntax as well:

* Equal to : `==`
* Not equal to : `!=`
* Less than : `=lt=` or `<`
* Less than or equal to : `=le=` or `<=`
* Greater than operator : `=gt=` or `>`
* Greater than or equal to : `=ge=` or `>=`
* In : `=in=`
* Not in : `=out=`

You can also simply extend this parser with your own operators (see the [next section](#how-to-add-custom-operators)).

    comparison-op  = comp-fiql | comp-alt;
    comp-fiql      = ( ( "=", { ALPHA } ) | "!" ), "=";
    comp-alt       = ( ">" | "<" ), [ "=" ];

Argument can be a single value, or multiple values in parenthesis separated by comma. Value that doesn’t contain any
reserved character or a white space can be unquoted, other arguments must be enclosed in single or double quotes.

    arguments      = ( "(", value, { "," , value }, ")" ) | value;
    value          = unreserved-str | double-quoted | single-quoted;

    unreserved-str = unreserved, { unreserved }
    single-quoted  = "'", { all-chars - "'" }, "'";
    double-quoted  = '"', { all-chars - '"' }, '"';

    reserved       = '"' | "'" | "(" | ")" | ";" | "," | "=" | "!" | "~" | "<" | ">";
    unreserved     = all-chars - reserved - " ";
    all-chars      = ? all unicode characters ?;


Examples
--------

Examples of RSQL expressions in both FIQL-like and alternative notation:

    - name=="Kill Bill";year=gt=2003
    - name=="Kill Bill" and year>2003
    - genres=in=(sci-fi,action);(director=='Christopher Nolan',actor==*Bale);year=ge=2000
    - genres=in=(sci-fi,action) and (director=='Christopher Nolan' or actor==*Bale) and year>=2000
    - director.lastName==Nolan;year=ge=2000;year=lt=2010
    - director.lastName==Nolan and year>=2000 and year<2010
    - genres=in=(sci-fi,action);genres=out=(romance,animated,horror),director==Que*Tarantino
    - genres=in=(sci-fi,action) and genres=out=(romance,animated,horror) or director==Que*Tarantino


How to use
----------

Nodes are [visitable](http://en.wikipedia.org/wiki/Visitor_pattern), so to traverse the parsed AST (and convert it to
SQL query maybe), you can implement the provided [RSQLVisitor] interface or one of its adapters
([NoArgRSQLVisitorAdapter], [SuperNodesRSQLVisitorAdapter]).

```java
Node rootNode = new RSQLParser().parse("name==RSQL;version=ge=2.0");

rootNode.accept(yourShinyVisitor);
```


How to add custom operators
---------------------------

Need more operators? The parser can be simply enhanced by custom FIQL-like comparison
operators, so you can add your own. Just write an AST nodes for your operators (extend [ComparisonNode]), then extend
[RSQLNodesFactory] class and [RSQLVisitor] interface.

Take a look at the [CustomOperatorsTest] for an example.


Maven
-----

Released versions are available in The Central Repository. Just add this artifact to your project:

```xml
<dependency>
    <groupId>cz.jirutka.rsql</groupId>
    <artifactId>rsql-parser</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>
```

However if you want to use the last snapshot version, you have to add the Sonatype OSS repository:

```xml
<repository>
    <id>sonatype-snapshots</id>
    <name>Sonatype repository for deploying snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```


License
-------

This project is licensed under [MIT license](http://opensource.org/licenses/MIT).


[ISO 14977]: http://www.cl.cam.ac.uk/~mgk25/iso-14977.pdf
[ComparisonNode]: src/main/java/cz/jirutka/rsql/parser/ast/ComparisonNode.java
[RSQLNodesFactory]: src/main/java/cz/jirutka/rsql/parser/ast/RSQLNodesFactory.java
[RSQLVisitor]: src/main/java/cz/jirutka/rsql/parser/ast/RSQLVisitor.java
[NoArgRSQLVisitorAdapter]: src/main/java/cz/jirutka/rsql/parser/ast/NoArgRSQLVisitorAdapter.java
[SuperNodesRSQLVisitorAdapter]: src/main/java/cz/jirutka/rsql/parser/ast/SuperNodesRSQLVisitorAdapter.java
[CustomOperatorsTest]: src/test/groovy/cz/jirutka/rsql/parser/CustomOperatorsTest.groovy
