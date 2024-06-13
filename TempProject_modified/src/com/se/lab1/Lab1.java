/**
 * 
 */
package com.se.lab1;

import java.io.*;
import java.util.Scanner;

import javax.swing.JComponent;

public class Lab1  {
  public static String fileUrl;
  public static String[] words;
  public static Graph t;

  public static void main(String[] args) throws CloneNotSupportedException {
    System.out.println("这里是修改2");
    fileUrl = "C:\\Users\\LX\\Desktop\\e\\2.txt";
    readInFile();
    Scanner sc = new Scanner(System.in);
    while (true){
      System.out.println("请输入您想执行的操作:1.查询桥接词 2.根据桥接词生成新的文本 3.计算两个单词之间的最短路径 4.随机游走 5.停止");
      int op = Integer.parseInt(sc.nextLine());
      if (op == 1){
        System.out.println("请输入词1:");
        String word1 = sc.nextLine();
        System.out.println("请输入词2:");
        String word2 = sc.nextLine();
        System.out.println(t.queryBridgeWords(word1,word2));
      }else if (op == 2){
        System.out.println("请输入新的文本");
        String inputText = sc.nextLine();
        System.out.println(t.generateNewText(inputText));
      }else if (op == 3){
        System.out.println("请输入词1:");
        String word1 = sc.nextLine();
        System.out.println("请输入词2:");
        String word2 = sc.nextLine();
        System.out.println(t.calcShortestPath(word1,word2));
      }else if (op == 4){
        System.out.println(t.randomWalk());
      }else {
        break;
      }
    }
  }
  public static void readInFile() {
    File file = new File(fileUrl);
    String wordsStr = "";
    Scanner in;
    try {
      in = new Scanner(file);
      while (in.hasNextLine()) {
        String str = in.nextLine();
        wordsStr = wordsStr.concat(replaceStr(str) + " ");
      }
      words=wordsStr.split("\\s+");
      t = new Graph(words);
      createDirectedGraph(t, fileUrl, "Verdana", 12);
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * 处理读入的字符串(删除标点符号，并转换为小写)
   * 
   * @param str 读入的字符串
   * @return 处理后的字符串
   */
  public static String replaceStr(String str) {
    return str.replaceAll("[^a-zA-Z]", " ").toLowerCase();
  }




  public static boolean createDirectedGraph(Graph t, final String fileUrl, String fontname, int fontsize) {
    final File dotFile = new File(fileUrl.replace("txt", "dot"));
    try {
      dotFile.createNewFile();
      BufferedWriter outBuffer = new BufferedWriter(new FileWriter(dotFile));
      outBuffer.write(String.format("digraph %s {\n\tfontname = \"%s\";\n\tfontsize = %d;\n\n", "test", fontname, fontsize));
      outBuffer.write(String.format("\tnode [ fontname = \"%s\", fontsize = %d ]\n", fontname, fontsize));
      outBuffer.write(String.format("\tedge [ fontname = \"%s\", fontsize = %d ]\n\n", fontname, fontsize));
      for (int i = 0; i < Lab1.words.length; i++) {
        outBuffer.write(String.format("\t%s;\n", Lab1.words[i]));
      }
      for (int i = 0; i < t.graphNodes.size(); i++) {
        for (int j = 0; j < t.graphNodes.size(); j++) {
          GraphNode node1 = t.graphNodes.get(i);
          GraphNode node2 = t.graphNodes.get(j);
          if (node1.childList.indexOf(node2) != -1) {
            outBuffer.write(String.format("\t%s -> %s [label=\"%d\"];\n", node1.getWord(), node2.getWord(),
                    node1.getWeightOfNode(node2)));
          }
        }
      }
      outBuffer.write("}");
      outBuffer.flush();
      outBuffer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String txtPath = Lab1.fileUrl.replace("txt", "dot");
    String graphPath = Lab1.fileUrl.replace("txt", "png");
    Runtime run = Runtime.getRuntime();
    System.out.println(txtPath);
    System.out.println(graphPath);
    String str = String.format("dot -Tpng %s -o %s", txtPath, graphPath);
    System.out.println(str);
    Process process = null;
    try {
      process = run.exec(str);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return true;
  }
}
