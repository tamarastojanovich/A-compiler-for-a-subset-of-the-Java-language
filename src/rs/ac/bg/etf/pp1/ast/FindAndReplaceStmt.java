// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class FindAndReplaceStmt extends Statement {

    private DesignatorFind DesignatorFind;
    private FindVar FindVar;
    private Expr Expr;

    public FindAndReplaceStmt (DesignatorFind DesignatorFind, FindVar FindVar, Expr Expr) {
        this.DesignatorFind=DesignatorFind;
        if(DesignatorFind!=null) DesignatorFind.setParent(this);
        this.FindVar=FindVar;
        if(FindVar!=null) FindVar.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignatorFind getDesignatorFind() {
        return DesignatorFind;
    }

    public void setDesignatorFind(DesignatorFind DesignatorFind) {
        this.DesignatorFind=DesignatorFind;
    }

    public FindVar getFindVar() {
        return FindVar;
    }

    public void setFindVar(FindVar FindVar) {
        this.FindVar=FindVar;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorFind!=null) DesignatorFind.accept(visitor);
        if(FindVar!=null) FindVar.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorFind!=null) DesignatorFind.traverseTopDown(visitor);
        if(FindVar!=null) FindVar.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorFind!=null) DesignatorFind.traverseBottomUp(visitor);
        if(FindVar!=null) FindVar.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FindAndReplaceStmt(\n");

        if(DesignatorFind!=null)
            buffer.append(DesignatorFind.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FindVar!=null)
            buffer.append(FindVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FindAndReplaceStmt]");
        return buffer.toString();
    }
}
