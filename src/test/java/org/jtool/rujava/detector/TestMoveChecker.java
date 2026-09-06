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

public class TestMoveChecker {
    
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
    public void test111() {
        String filename = "/src/Example111.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(3, messages.size());
            assertEquals("AccessMoved:16, 24", TestUtil.getMessage(messages.get(0)));
            assertEquals("AccessMoved:16, 26", TestUtil.getMessage(messages.get(1)));
            assertEquals("AccessMoved:16, 38", TestUtil.getMessage(messages.get(2)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test112() {
        String filename = "/src/Example112.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(3, messages.size());
            assertEquals("AccessMoved:16, 25", TestUtil.getMessage(messages.get(0)));
            assertEquals("AccessMoved:16, 28", TestUtil.getMessage(messages.get(1)));
            assertEquals("AccessMoved:16, 36", TestUtil.getMessage(messages.get(2)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test113() {
        String filename = "/src/Example113.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(4, messages.size());
            assertEquals("AccessMoved:16, 21", TestUtil.getMessage(messages.get(0)));
            assertEquals("AccessMoved:16, 25", TestUtil.getMessage(messages.get(1)));
            assertEquals("AccessMoved:16, 28", TestUtil.getMessage(messages.get(2)));
            assertEquals("AccessMoved:16, 37", TestUtil.getMessage(messages.get(3)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test114() {
        String filename = "/src/Example114.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AccessMoved:16, 23", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test115() {
        String filename = "/src/Example115.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AccessMoved:16, 24", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test116() {
        String filename = "/src/Example116.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(2, messages.size());
            assertEquals("AccessMoved:18, 22", TestUtil.getMessage(messages.get(0)));
            assertEquals("AccessMoved:18, 26", TestUtil.getMessage(messages.get(1)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test117() {
        String filename = "/src/Example117.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test118() {
        String filename = "/src/Example118.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(2, messages.size());
            
            assertEquals("AccessMoved:22, 26", TestUtil.getMessage(messages.get(0)));
            assertEquals("AccessMoved:22, 34", TestUtil.getMessage(messages.get(1)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test119() {
        String filename = "/src/Example119.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            MoveChecker checker = new MoveChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            
            assertEquals("AccessMoved:22, 34", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
