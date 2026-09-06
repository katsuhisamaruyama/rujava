/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.WarningMessage;
import org.jtool.rujava.Logger;

import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaMethod;
import org.jtool.cfg.CFGMethodCall;
import org.jtool.cfg.CFGStatement;
import org.jtool.cfg.CFGParameter;
import org.jtool.cfg.JVariableReference;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.EnhancedForStatement;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

public class AnnotationCollector {
    
    private final JavaProject targetProject;
    
    private final DataFlowFinder finder;
    
    public AnnotationCollector(JavaProject targetProject) {
        this.targetProject = targetProject;
        this.finder = new DataFlowFinder(targetProject);
    }
    
    public RuProject collectWhole() throws IOException {
        Logger.print("-Collecting dataflows");
        
        List<JavaClass> classes = targetProject.getClasses();
        if (classes.size() == 0) {
            throw new IOException("** Found no classes: " + targetProject.getName());
        }
        
        List<RuMethod> methods = collect(classes);
        
        return new RuProject(targetProject, methods);
    }
    
    public RuProject collect(String filename) throws IOException {
        Logger.print("-Collecting dataflows for " + filename);
        
        String filePath = targetProject.getPath() + filename;
        JavaFile jfile = targetProject.getFile(filePath);
        if (jfile == null) {
            throw new IOException("** Cannot compile the file: " + filename);
        }
        
        Set<JavaClass> classes = new HashSet<>();
        for (JavaClass jclass : jfile.getClasses()) {
            collectUsedClasses(jclass, classes);
            classes.add(jclass);
        }
        if (classes.size() == 0) {
            throw new IOException("** Found no classes: " + targetProject.getName());
        }
        
        List<RuMethod> methods = collect(new ArrayList<JavaClass>(classes));
        return new RuProject(targetProject, methods);
    }
    
    private void collectUsedClasses(JavaClass jclass, Set<JavaClass> classes) {
        if (classes.contains(jclass)) {
            return;
        }
        if (jclass != null && targetProject.getClass(jclass.getQualifiedName().fqn()) != null) {
            for (JavaClass jc : jclass.getAncestors()) {
                if (jc.isInProject() && !classes.contains(jc)) {
                    classes.add(jc);
                    collectUsedClasses(jc, classes);
                }
            }
            for (JavaClass jc: jclass.getEfferentClassesInProject()) {
                if (jc.isInProject() && !classes.contains(jc)) {
                    classes.add(jc);
                    collectUsedClasses(jc, classes);
                }
            }
        }
    }
    
    private List<RuMethod> collect(List<JavaClass> classes) {
        List<JavaMethod> jmethods = classes.stream()
                .flatMap(jc -> jc.getMethods().stream()).toList();
        List<RuMethod> methods = finder.find(jmethods);
        
        Map<String, RuMethod> methodMap = new HashMap<>();
        methods.forEach(m -> methodMap.put(m.getQualifiedName(), m));
        
        collectAnnotations(methodMap);
        return methods;
    }
    
    private void setAutoImmutabilityAnnotations(RuMethod method) {
        for (DataFlow dataflow : method.getDataFlows()) {
            RuVariable var = dataflow.getVariable();
            if (!var.isPrimitiveType()) {
                if (var.getMutabilityAnnotation() == MutabilityAnnotation.AUTO) {
                    
                    if (dataflow.isDefDef() || dataflow.isDefDefCall()) {
                        var.setAnnotation(MutabilityAnnotation.MUTABLE);
                        
                        for (DataFlow d : method.getDataFlows()) {
                            if (var.getDeclaration().equals(d.getVariable().getDeclaration())) {
                                d.getVariable().setAnnotation(MutabilityAnnotation.MUTABLE);
                            }
                        }
                    } else {
                        var.setAnnotation(MutabilityAnnotation.IMMUTABLE);
                    }
                }
            }
        }
    }
    
    private void setAutoOwnershipAnnotations(RuMethod method) {
        for (DataFlow dataflow : method.getDataFlows()) {
            RuVariable var = dataflow.getVariable();
            if (!var.isPrimitiveType()) {
                if (var.getMutabilityAnnotation() == MutabilityAnnotation.AUTO) {
                    
                    if (dataflow.isDefDef() || dataflow.isDefDefCall()) {
                        var.setAnnotation(MutabilityAnnotation.MUTABLE);
                        
                        for (DataFlow d : method.getDataFlows()) {
                            if (var.getDeclaration().equals(d.getVariable().getDeclaration())) {
                                d.getVariable().setAnnotation(MutabilityAnnotation.MUTABLE);
                            }
                        }
                    } else {
                        var.setAnnotation(MutabilityAnnotation.IMMUTABLE);
                    }
                }
            }
        }
    }
    
    private void collectAnnotations(Map<String, RuMethod> methodMap) {
        Logger.print("-Collecting annotations for methods");
        methodMap.values().forEach(m -> collectAnnotationsForMethod(m, methodMap));
        
        Logger.print("-Collecting annotations within methods");
        methodMap.values().forEach(m -> collectAnnotationsWithinMethod(m, methodMap));
        
        methodMap.values().forEach(m -> setAutoImmutabilityAnnotations(m));
        methodMap.values().forEach(m -> setAutoOwnershipAnnotations(m));
        
        Map<RuMethod, List<RuMethod>> overriddingMethodMap = new HashMap<>();
        Logger.print("-Checking annotations for methods having the overriding relationships");
        methodMap.values().forEach(m -> overriddingMethodMap.put(m, checkOverridingAnnotations(m, methodMap)));
        
        Logger.print("-Checking annotations for parameters of methods having the overriding relationships");
        methodMap.values().forEach(m -> checkOverridingParameterAnnotations(m, overriddingMethodMap.get(m)));
        
        methodMap.values().forEach(m -> m.cleanDataFlows());
    }
    
    private void collectAnnotationsForMethod(RuMethod method, Map<String, RuMethod> methodMap) {
        ASTNode node = method.getJaveMethod().getASTNode();
        if (node instanceof MethodDeclaration) {
            MethodDeclaration methodDecl = (MethodDeclaration)node;
            @SuppressWarnings("unchecked")
            List<String> names = getAnnotaionNames((List<IExtendedModifier>)methodDecl.modifiers());
            collectAnnotationsForMethodDeclaration(method, names);
        }
    }
    
    private void collectAnnotationsForMethodDeclaration(RuMethod method, List<String> names) {
        checkCoexistenceOfMutabilityAnnotations(method, names);
        checkCoexistenceOfOwnershipAnnotations(method, names);
        
        JavaMethod jmethod = method.getJaveMethod();
        if (jmethod.isVoid() || jmethod.isPrimitiveReturnType()) {
            if (existsAnyAnnotation(names)) {
                JavaFile jfile = jmethod.getFile();
                method.addWarningMessage(new WarningMessage(3, "PrimitiveTypeReturn", jfile, jmethod.getASTNode()));
            }
            return;
        }
        
        method.setAnnotation(getMutabilityAnnotation(names));
        method.setAnnotation(getOwnershipAnnotation(names));
    }
    
    private boolean checkCoexistenceOfMutabilityAnnotations(RuMethod method, List<String> names) {
        JavaMethod jmethod = method.getJaveMethod();
        if (existsExclusiveMutabilityAnnotations(names)) {
            JavaFile jfile = jmethod.getFile();
            method.addWarningMessage(new WarningMessage(1, "CoexistenceMutability", jfile, jmethod.getASTNode()));
            return true;
        }
        return false;
    }
    
    private boolean checkCoexistenceOfOwnershipAnnotations(RuMethod method, List<String> names) {
        JavaMethod jmethod = method.getJaveMethod();
        if (existsExclusiveOwnershipAnnotations(names)) {
            JavaFile jfile = jmethod.getFile();
            method.addWarningMessage(new WarningMessage(2, "CoexistenceOwnership", jfile, jmethod.getASTNode()));
            return true;
        }
        return false;
    }
    
    private List<String> getAnnotaionNames(List<IExtendedModifier> modifiers) {
        List<String> names = new ArrayList<>();
        for (IExtendedModifier mod : modifiers) {
            if (mod.isAnnotation()) {
                Annotation anno = (Annotation)mod;
                Name name = anno.getTypeName();
                names.add(name.getFullyQualifiedName());
            }
        }
        return names;
    }
    
    private boolean existsExclusiveMutabilityAnnotations(List<String> names) {
        boolean mutable = existsAnnotation(MutabilityAnnotation.MUTABLE.name(), names);
        boolean immutable = existsAnnotation(MutabilityAnnotation.IMMUTABLE.name(), names);
        return mutable && immutable;
    }
    
    private boolean existsExclusiveOwnershipAnnotations(List<String> names) {
        boolean mutable = existsAnnotation(OwnershipAnnotation.OWNED.name(), names);
        boolean immutable = existsAnnotation(OwnershipAnnotation.BORROW.name(), names);
        return mutable && immutable;
    }
    
    private boolean existsAnyAnnotation(List<String> names) {
        boolean mutable = existsAnnotation(MutabilityAnnotation.MUTABLE.name(), names);
        boolean immutable = existsAnnotation(MutabilityAnnotation.IMMUTABLE.name(), names);
        boolean owned = existsAnnotation(OwnershipAnnotation.OWNED.name(), names);
        boolean borrow = existsAnnotation(OwnershipAnnotation.BORROW.name(), names);
        return mutable || immutable || owned || borrow;
    }
    
    private boolean existsAnnotation(String annotation, List<String> names) {
        return names.stream().map(n -> n.toUpperCase())
                .anyMatch(n -> n.equals(annotation));
    }
    
    private MutabilityAnnotation getMutabilityAnnotation(List<String> names) {
        if (existsAnnotation(MutabilityAnnotation.IMMUTABLE.name(), names)) {
            return MutabilityAnnotation.IMMUTABLE;
        } else if (existsAnnotation(MutabilityAnnotation.MUTABLE.name(), names)) {
            return MutabilityAnnotation.MUTABLE;
        }
        return MutabilityAnnotation.NONE;
    }
    
    private OwnershipAnnotation getOwnershipAnnotation(List<String> names) {
        if (existsAnnotation(OwnershipAnnotation.OWNED.name(), names)) {
            return OwnershipAnnotation.OWNED;
        } else if (existsAnnotation(OwnershipAnnotation.BORROW.name(), names)) {
            return OwnershipAnnotation.BORROW;
        }
        return OwnershipAnnotation.NONE;
    }
    
    private void collectAnnotationsWithinMethod(RuMethod method, Map<String, RuMethod> methodMap) {
        for (DataFlow dataflow : method.getDataFlows()) {
            RuVariable var = dataflow.getVariable();
            JVariableReference jvar = var.getJVariableReference();
            RuStatement src = dataflow.getSrcNode();
            
            if (jvar.isLocalAccess()) {
                var.findDeclaration(src.getCFGStatement(), method.getCFG());
                collectAnnotationsByDeclaration(var, method);
                
                RuStatement dst = dataflow.getSrcNode();
                RuVariable def = dst.getDefFirst();
                if (def != null) {
                    def.findDeclaration(dst.getCFGStatement(), method.getCFG());
                }
            } else if (jvar.isReturnValueReference()) {
                RuMethod calledMethod = getCalledMethod(src.getCFGStatement(), methodMap);
                collectAnnotationsByMethodCall(var, calledMethod);
            }
        }
    }
    
    private void collectAnnotationsByDeclaration(RuVariable var, RuMethod method) {
        if (var.getDeclaration() == null) {
            return;
        }
        
        ASTNode node = var.getDeclaration().getASTNode();
        if (node instanceof SingleVariableDeclaration) {
            SingleVariableDeclaration varDecl = (SingleVariableDeclaration)node;
            
            @SuppressWarnings("unchecked")
            List<String> names = getAnnotaionNames((List<IExtendedModifier>)varDecl.modifiers());
            collectAnnotationsByVariableDeclaration(var, method, names, varDecl);
            
        } else if (node instanceof VariableDeclarationFragment) {
            if (node.getParent() instanceof VariableDeclarationExpression) {
                VariableDeclarationExpression varDecl = (VariableDeclarationExpression)node.getParent();
                
                @SuppressWarnings("unchecked")
                List<String> names = getAnnotaionNames((List<IExtendedModifier>)varDecl.modifiers());
                collectAnnotationsByVariableDeclaration(var, method, names, varDecl);
                
            } else if (node.getParent() instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement varDecl = (VariableDeclarationStatement)node.getParent();
                
                @SuppressWarnings("unchecked")
                List<String> names = getAnnotaionNames((List<IExtendedModifier>)varDecl.modifiers());
                collectAnnotationsByVariableDeclaration(var, method, names, varDecl);
            }
        } else if (node instanceof EnhancedForStatement) {
            EnhancedForStatement enhancedFor = (EnhancedForStatement)node;
            SingleVariableDeclaration varDecl = (SingleVariableDeclaration)enhancedFor.getParameter();
            
            @SuppressWarnings("unchecked")
            List<String> names = getAnnotaionNames((List<IExtendedModifier>)varDecl.modifiers());
            collectAnnotationsByVariableDeclaration(var, method, names, varDecl);
        }
    }
    
    private void collectAnnotationsByVariableDeclaration(RuVariable var, RuMethod method, List<String> names, ASTNode varDecl) {
        checkCoexistenceOfMutabilityAnnotations(method, names);
        checkCoexistenceOfOwnershipAnnotations(method, names);
        
        JVariableReference jvar = var.getJVariableReference();
        if (jvar.isPrimitiveType()) {
            if (existsAnyAnnotation(names)) {
                JavaMethod jmethod = method.getJaveMethod();
                JavaFile jfile = jmethod.getFile();
                method.addWarningMessage(new WarningMessage(3, "PrimitiveTypeDeclaration", jfile, varDecl));
            }
            return;
        }
        
        var.setAnnotation(getMutabilityAnnotation(names));
        var.setAnnotation(getOwnershipAnnotation(names));
    }
    
    private RuMethod getCalledMethod(CFGStatement stNode, Map<String, RuMethod> methodMap) {
        if (!stNode.isActualOut()) {
            return null;
        }
        
        CFGParameter actualOut = (CFGParameter)stNode;
        CFGMethodCall callNode = (CFGMethodCall)actualOut.getParent();
        if (callNode.isPrimitiveType()) {
            return null;
        }
        
        String methodFqn = callNode.getQualifiedName().fqn();
        return methodMap.get(methodFqn);
    }
    
    private void collectAnnotationsByMethodCall(RuVariable var, RuMethod calledMethod) {
        if (var.getJVariableReference().isPrimitiveType()) {
            return;
        }
        
        if (calledMethod != null) {
            var.setAnnotation(calledMethod.getMutabilityAnnotation());
            var.setAnnotation(calledMethod.getOwnershipAnnotation());
        } else {
            var.setAnnotation(RuMethod.getDefaultMutabilityAnnotation());
            var.setAnnotation(RuMethod.getDefaultOwnershipAnnotation());
        }
    }
    
    private List<RuMethod> checkOverridingAnnotations(RuMethod method, Map<String, RuMethod> methodMap) {
        List<RuMethod> overriddenMethods = getOverriddenMethods(method, methodMap);
        checkOverridingMutabilityReturnAnnotations(method, overriddenMethods);
        checkOverridingOwnershipReturnAnnotations(method, overriddenMethods);
        return overriddenMethods;
    }
    
    private List<RuMethod> getOverriddenMethods(RuMethod method, Map<String, RuMethod> methodMap) {
        List<RuMethod> overriddenMethods = new ArrayList<>();
        for (JavaMethod omethod : method.getJaveMethod().getOverridingMethods()) {
            String methodFqn = omethod.getQualifiedName().fqn();
            RuMethod overriddingMethod = methodMap.get(methodFqn);
            if (overriddingMethod != null) {
                overriddenMethods.add(overriddingMethod);
            }
        }
        return overriddenMethods;
    }
    
    private void checkOverridingMutabilityReturnAnnotations(RuMethod method, List<RuMethod> overriddingMethods) {
        MutabilityAnnotation annotationOfOverriddenMethods = getMutabilityAnnotationAmong(overriddingMethods);
        if (method.getMutabilityAnnotation() == MutabilityAnnotation.MUTABLE) {
            if (annotationOfOverriddenMethods == MutabilityAnnotation.IMMUTABLE) {
                JavaMethod jmethod = method.getJaveMethod();
                JavaFile jfile = jmethod.getFile();
                overriddingMethods.forEach(m -> m.addWarningMessage(
                        new WarningMessage(1, "OverrideImmutable", jfile, jmethod.getASTNode())));
            }
        }
    }
    
    private void checkOverridingOwnershipReturnAnnotations(RuMethod method, List<RuMethod> overriddingMethods) {
        OwnershipAnnotation annotationOfOverriddenMethods = getOwnershipAnnotationAmong(overriddingMethods);
        if (method.getOwnershipAnnotation() == OwnershipAnnotation.BORROW) {
            if (annotationOfOverriddenMethods == OwnershipAnnotation.OWNED) {
                JavaMethod jmethod = method.getJaveMethod();
                JavaFile jfile = jmethod.getFile();
                overriddingMethods.forEach(m -> m.addWarningMessage(
                        new WarningMessage(2, "OverrideBorrow", jfile, jmethod.getASTNode())));
            }
        }
    }
    
    private MutabilityAnnotation getMutabilityAnnotationAmong(List<RuMethod> methods) {
        if (methods.size() == 0) {
            return MutabilityAnnotation.NONE;
        }
        
        if (existsAnnotationInMethods(MutabilityAnnotation.MUTABLE, methods)) {
            return MutabilityAnnotation.MUTABLE;
        } else if (existsAnnotationInMethods(MutabilityAnnotation.IMMUTABLE, methods)) {
            return MutabilityAnnotation.IMMUTABLE;
        }
        return MutabilityAnnotation.NONE;
    }
    
    private OwnershipAnnotation getOwnershipAnnotationAmong(List<RuMethod> methods) {
        if (methods.size() == 0) {
            return OwnershipAnnotation.NONE;
        }
        
        if (existsAnnotationInMethods(OwnershipAnnotation.OWNED, methods)) {
            return OwnershipAnnotation.OWNED;
        } else if (existsAnnotationInMethods(OwnershipAnnotation.BORROW, methods)) {
            return OwnershipAnnotation.BORROW;
        }
        return OwnershipAnnotation.NONE;
    }
    
    private boolean existsAnnotationInMethods(MutabilityAnnotation annotation, List<RuMethod> methods) {
        return methods.stream().anyMatch(m -> m.getMutabilityAnnotation() == annotation);
    }
    
    private boolean existsAnnotationInMethods(OwnershipAnnotation annotation, List<RuMethod> methods) {
        return methods.stream().anyMatch(m -> m.getOwnershipAnnotation() == annotation);
    }
    
    private void checkOverridingParameterAnnotations(RuMethod method, List<RuMethod> overriddingMethods) {
        checkOverridingMutabilityParameterAnnotations(method, overriddingMethods);
        checkOverridingOwnershipParameterAnnotations(method, overriddingMethods);
    }
    
    private void checkOverridingMutabilityParameterAnnotations(RuMethod method, List<RuMethod> overriddingMethods) {
        for (int paramIndex = 0; paramIndex < method.getParameterSize(); paramIndex++) {
            RuVariable param = method.getParameter(paramIndex);
            
            MutabilityAnnotation annotationOfOverriddenMethods = getMutabilityAnnotationAmong(overriddingMethods, paramIndex);
            if (param.getMutabilityAnnotation() == MutabilityAnnotation.MUTABLE) {
                if (annotationOfOverriddenMethods == MutabilityAnnotation.IMMUTABLE) {
                    JavaFile jfile = method.getJaveMethod().getFile();
                    overriddingMethods.forEach(m -> m.addWarningMessage(
                            new WarningMessage(1, "OverrideImmutableParameter", jfile, param.getDeclaration().getASTNode())));
                }
            }
        }
    }
    
    private void checkOverridingOwnershipParameterAnnotations(RuMethod method, List<RuMethod> overriddingMethods) {
        for (int paramIndex = 0; paramIndex < method.getParameterSize(); paramIndex++) {
            RuVariable param = method.getParameter(paramIndex);
            
            OwnershipAnnotation annotationOfOverriddenMethods = getOwnershipAnnotationAmong(overriddingMethods, paramIndex);
            if (param.getOwnershipAnnotation() == OwnershipAnnotation.OWNED) {
                if (annotationOfOverriddenMethods == OwnershipAnnotation.BORROW) {
                    JavaFile jfile = method.getJaveMethod().getFile();
                    overriddingMethods.forEach(m -> m.addWarningMessage(
                            new WarningMessage(1, "OverrideBorrowParameter", jfile, param.getDeclaration().getASTNode())));
                }
            }
        }
    }
    
    private MutabilityAnnotation getMutabilityAnnotationAmong(List<RuMethod> methods, int paramIndex) {
        if (methods.size() == 0) {
            return MutabilityAnnotation.NONE;
        }
        
        List<RuVariable> vars = methods.stream().map(m -> m.getParameter(paramIndex)).filter(v -> v != null).toList();
        if (existsAnnotationInVariables(MutabilityAnnotation.MUTABLE, vars)) {
            return MutabilityAnnotation.MUTABLE;
        } else if (existsAnnotationInVariables(MutabilityAnnotation.IMMUTABLE, vars)) {
            return MutabilityAnnotation.IMMUTABLE;
        }
        return MutabilityAnnotation.NONE;
    }
    
    private OwnershipAnnotation getOwnershipAnnotationAmong(List<RuMethod> methods, int paramIndex) {
        if (methods.size() == 0) {
            return OwnershipAnnotation.NONE;
        }
        
        List<RuVariable> vars = methods.stream().map(m -> m.getParameter(paramIndex)).filter(v -> v != null).toList();
        if (existsAnnotationInVariables(OwnershipAnnotation.OWNED, vars)) {
            return OwnershipAnnotation.OWNED;
        } else if (existsAnnotationInVariables(OwnershipAnnotation.BORROW, vars)) {
            return OwnershipAnnotation.BORROW;
        }
        return OwnershipAnnotation.NONE;
    }
    
    private boolean existsAnnotationInVariables(MutabilityAnnotation annotation, List<RuVariable> vars) {
        return vars.stream().anyMatch(v -> v.getMutabilityAnnotation() == annotation);
    }
    
    private boolean existsAnnotationInVariables(OwnershipAnnotation annotation, List<RuVariable> vars) {
        return vars.stream().anyMatch(v -> v.getOwnershipAnnotation() == annotation);
    }
}
