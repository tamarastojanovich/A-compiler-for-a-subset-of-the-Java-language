// generated with ast extension for cup
// version 0.8
// 25/7/2023 0:35:39


package rs.ac.bg.etf.pp1.ast;

public class ClassMethodOnly extends Info {

    private MethodInfo MethodInfo;

    public ClassMethodOnly (MethodInfo MethodInfo) {
        this.MethodInfo=MethodInfo;
        if(MethodInfo!=null) MethodInfo.setParent(this);
    }

    public MethodInfo getMethodInfo() {
        return MethodInfo;
    }

    public void setMethodInfo(MethodInfo MethodInfo) {
        this.MethodInfo=MethodInfo;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodInfo!=null) MethodInfo.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodInfo!=null) MethodInfo.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodInfo!=null) MethodInfo.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassMethodOnly(\n");

        if(MethodInfo!=null)
            buffer.append(MethodInfo.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassMethodOnly]");
        return buffer.toString();
    }
}
