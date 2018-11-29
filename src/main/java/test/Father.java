package test;
import java.util.ArrayList;
import java.util.List;

//三个类，父 子 子
public class Father {
	public String type;
}
class C1 extends Father{
	public int hp = 1;
	public C1() {
		this.type = "C1";
	}
}
class C2 extends Father{
	public int mp = 2;
	public C2() {
		this.type = "C2";
	}
}

class MAIN extends Father{
	public static List<Father> list = new ArrayList<Father>();
	public static void main(String[] args) {
		setCell();
		//开始 使用 list了，比如 点击了 一个位置。 从 list 随机 pop出一个。
		//可以理解为，每次点击都是从 list 里 抽奖。拿出一个， 如果是 怪物，则进行攻击，如果是 宝物属性， 则进行 其他计算。
		Father f = list.get(0);//假设拿到了 0
		System.out.println(f.type);//查看 type
		
		if(f.type == "c1") {
			C1 c = (C1) f;//强制转换
			int i = c.hp;
		}else if(f.type == "c2"){
			C2 c = (C2) f;//强制转换
			int i = c.mp;
		}
		//????? ：  如果我有 1000个子类，我就要写1000个 强制转换么？
	}
	public static void setCell() {
		//业务： 这是个 随机生成 个格子 （公共方法，写一次）
		for (int i = 0; i < 5; i++) {
			Father f = new C1();
			list.add(f);
		}
		for (int i = 0; i < 5; i++) {
			Father f = new C2();
			list.add(f);
		}
	}
}