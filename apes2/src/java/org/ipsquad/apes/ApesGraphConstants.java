package org.ipsquad.apes;

import java.util.Map;

import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;

public class ApesGraphConstants extends GraphConstants 
{
	static public String FIRST_PORT = "firstPort";
	static public String LAST_PORT = "lastPort";
	static public String INSERT_CONNECTION_SET = "insertConnectionSet";
	static public String GRAPH_CELLS = "apesGraphCells";
	static public String INSERT_PARENT_MAP_CELLS = "insertParentMap";
	static public String INSERT_UNDOABLE_EDITS = "insertUndoableEdits";
	static public String ATTRIBUTES = "attributes";
	
	static public String INSERT_APES_TREE_NODE = "insertApesTreeNode";
	static public String REMOVE_APES_TREE_NODES = "removeTreeNodes";
	static public String INSERT_PARENT_APES_TREE_NODE = "insertParentApesTreeNode";
	
	static public void setFistPort(Map map, Port port )
	{
		map.put(FIRST_PORT, port);
	}
	
	static public Port getFirstPort(Map map)
	{
		return (Port)map.get(FIRST_PORT);
	}
	
	static public void setLastPort(Map map, Port port )
	{
		map.put(LAST_PORT, port);
	}
	
	static public Port getLastPort(Map map)
	{
		return (Port)map.get(LAST_PORT);
	}
	
	static public void setCells(Map map, Map cells)
	{
	    map.put(GRAPH_CELLS, cells);
	}
	
	static public  Map getCells(Map map)
	{
	    return (Map) map.get(GRAPH_CELLS);
	}
	
	static public void setInsertConnectionSet(Map map, ConnectionSet cs)
	{
	    map.put(INSERT_CONNECTION_SET, cs);
	}
	
	static public ConnectionSet getInsertConnectionSet(Map map)
	{
	    return (ConnectionSet)map.get(INSERT_CONNECTION_SET);
	}
	
	static public void setInsertParentMapCells(Map map, ParentMap parentMap)
	{
	    map.put(INSERT_PARENT_MAP_CELLS, parentMap);
	}
	
	static public ParentMap getInsertParentMapCells(Map map)
	{
	    return (ParentMap)map.get(INSERT_PARENT_MAP_CELLS);
	}
	
	static public void setInsertUndoableEdits(Map map, UndoableEdit[] edits)
	{
	    map.put(INSERT_UNDOABLE_EDITS, edits);
	}
    
	static public UndoableEdit[] getInsertUndoableEdits(Map map)
	{
	    return (UndoableEdit[])map.get(INSERT_UNDOABLE_EDITS);
	}
   
	static public void setAttributes(Map map, Map attr )
	{
		map.put(ATTRIBUTES, attr);
	}
	
	static public Map getAttributes(Map map)
	{
		return (Map)map.get(LAST_PORT);
	}
	
	static public void setInsertApesTreeNodes(Map map, Map nodes)
	{
	    map.put(INSERT_APES_TREE_NODE, nodes);
	}
	
	static public Map getInsertApesTreeNodes(Map map)
	{
	    return (Map)map.get(INSERT_APES_TREE_NODE);
	}
	
	static public void setInsertParentApesTreeNodes(Map map, Map parent)
	{
	    map.put(INSERT_PARENT_APES_TREE_NODE, parent);
	}
	
	static public Map getInsertParentApesTreeNodes(Map map)
	{
	    return (Map)map.get(INSERT_PARENT_APES_TREE_NODE);
	}
	
	static public void setRemoveApesTreeNodes(Map map, Map nodes)
	{
	    map.put(REMOVE_APES_TREE_NODES, nodes);
	}
	
	static public Map getRemoveApesTreeNodes(Map map)
	{
	    return (Map) map.get(REMOVE_APES_TREE_NODES);
	}
}
