package idh.java;

import java.util.HashSet;
import java.util.Set;

public class Tree<T> {

    T payload;

    Set<Tree<T>> children = new HashSet<Tree<T>>();

    public Tree(T value) {
	this.payload = value;
    }

    public Set<Tree<T>> children() {
	return children;
    }

    public int size() {
	if (children.isEmpty()) {
	    return 1;
	} else {
	    int sum = 1;
	    for (Tree<T> child : children) {
		sum = sum + child.size();
	    }
	    return sum;
	}
    }

    public void dfs() {
	System.out.println(this.payload);
	for (Tree<T> child : children) {
	    child.dfs();
	}

    }

    public static void main(String[] args) {

	Tree<String> ebike = new Tree<String>("e-bike");
	Tree<String> tandem = new Tree<String>("tandem");
	Tree<String> bike = new Tree<String>("bike");
	Tree<String> buggy = new Tree<String>("buggy");
	Tree<String> wheeled_vehicle = new Tree<String>("wheeled vehicle");

	wheeled_vehicle.children().add(bike);
	wheeled_vehicle.children().add(buggy);
	bike.children().add(tandem);
	bike.children().add(ebike);

	System.out.println(wheeled_vehicle);

	System.out.println(wheeled_vehicle.size());

	wheeled_vehicle.dfs();
    }

}
