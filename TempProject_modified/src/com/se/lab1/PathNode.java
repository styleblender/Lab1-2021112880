/**
 * 
 */
package com.se.lab1;

/**
 * @author Yumi
 *
 */
public class PathNode {
  int pathLength;
  GraphNode presentNode;
  GraphNodeList<GraphNode> path;

  public PathNode(GraphNode startNode) {
    this.pathLength = 0;
    this.presentNode = startNode;
    this.path = new GraphNodeList<GraphNode>();
    this.path.add(startNode);
  }

  public PathNode(PathNode pathNode){
    this.pathLength = pathNode.pathLength;
    this.presentNode = pathNode.presentNode;
    this.path = new GraphNodeList<>();
    path.addAll(pathNode.path);
  }

}
