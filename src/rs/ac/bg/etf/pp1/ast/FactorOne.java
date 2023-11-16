// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class FactorOne extends FactorList {

    private ModifFactorList ModifFactorList;

    public FactorOne (ModifFactorList ModifFactorList) {
        this.ModifFactorList=ModifFactorList;
        if(ModifFactorList!=null) ModifFactorList.setParent(this);
    }

    public ModifFactorList getModifFactorList() {
        return ModifFactorList;
    }

    public void setModifFactorList(ModifFactorList ModifFactorList) {
        this.ModifFactorList=ModifFactorList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ModifFactorList!=null) ModifFactorList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ModifFactorList!=null) ModifFactorList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ModifFactorList!=null) ModifFactorList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorOne(\n");

        if(ModifFactorList!=null)
            buffer.append(ModifFactorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorOne]");
        return buffer.toString();
    }
}
