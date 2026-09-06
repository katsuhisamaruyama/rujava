/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

import org.jtool.rujava.collector.RuProject;

import org.jtool.srcmodel.JavaProject;

import java.io.File;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestRuJava {
    
    @Test
    public void testSimple() {
        String name = "Simple";
        String target = "test_target" + File.separator + name;
        String[] args = { "-target", target };
        
        RuJava rujava = new RuJava(args);
        List<JavaProject> targetProjects = rujava.build();
        
        assertEquals(1, targetProjects.size());
        JavaProject targetProject = targetProjects.get(0);
        String targetPath = targetProject.getPath();
        
        assertEquals(name, targetProject.getName());
        assertEquals(", /src",
                TestUtil.getPathString(targetPath, targetProject.getSourcePath()));
        assertEquals("/bin",
                TestUtil.getPathString(targetPath, targetProject.getBinaryPath()));
        
        List<RuProject> projects = rujava.detect();
        List<OutputMessage> warningMessages = rujava.getWarningMessages(projects.get(0));
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(22, warningMessages.size());
        assertEquals(48, errorMessages.size());
        
        rujava.printOutputMessages();
        
        rujava.unbuild();
    }
    
    @Test
    public void testTetris() {
        String name = "Tetris";
        String target = "test_target" + File.separator + name;
        String[] args = { "-target", target };
        
        RuJava rujava = new RuJava(args);
        List<JavaProject> targetProjects = rujava.build();
        
        assertEquals(1, targetProjects.size());
        JavaProject targetProject = targetProjects.get(0);
        String targetPath = targetProject.getPath();
        
        assertEquals(name, targetProject.getName());
        assertEquals("/src/main/java, /target/generated-sources",
                TestUtil.getPathString(targetPath, targetProject.getSourcePath()));
        assertEquals("/target/classes, /target/test-classes",
                TestUtil.getPathString(targetPath, targetProject.getBinaryPath()));
        
        List<RuProject> projects = rujava.detect();
        List<OutputMessage> warningMessages = rujava.getWarningMessages(projects.get(0));
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(0, warningMessages.size());
        assertEquals(0, errorMessages.size());
        
        rujava.unbuild();
    }
    
    @Test
    public void testDrawTool() {
        String name = "DrawTool";
        String target = "test_target" + File.separator + name;
        String[] args = { "-target", target };
        
        RuJava rujava = new RuJava(args);
        List<JavaProject> targetProjects = rujava.build();
        
        assertEquals(1, targetProjects.size());
        JavaProject targetProject = targetProjects.get(0);
        String targetPath = targetProject.getPath();
        
        assertEquals(name, targetProject.getName());
        assertEquals("/src/main/java, /target/generated-sources",
                TestUtil.getPathString(targetPath, targetProject.getSourcePath()));
        assertEquals("/target/classes, /target/test-classes",
                TestUtil.getPathString(targetPath, targetProject.getBinaryPath()));
        
        List<RuProject> projects = rujava.detect();
        List<OutputMessage> warningMessages = rujava.getWarningMessages(projects.get(0));
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(0, warningMessages.size());
        assertEquals(0, errorMessages.size());
        
        rujava.unbuild();
    }
    
    @Test
    public void testSimpleFile1() {
        String name = "Simple";
        String target = "test_target" + File.separator + name;
        String filename = "/src/Example111.java";
        String[] args = { "-target", target, "-file", filename };
        
        RuJava rujava = new RuJava(args);
        List<JavaProject> targetProjects = rujava.build();
        
        assertEquals(1, targetProjects.size());
        JavaProject targetProject = targetProjects.get(0);
        String targetPath = targetProject.getPath();
        
        assertEquals(name, targetProject.getName());
        assertEquals(", /src",
                TestUtil.getPathString(targetPath, targetProject.getSourcePath()));
        assertEquals("/bin",
                TestUtil.getPathString(targetPath, targetProject.getBinaryPath()));
        
        List<RuProject> projects = rujava.detect();
        List<OutputMessage> warningMessages = rujava.getWarningMessages(projects.get(0));
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(0, warningMessages.size());
        assertEquals(3, errorMessages.size());
        
        rujava.printOutputMessages();
        
        rujava.unbuild();
    }
    
    @Test
    public void testSimpleFile2() {
        String name = "Simple";
        String target = "test_target" + File.separator + name;
        String filename = "/src/ExampleWarning102.java";
        String[] args = { "-target", target, "-file", filename };
        
        RuJava rujava = new RuJava(args);
        List<JavaProject> targetProjects = rujava.build();
        
        assertEquals(1, targetProjects.size());
        JavaProject targetProject = targetProjects.get(0);
        String targetPath = targetProject.getPath();
        
        assertEquals(name, targetProject.getName());
        assertEquals(", /src",
                TestUtil.getPathString(targetPath, targetProject.getSourcePath()));
        assertEquals("/bin",
                TestUtil.getPathString(targetPath, targetProject.getBinaryPath()));
        
        List<RuProject> projects = rujava.detect();
        List<OutputMessage> warningMessages = rujava.getWarningMessages(projects.get(0));
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(1, warningMessages.size());
        assertEquals(0, errorMessages.size());
        
        rujava.printOutputMessages();
        
        rujava.unbuild();
    }
}
