package net.popzi.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a node in the command tree.
 */
public class CommandNode {
    private final String name;
    private final Map<String, CommandNode> children = new LinkedHashMap<>();
    private CommandNode parent;

    public CommandNode(String name) {
        this.name = name;
    }

    public CommandNode addChild(String childName) {
        CommandNode child = new CommandNode(childName);
        child.parent = this;
        this.children.put(childName.toLowerCase(), child);
        return child;
    }

    public CommandNode getChild(String name) {
        return children.get(name.toLowerCase());
    }

    public Set<String> getChildrenNames() {
        return children.keySet();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public CommandNode getParent() {
        return parent;
    }
}