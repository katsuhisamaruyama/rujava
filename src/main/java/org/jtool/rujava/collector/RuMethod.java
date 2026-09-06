/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.ErrorMessage;
import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OutputMessage;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.WarningMessage;

import org.jtool.srcmodel.JavaMethod;
import org.jtool.cfg.CFG;
import org.jtool.cfg.CFGNode;
import org.jtool.cfg.CFGParameter;
import org.jtool.cfg.CFGStatement;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Stack;

public class RuMethod extends AnnotationDeclaration {
    
    private final JavaMethod jmethod;
    
    private final CFG cfg;
    
    private final List<DataFlow> dataflows;
    
    private final List<RuStatement> statements;
    
    private final Map<Integer, RuVariable> parameters = new HashMap<>();
    
    private final List<WarningMessage> warningMessages = new ArrayList<>();
    
    private final List<ErrorMessage> errorMessages = new ArrayList<>();
    
    private static OwnershipAnnotation defaultOwnership = OwnershipAnnotation.OWNED;
    
    private static MutabilityAnnotation defaultMutability = MutabilityAnnotation.MUTABLE;
    
    RuMethod(JavaMethod jmethod, CFG cfg, List<DataFlow> dataflows, List<RuStatement> statements) {
        this.jmethod = jmethod;
        this.cfg = cfg;
        this.dataflows = dataflows;
        this.statements = statements;
        
        setParameters(cfg);
    }
    
    private void setParameters(CFG cfg) {
        for (DataFlow dataflow : dataflows) {
            RuVariable var = dataflow.getVariable();
            CFGStatement src = dataflow.getSrcNode().getCFGStatement();
            if (src.isFormalIn()) {
                CFGParameter param = (CFGParameter)src;
                parameters.put(param.getIndex(), var);
            }
        }
    }
    
    public JavaMethod getJaveMethod() {
        return jmethod;
    }
    
    public String getQualifiedName() {
        return jmethod.getQualifiedName().fqn();
    }
    
    public CFG getCFG() {
        return cfg;
    }
    
    public List<DataFlow> getDataFlows() {
        return dataflows;
    }
    
    public List<DataFlow> getDataFlows(DataFlow.Kind kind) {
        return dataflows.stream().filter(f -> f.getKind() == kind).toList();
    }
    
    public void cleanDataFlows() {
        Set<DataFlow> dataflows2 = new HashSet<>(dataflows);
        dataflows.clear();
        dataflows2.stream()
                .filter(f -> f.getVariable().getMutabilityAnnotation() != null
                          && f.getVariable().getOwnershipAnnotation() != null)
                .forEach(f -> dataflows.add(f));
    }
    
    public List<RuStatement> getStatements() {
        return statements;
    }
    
    public List<RuStatement> getDefUseStatements() {
        return statements.stream().filter(s -> s.hasDefVariable() || s.hasUseVariable()).toList();
    }
    
    public RuStatement getRuStatement(CFGNode node) {
        return statements.stream().filter(st -> st.getCFGStatement().equals(node)).findFirst().orElse(null);
    }
    
    public List<RuStatement> getRuStatements(List<? extends CFGNode> nodes) {
        return statements.stream().filter(st -> nodes.contains(st.getCFGStatement())).toList();
    }
    
    public List<RuVariable> getVariables() {
        return dataflows.stream().map(f -> f.getVariable()).toList();
    }
    
    public List<RuStatement> getSrcStatementsOfDataFlow(RuStatement dst, RuVariable var, DataFlow.Kind kind) {
        return dataflows.stream()
                .filter(f -> f.getDstNode().equals(dst) && f.getVariable().equals(var) && f.getKind() == kind)
                .map(f -> f.getSrcNode()).toList();
    }
    
    public List<RuStatement> getDstStatementsOfDataFlow(RuStatement src, RuVariable var, DataFlow.Kind kind) {
        return dataflows.stream()
                .filter(f -> f.getSrcNode().equals(src) && f.getVariable().equals(var) && f.getKind() == kind)
                .map(f -> f.getDstNode()).toList();
    }
    
    public List<RuVariable> getParameters() {
        return new ArrayList<>(parameters.values());
    }
    
    public int getParameterSize() {
        return parameters.keySet().size();
    }
    
    public RuVariable getParameter(int index) {
        return parameters.get(index);
    }
    
    public void addWarningMessage(WarningMessage message) {
        if (!warningMessages.contains(message)) {
            warningMessages.add(message);
        }
    }
    
    public List<OutputMessage> getWarningMessages() {
        return OutputMessage.sort(warningMessages);
    }
    
    public void addErrorMessage(ErrorMessage message) {
        if (!errorMessages.contains(message)) {
            errorMessages.add(message);
        }
    }
    
    public List<OutputMessage> getErrorMessages() {
        return OutputMessage.sort(errorMessages);
    }
    
    public static void setDefaultMutabilityAsMutable() {
        defaultMutability = MutabilityAnnotation.MUTABLE;
    }
    
    public static void setDefaultMutabilityAsImmutable() {
        defaultMutability = MutabilityAnnotation.IMMUTABLE;
    }
    
    public static MutabilityAnnotation getDefaultMutabilityAnnotation() {
        return defaultMutability;
    }
    
    public static void setDefaultOwnershipAsOwned() {
        defaultOwnership = OwnershipAnnotation.OWNED;
    }
    
    public static void setDefaultOwnershipAsBorrow() {
        defaultOwnership = OwnershipAnnotation.BORROW;
    }
    
    public static OwnershipAnnotation getDefaultOwnershipAnnotation() {
        return defaultOwnership;
    }
    
    @Override
    public MutabilityAnnotation getMutabilityAnnotation() {
        if (mutabilityAnnotation == MutabilityAnnotation.NONE) {
            return defaultMutability;
        }
        return mutabilityAnnotation;
    }
    
    @Override
    public OwnershipAnnotation getOwnershipAnnotation() {
        if (ownershipAnnotation == OwnershipAnnotation.NONE) {
            return defaultOwnership;
        }
        return ownershipAnnotation;
    }
    
    public Set<CFGNode> getSurvivalDuration(Set<RuStatement> sharedStatements) {
        Set<CFGNode> survivalDuration = new HashSet<>();
        for (RuStatement sharedSt : sharedStatements) {
            survivalDuration.addAll(getSurvivalDuration(sharedSt));
        }
        return survivalDuration;
    }
    
    public Set<CFGNode> getSurvivalDuration(RuStatement start) {
        Set<RuStatement> ends = collectForward(start);
        return getSurvivalDuration(start, ends);
    }
    
    public Set<CFGNode> getSurvivalDuration(RuStatement start, Set<RuStatement> ends) {
        Set<CFGNode> nodes = new HashSet<>();
        Set<CFGNode> startNodes = start.getCFGStatement().getSuccessors();
        for (RuStatement end : ends) {
            CFGNode endNode = end.getCFGStatement();
            List<CFGNode> rechableNodes = startNodes.stream()
                    .flatMap(n -> cfg.forwardReachableNodes(n, true, false,
                        node -> { return endNode.equals(node); }).stream()).toList();
            nodes.addAll(rechableNodes);
        }
        return nodes;
    }
    
    public Set<RuStatement> getSharedStatements(RuStatement st) {
        Set<RuStatement> statements = new HashSet<>();
        Set<RuStatement> srcStatements = collectBackward(st);
        
        for (RuStatement srcSt : srcStatements) {
            RuVariable def = srcSt.getDefFirst();
            List<RuStatement> dstStatements = getDstStatementsOfDataFlow(srcSt, def, DataFlow.Kind.defuse);
            statements.addAll(dstStatements);
        }
        return statements;
    }
    
    public Set<RuStatement> collectBackward(RuStatement start) {
        Set<RuStatement> statements = new HashSet<>();
        Stack<RuStatement> statementStack = new Stack<>();
        statementStack.push(start);
        
        while (!statementStack.isEmpty()) {
            RuStatement statement = statementStack.pop();
            
            if (!statement.isUseSingle()) {
                continue;
            }
            
            statements.add(statement);
            
            RuVariable righthand = statement.getUseFirst();
            for (RuStatement st : getSrcStatementsOfDataFlow(statement, righthand, DataFlow.Kind.defuse)) {
                if (st.isVariableDeclaration(OwnershipAnnotation.OWNED)) {
                    statements.add(st);
                } else if (st.isVariableDeclaration(OwnershipAnnotation.BORROW)) {
                    if (!statements.contains(st)) {
                        statementStack.push(st);
                    }
                }
            }
        }
        statements.remove(start);
        return statements;
    }
    
    public Set<RuStatement> collectForward(RuStatement start) {
        Set<RuStatement> statements = new HashSet<>();
        Stack<RuStatement> statementStack = new Stack<>();
        statementStack.push(start);
        
        while (!statementStack.isEmpty()) {
            RuStatement statement = statementStack.pop();
            
            if (!statement.isDefSingle()) {
                continue;
            }
            
            statements.add(statement);
            
            RuVariable lefthand = statement.getDefFirst();
            for (RuStatement st : getDstStatementsOfDataFlow(statement, lefthand, DataFlow.Kind.defuse)) {
                statements.add(st);
                if (st.isVariableDeclaration(OwnershipAnnotation.BORROW)) {
                    if (st.isDefSingle()) {
                        if (!statements.contains(st)) {
                            statementStack.push(st);
                        }
                    }
                }
            }
        }
        statements.remove(start);
        return statements;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RuMethod) ? equals((RuMethod)obj) : false;
    }
    
    public boolean equals(RuMethod method) {
        return method != null && jmethod.equals(method.jmethod);
    }
    
    @Override
    public int hashCode() {
        return jmethod.hashCode();
    }
    
    @Override
    public String toString() {
        return jmethod.getQualifiedName().fqn() + getAnnotaionString();
    }
    
    private String getAnnotaionString() {
        if (mutabilityAnnotation == null || ownershipAnnotation == null) {
            return "";
        }
        return "[" + mutabilityAnnotation.name() + "/" + ownershipAnnotation.name() + "]";
    }
    
    public static List<RuMethod> sort(List<RuMethod> methods) {
        List<RuMethod> collection = new ArrayList<>(methods);
        collection.sort(Comparator.comparing((RuMethod m) -> m.getQualifiedName()));
        return collection;
    }
}
