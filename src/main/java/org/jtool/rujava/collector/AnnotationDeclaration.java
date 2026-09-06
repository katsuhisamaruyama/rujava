/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;

public abstract class AnnotationDeclaration {
    
    protected MutabilityAnnotation mutabilityAnnotation = null;
    
    protected OwnershipAnnotation ownershipAnnotation = null;
    
    protected AnnotationDeclaration() {
    }
    
    void setAnnotation(MutabilityAnnotation annotation) {
        this.mutabilityAnnotation = annotation;
    }
    
    public MutabilityAnnotation getDeclaredMutabilityAnnotation() {
        return mutabilityAnnotation;
    }
    
    public abstract MutabilityAnnotation getMutabilityAnnotation();
    
    void setAnnotation(OwnershipAnnotation annotation) {
        this.ownershipAnnotation = annotation;
    }
    
    public OwnershipAnnotation getDeclaredOwnershipAnnotation() {
        return ownershipAnnotation;
    }
    
    public abstract OwnershipAnnotation getOwnershipAnnotation();
}
