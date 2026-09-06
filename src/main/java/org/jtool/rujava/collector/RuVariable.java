/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.cfg.CFG;
import org.jtool.cfg.CFGNode;
import org.jtool.cfg.CFGStatement;
import org.jtool.cfg.JVariableReference;
import org.jtool.cfg.JLocalVarReference;
import org.jtool.cfg.StopConditionOnReachablePath;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class RuVariable extends AnnotationDeclaration {
    
    private final JVariableReference jvar;
    
    private RuStatement statement;
    
    private CFGNode declnode = null;
    
    RuVariable(JVariableReference jvar) {
        assert jvar != null;
        this.jvar = jvar;
    }
    
    static RuVariable createRuVariable(JVariableReference jvar) {
        if (jvar.isLocalAccess()) {
            JLocalVarReference v = (JLocalVarReference)jvar;
            if (v.isParameter()) {
                return new RuFormalVariable(jvar);
            }
        }
        return new RuLocalVariable(jvar);
    }
    
    public JVariableReference getJVariableReference() {
        return jvar;
    }
    
    public String getQualifiedName() {
        return jvar.getQualifiedName().fqn();
    }
    
    public boolean isPrimitiveType() {
        return jvar.isPrimitiveType();
    }
    
    public boolean isReturnValueReference() {
        return jvar.isReturnValueReference();
    }
    
    void setStatement(RuStatement st) {
        this.statement = st;
    }
    
    public RuStatement getStatement() {
        return statement;
    }
    
    public void setDeclaration(CFGNode declnode) {
        this.declnode = declnode;
    }
    
    public CFGNode getDeclaration() {
        return declnode;
    }
    
    public boolean isFormal() {
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RuVariable) ? equals((RuVariable)obj) : false;
    }
    
    public boolean equals(RuVariable var) {
        return var != null && jvar.equals(var.jvar);
    }
    
    @Override
    public int hashCode() {
        return jvar.hashCode();
    }
    
    @Override
    public String toString() {
        String name = jvar.getQualifiedName().fqn();
        return name.substring(name.indexOf("!") + 1) + "@" + jvar.getType() + getAnnotaionString();
    }
    
    public String toShortString() {
        String name = getQualifiedName();
        return name.substring(name.indexOf("!") + 1);
    }
    
    public String toLongString() {
        String name = getQualifiedName();
        return name.substring(name.indexOf("!") + 1) + getAnnotaionString();
    }
    
    private String getAnnotaionString() {
        if (mutabilityAnnotation == null || ownershipAnnotation == null) {
            return "";
        }
        return "[" + mutabilityAnnotation.name() + "/" + ownershipAnnotation.name() + "]";
    }
    
    public void findDeclaration(CFGStatement stNode, CFG cfg) {
        if (!jvar.isLocalAccess()) {
            return;
        }
        
        cfg.backwardReachableNodes(stNode, true, true, new StopConditionOnReachablePath() {
            
            @Override
            public boolean isStop(CFGNode node) {
                if (node.isLocalDeclaration() || node.isFormalIn() || node.isEnhancedFor()) {
                    CFGStatement declnode = (CFGStatement)node;
                    if (declnode.defineVariable(jvar)) {
                        setDeclaration(declnode);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    
    public static List<RuVariable> sort(List<RuVariable> variables) {
        List<RuVariable> collection = new ArrayList<>(variables);
        collection.sort(Comparator.comparing((RuVariable v) -> v.toShortString()));
        return collection;
    }
}
