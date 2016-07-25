package edu.njupt.zhb.bean;

public class PublicData {
  public static String getDeviceTypeName(String code)
  {
	  String typename="";
	  if(code.equals("light")){
		typename="灯光";    
	  }else if(code.equals("air")){
		typename="空调";    
	  }
	  else if(code.equals("hot")){
			typename="地暖";    
		  }
	  else if(code.equals("curtain")){
			typename="窗帘";    
		  }
	  else if(code.equals("window")){
			typename="电动窗";    
		  }
	  
	  return typename;
  }
}
