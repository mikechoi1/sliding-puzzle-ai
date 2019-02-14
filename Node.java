import java.util.ArrayList;

public class Node implements Comparable<Node> {
	
	private String state;	//replace
	private int cost;
	private int totalCost;
	private int estimatedCost;
	private int depth;
	private Node parent;
	private ArrayList<Node> children;

	
	public Node(Node parent, String state, int cost, int estimatedCost) {
		this.parent = parent;
		this.state = state;
		this.cost = cost;
		this.estimatedCost = estimatedCost;
		totalCost = cost + estimatedCost;
		children = new ArrayList<>();
	}
	
	@Override
	public int compareTo(Node o2) {
		return Double.compare(this.getTotalCost(), o2.getTotalCost());
	}
	public Node(String state) {
		this.state = state;
		children = new ArrayList<>();
	}
	//getters
	public String getState() {
		return state;
	}
	public int getCost() {
		return cost;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public int getEstimatedCost() {
		return estimatedCost;
	}
	public int getDepth() {
		return depth;
	}
	public Node getParent() {
		return parent;
	}
	public ArrayList<Node> getChildren() {
		return children;
	}
	//setters
	public void setState(String state) {
		this.state = state;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	public void setEstimatedCost(int estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public void addChildren(Node child) {
		children.add(child);
	}
	public String toString() {
		String content = "";
		for(int i = 0; i < state.length(); i++) {
			content += state.charAt(i) + "   ";
			if(i % 3 == 2)
				content += "\n";
		}
		content += "g: " + cost + " h: " + estimatedCost + " ";
		content += "f: " + totalCost;
		return content;
	}
	
}
