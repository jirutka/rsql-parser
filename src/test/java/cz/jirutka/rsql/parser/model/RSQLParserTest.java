/*
 * The MIT License
 *
 * Copyright 2013 Jakub Jirutka <jakub@jirutka.cz>.
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
package cz.jirutka.rsql.parser.model;

import cz.jirutka.rsql.parser.ParseException;
import cz.jirutka.rsql.parser.RSQLParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit tests for RSQLParser.
 * 
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class RSQLParserTest {
    
    private ComparisonExpression comp0 = new ComparisonExpression("sel0", Comparison.EQUAL, "foo");
    private ComparisonExpression comp1 = new ComparisonExpression("sel1", Comparison.NOT_EQUAL, "bar");
    private ComparisonExpression comp2 = new ComparisonExpression("sel2", Comparison.LESS_THAN, "baz");
    
    
    @Test
    public void testSelector() throws ParseException {
        testSelector("s1M-pl_e");
        testSelector("WiTh:pr3fi-x");
        testSelector("p4-Th/P_4th/path");
        testSelector("pre:fi/x:ed/path");
        
        try {
            testSelector("stu:pi:d");
            testSelector("comm,a");
            fail("Should raise a TokenMgrError");
        } catch (Error ex) {
            //ok
        }
    }
    
    private void testSelector(String selector) throws ParseException {
        Expression exp = RSQLParser.parse(selector + "==foo");
        ComparisonExpression comp = (ComparisonExpression) exp;
        assertEquals(selector, comp.getSelector());
    }
    
    
    @Test
    public void testArgument() throws ParseException {
        testArgument("@r-G_Um3nt", false);
        testArgument("*@r-g_Um3nt*", false);
        testArgument("ěščřžýáíé", false);
        testArgument("'wh1(te, :sp4) \"c;e - s'", true);
        testArgument("\"wh1(te, :sp4) 'c;e - s\"", true);
    }

    private void testArgument(String argument, boolean quoted) throws ParseException {
        Expression exp = RSQLParser.parse("sel==" + argument);
        ComparisonExpression comp = (ComparisonExpression) exp;
        if (quoted) argument = argument.substring(1, argument.length() -1);
        assertEquals(argument, comp.getArgument());
    }
    
    
    @Test
    public void testComparison() throws ParseException {
        testComparison("==", Comparison.EQUAL);
        testComparison("=", Comparison.EQUAL);
        testComparison("!=", Comparison.NOT_EQUAL);
        testComparison("=lt=", Comparison.LESS_THAN);
        testComparison("<", Comparison.LESS_THAN);
        testComparison("=le=", Comparison.LESS_EQUAL);
        testComparison("<=", Comparison.LESS_EQUAL);
        testComparison("=gt=", Comparison.GREATER_THAN);
        testComparison(">", Comparison.GREATER_THAN);
        testComparison("=ge=", Comparison.GREATER_EQUAL);
        testComparison(">=", Comparison.GREATER_EQUAL);
    }
    
    private void testComparison(String operatorStr, Comparison operatorEnum) 
            throws ParseException {
        Expression exp = RSQLParser.parse("sel" + operatorStr + "foo");
        ComparisonExpression comp = (ComparisonExpression) exp;
        assertEquals(operatorEnum, comp.getOperator());
    }
    
    
    @Test
    public void testConjunction() throws ParseException {
        Expression expected;
        Expression actual;
        
        expected = new LogicalExpression(comp0, Logical.AND, comp1);
        actual = RSQLParser.parse("sel0==foo;sel1!=bar");
        assertEquals("Operator: ;", expected, actual);
        
        expected = new LogicalExpression(comp0, Logical.AND, comp1);
        actual = RSQLParser.parse("sel0==foo and sel1!=bar");
        assertEquals("Operator:  and ", expected, actual);
        
        expected = new LogicalExpression(comp0, Logical.AND, 
                        new LogicalExpression(comp1, Logical.AND, comp2));
        actual = RSQLParser.parse("sel0==foo;sel1!=bar;sel2<baz");
        assertEquals("Operators: ;", expected, actual);
    }
    
    @Test
    public void testDisjunction() throws ParseException {
        Expression expected;
        Expression actual;
        
        expected = new LogicalExpression(comp0, Logical.OR, comp1);
        actual = RSQLParser.parse("sel0==foo,sel1!=bar");
        assertEquals("Operator: ,", expected, actual);
                
        expected = new LogicalExpression(comp0, Logical.OR, comp1);
        actual = RSQLParser.parse("sel0==foo or sel1!=bar");
        assertEquals("Operator: or ", expected, actual);
        
        expected = new LogicalExpression(comp0, Logical.OR, 
                        new LogicalExpression(comp1, Logical.OR, comp2));
        actual = RSQLParser.parse("sel0==foo,sel1!=bar,sel2<baz");
        assertEquals("Operator: ,", expected, actual);
    }
    
    @Test
    public void testLogical() throws ParseException {
        Expression expected;
        Expression actual;
        
        expected = new LogicalExpression(comp0, Logical.OR, 
                        new LogicalExpression(comp1, Logical.AND, comp2));
        actual = RSQLParser.parse("sel0==foo,sel1!=bar;sel2<baz");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(
                        new LogicalExpression(comp0, Logical.AND, comp1), 
                        Logical.OR, comp2);
        actual = RSQLParser.parse("sel0==foo;sel1!=bar,sel2<baz");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(
                        new LogicalExpression(comp0, Logical.AND, 
                            new LogicalExpression(comp1, Logical.AND, comp2)), 
                                Logical.OR, comp0);
        actual = RSQLParser.parse("sel0==foo;sel1!=bar;sel2<baz,sel0==foo");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(
                        new LogicalExpression(comp0, Logical.AND, comp1), 
                        Logical.OR,
                        new LogicalExpression(comp2, Logical.AND, comp0));
        actual = RSQLParser.parse("sel0==foo;sel1!=bar,sel2<baz;sel0==foo");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParens() throws ParseException {
        Expression expected;
        Expression actual;
        
        expected = new LogicalExpression(comp0, Logical.OR, 
                        new LogicalExpression(comp1, Logical.AND, comp2));
        actual = RSQLParser.parse("sel0==foo,(sel1!=bar;sel2<baz)");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(comp0, Logical.AND, 
                        new LogicalExpression(comp1, Logical.OR, comp2));
        actual = RSQLParser.parse("sel0==foo;(sel1!=bar,sel2<baz)");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(
                        new LogicalExpression(comp0, Logical.AND, comp1), 
                        Logical.OR, comp2);
        actual = RSQLParser.parse("(sel0==foo;sel1!=bar),sel2<baz");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(comp0, Logical.AND,
                        new LogicalExpression(comp1, Logical.AND, 
                            new LogicalExpression(comp2, Logical.OR, comp0)));
        actual = RSQLParser.parse("sel0==foo;(sel1!=bar;(sel2<baz,sel0==foo))");
        assertEquals(expected, actual);
        
        expected = new LogicalExpression(comp0, Logical.AND,
                        new LogicalExpression(
                            new LogicalExpression(comp1, Logical.AND, 
                                new LogicalExpression(comp2, Logical.OR, comp0)),
                            Logical.OR, comp1));
        actual = RSQLParser.parse("(sel0==foo);(sel1!=bar;(sel2<baz,sel0==foo),sel1!=bar)");
        assertEquals(expected, actual);
        
    }
    
    @Test
    public void testExamples() throws ParseException {
        Expression expected;
        Expression actual;
        
        ComparisonExpression exp0 = new ComparisonExpression("name", Comparison.EQUAL, "programování v*");
        ComparisonExpression exp1 = new ComparisonExpression("credits", Comparison.GREATER_EQUAL, "4");
        ComparisonExpression exp2 = new ComparisonExpression("completion", Comparison.EQUAL, "CLFD_CREDIT");
        ComparisonExpression exp3 = new ComparisonExpression("completion", Comparison.EQUAL, "CREDIT");
        
        expected = new LogicalExpression(exp0, Logical.AND, 
                        new LogicalExpression(exp1, Logical.AND,
                            new LogicalExpression(exp2, Logical.OR, exp3)));
        actual = RSQLParser.parse("name=='programování v*';credits>=4;(completion==CLFD_CREDIT,completion==CREDIT)");
        assertEquals(expected, actual);
    }
   
}
