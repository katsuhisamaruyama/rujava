/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.TestUtil;

import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaMethod;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestDataFlowFinder {
    
    private static JavaProject targetProject;
    
    @BeforeClass
    public static void start() {
        targetProject = TestUtil.getCheckerForSimpleProject();
    }
    
    @AfterClass
    public static void close() {
        targetProject.getModelBuilder().unbuild();
    }
    
    @Test
    public void test101() {
        String filename = "/src/Example101.java";
        List<JavaClass> jclasses = TestUtil.getJavaFile(targetProject, filename);
        List<JavaMethod> jmethods = jclasses.stream().flatMap(c -> c.getMethods().stream()).toList();
        
        DataFlowFinder finder = new DataFlowFinder(targetProject);
        List<RuMethod> methods = RuMethod.sort(finder.find(jmethods));
        assertEquals(3, methods.size());
        
        String methodNames = TestUtil.getMethodsString(methods);
        assertEquals("Example101#Example101( );"
                   + "Example101#exec( );"
                   + "Example101#main( java.lang.String[] )", methodNames);
        
        List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
        assertEquals(0, dataflows0.size());
        
        String dataflowInfo0 = TestUtil.getDataFlowsString(methods.get(0));
        assertEquals("", dataflowInfo0);
        
        List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
        assertEquals(11, dataflows1.size());
        
        String dataflowInfo1 = TestUtil.getDataFlowsString(methods.get(1));
        assertEquals("1 -> 10 defuse list$0;"
                   + "1 -> 13 defuse list$0;"
                   + "1 -> 15 defdefCall list$0;"
                   + "1 -> 15 defdefCall list$0;"
                   + "1 -> 18 defuse list$0;"
                   + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object );"
                   + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection );"
                   + "11 -> 8 defuse System.out.!println( java.lang.Object );"
                   + "11 -> 19 defdef System.out.!println( java.lang.Object );"
                   + "15 -> 12 defuse list$0.!add( java.lang.Object );"
                   + "19 -> 16 defuse System.out.!println( java.lang.Object )",
                dataflowInfo1);
        
        List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
        assertEquals(12, dataflows2.size());
        
        String dataflowInfo2 = TestUtil.getDataFlowsString(methods.get(2));
        assertEquals("1 -> 1 defonly args$0;"
                   + "2 -> 7 defuse example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "5 -> 2 defuse Example101.!Example101( );"
                   + "9 -> 6 defuse example$1.!exec( )",
                dataflowInfo2);
    }
    
    @Test
    public void test133() {
        String filename = "/src/Example133.java";
        
        List<JavaClass> jclasses = TestUtil.getJavaFile(targetProject, filename);
        List<JavaMethod> jmethods = jclasses.stream().flatMap(c -> c.getMethods().stream()).toList();
        
        DataFlowFinder finder = new DataFlowFinder(targetProject);
        List<RuMethod> methods = RuMethod.sort(finder.find(jmethods));
        assertEquals(4, methods.size());
        
        String methodNames = TestUtil.getMethodsString(methods);
        assertEquals("Example133#Example133( );"
                   + "Example133#exec( );"
                   + "Example133#main( java.lang.String[] );"
                   + "Example133#print( java.util.List )", methodNames);
        
        List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
        assertEquals(0, dataflows0.size());
        
        String dataflowInfo0 = TestUtil.getDataFlowsString(methods.get(0));
        assertEquals("", dataflowInfo0);
        
        List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
        assertEquals(14, dataflows1.size());
        
        String dataflowInfo1 = TestUtil.getDataFlowsString(methods.get(1));
        assertEquals("1 -> 9 defuse muList$0;"
                   + "1 -> 26 defuse muList$0;"
                   + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object );"
                   + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection );"
                   + "8 -> 13 defuse it$1;"
                   + "8 -> 19 defuse it$1;"
                   + "11 -> 8 defuse muList$0.!iterator( );"
                   + "15 -> 12 defuse it$1.!hasNext( );"
                   + "15 -> 15 defdef it$1.!hasNext( );"
                   + "21 -> 18 defuse it$1.!next( );"
                   + "21 -> 21 defdef it$1.!next( );"
                   + "22 -> 16 defuse System.out.!println( java.lang.Object );"
                   + "22 -> 22 defdef System.out.!println( java.lang.Object );"
                   + "27 -> 23 defuse this.!print( java.util.List )",
                dataflowInfo1);
        
        List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
        assertEquals(10, dataflows2.size());
        
        String dataflowInfo2 = TestUtil.getDataFlowsString(methods.get(2));
        assertEquals("1 -> 1 defonly args$0;"
                   + "2 -> 7 defuse example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "2 -> 9 defdefCall example$1;"
                   + "5 -> 2 defuse Example133.!Example133( );"
                   + "9 -> 6 defuse example$1.!exec( )",
                dataflowInfo2);
        
        List<DataFlow> dataflows3 = methods.get(3).getDataFlows();
        assertEquals(11, dataflows3.size());
        
        String dataflowInfo3 = TestUtil.getDataFlowsString(methods.get(3));
        assertEquals("1 -> 4 defuse muList$0;"
                   + "2 -> 3 defuse i$1;"
                   + "2 -> 9 defuse i$1;"
                   + "2 -> 11 defuse i$1;"
                   + "6 -> 3 defuse muList$0.!size( );"
                   + "6 -> 6 defdef muList$0.!size( );"
                   + "10 -> 7 defuse System.out.!println( int );"
                   + "10 -> 10 defdef System.out.!println( int );"
                   + "11 -> 3 defuse i$1;"
                   + "11 -> 9 defuse i$1;"
                   + "11 -> 11 defuse i$1",
                dataflowInfo3);
    }
}
