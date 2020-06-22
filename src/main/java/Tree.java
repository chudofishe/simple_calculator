interface TreeNode  {
    Integer accept(TreeNodeVisitor visitor);
}

interface TreeNodeVisitor {
    Integer visit(NumberNode numberNode);
    Integer visit(BinaryNode binaryNode);
    Integer visit(Tree tree);
}

class NumberNode implements TreeNode {

    Integer value;

    NumberNode (Integer value) {
        this.value = value;
    }

    @Override
    public Integer accept (TreeNodeVisitor visitor) {
        return visitor.visit(this);
    }
}

class BinaryNode implements TreeNode {

    TreeNode leftChild;
    TreeNode rightChild;
    String operand;

    BinaryNode (TreeNode leftChild, TreeNode rightChild, String operand) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.operand = operand;
    }

    @Override
    public Integer accept(TreeNodeVisitor visitor) {
        return visitor.visit(this);
    }

}

public class Tree implements TreeNode {

    BinaryNode root;

    public Tree (BinaryNode root) {
        this.root = root;
    }

    @Override
    public Integer accept(TreeNodeVisitor visitor) {
        return visitor.visit(this);
    }
}

class TreeEvaluator implements TreeNodeVisitor {

    @Override
    public Integer visit(NumberNode numberNode) {
        return numberNode.value;
    }

    @Override
    public Integer visit(BinaryNode binaryNode) {
        switch (binaryNode.operand) {
            case "+":
                return  binaryNode.leftChild.accept(this) + binaryNode.rightChild.accept(this);
            case "-":
                return  binaryNode.leftChild.accept(this) - binaryNode.rightChild.accept(this);
            case "*":
                return binaryNode.leftChild.accept(this) * binaryNode.rightChild.accept(this);
            case "/":
                try{
                    return binaryNode.leftChild.accept(this) / binaryNode.rightChild.accept(this);
                } catch (ArithmeticException ae) {
                    System.out.println("Деленеие на ноль. Результат деления приведен к нулю.");
                    return 0;
                }
            default:
                System.out.println("????");
                return 0;
        }
    }

    @Override
    public Integer visit(Tree tree) {
        return tree.root.accept(this);
    }
}
