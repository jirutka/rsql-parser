RSQL / FIQL parser
==================
[![Build Status](https://travis-ci.org/jirutka/rsql-parser.svg)](https://travis-ci.org/jirutka/rsql-parser)
[![Coverage Status](https://img.shields.io/coveralls/jirutka/rsql-parser/master.svg)](https://coveralls.io/r/jirutka/rsql-parser?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)

RSQL is a query language for parametrized filtering of entries in RESTful APIs. It’s based on
[FIQL](http://tools.ietf.org/html/draft-nottingham-atompub-fiql-00) (Feed Item Query Language) – an URI-friendly syntax
for expressing filters across the entries in an Atom Feed. FIQL is great for use in URI; there are no unsafe characters,
so URL encoding is not required. On the other side, FIQL’s syntax is not very intuitive and URL encoding isn’t always
that big deal, so RSQL also provides a friendlier syntax for logical operators and some of the comparison operators.

For example, you can query your resource like this: `/movies?query=name=="Kill Bill";year=gt=2003` or
`/movies?query=director.lastName==Nolan and year>=2000`. See [examples](#examples) below.

This is a complete and thoroughly tested parser for RSQL written in [JavaCC](http://javacc.java.net/) and Java. Since
RSQL is a superset of the FIQL, it can be used for parsing FIQL as well.


Related libraries
-----------------

RSQL-parser can be used with:

* [rsql-jpa](https://github.com/tennaito/rsql-jpa) to convert RSQL into JPA2 CriteriaQuery,
* [rsql-mongodb](https://github.com/RutledgePaulV/rsql-mongodb) to convert RSQL into MongoDB query using Spring Data MongoDB,
* [q-builders](https://github.com/RutledgePaulV/q-builders) to build (not only) RSQL query in type-safe manner,
* _your own library…_

It’s very easy to write a converter for RSQL using its AST. Take a look at very simple and naive converter to JPA2 in
less than 100 lines of code [here](https://gist.github.com/jirutka/42a0f9bfea280b3c5dca). You may also read a [blog
article about RSQL](http://www.baeldung.com/rest-api-search-language-rsql-fiql) by
[Eugen Paraschiv](https://github.com/eugenp).


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
SQL query maybe), you can implement the provided [RSQLVisitor] interface or simplified [NoArgRSQLVisitorAdapter].

```java
Node rootNode = new RSQLParser().parse("name==RSQL;version=ge=2.0");

rootNode.accept(yourShinyVisitor);
```


How to add custom operators
---------------------------

Need more operators? The parser can be simply enhanced by custom FIQL-like comparison operators,
so you can add your own.

```java
Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
operators.add(new ComparisonOperator("=all=", true));

Node rootNode = new RSQLParser(operators).parse("genres=all=('thriller','sci-fi')");
```

Maven
-----

Released versions are available in The Central Repository. Just add this artifact to your project:

```xml
<dependency>
    <groupId>cz.jirutka.rsql</groupId>
    <artifactId>rsql-parser</artifactId>
    <version>2.0.0</version>
</dependency>
```

However if you want to use the last snapshot version, you have to add the Sonatype OSS repository:

```xml
<repository>
    <id>jfrog-oss-snapshot-local</id>
    <name>JFrog OSS repository for snapshots</name>
    <url>https://oss.jfrog.org/oss-snapshot-local</url>
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
[RSQLVisitor]: src/main/java/cz/jirutka/rsql/parser/ast/RSQLVisitor.java
[NoArgRSQLVisitorAdapter]: src/main/java/cz/jirutka/rsql/parser/ast/NoArgRSQLVisitorAdapter.java
