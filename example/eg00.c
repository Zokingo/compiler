void main()
{
  int sum=0;
  int a=5,b=4,c=3,d_flag;
  char e='e';
  for(int i=1;i<3;i++)//test for 语句
  {
     sum=sum+i;//test 算术运算语句
  }

  while(sum!= 0)//test while语句
  {
     sum=sum-1;
  }
  d_flag=0;//test 赋值语句
  if(a>b)//test 嵌套if else//找出a,b,c最大的那个
  {
    if(a>c)//test 逻辑运算语句
    {
       d_flag=a;
    }
    else
    {
       d_flag=c;
    }
  }
  else
  {
    if(b>c)
    {
       d_flag=b;
    }
    else
    {
       d_flag=c;
    }
  }
  printf(d_flag);//test 功能函数调用
}
#
