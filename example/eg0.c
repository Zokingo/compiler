void main()
{
  //开始分析前将lexanalyse下切割单词的方法对应注释一下，否则不可用

  /*int a=1,b=2,c;//test  连续赋值
  char d='d';//test 常量表'd'去重只保留一份
  char ad='d';
  float e=1.22;//test 浮点数常量

  struct f//test 结构体
  {
      int x=0;
      int y=0;
  };*/
  //test wrong information

  int wr$";//test 错误标识符识别词法，错误赋值语法"
  int d+dd;
  int a = +10;
  a = -10;
  char c = '\\';
  int a = b + c;
}
#
