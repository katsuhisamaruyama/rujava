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

public class TestRuJavaRust {
    
    @Test
    public void testTetris() {
        String name = "Tetris";
        String target = "test_target" + File.separator + name;
        String[] args = { "-target", target, "-mode", "rust" };
        
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
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(30, errorMessages.size());
        
        rujava.printOutputMessages();
        
        rujava.unbuild();
    }
    
    @Test
    public void testDrawTool() {
        String name = "DrawTool";
        String target = "test_target" + File.separator + name;
        String[] args = { "-target", target, "-mode", "rust" };
        
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
        List<OutputMessage> errorMessages = rujava.getErrorMessages(projects.get(0));
        
        assertEquals(130, errorMessages.size());
        
        rujava.printOutputMessages();
        
        rujava.unbuild();
    }
}
