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

public class TestImmutabilityChecker {
    
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("ImmutableRedefine:16, 20", TestUtil.getMessage(messages.get(0)));
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            
            assertEquals("ImmutableRedefine:16, 22", TestUtil.getMessage(messages.get(0)));
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("ImmutableRedefine:16, 18", TestUtil.getMessage(messages.get(0)));
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("ImmutableRedefine:16, 20", TestUtil.getMessage(messages.get(0)));
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
            
            ImmutabilityChecker checker = new ImmutabilityChecker();
            checker.check(project);
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
