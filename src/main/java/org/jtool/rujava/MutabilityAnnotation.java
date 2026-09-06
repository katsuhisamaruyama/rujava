/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

public enum MutabilityAnnotation {
    MUTABLE("@Mutable"),
    IMMUTABLE("@Immutable"),
    AUTO("Auto"),
    NONE("None");
    
    private final String description;
    
    private MutabilityAnnotation(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
