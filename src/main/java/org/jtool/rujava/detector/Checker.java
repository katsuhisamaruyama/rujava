/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuStatement;
import org.jtool.rujava.collector.RuVariable;

import org.jtool.cfg.CFGNode;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.ArrayList;

public abstract class Checker {
    
    public abstract void check(RuProject project);
    
    protected List<ASTNode> getErrorNodes(RuStatement st, RuVariable lefthand, RuVariable righthand) {
        List<CFGNode> errorNodes = new ArrayList<>();
        errorNodes.add(st.getCFGStatement());
        
        if (lefthand != null) {
            CFGNode declNode = lefthand.getDeclaration();
            if (declNode != null && !errorNodes.contains(declNode)) {
                errorNodes.add(declNode);
            }
        }
        
        if (righthand != null) {
            CFGNode declNode = righthand.getDeclaration();
            if (declNode != null && !errorNodes.contains(declNode)) {
                errorNodes.add(declNode);
            }
        }
        return errorNodes.stream().map(n -> n.getASTNode()).toList();
    }
}
