/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

public enum OwnershipAnnotation {
    OWNED("@Owned"),
    BORROW("@Borrow"),
    AUTO("Auto"),
    NONE("None");
    
    private final String description;
    
    private OwnershipAnnotation(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
