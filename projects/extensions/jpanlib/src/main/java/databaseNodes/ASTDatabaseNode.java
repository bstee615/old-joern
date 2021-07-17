package databaseNodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

import ast.ASTNode;
import ast.CodeLocation;
import ast.expressions.Expression;
import ast.expressions.Callee;
import ast.functionDef.ParameterBase;
import ast.functionDef.ParameterList;
import ast.functionDef.ReturnType;
import ast.logical.statements.CompoundStatement;
import ast.expressions.Identifier;

public class ASTDatabaseNode extends DatabaseNode
{

	ASTNode astNode;
	private FunctionDatabaseNode currentFunction;

	@Override
	public void initialize(Object node)
	{
		astNode = (ASTNode) node;
	}

	@Override
	public Map<String, Object> createProperties()
	{

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(NodeKeys.NODE_TYPE, astNode.getTypeAsString());

		// Only calculate and store code strings for
		// leave-nodes and statements
		// if(astNode.getChildCount() == 0)
		properties.put(NodeKeys.CODE, astNode.getEscapedCodeStr());
		
		// if (astNode instanceof Identifier) {
		// 	properties.put(NodeKeys.LOCATION, getCorrectedLocationString());
			// properties.put(NodeKeys.LOCATION, astNode.getLocationString());
		// }
		properties.put(NodeKeys.LOCATION, getCorrectedLocationString());


		if (astNode.isInCFG())
		{
			properties.put(NodeKeys.IS_CFG_NODE, "True");
			properties.put(NodeKeys.LOCATION, getCorrectedLocationString());
		}

		if (astNode instanceof CompoundStatement)
		{
			properties.put(NodeKeys.LOCATION, astNode.getLocationString());
		}

		if (astNode instanceof Expression)
		{
			String operator = ((Expression) astNode).getOperator();
			if (operator != "")
				properties.put(NodeKeys.OPERATOR, operator);
		}

		// if(astNode.getChildCount() > 1){
		String childNumStr = Integer.toString(astNode.getChildNumber());
		properties.put(NodeKeys.CHILD_NUMBER, childNumStr);
		// }

		return properties;
	}

	private boolean nodeIsInParameterList()
	{
		// Early return for known type
		if (astNode instanceof ParameterList) {
			return true;
		}

		// BFS with predecessors
		Queue<ASTNode> queue = new LinkedList();
		Map<ASTNode, ASTNode> pred = new HashMap<>();
		queue.add(currentFunction.getASTRoot());
		while (!queue.isEmpty()) {
			ASTNode u = queue.remove();
			if (u == astNode) {
				// Trace back path to function AST root.
				// Return whether an ancestor is ParameterList.
				ASTNode pointer = u;
				while (pred.containsKey(pointer)) {
					ASTNode next = pred.get(pointer);
					pointer = next;
					if (pointer instanceof ParameterList) {
						return true;
					}
				}
				return false;
			}
			// Add all children to queue and log predecessors
			for (int i = 0; i < u.getChildCount(); i ++) {
				ASTNode v = u.getChild(i);
				pred.put(v, u);
				queue.add(v);
			}
		}
		throw new RuntimeException(String.format("Error: didn't find ASTNode %s in FunctionDatabaseNode %s.", astNode.toString(), currentFunction.toString()));
	}

	private String getCorrectedLocationString()
	{
		// Old brittle condition
		// if (!(astNode instanceof ParameterBase) || (astNode instanceof ReturnType))

		// If astNode's ancestor is ParameterList, don't modify its location
		if (!nodeIsInParameterList() && !(astNode instanceof Callee))
		{
			CodeLocation funcLocation = currentFunction.getContentLocation();
			CodeLocation tempLocation = new CodeLocation();
			tempLocation.startLine = astNode.getLocation().startLine;
			tempLocation.endLine = astNode.getLocation().endLine;
			tempLocation.startPos = astNode.getLocation().startPos;
			tempLocation.startIndex = astNode.getLocation().startIndex;
			tempLocation.stopIndex = astNode.getLocation().stopIndex;

			tempLocation.startIndex += funcLocation.startIndex + 1;
			tempLocation.startLine += funcLocation.startLine - 1;
			tempLocation.stopIndex += funcLocation.startIndex + 1;
			return tempLocation.toString();
		}
		else
		{
			return astNode.getLocation().toString();
		}
	}

	public FunctionDatabaseNode getCurrentFunction()
	{
		return currentFunction;
	}

	public void setCurrentFunction(FunctionDatabaseNode currentFunction)
	{
		this.currentFunction = currentFunction;
	}

}
