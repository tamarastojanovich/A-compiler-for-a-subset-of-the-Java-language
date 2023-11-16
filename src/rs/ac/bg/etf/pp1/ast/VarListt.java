// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class VarListt extends VarList {

    private PreCommaVar PreCommaVar;
    private VarList VarList;

    public VarListt (PreCommaVar PreCommaVar, VarList VarList) {
        this.PreCommaVar=PreCommaVar;
        if(PreCommaVar!=null) PreCommaVar.setParent(this);
        this.VarList=VarList;
        if(VarList!=null) VarList.setParent(this);
    }

    public PreCommaVar getPreCommaVar() {
        return PreCommaVar;
    }

    public void setPreCommaVar(PreCommaVar PreCommaVar) {
        this.PreCommaVar=PreCommaVar;
    }

    public VarList getVarList() {
        return VarList;
    }

    public void setVarList(VarList VarList) {
        this.VarList=VarList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(PreCommaVar!=null) PreCommaVar.accept(visitor);
        if(VarList!=null) VarList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(PreCommaVar!=null) PreCommaVar.traverseTopDown(visitor);
        if(VarList!=null) VarList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(PreCommaVar!=null) PreCommaVar.traverseBottomUp(visitor);
        if(VarList!=null) VarList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarListt(\n");

        if(PreCommaVar!=null)
            buffer.append(PreCommaVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarList!=null)
            buffer.append(VarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarListt]");
        return buffer.toString();
    }
}
