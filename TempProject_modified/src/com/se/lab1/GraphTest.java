package com.se.lab1;

import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @org.junit.jupiter.api.Test
    void queryBridgeWords() {
        //Lab1 lab1 = new Lab1();
        //Lab1.fileUrl = "C:\\Users\\LX\\Desktop\\e\\2.txt";
        Lab1.readInFile();
        Graph graph = Lab1.t;
        String word1 = "quick";
        String word2 = "fox";
        Assert.assertEquals("The bridge words from quick to fox are: brown, blue,and red.",graph.queryBridgeWords(word1,word2));
        word1 = "report";
        word2 = "roman";
        Assert.assertEquals("No report or roman in the graph!",graph.queryBridgeWords(word1,word2));
        word1 = "digit010";
        word2 = "(word)*";
        Assert.assertEquals("No digit010 or (word)* in the graph!",graph.queryBridgeWords(word1,word2));
        word1 = "abc";
        word2 = "abc";
        Assert.assertEquals("No abc or abc in the graph!",graph.queryBridgeWords(word1,word2));
        word1 = "the";
        word2 = "quick";
        Assert.assertEquals("No bridge words from the to quick!",graph.queryBridgeWords(word1,word2));
        word1 = "a";
        word2 = "hound";
        Assert.assertEquals("The bridge word from a to hound is: tired.",graph.queryBridgeWords(word1,word2));
        word1 = "quick";
        word2 = "fox";
        Assert.assertEquals("The bridge words from quick to fox are: brown, blue,and red.",graph.queryBridgeWords(word1,word2));
    }
}