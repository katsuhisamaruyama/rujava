/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

import org.jtool.rujava.collector.AnnotationCollector;
import org.jtool.rujava.detector.ErrorDetector;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuLocalVariable;
import org.jtool.rujava.collector.RuFormalVariable;

import org.jtool.jxplatform.builder.ModelBuilder;
import org.jtool.jxplatform.builder.ModelBuilderBatch;
import org.jtool.jxplatform.builder.CommandLineOptions;
import org.jtool.jxplatform.project.ModelBuilderBatchImpl;
import org.jtool.srcmodel.JavaProject;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class RuJava {
    
    private final String projectName;
    private final String projectPath;
    private final String classpath;
    private final String srcpath;
    private final String binpath;
    private final String autoCheckEnv;
    
    private final String filename;
    
    private ModelBuilder builder;
    
    private final boolean allowImmutableToMutable;
    private final boolean allowMutableBorrow;
    
    private final boolean analyzingBytecode = true;
    private final boolean useCache = true;
    
    private final boolean analysisVerbose = false;
    private final boolean verbose = true;
    
    private List<JavaProject> targetProjects;
    
    private final List<RuProject> checkedProjects = new ArrayList<>();
    
    public RuJava(String[] args) {
        String cdir = new File(".").getAbsoluteFile().getParent();
        CommandLineOptions options = new CommandLineOptions(args);
        
        String target = removeLastFileSeparator(options.get("-target", "."));
        
        this.projectName = options.get("-name", getProjectName(target, cdir));
        this.projectPath = ModelBuilderBatchImpl.getFullPath(target, cdir);
        this.classpath = getPath(target, options.get("-classpath", target));
        
        autoCheckEnv = options.get("auto-check-env", "yes");
        if (autoCheckEnv.equals("yes")) {
            this.srcpath = target;
            this.binpath = target;
        } else {
            this.srcpath = getPath(target, options.get("-srcpath", target));
            this.binpath = getPath(target, options.get("-binpath", target));
        }
        
        this.filename = options.get("-file", "");
        
        Logger.setVerbose(verbose);
        
        String mode = options.get("-mode", "java");
        if (mode.equals("java")) {
            RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
            RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
            
            this.allowImmutableToMutable = true;
            this.allowMutableBorrow = true;
        } else if (mode.equals("java-auto")) {
            RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.AUTO);
            RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.AUTO);
            
            this.allowImmutableToMutable = true;
            this.allowMutableBorrow = true;
            
        } else if (mode.equals("java-loose")) {
            RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
            RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.IMMUTABLE);
            
            this.allowImmutableToMutable = true;
            this.allowMutableBorrow = true;
            
        } else if (mode.equals("java-strict")) {
            RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
            RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.IMMUTABLE);
            
            this.allowImmutableToMutable = false;
            this.allowMutableBorrow = false;
            
        } else if (mode.equals("rust")) {
            RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.OWNED, MutabilityAnnotation.IMMUTABLE);
            RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.OWNED, MutabilityAnnotation.IMMUTABLE);
            
            this.allowImmutableToMutable = true;
            this.allowMutableBorrow = false;
        } else {
            System.err.println("\"-mode java\" is automatically specified");
            System.err.println("\"-mode\" must be one of (java, java-auto, java-loose, java-strict, and rust)");
            
            RuLocalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
            RuFormalVariable.setDefaultAnnotations(OwnershipAnnotation.BORROW, MutabilityAnnotation.MUTABLE);
            
            this.allowImmutableToMutable = true;
            this.allowMutableBorrow = true;
        }
    }
    
    public void setVerbose(boolean verbose) {
        Logger.setVerbose(verbose);
    }
    
    private String removeLastFileSeparator(String path) {
        if (path.charAt(path.length() - 1) == File.separatorChar) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }
    
    private String getProjectName(String target, String cdir) {
        String name = removeLastFileSeparator(target);
        if (name.startsWith(cdir)) {
            name = name.substring(cdir.length() + 1);
        }
        int index = name.lastIndexOf(File.separatorChar + "src");
        if (index > 0) {
            name = name.substring(0, index);
        }
        int index2 = name.lastIndexOf(File.separatorChar);
        if (index2 > 0) {
            name = name.substring(index2 + 1);
        }
        return name;
    }
    
    private String getPath(String target, String option) {
        if (option == null) {
            return target;
        }
        
        String path[] = option.split(File.pathSeparator);
        for (int i = 0; i < path.length; i++) {
            if (!path[i].startsWith(File.separator)) {
                path[i] = target + File.separator + path[i];
            }
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            buf.append(File.pathSeparator);
            buf.append(path[i]);
        }
        return buf.substring(1);
    }
    
    public List<JavaProject> build() {
        if (!new File(projectPath).isDirectory()) {
            System.err.println("Not found the project: " + projectPath);
            return new ArrayList<JavaProject>();
        }
        
        String filepath = projectPath + File.separatorChar + filename;
        if (filename.length() != 0 && !new File(filepath).isFile()) {
            System.err.println("Not found the file: " + filepath);
            return new ArrayList<JavaProject>();
        }
        
        Logger.print("-Building source models");
        
        builder = new ModelBuilderBatch(analyzingBytecode, useCache);
        builder.setConsoleVisible(analysisVerbose);
        
        if (autoCheckEnv.equals("yes")) {
            targetProjects = builder.build(projectName, projectPath);
        } else {
            List<JavaProject> projects = new ArrayList<>();
            projects.add(builder.build(projectName, projectPath, classpath, srcpath, binpath));
            targetProjects = projects;
        }
        return targetProjects;
    }
    
    public void unbuild() {
        builder.unbuild();
    }
    
    public List<RuProject> detect() {
        Logger.print("-Checking source code for " + targetProjects.size() + " project(s)");
        
        for (JavaProject targetProject : targetProjects) {
            Logger.print("*Target: " + targetProject.getName());
            
            try {
                AnnotationCollector collector = new AnnotationCollector(targetProject);
                RuProject project;
                if (filename.length() == 0) {
                    project = collector.collectWhole();
                } else {
                    project = collector.collect(filename);
                }
                
                ErrorDetector detector = new ErrorDetector(project, allowImmutableToMutable, allowMutableBorrow);
                detector.detect();
                
                checkedProjects.add(project);
            } catch (IOException e) {
                Logger.printError(e.getMessage());
            }
        }
        return checkedProjects;
    }
    
    public List<OutputMessage> getWarningMessages(RuProject project) {
        List<OutputMessage> allWarningMessages = new ArrayList<>();
        
        List<RuMethod> methods = RuMethod.sort(project.getMethods());
        for (RuMethod method : methods) {
            List<OutputMessage> warningMessages = OutputMessage.sort(method.getWarningMessages());
            allWarningMessages.addAll(warningMessages);
        }
        return allWarningMessages;
    }
    
    public List<OutputMessage> getErrorMessages(RuProject project) {
        List<OutputMessage> allErrorMessages = new ArrayList<>();
        
        List<RuMethod> methods = RuMethod.sort(project.getMethods());
        for (RuMethod method : methods) {
            List<OutputMessage> errorMessages = OutputMessage.sort(method.getErrorMessages());
            allErrorMessages.addAll(errorMessages);
        }
        return allErrorMessages;
    }
    
    public void printOutputMessages() {
        for (RuProject project : checkedProjects) {
            Logger.print("*Target: " + project.getName());
            
            int messages = 0;
            List<RuMethod> methods = RuMethod.sort(project.getMethods());
            for (RuMethod method : methods) {
                List<OutputMessage> warningMessages = OutputMessage.sort(method.getWarningMessages());
                List<OutputMessage> errorMessages = OutputMessage.sort(method.getErrorMessages());
                
                if (warningMessages.size() > 0 || errorMessages.size() > 0) {
                    System.out.println("!Method: " + method.getQualifiedName());
                    warningMessages.forEach(mesg -> System.out.println(mesg));
                    errorMessages.forEach(mesg -> System.out.println(mesg));
                    
                    messages = messages + warningMessages.size() + errorMessages.size();
                }
            }
            System.out.println("-Found " + messages + " errors/warnings in " + project.getName());
        }
        System.out.println();
    }
    
    public void check() {
        build();
        detect();
        printOutputMessages();
        unbuild();
    }
    
    public static void main(String[] args) {
        RuJava rujava = new RuJava(args);
        rujava.check();
    }
}
