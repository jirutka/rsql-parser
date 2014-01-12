RSQL / FIQL parser [![Build Status](https://travis-ci.org/jirutka/rsql-parser.png)](https://travis-ci.org/jirutka/rsql-parser)
==================

RESTful Service Query Language (RSQL) is my language and library that is designed for searching entries in RESTful services. RSQL is based on URI-friendly syntax [FIQL](http://tools.ietf.org/html/draft-nottingham-atompub-fiql-00) (Feed Item Query Language) - IETF Internet-Draft of syntax for expressing filters across the entries in an Atom Feed. FIQL is great for use in URL because it doesn’t contain unsafe characters, so URL encoding isn’t necessary. On the other side, it’s not very intuitive and URL encoding isn’t always that big deal, so RSQL provides also more common alternative syntax.

This is complete parser for RSQL written in [JavaCC](http://javacc.java.net/) and Java. Since RSQL is based on [FIQL](http://tools.ietf.org/html/draft-nottingham-atompub-fiql-00), it can be used for FIQL as well. RSQL-parser is related with [RSQL-hibernate](https://github.com/jirutka/rsql-hibernate) - library for translating RSQL expression to Hibernate’s Criteria query.


Grammar and semantic
--------------------

### Expression

Expression is composed of one or more constraints, related to each other with Boolean operators.

    expression = [ "(" ],
                 ( constraint | expression ),
                 [ logical-operator, ( constraint | expression ) ],
                 [ ")" ];

Logical operators are:

* AND : `;` or ` and `
* OR : `,` or ` or `

<!-- -->
    logical-operator = ";" | " and " | "," | " or ";

By default, the AND operator takes precedence (i.e., it is evaluated before any OR operators are). However, a parenthesized expression can be used to change precedence, yielding whatever the contained expression yields.


### Constraint

A constraint is composed of a selector, which identifies an entry’s element, and a comparison/argument pair, which refines the constraint.

    constraint = selector, comparison-operator, argument;

Comparison operators are:

* Equal to : `==` or `=`
* Not equal to : `!=`
* Less than : `=lt=` or `<`
* Less than or equal to : `=le=` or `<=`
* Greater than operator : `=gt=` or `>`
* Greater than or equal to: `=ge=` or `>=`

<!-- -->
    comparison-operator = "==" | "=" | "!=" | "=lt=" | "<" | "=le=" | "<=" | "=gt=" | ">" | "=ge=" | ">=";

A selector can be a single Local Name (e.g. _course_), QName (e.g. _my:course_) or path (e.g. _course/name_, _my:course/my:name_). It may contain a link “dereference” via dot-notation (eg. _course/department.code_).

    selector   = qname, { ("/" | "."), qname };
    qname      = identifier, [ ":", identifier ];
    identifier = ? ["a"-"z","A"-"Z","_","0"-"9","-"]+ ?

An argument can be of two types. Any character sequence between single or double quotation marks, or unquoted sequence without whitespace, parentheses, comma or semicolon.

    argument    = arg_ws | arg_sq | arg_dq;
    argument-ws = ? ( ~["(", ")", ";", ",", " "] )+ ?;
    argument-sq = ? "'" ~["'"]+ "'" ?;
    argument-dq = ? "\"" ~["\""]+ "\"" ?;


Examples
--------

Let’s look at few examples of RSQL expressions in both FIQL-like and alternative syntax (you can also combine them):

    - code==MI-MDW;credits=gt=4
    - code=MI-MDW and credits>4
    - name=="Programming in Java";(completion==CLFD_CREDIT,completion==CREDIT)
    - name="Programming in Java" and (completion=CLFD_CREDIT or completion=CREDIT)
    - teachers/instructor==jirutjak;department.code==KSI
    - teachers/instructor=jirutjak and department.code=KSI
    - kos:capacity=lt=100,kos:capacity=ge=50
    - kos:capacity<100 or kos:capacity>=50
	
For more examples see [RSQL-hibernate](https://github.com/jirutka/rsql-hibernate).


How to use
----------

```java
Expression ast = new RSQLParser().parse("name==RSQL;version=ge=1.0.2");
```


Maven
-----

Released versions are available in The Central Repository. Just add this artifact to your project:

```xml
<dependency>
    <groupId>cz.jirutka.rsql</groupId>
    <artifactId>rsql-parser</artifactId>
    <version>1.0.2</version>
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
