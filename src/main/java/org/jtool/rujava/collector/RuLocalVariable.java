/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import org.jtool.rujava.MutabilityAnnotation;
import org.jtool.rujava.OwnershipAnnotation;

import org.jtool.cfg.JVariableReference;

public class RuLocalVariable extends RuVariable {
    
    private static OwnershipAnnotation defaultOwnership = OwnershipAnnotation.BORROW;
    
    private static MutabilityAnnotation defaultMutability = MutabilityAnnotation.MUTABLE;
    
    RuLocalVariable(JVariableReference jvar) {
        super(jvar);
    }
    
    public static void setDefaultAnnotations(OwnershipAnnotation ownership, MutabilityAnnotation mutability) {
        defaultOwnership = ownership;
        defaultMutability = mutability;
    }
    
    public static MutabilityAnnotation getDefaultMutabilityAnnotation() {
        return defaultMutability;
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
}
