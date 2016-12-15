package com.uf.stock;

import org.junit.Test;

import com.uf.stock.analysis.LowPriceUpPointStatistics;
import com.uf.stock.analysis.TargetDefinition;

public class AnalysisTest {

  @Test
  public void testLowPriceUpPointStatistics() {
    LowPriceUpPointStatistics analysis=new LowPriceUpPointStatistics(new TargetDefinition(7, 0.03f)); 
    analysis.analyseAccuracy();
  }

}
