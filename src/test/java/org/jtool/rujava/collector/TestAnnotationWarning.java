/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.OutputMessage;
import org.jtool.rujava.TestUtil;

import org.jtool.srcmodel.JavaProject;

import java.util.List;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestAnnotationWarning {
    
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
    public void testWarning101() {
        String filename = "/src/ExampleWarning101.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("PrimitiveTypeReturn:10", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning102() {
        String filename = "/src/ExampleWarning102.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("PrimitiveTypeDeclaration:11", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning103() {
        String filename = "/src/ExampleWarning103.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(2, messages.size());
            
            assertEquals("OverrideImmutable:70", TestUtil.getMessage(messages.get(0)));
            assertEquals("OverrideImmutable:79", TestUtil.getMessage(messages.get(1)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning104() {
        String filename = "/src/ExampleWarning104.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("CoexistenceMutability:12", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning105() {
        String filename = "/src/ExampleWarning105.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(5, messages.size());
            
            assertEquals("OverrideImmutableParameter:79", TestUtil.getMessage(messages.get(0)));
            assertEquals("OverrideImmutableParameter:87", TestUtil.getMessage(messages.get(1)));
            assertEquals("OverrideImmutableParameter:119", TestUtil.getMessage(messages.get(2)));
            assertEquals("OverrideImmutableParameter:123", TestUtil.getMessage(messages.get(3)));
            assertEquals("OverrideImmutableParameter:127", TestUtil.getMessage(messages.get(4)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning106() {
        String filename = "/src/ExampleWarning106.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("CoexistenceMutability:12", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning201() {
        String filename = "/src/ExampleWarning201.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("PrimitiveTypeReturn:10", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning202() {
        String filename = "/src/ExampleWarning202.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("PrimitiveTypeDeclaration:11", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning203() {
        String filename = "/src/ExampleWarning203.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(2, messages.size());
            assertEquals("OverrideBorrow:60", TestUtil.getMessage(messages.get(0)));
            assertEquals("OverrideBorrow:90", TestUtil.getMessage(messages.get(1)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning204() {
        String filename = "/src/ExampleWarning204.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("CoexistenceOwnership:12",TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning205() {
        String filename = "/src/ExampleWarning205.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(4, messages.size());
            assertEquals("OverrideBorrowParameter:79", TestUtil.getMessage(messages.get(0)));
            assertEquals("OverrideBorrowParameter:91", TestUtil.getMessage(messages.get(1)));
            assertEquals("OverrideBorrowParameter:119", TestUtil.getMessage(messages.get(2)));
            assertEquals("OverrideBorrowParameter:123", TestUtil.getMessage(messages.get(3)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning206() {
        String filename = "/src/ExampleWarning206.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            List<OutputMessage> messages = TestUtil.getWarningMessages(project);
            assertEquals(1, messages.size());
            assertEquals("CoexistenceOwnership:12", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void testWarning98() {
        String filename = "/src/ExampleWarning98.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            collector.collect(filename);
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("** Cannot compile the file:"));
        }
    }
    
    @Test
    public void testWarning99() {
        String filename = "/src/ExampleWarning99.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            collector.collect(filename);
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("** Found no classes:"));
        }
    }
}
