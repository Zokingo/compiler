void main()
{
  int sum=0;
  int a=5,b=4,c=3,d_flag;
  char e='e';
  for(int i=1;i<3;i++)//test for ���
  {
     sum=sum+i;//test �����������
  }

  while(sum!= 0)//test while���
  {
     sum=sum-1;
  }
  d_flag=0;//test ��ֵ���
  if(a>b)//test Ƕ��if else//�ҳ�a,b,c�����Ǹ�
  {
    if(a>c)//test �߼��������
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
  printf(d_flag);//test ���ܺ�������
}
#
