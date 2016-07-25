package edu.njupt.zhb.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
//edu.njupt.zhb.slidemenu.R;
import edu.njupt.zhb.bean.DataModel;

public class JsonWrite {
	public static ArrayList<DataModel> dataModel;
	public static File saveFile;
	public static String jsonData;
	//��ȡJson
	public static  ArrayList<DataModel>  getJson(Context txt,String strFile,String keys)
	{
		 ArrayList<DataModel> datalist=new ArrayList<DataModel>();
		 try{
			 String strJson=StringUtil.inputStream2String(txt.getAssets().open(strFile));
			 String pathx=txt.getFilesDir().getAbsolutePath()+"/"+strFile;
			 setFilePath(pathx);
			 getJsonStr(pathx);//||jsonData.equals("{\"areas\":[]}")
			if(jsonData.equals("{\"devices\":[]}")||jsonData.equals(""))  writeData(strJson);
			 datalist= getJsonListData(keys);	
		 }
		 catch(Exception ex)
		{
		}
		 return datalist;
	}
	//����Json
	public static void saveJson(Context txt,String strFile,String keys,ArrayList<DataModel> dlist)
	{
		  String pathx=txt.getFilesDir().getAbsolutePath()+"/"+strFile;
		  dataModel=dlist;
		  setFilePath(pathx);
		  getJsonData(keys);
	}
	public static boolean checkFilePath(String filePath)
	{
		saveFile=new File(filePath);
		if(saveFile.isFile())
		    return true ;
		else 
			return false;
		
	}
	public static void setFilePath(String filepath){
		saveFile=new File(filepath);
		try {
			if(!saveFile.isFile())
			saveFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getJsonData(String keys){
		String jsonData = null;
//		String jsonData=new JSONStringer().object().key("village").value("abc").endObject().toString();
		try {
			StringBuilder builder=new StringBuilder();
			ArrayList<String> folksData=new ArrayList<String>();
			JSONArray array=new JSONArray();
			for(int i=0;i<dataModel.size();i++){
				DataModel data=dataModel.get(i);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", data.getId());
				jsonObject.put("name", data.getName());
				jsonObject.put("devNum", data.getDevNum());
				jsonObject.put("sType", data.getSType());
				jsonObject.put("devId", data.getDevId());
				jsonObject.put("revIp", data.getRevIp());
				jsonObject.put("State", data.getState());
				jsonObject.put("otherName", data.getOtherName());
				jsonObject.put("uiCode", data.getUiCode());
				jsonObject.put("uiImg", data.getUiImg());
				jsonObject.put("areaId", data.getAreaId());
				  if(data.getDataModel()!=null){
					  JSONArray carray=new JSONArray();
					for(int j=0;j<data.getDataModel().size();j++)
					{
						DataModel cdata=data.getDataModel().get(j);
						JSONObject cjsonObject=new JSONObject();
						cjsonObject.put("id", cdata.getId());
						cjsonObject.put("name", cdata.getName());
						cjsonObject.put("devNum", cdata.getDevNum());
						cjsonObject.put("devId", cdata.getDevId());
						cjsonObject.put("revIp", cdata.getRevIp());
						cjsonObject.put("State", cdata.getState());
						cjsonObject.put("otherName", cdata.getOtherName());
						cjsonObject.put("uiCode", cdata.getUiCode());
						cjsonObject.put("uiImg", cdata.getUiImg());
						cjsonObject.put("areaId", cdata.getAreaId());
						carray.put(cjsonObject);  
					}
					 jsonObject.put("devices",carray);
				}
				//jsonObject.put("dataModel", data.getDataModel());
				folksData.add(jsonObject.toString());
				array.put(jsonObject);
			}
//			JSONArray jsonArray=new JSONArray(folksData);
			int len =	array.length();
			jsonData=new JSONStringer().object().key(keys).value(array).endObject().toString();
			System.out.println(jsonData);
	 		writeData(jsonData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
	}
	public static void getJsonStr(String filePath)  
	{
		try{
		  jsonData= StringUtil.getFileData(filePath);
		}catch(Exception ex){
			
			jsonData="";
		}
	}
	public static ArrayList<DataModel> getJsonListData(String keys){
		ArrayList<DataModel> folks=new ArrayList<DataModel>();
		try {
			JSONObject jsonObject=new JSONObject(jsonData);
			JSONArray jsonArray=jsonObject.getJSONArray(keys);
			int len = jsonArray.length();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject json=jsonArray.getJSONObject(i);
				DataModel data=new DataModel();
				data.setName(json.optString("name"));
				data.setId(json.optString("id"));
				data.setDevId(json.optString("devId"));
				data.setDevNum(json.optString("devNum"));
				data.setSType(json.optString("sType"));
				data.setRevIp(json.optString("revIp"));
				data.setState(json.optString("State"));
				data.setOtherName(json.optString("otherName"));
				data.setUiImg(json.optString("uiImg"));
				data.setUiCode(json.optString("uiCode"));
				data.setAreaId(json.optString("areaId"));
				  if(json.optString("devices")!=""){
					  JSONArray cjsonArray=json.getJSONArray("devices");
					  ArrayList<DataModel> cfolks=new ArrayList<DataModel>();
						for(int j=0;j<cjsonArray.length();j++)
						{
							JSONObject cjson=cjsonArray.getJSONObject(j);
							DataModel cdata=new DataModel();
							cdata.setName(cjson.optString("name"));
							cdata.setId(cjson.optString("id"));
							cdata.setDevId(cjson.optString("devId"));
							cdata.setDevNum(cjson.optString("devNum"));
							cdata.setRevIp(cjson.optString("revIp"));
							cdata.setState(cjson.optString("State"));
							cdata.setOtherName(cjson.optString("otherName"));
							cdata.setUiImg(cjson.optString("uiImg"));
							cdata.setUiCode(cjson.optString("uiCode"));
							cdata.setAreaId(cjson.optString("areaId"));
							cfolks.add(cdata);
						}
						data.setDataModel(cfolks);
						 
					}
				folks.add(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return folks;
	}
	public static void writeData(String jsonDatas) {
		// TODO Auto-generated method stub
		try {
			jsonData=jsonDatas;
			BufferedReader reader=new BufferedReader(new StringReader(jsonDatas));
			BufferedWriter writer=new BufferedWriter(new FileWriter(saveFile));
			int len=0;
			char[] buffer=new char[1024]; 
			while((len=reader.read(buffer))!=-1){
				writer.write(buffer, 0, len);
			}
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

