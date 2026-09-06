/*
 *  Copyright 2026
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.rujava;

import org.jtool.srcmodel.JavaFile;
import org.jtool.srcmodel.CodeRange;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Arrays;

abstract public class OutputMessage {
    
    protected final int sort;
    
    protected final String label;
    
    protected final JavaFile jfile;
    
    protected final CodeSnippet errorCodeSnippet;
    
    protected final List<CodeSnippet> codeSnippets = new ArrayList<>();
    
    public OutputMessage(int sort, String label, JavaFile jfile, ASTNode node) {
        this(sort, label, jfile, Arrays.asList(node));
    }
    
    public OutputMessage(int sort, String label, JavaFile jfile, List<ASTNode> nodes) {
        this.sort = sort;
        this.label = label;
        this.jfile = jfile;
        
        for (ASTNode node : nodes) {
            CodeRange codeRange;
            if (node instanceof VariableDeclarationFragment) {
                codeRange = new CodeRange(node.getParent());
            } else if (node instanceof SimpleName) {
                codeRange = new CodeRange(node.getParent());
            } else {
                codeRange = new CodeRange(node);
            }
            
            CodeRange codeRange2 = getCodeRange2(node);
            String code = getCode(codeRange, codeRange2);
            int lineNumber = getLineNumber(codeRange, codeRange2);
            
            this.codeSnippets.add(new CodeSnippet(code, lineNumber));
        }
        this.errorCodeSnippet = this.codeSnippets.get(0);
        this.codeSnippets.sort(Comparator.comparingInt((CodeSnippet c) -> c.lineNumber));
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getMessage() {
        return "Unknown message";
    }
    
    public String getFilename() {
        return jfile.getName();
    }
    
    protected String title() {
        return "";
    }
    
    public CodeSnippet getErrorCodeSnippets() {
        return errorCodeSnippet;
    }
    
    public List<CodeSnippet> getCodeSnippets() {
        return codeSnippets;
    }
    
    public int getLineNumber() {
        return errorCodeSnippet.lineNumber;
    }
    
    public List<Integer> getLineNumbers() {
        return codeSnippets.stream().map(c -> c.lineNumber).toList();
    }
    
    public int getLineNumber(int index) {
        if (0 <= index && index < codeSnippets.size()) {
            return codeSnippets.get(index).lineNumber;
        }
        return 0;
    }
    
    protected CodeRange getCodeRange2(ASTNode node) {
        CodeRange codeRange = null;
        if (node instanceof Assignment) {
            Assignment assignment = (Assignment)node;
            codeRange = new CodeRange(assignment);
        } else if (node instanceof SingleVariableDeclaration) {
            SingleVariableDeclaration varDecl = (SingleVariableDeclaration)node;
            codeRange = new CodeRange(varDecl.getType());
        } else if (node instanceof VariableDeclarationFragment) {
            if (node.getParent() instanceof VariableDeclarationExpression) {
                VariableDeclarationExpression varDecl = (VariableDeclarationExpression)node.getParent();
                codeRange = new CodeRange(varDecl.getType());
            } else if (node.getParent() instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement varDecl = (VariableDeclarationStatement)node.getParent();
                codeRange = new CodeRange(varDecl.getType());
            }
        }
        return codeRange;
    }
    
    protected String getCode(CodeRange codeRange, CodeRange codeRange2) {
        int pos = 0;
        if (codeRange2 != null) {
            pos = codeRange2.getStartPosition() - codeRange.getStartPosition();
        }
        
        String source = getSource(codeRange);
        int start = getAnnotationStartPosition(source);
        if (start == -1) {
            start = pos;
        }
        
        source = source.substring(start);
        int indexOfNL = source.indexOf("\n", pos);
        return indexOfNL != -1 ? source.substring(0, indexOfNL) : source;
    }
    
    protected int getLineNumber(CodeRange codeRange, CodeRange codeRange2) {
        String source = getSource(codeRange);
        int start = getAnnotationStartPosition(source);
        if (start == -1) {
            if (codeRange2 != null) {
                return codeRange2.getUpperLineNumber();
            } else {
                return codeRange.getUpperLineNumber();
            }
        }
        
        String prior = source.substring(0, start);
        int count = prior.split("\n", -1).length - 1;
        return codeRange.getUpperLineNumber() + count;
    }
    
    protected String getSource(CodeRange codeRange) {
        return jfile.getSource().substring(codeRange.getStartPosition(), codeRange.getEndPosition() + 1);
    }
    
    protected int getAnnotationStartPosition(String source) {
        if (sort == 1) {
            int index1 = source.indexOf(MutabilityAnnotation.IMMUTABLE.getDescription());
            int index2 = source.indexOf(MutabilityAnnotation.MUTABLE.getDescription());
            return getStartPosition(index1, index2);
        } else if (sort == 2) {
            int index1 = source.indexOf(OwnershipAnnotation.OWNED.getDescription());
            int index2 = source.indexOf(OwnershipAnnotation.BORROW.getDescription());
            return getStartPosition(index1, index2);
        } else if (sort == 3) {
            int index1 = source.indexOf(MutabilityAnnotation.IMMUTABLE.getDescription());
            int index2 = source.indexOf(MutabilityAnnotation.MUTABLE.getDescription());
            int index12 = getStartPosition(index1, index2);
            int index3 = source.indexOf(OwnershipAnnotation.OWNED.getDescription());
            int index4 = source.indexOf(OwnershipAnnotation.BORROW.getDescription());
            int index34 = getStartPosition(index3, index4);
            return getStartPosition(index12, index34);
        }
        return -1;
    }
    
    private int getStartPosition(int index1, int index2) {
        if (index1 == -1 && index2 == -1) {
            return -1;
        }
        if (index1 != -1) {
            return index1;
        } else if (index2 != -1) {
            return index2;
        } else {
            return Integer.min(index1, index2);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof OutputMessage) ? equals((OutputMessage)obj) : false;
    }
    
    public boolean equals(OutputMessage message) {
        return message != null && toString().equals(message.toString());
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
        List<String> cs = codeSnippets.stream().map(c -> c.toString()).toList();
        return getFilename() + ": " + title() + ": " + getMessage() + "\n"
                + String.join("\n    ", cs);
    }
    
    public static List<OutputMessage> sort(Collection<? extends OutputMessage> collection) {
        List<OutputMessage> messages = new ArrayList<>(collection);
        messages.sort(Comparator.comparing(m -> m.getLineNumberString()));
        return messages;
    }
    
    private String getLineNumberString() {
        List<String> strList = getLineNumbers().stream()
                .map(n -> String.format("%10s", String.valueOf(n)).replace(" ", "0")).toList();
        return String.join(" ", strList);
    }
    
    class CodeSnippet {
        final String code;
        final int lineNumber;
        
        CodeSnippet(String code, int lineNumber) {
            this.code = code;
            this.lineNumber = lineNumber;
        }
        
        @Override
        public String toString() {
            return lineNumber + ": " + code;
        }
    }
}