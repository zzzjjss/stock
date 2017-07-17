package com.uf.store.util.wordtree;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.LinkedList;
import java.util.Queue;

import com.google.common.base.Strings;

public class StopWordReader {
  private final Readable readable;
  private final Reader reader;
  private final char[] buf = new char[0x1000]; // 4K
  private final CharBuffer cbuf = CharBuffer.wrap(buf);
  private final Queue<String> segmentations = new LinkedList<String>();
  private final StopWordBuffer stopWordBuf = new StopWordBuffer() {
    @Override protected void handleSegment(String segment) {
      if(!Strings.isNullOrEmpty(segment)){
        segmentations.add(segment);
      }
    }
  };
  public StopWordReader(Readable readable) {
    this.readable = checkNotNull(readable);
    this.reader = (readable instanceof Reader) ? (Reader) readable : null;
  }
  public String readSegmentation() throws IOException {
    while (segmentations.peek() == null) {
      cbuf.clear();
      // The default implementation of Reader#read(CharBuffer) allocates a
      // temporary char[], so we call Reader#read(char[], int, int) instead.
      int read = (reader != null)
          ? reader.read(buf, 0, buf.length)
          : readable.read(cbuf);
      if (read == -1) {
        stopWordBuf.finish();
        break;
      }
      stopWordBuf.add(buf, 0, read);
    }
    return segmentations.poll();
  }
  
}
