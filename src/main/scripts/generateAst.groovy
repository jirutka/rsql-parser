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
import groovy.text.SimpleTemplateEngine

PACKAGE_NAME = 'cz.jirutka.rsql.parser.ast'

TEMPLATES_DIR = new File(properties.templatesDirectory)
PACKAGE_DIR = new File(properties.generatedSourcesDirectory, 'java/' + PACKAGE_NAME.replace('.', '/'))


comparisonNodes = [
    EqualNode: '==',
    InNode: '=in=',
    GreaterThanOrEqualNode: '=ge=',
    GreaterThanNode: '=gt=',
    LessThanOrEqualNode: '=le=',
    LessThanNode: '=lt=',
    NotEqualNode: '!=',
    NotInNode: '=out='
]

logicalNodes = ['AndNode', 'OrNode']


def generateClass(vars=[:], className, templateName=null) {
    def templateFile = new File(TEMPLATES_DIR, templateName ?: className + '.java.tmpl')
    def classFile = new File(PACKAGE_DIR, className + '.java')

    def binding = [
        packageName: PACKAGE_NAME,
        className: className,
        logicalNodes: logicalNodes,
        comparisonNodes: comparisonNodes,
        nodesNames: logicalNodes + comparisonNodes*.key
    ] + vars

    log.info "Generating source: ${PACKAGE_NAME}.${className}"
    def tmpl = new SimpleTemplateEngine().createTemplate(templateFile).make(binding)

    tmpl.writeTo(new FileWriter(classFile))
}


////////  M a i n  ////////

PACKAGE_DIR.mkdirs()

comparisonNodes.each { className, operator ->
    generateClass className, 'ComparisonNodeX.java.tmpl', operator: operator
}
generateClass 'RSQLVisitor'
generateClass 'NoArgRSQLVisitorAdapter'
generateClass 'SuperNodesRSQLVisitorAdapter'
