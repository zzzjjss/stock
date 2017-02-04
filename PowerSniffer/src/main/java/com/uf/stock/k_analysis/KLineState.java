package com.uf.stock.k_analysis;

public enum KLineState {
PURE_UP("�����ߣ���>>>��",3,0),PURE_DOWN("������,��>>>��",0,3),

UPPER_SHADOW_UP_LONG("�������裬��ʵ��>��Ӱ�ߣ���>>��",2,0),
UPPER_SHADOW_UP_MIDDLE("�������裬��ʵ��=��Ӱ�ߣ���  > ��",1,0),
UPPER_SHADOW_UP_SHORT("�������裬��ʵ��<��Ӱ�ߣ���>=��",0.5f,0),

UPPER_SHADOW_DOWN_LONG("���Ǻ������ʵ��>��Ӱ�ߣ���>>��",0,2),
UPPER_SHADOW_DOWN_MIDDLE("���Ǻ������ʵ��=��Ӱ�ߣ���>��",0,1),
UPPER_SHADOW_DOWN_SHORT("���Ǻ������ʵ��<��Ӱ�ߣ���>=��",0,0.5f),

LOWER_SHADOW_UP_LONG("�ȵ�����,����>��Ӱ��,��>>��",2,0),
LOWER_SHADOW_UP_MIDDLE("�ȵ�����,��ʵ��=��Ӱ�ߣ���>��",1,0),
LOWER_SHADOW_UP_SHORT("�ȵ�����,��ʵ��<��Ӱ�ߣ���>=��",0.5f,0),

LOWER_SHADOW_DOWN_LONG("�µ�������,��ʵ��>��Ӱ�ߣ���>>��",0,2),
LOWER_SHADOW_DOWN_MIDDLE("�µ�������,��ʵ��=��Ӱ�ߣ���>��",0,1),
LOWER_SHADOW_DOWN_SHORT("�µ�������,��ʵ��<��Ӱ�߶̣���>=��",0,0.5f),

TWO_SHADOW_UP_UPPER_SHADOW_LONGER_LONG("��ת��̽,��Ӱ��>��Ӱ��,��ʵ��>��Ӱ��,��>>��",2,0),
TWO_SHADOW_UP_UPPER_SHADOW_LONGER_MIDDLE("��ת��̽,��Ӱ��>��Ӱ��,��ʵ��=��Ӱ�ߣ���>��",1,0),
TWO_SHADOW_UP_UPPER_SHADOW_LONGER_SHORT("��ת��̽,��Ӱ��>��Ӱ��,��ʵ��<��Ӱ�ߣ���>=��",0.5f,0),
TWO_SHADOW_UP_MIDDLE("��ת��̽,��Ӱ��=��Ӱ�ߣ��������������� ����>=��",0.5f,0),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_LONG("��ת��̽,��Ӱ��<��Ӱ��,��ʵ��>��Ӱ�ߣ���>>��",2,0),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_MIDDLE("��ת��̽,��Ӱ��<��Ӱ��,��ʵ��=��Ӱ�ߣ���>��",1,0),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_SHORT("��ת��̽,��Ӱ��<��Ӱ��,��ʵ��<��Ӱ�ߣ���>=��",0.5f,0),

TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_LONG("��Ӱ��>��Ӱ�ߣ���ʵ��>��Ӱ�ߣ���>>��",0,2),
TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_MIDDLE("��Ӱ��>��Ӱ�ߣ���ʵ��==��Ӱ�ߣ���>��",0,1),
TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_SHORT("��Ӱ��>��Ӱ�ߣ���ʵ��<��Ӱ�ߣ���>=��",0,0.5f),
TWO_SHADOW_DOWN_MIDDLE("��Ӱ��=��Ӱ�ߣ��������������� ����>=��",0,0.5f),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_LONG("��Ӱ��<��Ӱ�ߣ���ʵ��>��Ӱ�ߣ���>>��",0,2),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_MIDDLE("��Ӱ��<��Ӱ�ߣ���ʵ��==��Ӱ�ߣ���>��",0,1),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_SHORT("��Ӱ��<��Ӱ�ߣ���ʵ��<��Ӱ�ߣ���>=��",0,0.5f),

TWO_SHADOW_FLAT_UPPER_SHADOW_LONGER("��Ӱ��>��Ӱ�ߣ���Ӱ��Խ������ʾ��ѹԽ��",0,0),
TWO_SHADOW_FLAT("��Ӱ��=��Ӱ�ߣ�ת���ߣ�",0,0),
TWO_SHADOW_FLAT_LOWER_SHADOW_LONGER("��Ӱ��>��Ӱ��,��Ӱ��Խ������ʾ����ʢ",0,0),

UPPER_SHADOW_FLAT("��������ͦ�������忴������ռ����,���ڸ߼�����������ܻ��µ�,��>=��",0,0.5f),
LOWER_SHADOW_FLAT("������ǿ������ʵ�����󣬾��ƶ������������ڵͼ��������齫�����,��>=��",0.5f,0),

PURE_FLAT("",0,0);
private String description;
private float upPower,downPower;
private KLineState(String description,float upPower,float downPower){
  this.description=description;
  this.upPower=upPower;
  this.downPower=downPower;
}
public String toString(){
  return description;
}
public float getUpPower() {
  return upPower;
}
public float getDownPower() {
  return downPower;
}

}
