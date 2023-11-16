// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class ReturnStmt extends Statement {

    private RetExpr RetExpr;

    public ReturnStmt (RetExpr RetExpr) {
        this.RetExpr=RetExpr;
        if(RetExpr!=null) RetExpr.setParent(this);
    }

    public RetExpr getRetExpr() {
        return RetExpr;
    }

    public void setRetExpr(RetExpr RetExpr) {
        this.RetExpr=RetExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RetExpr!=null) RetExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RetExpr!=null) RetExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RetExpr!=null) RetExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnStmt(\n");

        if(RetExpr!=null)
            buffer.append(RetExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnStmt]");
        return buffer.toString();
    }
}
