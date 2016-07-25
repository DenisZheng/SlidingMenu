package edu.njupt.zhb.view;

import android.view.View;

import edu.njupt.zhb.bean.BaseInfo;
import edu.njupt.zhb.bean.DataModel;
import edu.njupt.zhb.comm.IComm;

public interface IShow {
	View GetView();
	void InitView();
	
	void SetSTA();
	
	DataModel GetInfo();
	
	void SetInfo(DataModel _info);
	
	void SetComm(IComm comm);
	
	void Stop();
}