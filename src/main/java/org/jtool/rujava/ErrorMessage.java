/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

import org.jtool.srcmodel.JavaFile;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ErrorMessage extends OutputMessage {
    
    private static final Map<String, String> messages = new HashMap<>() {
        {
            // AssignmentChecker
            put("BorrowAsMutable", "cannot borrow an immutable variable as mutable");
            put("OwnBorrowed", "cannot own a borrowed variable");
            
            // AssignmentChecker + Strict mode
            put("ImmutableAsMutable", "cannot an immutable varaible as mutable");
            put("MultipleMutableBorrowed", "cannot create multiple mutable borrowed variables");
            
            // ImmutabilityChecker
            put("ImmutableRedefine", "cannot re-define an immutable variable");
            
            // MoveChecker
            put("AccessMoved", "cannot access the dropped value after move");
            
            // MutableReferenceChecker
            put("AssignMutableBorrowed", "cannot assign to a borrowed variable as mutable");
            put("AssignOwned", "cannot use a owned variable mutably borrowed");
            
            // MutableReferenceChecker/SharedReferenceChecker
            put("AssignBorrowed", "cannot assign to a borrowed variable");
            
            // ReferenceChecker
            put("AccessBorrowed", "cannot access a borrowed variable outside its scope");
        }
    };
    
    public ErrorMessage(int sort, String label, JavaFile jfile, List<ASTNode> nodes) {
        super(sort, label, jfile, nodes);
    }
    
    @Override
    public String getMessage() {
        String message = messages.get(label);
        return message != null ? message : super.getMessage();
    }
    
    @Override
    protected String title() {
        return "error";
    }
}