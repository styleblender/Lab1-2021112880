package com.se.lab1;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Yumi
 *
 */
 
 //this is the modify on branch C4
class Graph {
  GraphNode head;
  GraphNodeList<GraphNode> graphNodes;

  public Graph(String[] words) {
    GraphNode nodepr, nodeafter;
    this.head = new GraphNode(words[0], null);
    this.graphNodes = new GraphNodeList<GraphNode>();
    nodepr = this.head;
    this.graphNodes.add(nodepr);
    for (int i = 1; i < words.length; i++) {
      nodeafter = this.graphNodes.nodeCheck(words[i]);
      if (nodeafter != null) {
        // 节点表中已经有该节点
        nodepr.addChild(nodeafter); //交由节点方法进行处理
      } else {
        // 节点未出现过
        nodeafter = new GraphNode(words[i], nodepr);
        nodepr.addChild(nodeafter);
        this.graphNodes.add(nodeafter);
      }
      nodeafter.addParent(nodepr);
      nodepr = nodeafter;
    }
  }


  public String queryBridgeWords(String word1, String word2) {
    String res = "";
    GraphNodeList<GraphNode> retNodes = calculateBridge(word1, word2);
    if (retNodes == null) {
      // No word1 or word2 in the graph!
      res += "No " + word1 + " or " + word2 + " in the graph!";
    } else if (retNodes.size() == 0) {
      // No bridge words from word1 to word2!
      res += "No bridge words from " + word1 + " to " + word2 + "!";
    } else {
      if (retNodes.size() == 1) {
        // The bridge word from word1 to word2 is: .
        res += "The bridge word from " + word1 + " to " + word2 + " is: " + retNodes.get(0).getWord() + ".";
      } else {
        // The bridge words from word1 to word2 are: xxx, xxx, and xxx.
        res += "The bridge words from " + word1 + " to " + word2 + " are:";
        for (int i = 0; i < retNodes.size() - 1; i++) {
          res += " " + retNodes.get(i).getWord() + ",";
        }
        res += "and" + " " + retNodes.get(retNodes.size() - 1).getWord() + ".";
      }
    }
    return res;
  }

  public String generateNewText(String inputText) {
    String res = "", word1, word2;
    String wordsStr = Lab1.replaceStr(inputText);
    String[] words = wordsStr.split("\\s+");
    GraphNodeList<GraphNode> retNodes;
    for (int i = 0; i < words.length - 1; i++) {
      word1 = words[i];
      word2 = words[i + 1];
      res += word1 + " ";
      retNodes = calculateBridge(word1, word2);
      if (retNodes != null && retNodes.size() != 0) {
        if (retNodes.size() == 1) {
          res += "[" + retNodes.get(0).getWord() + "] ";
        } else {
          Random random = new Random();
          int s = random.nextInt(retNodes.size() - 1);
          res += "[" + retNodes.get(s).getWord() + "] ";
        }
      }
    }
    res += words[words.length - 1];
    return res;
  }

  public GraphNodeList<GraphNode> calculateBridge(String word1, String word2) {
    GraphNode preNode = this.graphNodes.nodeCheck(word1), afterNode = this.graphNodes.nodeCheck(word2), retNode;
    GraphNodeList<GraphNode> retNodes = null;
    if (preNode != null && afterNode != null) {
      retNodes = new GraphNodeList<GraphNode>();
      for (GraphNode preChild : preNode.childList) {
        for (GraphNode afterParent : afterNode.parentList) {
          if (preChild == afterParent) {
            retNodes.add(preChild);
            break;
          }
        }
      }
    }
    return retNodes;
  }

  public String calcShortestPath(String word1, String word2) throws CloneNotSupportedException {
    String res = "";
    GraphNode startNode = this.graphNodes.nodeCheck(word1), endNode = this.graphNodes.nodeCheck(word2);
    // 迭代遍历的路径点列表
    PathNodeList<PathNode> findPaths = new PathNodeList<PathNode>();
    // 确认的路径列表
    PathNodeList<PathNode> certainPaths = new PathNodeList<PathNode>();
    // 添加初始节点
    findPaths.add(new PathNode(startNode));
    // 进行迭代获得确认路径列表
    while (findPaths.size() != 0) {
      PathNode popPathNode = findPaths.pop();
      GraphNode presentNode = popPathNode.presentNode;
      for (int i = 0; i < presentNode.childList.size(); i++) {
        // 获取当前路径分支的节点
        GraphNode childNode = presentNode.childList.get(i);
        // 确认当前已经遍历路径节点里无该节点
        if (popPathNode.path.nodeCheck(childNode.getWord()) == null) {
          // 生成新的分支节点
          PathNode branchNode = new PathNode(popPathNode);
          // 获取当前分支需要加的权值数
          int bridgeWeightValue = presentNode.getWeightOfNode(childNode);
          branchNode.pathLength += bridgeWeightValue;
          branchNode.path.add(childNode);
          branchNode.presentNode = childNode;

          // 对当前路径分支进行判断是否已经到目标节点
          if (childNode.equals(endNode)) {
            // 结束则添加到确认路径列表中
            certainPaths.push(branchNode);
          } else {
            // 添加到迭代列表前先根据已有确认路径节点权值进行爬山法筛选
            if (certainPaths.size() != 0) {
              PathNode path = certainPaths.getShortestPath();
              if (path.pathLength > branchNode.pathLength) {
                // 当前分支已有的权值数小于已知最短路径才进行迭代
                findPaths.push(branchNode);
              }
            } else {
              // 继续添加到需要迭代的路径列表
              findPaths.push(branchNode);
            }
          }
        }
      }
    }

    // 迭代完毕进行路径检查和输出
    if (certainPaths.size() != 0) {
      // 即有确认的路径(可能不只一条)
      for (int i = 0; i < certainPaths.size(); i++) {
        res += "Path " + i + " :";
        GraphNodeList<GraphNode> path = certainPaths.get(i).path;
        for (int j = 0; j < path.size() - 1; j++) {
          res += path.get(j).getWord() + "->";
        }
        res += endNode.getWord() + ".\n";
      }
    } else {
      // 不存在的
      res += "There's no path from " + word1 + " to " + word2 + ".";
    }
    return res;
  }

  public String randomWalk() {
    String ret = "";
    Random random = new Random();
    int randomNodeIndex;
    if (this.graphNodes.size() == 1) {
      randomNodeIndex = 0;
    } else {
      randomNodeIndex = random.nextInt(this.graphNodes.size() - 1);
    }
    GraphNode startNode = this.graphNodes.get(randomNodeIndex), walkNode;
    walkNode = startNode;
    GraphNodeList<GraphNode> walkNodes = new GraphNodeList<GraphNode>();
    walkNodes.add(walkNode);
    while (walkNode.childList.size() != 0) {
      boolean endState = false;
      ret += walkNode.getWord() + " ";
      if (walkNode.childList.size() == 1) {
        randomNodeIndex = 0;
      } else {
        randomNodeIndex = random.nextInt(walkNode.childList.size() - 1);
      }
      GraphNode nextNode = walkNode.childList.get(randomNodeIndex);
      ArrayList<Integer> multiIndex = walkNodes.multiIndexOf(walkNode);
      for (int i = 0; i < multiIndex.size() - 1; i++) {
        int index = multiIndex.get(i).intValue();
        if (walkNodes.get(index + 1).equals(nextNode)) {
          // 检测到相同边，进行循环跳出
          endState = true;
          break;
        }
      }
      if (endState) {
        ret += nextNode.getWord();
        break;
      } else {
        walkNode = nextNode;
        walkNodes.add(walkNode);
      }
    }
    return ret;
  }
}
