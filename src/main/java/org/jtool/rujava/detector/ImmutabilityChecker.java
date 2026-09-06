/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.ErrorMessage;
import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.collector.DataFlow;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuStatement;
import org.jtool.rujava.collector.RuVariable;

import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.JavaMethod;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

public class ImmutabilityChecker extends Checker {
    
    public ImmutabilityChecker() {
    }
    
    public void check(RuProject project) {
        for (RuMethod method : project.getMethods()) {
            // ErrorDetector.print(method);
            checkImmutability(method);
        }
    }
    
    private void checkImmutability(RuMethod method) {
        for (DataFlow dataflow : method.getDataFlows()) {
            if (dataflow.isDefDef() || dataflow.isDefDefCall()) {
                RuVariable var = dataflow.getVariable();
                if (!var.isPrimitiveType()) {
                    if (var.getMutabilityAnnotation() == MutabilityAnnotation.IMMUTABLE) {
                        
                        RuStatement src = dataflow.getSrcNode();
                        if (src.isAssignment()) {
                            JavaMethod jmethod = method.getJaveMethod();
                            JavaFile jfile = jmethod.getFile();
                            RuStatement dst = dataflow.getDstNode();
                            
                            List<ASTNode> errorNodes = getErrorNodes(dst, dst.getDefFirst(), var);
                            method.addErrorMessage(new ErrorMessage(1, "ImmutableRedefine", jfile, errorNodes));
                        }
                    }
                }
            }
        }
    }
}
