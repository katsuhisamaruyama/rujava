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

public class WarningMessage extends OutputMessage {
    
    private static final Map<String, String> messages = new HashMap<>() {
        {
            put("PrimitiveTypeReturn", "no effect on a return value with the primitive type");
            put("PrimitiveTypeDeclaration", "no effect on a variable with the primitive type");
            
            put("CoexistenceMutability", "cannot simultaneously use @Mutable and @Immutable");
            put("CoexistenceOwnership", "cannot simultaneously use @Owned and @Borrow");
            
            put("OverrideImmutable", "cannot override a method with a return value bound by @Immutable");
            put("OverrideBorrow", "cannot override a method with a return value bound by @Borrow");
            
            put("OverrideImmutableParameter", "cannot override a method with a parameter bound by @Immutable");
            put("OverrideBorrowParameter", "cannot override a method with a parameter bound by @Borrow");
        }
    };
    
    public WarningMessage(int sort, String label, JavaFile jfile, ASTNode node) {
        super(sort, label, jfile, node);
    }
    
    public WarningMessage(int sort, String label, JavaFile jfile, List<ASTNode> nodes) {
        super(sort, label, jfile, nodes);
    }
    
    @Override
    public String getMessage() {
        String message = messages.get(label);
        return message != null ? message : super.getMessage();
    }
    
    @Override
    protected String title() {
        return "warning";
    }
}