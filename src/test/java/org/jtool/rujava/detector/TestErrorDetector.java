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

public class TestErrorDetector {
    
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
    public void test105_ProhibitImmutableToMutableAndMutableBorrow() {
        String filename = "/src/Example105.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project, false, false);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(8, messages.size());
            assertEquals("ImmutableAsMutable:37", TestUtil.getMessage(messages.get(0)));
            assertEquals("BorrowAsMutable:39", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:42", TestUtil.getMessage(messages.get(2)));
            assertEquals("OwnBorrowed:43", TestUtil.getMessage(messages.get(3)));
            assertEquals("MultipleMutableBorrowed:44", TestUtil.getMessage(messages.get(4)));
            assertEquals("OwnBorrowed:47", TestUtil.getMessage(messages.get(5)));
            assertEquals("OwnBorrowed:48", TestUtil.getMessage(messages.get(6)));
            assertEquals("BorrowAsMutable:49", TestUtil.getMessage(messages.get(7)));
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test109_ProhibitImmutableToMutableAndMutableBorrow() {
        String filename = "/src/Example109.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project, false, false);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(2, messages.size());
            assertEquals("ImmutableAsMutable:18, 20", TestUtil.getMessage(messages.get(0)));
            assertEquals("MultipleMutableBorrowed:22, 24", TestUtil.getMessage(messages.get(1)));
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
    public void test110_ProhibitImmutableToMutableAndMutableBorrow() {
        String filename = "/src/Example110.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project, false, false);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(8, messages.size());
            assertEquals("ImmutableAsMutable:49", TestUtil.getMessage(messages.get(0)));
            assertEquals("BorrowAsMutable:51", TestUtil.getMessage(messages.get(1)));
            assertEquals("OwnBorrowed:54", TestUtil.getMessage(messages.get(2)));
            assertEquals("OwnBorrowed:55", TestUtil.getMessage(messages.get(3)));
            assertEquals("MultipleMutableBorrowed:56", TestUtil.getMessage(messages.get(4)));
            assertEquals("OwnBorrowed:59", TestUtil.getMessage(messages.get(5)));
            assertEquals("OwnBorrowed:60", TestUtil.getMessage(messages.get(6)));
            assertEquals("BorrowAsMutable:61", TestUtil.getMessage(messages.get(7)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test111() {
        String filename = "/src/Example111.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AccessMoved:22, 34", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test120() {
        String filename = "/src/Example120.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test124() {
        String filename = "/src/Example124.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test125() {
        String filename = "/src/Example125.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AssignBorrowed:16, 27", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test126() {
        String filename = "/src/Example126.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AssignBorrowed:16, 28", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test127() {
        String filename = "/src/Example127.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AssignBorrowed:16, 27", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test128() {
        String filename = "/src/Example128.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AssignOwned:16, 24", TestUtil.getMessage(messages.get(0)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test129() {
        String filename = "/src/Example129.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(6, messages.size());
            assertEquals("AssignMutableBorrowed:16, 19", TestUtil.getMessage(messages.get(0)));
            assertEquals("AssignOwned:16, 27", TestUtil.getMessage(messages.get(1)));
            assertEquals("AccessMoved:16, 31", TestUtil.getMessage(messages.get(2)));
            assertEquals("AssignMutableBorrowed:16, 31", TestUtil.getMessage(messages.get(3)));
            assertEquals("AccessMoved:16, 35", TestUtil.getMessage(messages.get(4)));
            assertEquals("AssignMutableBorrowed:16, 35", TestUtil.getMessage(messages.get(5)));
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test130() {
        String filename = "/src/Example130.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
    
    @Test
    public void test131() {
        String filename = "/src/Example131.java";
        
        try {
            AnnotationCollector collector = new AnnotationCollector(targetProject);
            RuProject project = collector.collect(filename);
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(1, messages.size());
            assertEquals("AssignBorrowed:18, 24", TestUtil.getMessage(messages.get(0)));
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
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
            
            ErrorDetector detector = new ErrorDetector(project);
            detector.detect();
            
            List<OutputMessage> messages = TestUtil.getErrorMessages(project);
            assertEquals(0, messages.size());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
