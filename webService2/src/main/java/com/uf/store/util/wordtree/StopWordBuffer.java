package com.uf.store.util.wordtree;

import java.io.IOException;

public abstract class StopWordBuffer {
  private StringBuilder segment = new StringBuilder();
  protected void add(char[] cbuf, int off, int len) throws IOException {
    for (int i=0; i < len; i++) {
      switch (cbuf[i]) {
        case '\r':
          break;
        case '\n':
          break;
        default:
          boolean isStopChar=StopChars.getInstance().isStopChar(cbuf[i]);
          if(isStopChar){
            finishSegment();
            break;
          }else{
            segment.append(cbuf[i]);
          }
      }
    }
  
  }
  private void finishSegment() throws IOException {
    handleSegment(segment.toString());
    segment = new StringBuilder();
  }
  protected void finish() throws IOException {
    if (segment.length() > 0) {
      finishSegment();
    }
  }
  protected abstract void handleSegment(String segment)
      throws IOException;
}
