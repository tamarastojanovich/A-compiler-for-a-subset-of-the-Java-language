// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class PreCommaVarr extends PreCommaVar {

    private Var Var;

    public PreCommaVarr (Var Var) {
        this.Var=Var;
        if(Var!=null) Var.setParent(this);
    }

    public Var getVar() {
        return Var;
    }

    public void setVar(Var Var) {
        this.Var=Var;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Var!=null) Var.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Var!=null) Var.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Var!=null) Var.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PreCommaVarr(\n");

        if(Var!=null)
            buffer.append(Var.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PreCommaVarr]");
        return buffer.toString();
    }
}
