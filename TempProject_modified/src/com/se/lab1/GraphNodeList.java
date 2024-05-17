/**
 * 
 */
package com.se.lab1;

import java.util.ArrayList;


class GraphNodeList<E> extends ArrayList<E>  {
  /**
   * 检查节点是否已经存在
   * 
   * @param word 单词
   * @return 存在节点情况(是则返回节点，否则null)
   */
  public GraphNode nodeCheck(String word) {
    GraphNode existedNode = null, getNode;
    for (int i = 0; i < this.size(); i++) {
      getNode = (GraphNode) this.get(i);
      if (word.equals(getNode.getWord())) {
        existedNode = getNode;
        break;
      }
    }
    return existedNode;
  }

  public ArrayList<Integer> multiIndexOf(E e) {
    ArrayList<Integer> multiIndex = new ArrayList<Integer>();
    GraphNode queryNode = (GraphNode) e;
    for (int i = 0; i < this.size(); i++) {
      if (this.get(i).equals(queryNode)) {
        multiIndex.add(new Integer(i));
      }
    }
    return multiIndex;
  }
}
