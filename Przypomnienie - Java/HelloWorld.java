import java.util.*;


public class HelloWorld{
	
	public int skaner(){
		Scanner skaner = new Scanner(System.in);
		int s = skaner.nextInt();
		return s;
	}
	
	public void helloWorld(){
		System.out.println("Hello World! We're Rocking!");
		System.out.println("WprowdŸ liczbê 1, 2, 3 lub 4: ");
		int s = skaner();
		while(s > 4 || s <= 0){
			System.out.println("Wprowadzono niew³aœciw¹ liczbê\nWprowadŸ liczbê 1, 2, 3 lub 4: ");
			s = skaner();
		}
		switch(s){
		case 1: System.out.println("Wiosna:	marzec, kwiecieñ, maj, czerwiec"); break;
		case 2: System.out.println("Lato:	czerwiec, lipiec, sierpieñ, wrzesieñ"); break;
		case 3: System.out.println("Jesieñ:	wrzesieñ, paŸdziernik, listopad, grudzieñ"); break;
		case 4: System.out.println("Zima:	grudzieñ, styczeñ, luty, marzec"); break;
		}
	}
	
	
	
	public static void main(String[] args){
		HelloWorld r = new HelloWorld();
		r.helloWorld();
		
		int w = 0;
		for (int i=0; i>10; i++){
			w = w + i;
			System.out.println(w);
		}
	}
}