/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava.collector;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class DataFlow {
    
    public enum Kind {
        defuse,
        defonly,
        defdef,
        defdefCall,
    }
    
    private final RuStatement src;
    
    private final RuStatement dst;
    
    private final RuVariable var;
    
    private final Kind kind;
    
    DataFlow(RuStatement src, RuStatement dst, RuVariable var, Kind kind) {
        this.src = src;
        this.dst = dst;
        this.var = var;
        this.kind = kind;
    }
    
    public long getSrcId() {
        return src.getId();
    }
    
    public long getDstId() {
        return dst.getId();
    }
    
    public RuStatement getSrcNode() {
        return src;
    }
    
    public RuStatement getDstNode() {
        return dst;
    }
    
    public RuVariable getVariable() {
        return var;
    }
    
    public Kind getKind() {
        return kind;
    }
    
    public boolean isDefUse() {
        return kind == Kind.defuse;
    }
    
    public boolean isDefOnly() {
        return kind == Kind.defonly;
    }
    
    public boolean isDefDef() {
        return kind == Kind.defdef;
    }
    
    public boolean isDefDefCall() {
        return kind == Kind.defdefCall;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DataFlow) ? equals((DataFlow)obj) : false;
    }
    
    public boolean equals(DataFlow flow) {
        return flow != null && (this == flow || (src.equals(flow.src) && dst.equals(flow.dst)));
    }
    
    @Override
    public int hashCode() {
        return Long.valueOf(src.getId() + dst.getId()).hashCode();
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(src.getId() + " -> " + dst.getId());
        buf.append(" " + getKind());
        buf.append(" " + var.toString());
        return buf.toString();
    }
    
    public String toShortString() {
        StringBuilder buf = new StringBuilder();
        buf.append(src.getId() + " -> " + dst.getId());
        buf.append(" " + getKind());
        buf.append(" " + var.toShortString());
        return buf.toString();
    }
    
    public String toLongString() {
        StringBuilder buf = new StringBuilder();
        buf.append(src.getId() + " -> " + dst.getId());
        buf.append(" " + getKind());
        buf.append(" " + var.toLongString());
        return buf.toString();
    }
    
    public String toTinyString() {
        StringBuilder buf = new StringBuilder();
        buf.append(src.getId() + " -> " + dst.getId());
        buf.append(" " + getKind());
        return buf.toString();
    }
    
    public static List<DataFlow> sort(List<DataFlow> dataflows) {
        List<DataFlow> collection = new ArrayList<>(dataflows);
        collection.sort(Comparator.comparingLong((DataFlow flow) -> flow.getSrcId())
                             .thenComparingLong((DataFlow flow) -> flow.getDstId())
                             .thenComparing((DataFlow flow) -> flow.getKind().toString()));
        return collection;
    }
}
