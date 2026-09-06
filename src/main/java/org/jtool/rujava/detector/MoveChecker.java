/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.ErrorMessage;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.collector.DataFlow;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuStatement;
import org.jtool.rujava.collector.RuVariable;

import org.jtool.srcmodel.JavaFile;
import org.jtool.cfg.CFG;
import org.jtool.cfg.CFGNode;
import org.jtool.cfg.CFGStatement;
import org.jtool.cfg.JVariableReference;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.Map;

public class MoveChecker extends Checker {
    
    public MoveChecker() {
    }
    
    public void check(RuProject project) {
        for (RuMethod method : project.getMethods()) {
            // ErrorDetector.print(method);
            checkAssignment(method);
            checkMethodCall(method, project);
        }
    }
    
    public void checkAssignment(RuMethod method) {
        for (RuStatement st : method.getStatements()) {
            if (isMoveAssignment(st)) {
                RuVariable use = getMovedVariable(st);
                if (use != null) {
                    List<RuStatement> ends = getRedefineStatements(method, st, use);
                    List<RuStatement> statements = usesMovedVariable(method, st, ends, use);
                    JavaFile jfile = method.getJaveMethod().getFile();
                    
                    for (RuStatement s : statements) {
                        List<ASTNode> errorNodes = getErrorNodes(s, s.getDefFirst(), s.getUseFirst());
                        method.addErrorMessage(new ErrorMessage(2, "AccessMoved", jfile, errorNodes));
                    }
                }
            }
        }
    }
    
    private boolean isMoveAssignment(RuStatement st) {
        CFGStatement stNode = st.getCFGStatement();
        if (!stNode.isActual() && st.isDefSingle() && st.isUseSingle()) {
            RuVariable lefthand = st.getDefFirst();
            if (!lefthand.isPrimitiveType() && lefthand.getOwnershipAnnotation() == OwnershipAnnotation.OWNED) {
                return true;
            }
        }
        return false;
    }
    
    private RuVariable getMovedVariable(RuStatement st) {
        RuVariable righthand = st.getUseFirst();
        if (righthand.getOwnershipAnnotation() == OwnershipAnnotation.OWNED && !righthand.isReturnValueReference()) {
            return righthand;
        }
        return null;
    }
    
    private List<RuStatement> getRedefineStatements(RuMethod method, RuStatement st, RuVariable var) {
        List<RuStatement> srcNodes = method.getSrcStatementsOfDataFlow(st, var, DataFlow.Kind.defuse);
        List<RuStatement> dstNodes = srcNodes.stream()
                .flatMap(n -> method.getDstStatementsOfDataFlow(n, var, DataFlow.Kind.defdef).stream()).toList();
        return dstNodes;
    }
    
    private List<RuStatement> usesMovedVariable(RuMethod method,
            RuStatement start, List<RuStatement> ends, RuVariable var) {
        List<CFGNode> endNodes = ends.stream().map(n -> (CFGNode)n.getCFGStatement()).toList();
        JVariableReference jvar = var.getJVariableReference();
        
        CFG cfg = method.getCFG();
        List<CFGNode> rechableNodes = start.getCFGStatement().getSuccessors().stream()
            .flatMap(n -> cfg.forwardReachableNodes(n, true, false,
                    node -> { return endNodes.contains(node); }).stream()).toList();
        
        List<CFGStatement> usedNodes = rechableNodes.stream()
                .filter(n -> n.isStatement()).map(n -> (CFGStatement)n).filter(n -> n.useVariable(jvar)).toList();
        return method.getRuStatements(usedNodes);
    }
    
    private void checkMethodCall(RuMethod method, RuProject project) {
        Map<RuStatement, RuVariable> formals = project.getFormalInsForCalledMethods(method);
        for (RuStatement actualIn : formals.keySet()) {
            RuVariable formal = formals.get(actualIn);
            if (formal.getOwnershipAnnotation() == OwnershipAnnotation.OWNED) {
                
                RuVariable use = getMovedVariable(actualIn);
                if (use != null) {
                    List<RuStatement> ends = getRedefineStatements(method, actualIn, use);
                    List<RuStatement> statements = usesMovedVariable(method, actualIn, ends, use);
                    JavaFile jfile = method.getJaveMethod().getFile();
                    
                    for (RuStatement s : statements) {
                        List<ASTNode> errorNodes = getErrorNodes(s, s.getDefFirst(), s.getUseFirst());
                        method.addErrorMessage(new ErrorMessage(2, "AccessMoved", jfile, errorNodes));
                    }
                }
            }
        }
    }
}