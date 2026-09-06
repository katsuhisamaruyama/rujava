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
import org.jtool.cfg.CFGMethodCall;
import org.jtool.cfg.CFGNode;
import org.jtool.cfg.CFGStatement;
import org.jtool.cfg.CFGParameter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Block;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

public class ReferenceChecker extends Checker {
    
    public ReferenceChecker() {
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
            if (isBorrowAssignment(st)) {
                RuVariable use = getBorrowedVariable(st);
                
                if (use != null) {
                    List<CFGStatement> nodesInScope = getNodesInScope(method, use.getDeclaration());
                    checkScope(method, st, nodesInScope);
                }
            }
        }
    }
    
    private boolean isBorrowAssignment(RuStatement st) {
        CFGStatement stNode = st.getCFGStatement();
        if (!stNode.isActual() && st.isDefSingle() && st.isUseSingle()) {
            RuVariable lefthand = st.getDefFirst();
            if (!lefthand.isPrimitiveType() && lefthand.getOwnershipAnnotation() == OwnershipAnnotation.BORROW) {
                return true;
            }
        }
        return false;
    }
    
    private RuVariable getBorrowedVariable(RuStatement st) {
        RuVariable righthand = st.getUseFirst();
        if (!righthand.isReturnValueReference()) {
            return righthand;
        }
        return null;
    }
    
    private void checkMethodCall(RuMethod method, RuProject project) {
        Map<RuStatement, RuVariable> formals = project.getFormalInsForCalledMethods(method);
        for (RuStatement actualIn : formals.keySet()) {
            
            RuVariable formal = formals.get(actualIn);
            if (formal.getOwnershipAnnotation() == OwnershipAnnotation.BORROW) {
                CFGParameter actualInNode = (CFGParameter)actualIn.getCFGStatement();
                
                RuMethod calledMethod = project.getCalledMethod(actualInNode);
                if (calledMethod.getParameterSize() == 1 &&
                    calledMethod.getOwnershipAnnotation() == OwnershipAnnotation.BORROW) {
                    
                    RuVariable use = getBorrowedVariable(actualIn);
                    if (use != null) {
                        List<CFGStatement> nodesInScope = getNodesInScope(method, use.getDeclaration());
                        RuStatement st = getStatement(method, actualIn);
                        if (st != null) {
                            checkScope(method, st, nodesInScope);
                        }
                    }
                }
            }
        }
    }
    
    private RuStatement getStatement(RuMethod method, RuStatement actualIn) {
        CFGParameter actualInNode = (CFGParameter)actualIn.getCFGStatement();
        CFGMethodCall methodCall = (CFGMethodCall)actualInNode.getParent();
        CFGParameter actualOutNode = (CFGParameter)methodCall.getActualOut();
        if (actualOutNode != null) {
            RuStatement actualOut = method.getRuStatement(actualOutNode);
            RuVariable var = actualOut.getDefFirst();
            return method.getDstStatementsOfDataFlow(actualOut, var, DataFlow.Kind.defuse).get(0);
        }
        return null;
    }
    
    private void checkScope(RuMethod method, RuStatement st, List<CFGStatement> nodesInScope) {
        for (DataFlow dataflow : method.getDataFlows(DataFlow.Kind.defuse)) {
            if (!dataflow.getVariable().isPrimitiveType() && dataflow.getSrcNode().equals(st)) {
                RuStatement dst = dataflow.getDstNode();
                CFGStatement useNode = dst.getCFGStatement();
                
                if (!nodesInScope.contains(useNode)) {
                    JavaFile jfile = method.getJaveMethod().getFile();
                    
                    List<ASTNode> errorNodes = getErrorNodes(dst, dst.getDefFirst(), dataflow.getVariable());
                    method.addErrorMessage(new ErrorMessage(2, "AccessBorrowed", jfile, errorNodes));
                }
            }
        }
    }
    
    private List<CFGStatement> getNodesInScope(RuMethod method, CFGNode decl) {
        if (decl.isFormalIn()) {
            return method.getStatements().stream().map(s -> s.getCFGStatement()).toList();
        }
        
        Statement statement = getStatement(decl.getASTNode());
        if (statement != null) {
            ASTNode top = statement;
            ASTNode parent = statement.getParent();
            if (parent instanceof Block) {
                top = parent;
            }
            
            ASTNodeCollector collector = new ASTNodeCollector(top);
            Set<ASTNode> astNodes = collector.getNodes();
            
            return method.getStatements().stream()
                    .map(s -> s.getCFGStatement())
                    .filter(n -> astNodes.contains(n.getASTNode())).toList();
        }
        return new ArrayList<>(); 
    }
    
    private Statement getStatement(ASTNode node) {
        while (!(node instanceof MethodDeclaration)) {
            if (node instanceof Statement) {
                return (Statement)node;
            }
            node = node.getParent();
        }
        return null;
    }
    
    private class ASTNodeCollector extends ASTVisitor {
        
        private Set<ASTNode> nodes = new HashSet<>();
        
        public ASTNodeCollector(ASTNode node) {
            node.accept(this);
        }
        
        public Set<ASTNode> getNodes() {
            return nodes;
        }
        
        @Override
        public void preVisit(ASTNode node) {
            if (CFGNode.isStatement(node) || CFGNode.isExpression(node) || node instanceof Name) {
                nodes.add(node);
            }
        }
    }
}
