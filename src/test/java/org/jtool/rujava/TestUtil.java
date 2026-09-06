/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

import org.jtool.rujava.collector.DataFlow;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuVariable;

import org.jtool.jxplatform.builder.ModelBuilder;
import org.jtool.jxplatform.builder.ModelBuilderBatch;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.JavaClass;
import org.jtool.cfg.CFG;
import org.jtool.cfg.CFGEntry;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestUtil {
    
    public static final String BASE_DIR = new File(".").getAbsoluteFile().getParent();
    public static final String EXAMPLE_DIR = BASE_DIR + File.separatorChar + "test_target" + File.separatorChar;
    
    public static JavaProject getCheckerForSimpleProject() {
        String name = "Simple";
        String target = EXAMPLE_DIR + name + File.separatorChar;
        String classPath = target + "lib" + File.separatorChar + "*";
        String srcPath = target + "src";
        String testPath = target + "test";
        String sourcePath = srcPath + File.pathSeparator + testPath;
        String binaryPath = target + "bin";
        
        String[] classPaths = classPath.split(File.pathSeparator);
        String[] sourcePaths = sourcePath.split(File.pathSeparator);
        String[] binaryPaths = binaryPath.split(File.pathSeparator);
        
        ModelBuilder builder = new ModelBuilderBatch(true, true);
        builder.setConsoleVisible(false);
        
        JavaProject targetProject = builder.build(name, target, classPaths, sourcePaths, binaryPaths);
        return targetProject;
    }
    
    public static List<JavaClass> getJavaFile(JavaProject targetProject, String filename) {
        String filePath = targetProject.getPath() + filename;
        JavaFile jfile = targetProject.getFile(filePath);
        
        List<JavaClass> classes = new ArrayList<>();
        classes.addAll(jfile.getClasses());
        return classes;
    }
    
    public static String getMethodsString(List<RuMethod> methods) {
        return RuMethod.sort(methods).stream()
                .map(m -> m.getQualifiedName())
                .collect(Collectors.joining(";"));
    }
    
    public static String getVariablesString(List<RuVariable> vars) {
        return RuVariable.sort(vars).stream()
                .map(v -> v.toShortString())
                .collect(Collectors.joining(";"));
    }
    
    public static String getDataFlowsString(RuMethod method) {
        List<DataFlow> dataflows = method.getDataFlows();
        long cfgId = getCFGId(method);
        return DataFlow.sort(dataflows).stream()
                .map(df -> getDataFlowShort(df, cfgId))
                .collect(Collectors.joining(";"));
    }
    
    public static String getDataFlowsLongString(RuMethod method) {
        List<DataFlow> dataflows = method.getDataFlows();
        long cfgId = getCFGId(method);
        return DataFlow.sort(dataflows).stream()
                .map(df -> getDataFlowLong(df, cfgId))
                .collect(Collectors.joining(";"));
    }
    
    private static long getCFGId(RuMethod method) {
        CFG cfg = method.getCFG();
        CFGEntry entry = cfg.getEntryNode();
        return entry.getId();
    }
    
    private static String getDataFlowShort(DataFlow dataflow, long cfgId) {
        String name = dataflow.getVariable().getQualifiedName();
        String vname = name.substring(name.indexOf("!") + 1);
        return getDataFlow(dataflow, cfgId) + " " + vname;
    }
    
    private static String getDataFlowLong(DataFlow dataflow, long cfgId) {
        String name = dataflow.getVariable().getQualifiedName();
        String vname = name.substring(name.indexOf("!") + 1);
        return getDataFlow(dataflow, cfgId) + " " + vname + " "
                + "[" + dataflow.getVariable().getMutabilityAnnotation().name()
                + "/" + dataflow.getVariable().getOwnershipAnnotation().name() + "]";
    }
    
    private static String getDataFlow(DataFlow dataflow, long cfgId) {
        long src = dataflow.getSrcId() - cfgId;
        long dst = dataflow.getDstId() - cfgId;
        StringBuilder buf = new StringBuilder();
        buf.append(src + " -> " + dst);
        buf.append(" " + dataflow.getKind());
        return buf.toString();
    }
    
    public static RuMethod getMethod(String methodFqn, List<RuMethod> methods) {
        return methods.stream().filter(m -> m.getQualifiedName().equals(methodFqn))
                .findFirst().orElse(null);
    }
    
    public static List<OutputMessage> getWarningMessages(RuProject project) {
        List<RuMethod> sortedMethods = RuMethod.sort(project.getMethods());
        return OutputMessage.sort(sortedMethods.stream().flatMap(m -> m.getWarningMessages().stream()).toList());
    }
    
    public static List<OutputMessage> getErrorMessages(RuProject project) {
        List<RuMethod> sortedMethods = RuMethod.sort(project.getMethods());
        return OutputMessage.sort(sortedMethods.stream().flatMap(m -> m.getErrorMessages().stream()).toList());
    }
    
    public static String getPathString(String baseDir, String[] strs) {
        List<String> strList = Arrays.asList(strs).stream().sorted().map(s -> s.replace(baseDir, "")).toList();
        return String.join(", ", strList);
    }
    
    public static String getPathString(String baseDir, String str) {
        return str.replace(baseDir, "");
    }
    
    public static String getMessage(OutputMessage message) {
        List<String> strList = message.getLineNumbers().stream().map(n -> String.valueOf(n)).toList();
        return message.getLabel() + ":" + String.join(", ", strList);
    }
}
