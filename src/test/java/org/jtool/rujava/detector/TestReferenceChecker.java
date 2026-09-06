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

public class TestReferenceChecker {
    
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
    public void test120() {
        String filename = "/src/Example120.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ReferenceChecker checker = new ReferenceChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AccessBorrowed:16, 28", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test121() {
        String filename = "/src/Example121.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ReferenceChecker checker = new ReferenceChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AccessBorrowed:20, 32", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test122() {
        String filename = "/src/Example122.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ReferenceChecker checker = new ReferenceChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test123() {
        String filename = "/src/Example123.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ReferenceChecker checker = new ReferenceChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test132() {
        String filename = "/src/Example132.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ReferenceChecker checker = new ReferenceChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test133() {
        String filename = "/src/Example133.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ReferenceChecker checker = new ReferenceChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
