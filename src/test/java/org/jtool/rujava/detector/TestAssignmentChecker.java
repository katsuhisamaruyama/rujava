/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OutputMessage;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.collector.AnnotationCollector;
import org.jtool.rujava.collector.RuFormalVariable;
import org.jtool.rujava.collector.RuLocalVariable;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.TestUtil;

import org.jtool.srcmodel.JavaProject;

import java.util.List;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestAssignmentChecker {
    
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
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test102() {
        String filename = "/src/Example102.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test103() {
        String filename = "/src/Example103.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test104() {
        String filename = "/src/Example104.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test105() {
        String filename = "/src/Example105.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(6, messages.size());
            assertEquals("BorrowAsMutable:39", TestUtil.getMessage(messages.get(0)));
            assertEquals("OwnBorrowed:42", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:43", TestUtil.getMessage(messages.get(2)));
            assertEquals("OwnBorrowed:47", TestUtil.getMessage(messages.get(3)));
            assertEquals("OwnBorrowed:48", TestUtil.getMessage(messages.get(4)));
            assertEquals("BorrowAsMutable:49", TestUtil.getMessage(messages.get(5)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test105_ProhibitImmutableToMutable() {
        String filename = "/src/Example105.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker(false, true);
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(7, messages.size());
            assertEquals("ImmutableAsMutable:37", TestUtil.getMessage(messages.get(0)));
            assertEquals("BorrowAsMutable:39", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:42", TestUtil.getMessage(messages.get(2)));
            assertEquals("OwnBorrowed:43", TestUtil.getMessage(messages.get(3)));
            assertEquals("OwnBorrowed:47", TestUtil.getMessage(messages.get(4)));
            assertEquals("OwnBorrowed:48", TestUtil.getMessage(messages.get(5)));
            assertEquals("BorrowAsMutable:49", TestUtil.getMessage(messages.get(6)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test105_ProhibitMutableBorrow() {
        String filename = "/src/Example105.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker(true, false);
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(7, messages.size());
            assertEquals("BorrowAsMutable:39", TestUtil.getMessage(messages.get(0)));
            assertEquals("OwnBorrowed:42", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:43", TestUtil.getMessage(messages.get(2)));
            assertEquals("MultipleMutableBorrowed:44", TestUtil.getMessage(messages.get(3)));
            assertEquals("OwnBorrowed:47", TestUtil.getMessage(messages.get(4)));
            assertEquals("OwnBorrowed:48", TestUtil.getMessage(messages.get(5)));
            assertEquals("BorrowAsMutable:49", TestUtil.getMessage(messages.get(6)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test106() {
        String filename = "/src/Example106.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(2, messages.size());
            assertEquals("BorrowAsMutable:29, 36", TestUtil.getMessage(messages.get(0)));
            assertEquals("BorrowAsMutable:29, 39", TestUtil.getMessage(messages.get(1)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test107() {
        String filename = "/src/Example107.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test108() {
        String filename = "/src/Example108.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test109() {
        String filename = "/src/Example109.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test109_ProhibitImmutableToMutable() {
        String filename = "/src/Example109.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker(false, true);
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("ImmutableAsMutable:18, 20", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test109_ProhibitMutableBorrow() {
        String filename = "/src/Example109.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker(true, false);
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("MultipleMutableBorrowed:22, 24", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test110() {
        String filename = "/src/Example110.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(6, messages.size());
            assertEquals("BorrowAsMutable:51", TestUtil.getMessage(messages.get(0)));
            assertEquals("OwnBorrowed:54", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:55", TestUtil.getMessage(messages.get(2)));
            assertEquals("OwnBorrowed:59", TestUtil.getMessage(messages.get(3)));
            assertEquals("OwnBorrowed:60", TestUtil.getMessage(messages.get(4)));
            assertEquals("BorrowAsMutable:61", TestUtil.getMessage(messages.get(5)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test110_ProhibitImmutableToMutable() {
        String filename = "/src/Example110.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker(false, true);
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(7, messages.size());
            assertEquals("ImmutableAsMutable:49", TestUtil.getMessage(messages.get(0)));
            assertEquals("BorrowAsMutable:51", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:54", TestUtil.getMessage(messages.get(2)));
            assertEquals("OwnBorrowed:55", TestUtil.getMessage(messages.get(3)));
            assertEquals("OwnBorrowed:59", TestUtil.getMessage(messages.get(4)));
            assertEquals("OwnBorrowed:60", TestUtil.getMessage(messages.get(5)));
            assertEquals("BorrowAsMutable:61", TestUtil.getMessage(messages.get(6)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test110_ProhibitMutableBorrow() {
        String filename = "/src/Example110.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            AssignmentChecker checker = new AssignmentChecker(true, false);
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(7, messages.size());
            assertEquals("BorrowAsMutable:51", TestUtil.getMessage(messages.get(0)));
            assertEquals("OwnBorrowed:54", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:55", TestUtil.getMessage(messages.get(2)));
            assertEquals("MultipleMutableBorrowed:56", TestUtil.getMessage(messages.get(3)));
            assertEquals("OwnBorrowed:59", TestUtil.getMessage(messages.get(4)));
            assertEquals("OwnBorrowed:60", TestUtil.getMessage(messages.get(5)));
            assertEquals("BorrowAsMutable:61", TestUtil.getMessage(messages.get(6)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
