//声明语句
//符号表测试
void main()//
{
	int a=0,b=1,c=2;
	char g[20];
	char d='d';
	float e=23.1;
	struct i{
		int j=0;
		int k=4;
	};
	int f[10][20];
	struct h{
		int x=0;
		char y='y';
		float z=3.23;
	};
}
#
//数组多维定义测试
int main()
{
  char b[20];
  int a[10][20];
  return 0;
}
#
//结构体内定义数组测试
int main()
{
  struct g{
	  int b=0;
	  int a[10];
  };
  return 0;
}
#
//PRO_S_int测试
//重定义出错例子
int main()
{
	int a=0;
	char a='b';
	return 0;
}
#
//各种产生式PRO测试
void main()
{
	int a,b;//声明语句
	struct m//结构体声明语句
	{
	  int x=0;
	  int y=0;
	};
	int d[10];//数组声明语句

	char c='c';//初始化语句
	float e=2.23;////初始化语句

	c='d';//赋值语句
	printf(8);//功能函数语句
	scanf(a);

	if(a>b)//if else语句
	{
	  a=1;
	}else
	{
	  b=0;
	}

	for(int i=0;i<a;i++)//for语句
	{
	  sum=sum-1;
	}

	while(a!=0)//while语句
	{
	  a=0;
	}
}
#
