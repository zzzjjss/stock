package com.uf.stock;

import static org.junit.Assert.*;

import org.junit.Test;

import com.uf.stock.analysis.DayAveragePriceGoldXAnalysis;

public class AnalysisTest {

  @Test
  public void testDayAveragePriceGoldXAnalysis() {
    DayAveragePriceGoldXAnalysis analysis=new DayAveragePriceGoldXAnalysis(); 
    System.out.println(analysis.analyseAccuracy());
  }

}
