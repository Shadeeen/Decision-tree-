package application;

import java.util.*;

public class Node {
	
    public boolean isLeaf;
	private String attribute; 
	private String label; 
	private Map<String, Node> children; 

	public Node() {
		super();
	}

	public Node(String attribute) {
		this.attribute = attribute;
		this.isLeaf = false;
		this.children = new HashMap<>();
	}

	public Node(String label, boolean isLeaf) {
		this.label = label;
		this.isLeaf = isLeaf;
	}

	public String getAttribute() {
		return attribute;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public String getLabel() {
		return label;
	}

	public Map<String, Node> getChildren() {
		return children;
	}

	public void addChild(String value, Node child) {
		this.children.put(value, child);
	}
}
