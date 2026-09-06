/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.TestUtil;

import org.jtool.srcmodel.JavaProject;

import java.util.List;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestAnnotationCollector {
    
    private static JavaProject targetProject;
    
    @BeforeClass
    public static void start() {
        targetProject = TestUtil.getCheckerForSimpleProject();
        
        RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
        RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
    }
    
    @AfterClass
    public static void close() {
        targetProject.getModelBuilder().unbuild();
    }
    
    @Test
    public void test101() {
        String filename = "/src/Example101.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(6, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 10 defuse list$0 [MUTABLE/BORROW];"
                       + "1 -> 13 defuse list$0 [MUTABLE/BORROW];"
                       + "1 -> 15 defdefCall list$0 [MUTABLE/BORROW];"
                       + "1 -> 18 defuse list$0 [MUTABLE/BORROW];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example101.!Example101( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test102() {
        String filename = "/src/Example102.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(6, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 10 defuse muList$0 [MUTABLE/OWNED];"
                       + "1 -> 13 defuse muList$0 [MUTABLE/OWNED];"
                       + "1 -> 15 defdefCall muList$0 [MUTABLE/OWNED];"
                       + "1 -> 18 defuse muList$0 [MUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example102.!Example102( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test103() {
        String filename = "/src/Example103.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(6, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 10 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 13 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 15 defdefCall imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 18 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example103.!Example103( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test104() {
        String filename = "/src/Example104.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(14, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 17 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 24 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 26 defdefCall imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 33 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "6 -> 13 defdef this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "7 -> 14 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 21 defuse muList$1 [MUTABLE/OWNED];8 -> 28 defuse muList$1 [MUTABLE/OWNED];"
                       + "8 -> 30 defdefCall muList$1 [MUTABLE/OWNED];"
                       + "8 -> 37 defuse muList$1 [MUTABLE/OWNED];"
                       + "13 -> 10 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "14 -> 8 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example104.!Example104( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test105() {
        String filename = "/src/Example105.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(7, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(2, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(2, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo2);
            
            List<DataFlow> dataflows3 = methods.get(3).getDataFlows();
            assertEquals(2, dataflows3.size());
            
            String dataflowInfo3 = TestUtil.getDataFlowsLongString(methods.get(3));
            assertEquals("6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo3);
            
            List<DataFlow> dataflows4 = methods.get(4).getDataFlows();
            assertEquals(2, dataflows4.size());
            
            String dataflowInfo4 = TestUtil.getDataFlowsLongString(methods.get(4));
            assertEquals("6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo4);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test106() {
        String filename = "/src/Example106.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(6, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(2, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 4 defuse list$0 [MUTABLE/BORROW];"
                       + "1 -> 6 defdefCall list$0 [MUTABLE/BORROW]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(17, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 18 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 28 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "1 -> 40 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "6 -> 13 defdef this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "7 -> 14 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 23 defuse muList$1 [MUTABLE/OWNED];"
                       + "8 -> 34 defuse muList$1 [MUTABLE/OWNED];8 -> 46 defuse muList$1 [MUTABLE/OWNED];"
                       + "13 -> 10 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "14 -> 8 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "37 -> 37 defonly list1$2 [MUTABLE/BORROW];"
                       + "42 -> 37 defuse this.!inverse( java.util.List int ) [MUTABLE/OWNED];"
                       + "42 -> 48 defdef this.!inverse( java.util.List int ) [MUTABLE/OWNED];"
                       + "43 -> 43 defonly list2$3 [MUTABLE/BORROW];"
                       + "48 -> 43 defuse this.!inverse( java.util.List int ) [MUTABLE/OWNED]",
                 dataflowInfo2);
            
            List<DataFlow> dataflows3 = methods.get(3).getDataFlows();
            assertEquals(4, dataflows3.size());
            
            String dataflowInfo3 = TestUtil.getDataFlowsLongString(methods.get(3));
            assertEquals("1 -> 6 defuse list$0 [MUTABLE/BORROW];"
                       + "3 -> 10 defuse reversedList$2 [MUTABLE/BORROW];"
                       + "8 -> 5 defuse list$0.!reversed( ) [MUTABLE/OWNED];"
                       + "9 -> 3 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo3);
            
            List<DataFlow> dataflows4 = methods.get(4).getDataFlows();
            assertEquals(4, dataflows4.size());
            
            String dataflowInfo4 = TestUtil.getDataFlowsLongString(methods.get(4));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example106.!Example106( ) [MUTABLE/OWNED]",
                    dataflowInfo4);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test107() {
        String filename = "/src/Example107.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(7, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 8 defdef imList$0 [IMMUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "7 -> 14 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 17 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "13 -> 10 defuse this.!of( java.lang.Object java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "14 -> 8 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example107.!Example107( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test108() {
        String filename = "/src/Example108.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(8, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 2 defdef imList$0 [IMMUTABLE/OWNED];"
                       + "2 -> 9 defdef imList$0 [IMMUTABLE/OWNED];"
                       + "7 -> 4 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "8 -> 2 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 15 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "9 -> 18 defuse imList$0 [IMMUTABLE/OWNED];"
                       + "14 -> 11 defuse this.!of( java.lang.Object java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "15 -> 9 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example108.!Example108( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test109() {
        String filename = "/src/Example109.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(3, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(11, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 8 defuse muList$0 [MUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "6 -> 15 defdef this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "7 -> 16 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 9 defuse imList$1 [IMMUTABLE/OWNED];"
                       + "9 -> 9 defonly muList2$2 [MUTABLE/OWNED];"
                       + "10 -> 17 defuse muList3$3 [MUTABLE/BORROW];"
                       + "15 -> 12 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "16 -> 10 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "17 -> 17 defonly muList4$4 [MUTABLE/BORROW]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example109.!Example109( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test110() {
        String filename = "/src/Example110.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(11, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(2, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(2, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                   dataflowInfo2);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test132() {
        String filename = "/src/Example132.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(4, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(6, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 8 defuse muList$0 [MUTABLE/OWNED];"
                       + "1 -> 16 defuse muList$0 [MUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 8 defdef i$1 [MUTABLE/BORROW];"
                       + "8 -> 11 defuse i$1 [MUTABLE/BORROW]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example132.!Example132( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
            
            List<DataFlow> dataflows3 = methods.get(3).getDataFlows();
            assertEquals(3, dataflows3.size());
            
            String dataflowInfo3 = TestUtil.getDataFlowsLongString(methods.get(3));
            assertEquals("1 -> 2 defuse muList$0 [MUTABLE/OWNED];"
                       + "2 -> 2 defdef i$1 [MUTABLE/BORROW];"
                       + "2 -> 5 defuse i$1 [MUTABLE/BORROW]",
                   dataflowInfo3);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test133() {
        String filename = "/src/Example133.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(4, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(9, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 9 defuse muList$0 [MUTABLE/OWNED];"
                       + "1 -> 26 defuse muList$0 [MUTABLE/OWNED];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "8 -> 13 defuse it$1 [MUTABLE/BORROW];"
                       + "8 -> 19 defuse it$1 [MUTABLE/BORROW];"
                       + "11 -> 8 defuse muList$0.!iterator( ) [MUTABLE/OWNED];"
                       + "21 -> 18 defuse it$1.!next( ) [MUTABLE/OWNED];"
                       + "21 -> 21 defdef it$1.!next( ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example133.!Example133( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
            
            List<DataFlow> dataflows3 = methods.get(3).getDataFlows();
            assertEquals(1, dataflows3.size());
            
            String dataflowInfo3 = TestUtil.getDataFlowsLongString(methods.get(3));
            assertEquals("1 -> 4 defuse muList$0 [MUTABLE/OWNED]",
                   dataflowInfo3);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test134() {
        String filename = "/src/Example134.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            List<RuMethod> methods = RuMethod.sort(collector.collect(filename).getMethods());
            assertEquals(4, methods.size());
            
            List<DataFlow> dataflows0 = methods.get(0).getDataFlows();
            assertEquals(0, dataflows0.size());
            
            String dataflowInfo0 = TestUtil.getDataFlowsLongString(methods.get(0));
            assertEquals("", dataflowInfo0);
            
            List<DataFlow> dataflows1 = methods.get(1).getDataFlows();
            assertEquals(12, dataflows1.size());
            
            String dataflowInfo1 = TestUtil.getDataFlowsLongString(methods.get(1));
            assertEquals("1 -> 11 defuse imuList$0 [MUTABLE/BORROW];"
                       + "6 -> 3 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "6 -> 18 defdef this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "7 -> 1 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "7 -> 19 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "13 -> 20 defdef muList$1 [MUTABLE/BORROW];"
                       + "18 -> 15 defuse this.!of( java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "19 -> 13 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "19 -> 26 defdef java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED];"
                       + "20 -> 30 defuse muList$1 [MUTABLE/BORROW];"
                       + "25 -> 22 defuse this.!of( java.lang.Object java.lang.Object java.lang.Object ) [MUTABLE/OWNED];"
                       + "26 -> 20 defuse java.util.ArrayList.!ArrayList( java.util.Collection ) [MUTABLE/OWNED]",
                    dataflowInfo1);
            
            List<DataFlow> dataflows2 = methods.get(2).getDataFlows();
            assertEquals(4, dataflows2.size());
            
            String dataflowInfo2 = TestUtil.getDataFlowsLongString(methods.get(2));
            assertEquals("1 -> 1 defonly args$0 [MUTABLE/BORROW];"
                       + "2 -> 7 defuse example$1 [MUTABLE/BORROW];"
                       + "2 -> 9 defdefCall example$1 [MUTABLE/BORROW];"
                       + "5 -> 2 defuse Example134.!Example134( ) [MUTABLE/OWNED]",
                   dataflowInfo2);
            
            List<DataFlow> dataflows3 = methods.get(3).getDataFlows();
            assertEquals(3, dataflows3.size());
            
            String dataflowInfo3 = TestUtil.getDataFlowsLongString(methods.get(3));
            assertEquals("1 -> 2 defuse list$0 [MUTABLE/BORROW];"
                       + "2 -> 2 defdef item$1 [MUTABLE/BORROW];"
                       + "2 -> 5 defuse item$1 [MUTABLE/BORROW]",
                   dataflowInfo3);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
