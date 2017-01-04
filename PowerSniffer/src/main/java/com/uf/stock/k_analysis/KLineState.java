package com.uf.stock.k_analysis;

public enum KLineState {
PURE_UP("�����ߣ���>>>��"),PURE_DOWN("������,��>>>��"),

UPPER_SHADOW_UP_LONG("�������裬��ʵ��>��Ӱ�ߣ���>>��"),
UPPER_SHADOW_UP_MIDDLE("�������裬��ʵ��=��Ӱ�ߣ���  > ��"),
UPPER_SHADOW_UP_SHORT("�������裬��ʵ��<��Ӱ�ߣ���>=��"),

UPPER_SHADOW_DOWN_LONG("���Ǻ������ʵ��>��Ӱ�ߣ���>>��"),
UPPER_SHADOW_DOWN_MIDDLE("���Ǻ������ʵ��=��Ӱ�ߣ���>��"),
UPPER_SHADOW_DOWN_SHORT("���Ǻ������ʵ��<��Ӱ�ߣ���>=��"),

LOWER_SHADOW_UP_LONG("�ȵ�����,����>��Ӱ��,��>>��"),
LOWER_SHADOW_UP_MIDDLE("�ȵ�����,��ʵ��=��Ӱ�ߣ���>��"),
LOWER_SHADOW_UP_SHORT("�ȵ�����,��ʵ��<��Ӱ�ߣ���>=��"),

LOWER_SHADOW_DOWN_LONG("�µ�������,��ʵ��>��Ӱ�ߣ���>>��"),
LOWER_SHADOW_DOWN_MIDDLE("�µ�������,��ʵ��=��Ӱ�ߣ���>��"),
LOWER_SHADOW_DOWN_SHORT("�µ�������,��ʵ��<��Ӱ�߶̣���>=��"),

TWO_SHADOW_UP_UPPER_SHADOW_LONGER_LONG("��ת��̽,��Ӱ��>��Ӱ��,��ʵ��>��Ӱ��,��>>��"),
TWO_SHADOW_UP_UPPER_SHADOW_LONGER_MIDDLE("��ת��̽,��Ӱ��>��Ӱ��,��ʵ��=��Ӱ�ߣ���>��"),
TWO_SHADOW_UP_UPPER_SHADOW_LONGER_SHORT("��ת��̽,��Ӱ��>��Ӱ��,��ʵ��<��Ӱ�ߣ���>=��"),
TWO_SHADOW_UP_MIDDLE("��ת��̽,��Ӱ��=��Ӱ�ߣ��������������� ����>=��"),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_LONG("��ת��̽,��Ӱ��<��Ӱ��,��ʵ��>��Ӱ�ߣ���>>��"),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_MIDDLE("��ת��̽,��Ӱ��<��Ӱ��,��ʵ��=��Ӱ�ߣ���>��"),
TWO_SHADOW_UP_UPPER_SHADOW_SHORTER_SHORT("��ת��̽,��Ӱ��<��Ӱ��,��ʵ��<��Ӱ�ߣ���>=��"),

TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_LONG("��Ӱ��>��Ӱ�ߣ���ʵ��>��Ӱ�ߣ���>>��"),
TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_MIDDLE("��Ӱ��>��Ӱ�ߣ���ʵ��==��Ӱ�ߣ���>��"),
TWO_SHADOW_DOWN_UPPER_SHADOW_LONGER_SHORT("��Ӱ��>��Ӱ�ߣ���ʵ��<��Ӱ�ߣ���>=��"),
TWO_SHADOW_DOWN_MIDDLE("��Ӱ��=��Ӱ�ߣ��������������� ����>=��"),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_LONG("��Ӱ��<��Ӱ�ߣ���ʵ��>��Ӱ�ߣ���>>��"),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_MIDDLE("��Ӱ��<��Ӱ�ߣ���ʵ��==��Ӱ�ߣ���>��"),
TWO_SHADOW_DOWN_UPPER_SHADOW_SHORTER_SHORT("��Ӱ��<��Ӱ�ߣ���ʵ��<��Ӱ�ߣ���>=��"),

TWO_SHADOW_FLAT_UPPER_SHADOW_LONGER("��Ӱ��>��Ӱ�ߣ���Ӱ��Խ������ʾ��ѹԽ��"),
TWO_SHADOW_FLAT("��Ӱ��=��Ӱ�ߣ�ת���ߣ�"),
TWO_SHADOW_FLAT_LOWER_SHADOW_LONGER("��Ӱ��>��Ӱ��,��Ӱ��Խ������ʾ����ʢ"),

UPPER_SHADOW_FLAT("��������ͦ�������忴������ռ����,���ڸ߼�����������ܻ��µ�,��>=��"),
LOWER_SHADOW_FLAT("������ǿ������ʵ�����󣬾��ƶ������������ڵͼ��������齫�����,��>=��"),

PURE_FLAT("");
private String description;
private KLineState(String description){
  this.description=description;
}
public String toString(){
  return description;
}
}
