// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class FactorNewExpr extends Factor {

    private Type Type;
    private ListForNew ListForNew;

    public FactorNewExpr (Type Type, ListForNew ListForNew) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ListForNew=ListForNew;
        if(ListForNew!=null) ListForNew.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ListForNew getListForNew() {
        return ListForNew;
    }

    public void setListForNew(ListForNew ListForNew) {
        this.ListForNew=ListForNew;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ListForNew!=null) ListForNew.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ListForNew!=null) ListForNew.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ListForNew!=null) ListForNew.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNewExpr(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ListForNew!=null)
            buffer.append(ListForNew.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNewExpr]");
        return buffer.toString();
    }
}
