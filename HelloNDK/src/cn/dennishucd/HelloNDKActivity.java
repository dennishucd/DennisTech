package cn.dennishucd;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloNDKActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		TextView tv = (TextView)this.findViewById(R.id.tv_lable);
		
		int sum  = 0;
		sum = add(1, 2);
		
		tv.setText(String.valueOf(sum));
	}
	
	public native int add(int a, int b);
	
	static {
		System.loadLibrary("hellondk");
	}
}
