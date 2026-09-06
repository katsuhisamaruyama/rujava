/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.detector;

import org.jtool.rujava.ErrorMessage;
import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;
import org.jtool.rujava.collector.RuMethod;
import org.jtool.rujava.collector.RuProject;
import org.jtool.rujava.collector.RuStatement;
import org.jtool.rujava.collector.RuVariable;

import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.JavaMethod;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.Map;

public class AssignmentChecker extends Checker {
    
    private final boolean allowImmutableToMutable;
    private final boolean allowMutableBorrow;
    
    public AssignmentChecker() {
        this.allowImmutableToMutable = true;
        this.allowMutableBorrow = true;
    }
    
    public AssignmentChecker(boolean allowImmutableToMutable, boolean allowMutableBorrow) {
        this.allowImmutableToMutable = allowImmutableToMutable;
        this.allowMutableBorrow = allowMutableBorrow;
    }
    
    public void check(RuProject project) {
        for (RuMethod method : project.getMethods()) {
            // ErrorDetector.print(method);
            checkAssignment(method);
            checkMethodCall(method, project);
        }
    }
    
    private void checkAssignment(RuMethod method) {
        for (RuStatement st : method.getStatements()) {
            if (st.isAssignment()) {
                RuVariable lefthand = st.getDefFirst();
                if (!lefthand.isPrimitiveType()) {
                    RuVariable righthand = st.getUseFirst();
                    String result = prohibited(righthand.getMutabilityAnnotation(), righthand.getOwnershipAnnotation(),
                            lefthand.getMutabilityAnnotation(), lefthand.getOwnershipAnnotation());
                    if (result.length() > 0) {
                        JavaMethod jmethod = method.getJaveMethod();
                        JavaFile jfile = jmethod.getFile();
                        
                        List<ASTNode> errorNodes = getErrorNodes(st, lefthand, righthand);
                        method.addErrorMessage(new ErrorMessage(3, result, jfile, errorNodes));
                    }
                }
            }
        }
    }
    
    private void checkMethodCall(RuMethod method, RuProject project) {
        Map<RuStatement, RuVariable> formals = project.getFormalInsForCalledMethods(method);
        for (Map.Entry<RuStatement, RuVariable> entry : formals.entrySet()) {
            RuStatement actualIn = entry.getKey();
            RuVariable actual = actualIn.getUseFirst();
            RuVariable formal = entry.getValue();
            
            String result = prohibited(actual.getMutabilityAnnotation(), actual.getOwnershipAnnotation(),
                    formal.getMutabilityAnnotation(), formal.getOwnershipAnnotation());
            if (result.length() > 0) {
                JavaMethod jmethod = method.getJaveMethod();
                JavaFile jfile = jmethod.getFile();
                
                List<ASTNode> errorNodes = getErrorNodes(actualIn, null, actual);
                method.addErrorMessage(new ErrorMessage(3, result, jfile, errorNodes));
            }
        }
    }
    
    private String prohibited(MutabilityAnnotation srcMut, OwnershipAnnotation srcOwn,
            MutabilityAnnotation dstMut, OwnershipAnnotation dstOwn) {
        if (srcMut == MutabilityAnnotation.IMMUTABLE &&
                dstMut == MutabilityAnnotation.MUTABLE && dstOwn == OwnershipAnnotation.BORROW) {
            return "BorrowAsMutable";
        }
        
        if (srcOwn == OwnershipAnnotation.BORROW && dstOwn == OwnershipAnnotation.OWNED) {
            return "OwnBorrowed";
        }
        
        if (!allowImmutableToMutable) {
            if (srcMut == MutabilityAnnotation.IMMUTABLE && dstMut == MutabilityAnnotation.MUTABLE) {
                return "ImmutableAsMutable";
            }
        }
        
        if (!allowMutableBorrow) {
            if (srcMut == MutabilityAnnotation.MUTABLE && srcOwn == OwnershipAnnotation.BORROW &&
                dstMut == MutabilityAnnotation.MUTABLE && dstOwn == OwnershipAnnotation.BORROW) {
                return "MultipleMutableBorrowed";
            }
        }
        
        return "";
    }
}
