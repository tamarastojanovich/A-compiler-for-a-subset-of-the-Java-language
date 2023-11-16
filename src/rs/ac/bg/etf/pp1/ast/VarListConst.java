// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class VarListConst extends ListOfVariables {

    private ListOfVariables ListOfVariables;
    private ListPart ListPart;

    public VarListConst (ListOfVariables ListOfVariables, ListPart ListPart) {
        this.ListOfVariables=ListOfVariables;
        if(ListOfVariables!=null) ListOfVariables.setParent(this);
        this.ListPart=ListPart;
        if(ListPart!=null) ListPart.setParent(this);
    }

    public ListOfVariables getListOfVariables() {
        return ListOfVariables;
    }

    public void setListOfVariables(ListOfVariables ListOfVariables) {
        this.ListOfVariables=ListOfVariables;
    }

    public ListPart getListPart() {
        return ListPart;
    }

    public void setListPart(ListPart ListPart) {
        this.ListPart=ListPart;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ListOfVariables!=null) ListOfVariables.accept(visitor);
        if(ListPart!=null) ListPart.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ListOfVariables!=null) ListOfVariables.traverseTopDown(visitor);
        if(ListPart!=null) ListPart.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ListOfVariables!=null) ListOfVariables.traverseBottomUp(visitor);
        if(ListPart!=null) ListPart.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarListConst(\n");

        if(ListOfVariables!=null)
            buffer.append(ListOfVariables.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ListPart!=null)
            buffer.append(ListPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarListConst]");
        return buffer.toString();
    }
}
