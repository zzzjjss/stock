package com.uf.stock.k_analysis;

public enum KLineState {
PURE_UP("大阳线，买>>>卖"),PURE_DOWN("大阴线,卖>>>买"),

UPPER_SHADOW_UP_LONG("上升遇阻，红实体>上影线，买>>卖"),
UPPER_SHADOW_UP_MIDDLE("上升遇阻，红实体=上影线，买  > 卖"),
UPPER_SHADOW_UP_SHORT("上升遇阻，红实体<上影线，买>=卖"),

UPPER_SHADOW_DOWN_LONG("先涨后跌，绿实体>上影线，卖>>买"),
UPPER_SHADOW_DOWN_MIDDLE("先涨后跌，红实体=上影线，卖>买"),
UPPER_SHADOW_DOWN_SHORT("先涨后跌，红实体<上影线，卖>=买"),

LOWER_SHADOW_UP_LONG("先跌后涨,红体>下影线,买>>卖"),
LOWER_SHADOW_UP_MIDDLE("先跌后涨,红实体=下影线，买>卖"),
LOWER_SHADOW_UP_SHORT("先跌后涨,红实体<下影线，买>=卖"),

LOWER_SHADOW_DOWN_LONG("下跌遇阻力,绿实体>下影线，卖>>买"),
LOWER_SHADOW_DOWN_MIDDLE("下跌遇阻力,绿实体=下影线，卖>买"),
LOWER_SHADOW_DOWN_SHORT("下跌遇阻力,绿实体<下影线短，卖>=买"),

TWO_SHADOW_UP_UPPER_SHADOW_LONGER_LONG("反转试探,上影线>下影线,红实体>上影线,买>>卖"),
TWO_SHADOW_UP_UPPER_SHADOW_LONGER_MIDDLE("反转试探,上影线>下影线,红实体=上影线，买>卖"),
TWO_SHADOW_UP_UPPER_SHADOW_LONGER_SHORT("反转试探,上影线>下影线,红实体<上影线，买>=卖"),
TWO_SHADOW_UP_MIDDLE("反转试探,上影线=下影线，买卖方竞争激烈 ，买>=卖"),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_LONG("反转试探,上影线<下影线,红实体>下影线，买>>卖"),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_MIDDLE("反转试探,上影线<下影线,红实体=下影线，买>卖"),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_SHORT("反转试探,上影线<下影线,红实体<下影线，买>=卖"),

TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_LONG("上影线>下影线，绿实体>上影线，卖>>买"),
TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_MIDDLE("上影线>下影线，绿实体==上影线，卖>买"),
TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_SHORT("上影线>下影线，绿实体<上影线，卖>=买"),
TWO_SHADOW_DOWN_MIDDLE("上影线=下影线，买卖方竞争激烈 ，卖>=买"),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_LONG("上影线<下影线，绿实体>下影线，卖>>买"),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_MIDDLE("上影线<下影线，绿实体==下影线，卖>买"),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_SHORT("上影线<下影线，绿实体<下影线，卖>=买"),

TWO_SHADOW_FLAT_UPPER_SHADOW_LONGER("上影线>下影线，上影线越长，表示卖压越重"),
TWO_SHADOW_FLAT("上影线=下影线，转机线，"),
TWO_SHADOW_FLAT_LOWER_SHADOW_LONGER("下影线>上影线,下影线越长，表示买方旺盛"),

UPPER_SHADOW_FLAT("买方无力再挺升，总体看卖方稍占优势,如在高价区，行情可能会下跌,卖>=买"),
LOWER_SHADOW_FLAT("卖方虽强，但买方实力更大，局势对买方有利，如在低价区，行情将会回升,买>=卖"),

PURE_FLAT("");
private String description;
private KLineState(String description){
  this.description=description;
}
public String toString(){
  return description;
}
}
