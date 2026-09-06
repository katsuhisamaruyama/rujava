/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;

import org.jtool.cfg.CFGStatement;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class RuStatement {
    
    private final CFGStatement stNode;
    
    private final List<RuVariable> defs = new ArrayList<>();
    
    private final List<RuVariable> uses = new ArrayList<>();
    
    RuStatement(CFGStatement stNode) {
        this.stNode = stNode;
    }
    
    public CFGStatement getCFGStatement() {
        return stNode;
    }
    
    public long getId() {
        return stNode.getId();
    }
    
    public void addDefVariable(RuVariable var) {
        if (!defs.contains(var)) {
            defs.add(var);
            var.setStatement(this);
        }
    }
    
    public List<RuVariable> getDefVariables() {
        return defs;
    }
    
    public RuVariable getDefFirst() {
        if (hasDefVariable()) {
            return defs.get(0);
        }
        return null;
    }
    
    public boolean hasDefVariable() {
        return defs.size() != 0;
    }
    
    public boolean isDefSingle() {
        return defs.size() == 1;
    }
    
    public void addUseVariable(RuVariable var) {
        if (!uses.contains(var)) {
            uses.add(var);
            var.setStatement(this);
        }
    }
    
    public List<RuVariable> getUseVariables() {
        return uses;
    }
    
    public RuVariable getUseFirst() {
        if (hasUseVariable()) {
            return uses.get(0);
        }
        return null;
    }
    
    public boolean hasUseVariable() {
        return uses.size() != 0;
    }
    
    public boolean isUseSingle() {
        return uses.size() == 1;
    }
    
    public boolean isAssignment() {
        return !stNode.isActual() && isDefSingle() && isUseSingle();
    }
    
    public boolean isAssignment(OwnershipAnnotation ownership, MutabilityAnnotation mutability) {
        if (isAssignment()) {
            RuVariable lefthand = getDefFirst();
            RuVariable righthand = getUseFirst();
            return !lefthand.isPrimitiveType() &&
                   !righthand.isReturnValueReference() &&
                   lefthand.getOwnershipAnnotation() == ownership &&
                   lefthand.getMutabilityAnnotation() == mutability;
        }
        return false;
    }
    
    public boolean isVariableDeclaration(OwnershipAnnotation ownership) {
        if (isAssignment()) {
            RuVariable lefthand = getDefFirst();
            return !lefthand.isPrimitiveType() && lefthand.getOwnershipAnnotation() == ownership;
        }
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RuStatement) ? equals((RuStatement)obj) : false;
    }
    
    public boolean equals(RuStatement statement) {
        return statement != null && stNode.equals(statement.stNode);
    }
    
    @Override
    public int hashCode() {
        return stNode.hashCode();
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(stNode.getId() + " " + stNode.getKind().toString());
        buf.append(" D = { " + toStringForVariables(defs) + " }");
        buf.append(" U = { " + toStringForVariables(uses) + " }");
        return buf.toString();
    }
    
    private String toStringForVariables(List<RuVariable> jvars) {
        return jvars.stream().map(e -> e.toShortString()).collect(Collectors.joining(", "));
    }
    
    public static List<RuStatement> sort(Collection<RuStatement> statements) {
        List<RuStatement> collection = new ArrayList<>(statements);
        collection.sort(Comparator.comparingLong((RuStatement st) -> st.getId()));
        return collection;
    }
}
