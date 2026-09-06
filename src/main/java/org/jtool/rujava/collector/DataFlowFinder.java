/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.Logger;
import org.jtool.rujava.ProgressMonitor;
import org.jtool.rujava.collector.DataFlow.Kind;

import org.jtool.jxplatform.builder.ModelBuilder;
import org.jtool.srcmodel.JavaMethod;
import org.jtool.srcmodel.JavaProject;
import org.jtool.cfg.CFG;
import org.jtool.cfg.CFGNode;
import org.jtool.cfg.CFGStatement;
import org.jtool.cfg.ControlFlow;
import org.jtool.cfg.JVariableReference;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.Collections;

public class DataFlowFinder {
    
    private final JavaProject targetProject;
    
    public DataFlowFinder(JavaProject targetProject) {
        this.targetProject = targetProject;
    }
    
    public List<RuMethod> find(List<JavaMethod> jmethods) {
        ModelBuilder builder = targetProject.getModelBuilder();
        List<RuMethod> methods = new ArrayList<>();
        
        ProgressMonitor monitor = new ProgressMonitor();
        monitor.begin(jmethods.size());
        for (JavaMethod jmethod : jmethods) {
            
            if (Logger.isVerbose()) {
                //monitor.work(1);
                monitor.printWithCount(1, jmethod.getQualifiedName().fqn());
            }
            
            CFG cfg = builder.getCFG(jmethod, true);
            
            Map<CFGNode, RuStatement> statements = createStatementMap(cfg);
            List<DataFlow> dataflows = getDataFlow(jmethod, cfg, statements);
            
            List<RuStatement> stList = new ArrayList<>(statements.values());
            RuMethod method = new RuMethod(jmethod, cfg, dataflows, stList);
            methods.add(method);
        }
        
        monitor.done();
        return methods;
    }
    
    private Map<CFGNode, RuStatement> createStatementMap(CFG cfg) {
        Map<CFGNode, RuStatement> statements = new HashMap<>();
        for (CFGNode node : cfg.getNodes()) {
            if (node.isStatement()) {
                CFGStatement stNode = (CFGStatement)node;
                List<JVariableReference> uses = stNode.getUseVariables().stream()
                        .filter(v -> !v.isAliasReference()).toList();
                stNode.setUseVariables(uses);
                RuStatement statement = new RuStatement(stNode);
                statements.put(node, statement);
            }
        }
        return statements;
    }
    
    private List<DataFlow> getDataFlow(JavaMethod jmethod, CFG cfg, Map<CFGNode, RuStatement> statements) {
        //cfg.print();
        List<DataFlow> dataflows = new ArrayList<>();
        for (CFGNode node : cfg.getNodes()) {
            if (node.isStatement() && node.hasDefVariable()) {
                CFGStatement anchor = (CFGStatement)node;
                
                List<CFGNode> reachableNodes = cfg.forwardReachableNodes(anchor, true, false);
                for (JVariableReference jvar : anchor.getDefVariables()) {
                    if (jvar.isLocalAccess() || jvar.isReturnValueReference()) {
                        reachableNodes.remove(anchor);
                        if (needTraverse(jvar, reachableNodes)) {
                            for (ControlFlow flow : anchor.getOutgoingFlows()) {
                                if (!flow.isFallThrough()) {
                                    findDDWithinMethod(anchor, flow.getDstNode(), cfg, jvar, dataflows, statements);
                                }
                            }
                            
                        } else {
                            RuStatement src = statements.get(anchor);
                            RuVariable var = RuVariable.createRuVariable(jvar);
                            src.addDefVariable(var);
                            DataFlow dataflow = new DataFlow(src, src, var, Kind.defonly);
                            dataflows.add(dataflow);
                        }
                    }
                }
            }
        }
        return dataflows;
    }
    
    private boolean needTraverse(JVariableReference jvar, List<CFGNode> nodes) {
        for (CFGNode node : nodes) {
            if (node.isStatement()) {
                CFGStatement candidate = (CFGStatement)node;
                if (candidate.defineVariable(jvar) || candidate.useVariable(jvar)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void findDDWithinMethod(CFGStatement anchor, CFGNode startnode, CFG cfg, JVariableReference jvar,
            List<DataFlow> dataflows, Map<CFGNode, RuStatement> statements) {
        Set<CFGNode> track = new HashSet<>();
        Stack<CFGNode> nodeStack = new Stack<>();
        nodeStack.push(startnode);
        
        while (!nodeStack.isEmpty()) {
            CFGNode node = nodeStack.pop();
            
            if (track.contains(node)) {
                continue;
            }
            track.add(node);
            
            if (node.hasUseVariable()) {
                CFGStatement candidate = (CFGStatement)node;
                if (candidate.useVariable(jvar)) {
                    RuStatement src = statements.get(anchor);
                    RuStatement dst = statements.get(candidate);
                    RuVariable var = RuVariable.createRuVariable(jvar);
                    src.addDefVariable(var);
                    dst.addUseVariable(var);
                    
                    DataFlow dataflow = new DataFlow(src, dst, var, Kind.defuse);
                    dataflows.add(dataflow);
                }
            }
            
            if (node.hasDefVariable()) {
                CFGStatement candidate = (CFGStatement)node;
                if (candidate.defineVariable(jvar)) {
                    if (!candidate.useVariable(jvar)) {
                        RuStatement src = statements.get(anchor);
                        RuStatement dst = statements.get(candidate);
                        RuVariable var = RuVariable.createRuVariable(jvar);
                        src.addDefVariable(var);
                        dst.addDefVariable(var);
                        
                        DataFlow flow = new DataFlow(src, dst, var, Kind.defdef);
                        dataflows.add(flow);
                    }
                    continue;
                }
                
                for (JVariableReference def : candidate.getDefVariables()) {
                    if (existsUncoveredFieldVariable(def, jvar)) {
                        RuStatement src = statements.get(anchor);
                        RuStatement dst = statements.get(candidate);
                        RuVariable var = RuVariable.createRuVariable(jvar);
                        src.addDefVariable(var);
                        dst.addDefVariable(var);
                        
                        DataFlow flow = new DataFlow(src, dst, var, Kind.defdefCall);
                        dataflows.add(flow);
                    }
                }
            }
            
            List<ControlFlow> edges = ControlFlow.sortEdges(node.getOutgoingFlows());
            Collections.reverse(edges);
            edges.stream().filter(f -> !f.isFallThrough())
                    .forEach(f -> nodeStack.push(f.getDstNode()));
        }
    }
    
    private boolean existsUncoveredFieldVariable(JVariableReference def, JVariableReference jvar) {
        if (!def.isUncoveredFieldReference()) {
            return false;
        }
        
        String rname = getReceiverName(def);
        if (rname.startsWith(jvar.getReferenceForm() + ".")) {
            return true;
        }
        return false;
    }
    
    private String getReceiverName(JVariableReference jvar) {
        String name = jvar.getReferenceForm();
        int index = name.indexOf("!");
        if (index == -1) {
            return "";
        }
        name = name.substring(0, index);
        index = name.lastIndexOf(".");
        return index != -1 ? name.substring(0, index + 1) : "";
    }
}
