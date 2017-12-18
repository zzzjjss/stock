package com.uf.store.service.searcher.wordtree;

import java.util.HashMap;
import java.util.Map;

public class CharacterNode {
  private CharacterNode parentNode;
  private Map<Character,CharacterNode> childerNodes=new HashMap<Character,CharacterNode>();
  private Character character;
  private boolean isWordEnd=false;
  public CharacterNode getParentNode() {
    return parentNode;
  }
  public void setParentNode(CharacterNode parentNode) {
    this.parentNode = parentNode;
  }
  public Map<Character,CharacterNode> getChilderNodes() {
    return childerNodes;
  }
  public void setChilderNodes(Map<Character,CharacterNode> childerNodes) {
    this.childerNodes = childerNodes;
  }
  public Character getCharacter() {
    return character;
  }
  public void setCharacter(Character character) {
    this.character = character;
  }
  public boolean isWordEnd() {
    return isWordEnd;
  }
  public void setWordEnd(boolean isWordEnd) {
    this.isWordEnd = isWordEnd;
  }
}
