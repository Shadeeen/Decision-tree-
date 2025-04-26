package application;
import javafx.scene.control.TextArea;

public class ID3TreePrinter {

    public static void printTree(Node root, TextArea textArea) {
        StringBuilder sb = new StringBuilder();
        buildTreeString(root, sb, "", true);
        textArea.setText(sb.toString());
    }

    private static void buildTreeString(Node node, StringBuilder sb, String prefix, boolean isTail) {
        sb.append(prefix).append(isTail ? "└── " : "├── ");
        sb.append(node.isLeaf() ? "[Label: " + node.getLabel() + "]" : "[Attribute: " + node.getAttribute() + "]")
          .append("\n");

        if (!node.isLeaf()) {
            var children = node.getChildren().entrySet();
            int i = 0;
            for (var entry : children) {
                boolean isLast = i == children.size() - 1;
                sb.append(prefix).append(isTail ? "    " : "│   ").append("Value: ").append(entry.getKey()).append("\n");
                buildTreeString(entry.getValue(), sb, prefix + (isTail ? "    " : "│   "), isLast);
                i++;
            }
        }
    }
}
