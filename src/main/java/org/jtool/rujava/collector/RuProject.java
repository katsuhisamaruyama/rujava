/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.srcmodel.JavaProject;
import org.jtool.cfg.CFGMethodCall;
import org.jtool.cfg.CFGParameter;
import org.jtool.cfg.CFGStatement;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RuProject {
    
    private final JavaProject targetProject;
    
    private final Map<String, RuMethod> methodMap = new HashMap<>();
    
    RuProject(JavaProject targetProject, List<RuMethod> methods) {
        this.targetProject = targetProject;
        methods.forEach(m -> methodMap.put(m.getQualifiedName(), m));
    }
    
    public JavaProject getTargetProject() {
        return targetProject;
    }
    
    public String getName() {
        return targetProject.getName();
    }
    
    public List<RuMethod> getMethods() {
        return methodMap.values().stream().toList();
    }
    
    public RuMethod getMethod(String fqn) {
        return methodMap.get(fqn);
    }
    
    public Map<RuStatement, RuVariable> getFormalInsForCalledMethods(RuMethod method) {
        Map<RuStatement, RuVariable> formalIns = new HashMap<>();
        for (RuStatement st : method.getStatements()) {
            CFGParameter actualInNode = getActualIn(st);
            if (actualInNode != null) {
                RuMethod calledMethod = getCalledMethod(actualInNode);
                if (calledMethod != null) {
                    RuVariable actual = st.getUseFirst();
                    if (!actual.isPrimitiveType()) {
                        RuVariable formal = getFormalForVariableArguments(calledMethod, actualInNode.getIndex());
                        if (formal != null) {
                            formalIns.put(st, formal);
                        }
                    }
                }
            }
        }
        return formalIns;
    }
    
    private RuVariable getFormalForVariableArguments(RuMethod calledMethod, int index) {
        while (index >= 0) {
            RuVariable formal = calledMethod.getParameter(index);
            if (formal != null) {
                return formal;
            }
            index--;
        }
        return null;
    }
    
    public CFGParameter getActualIn(RuStatement st) {
        CFGStatement stNode = st.getCFGStatement();
        if (stNode.isActualIn() && st.isUseSingle()) {
            return (CFGParameter)stNode;
        }
        return null;
    }
    
    public RuMethod getCalledMethod(CFGParameter actualInNode) {
        CFGMethodCall callNode = (CFGMethodCall)actualInNode.getParent();
        String methodFqn = callNode.getQualifiedName().fqn();
        return methodMap.get(methodFqn);
    }
}
