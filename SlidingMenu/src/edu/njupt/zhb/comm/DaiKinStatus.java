package edu.njupt.zhb.comm;

/**
 * Created by Administrator on 2014/5/29.
 */
public class DaiKinStatus {
    public int SetTemp = 0;
    public int RoomTemp = 0;
    public int ColdTempMax = 0x20;
    public int ColdTempMin = 0x10;
    public int HeatTempMax = 0x20;
    public int HeatTempMin = 0x10;
    public int Power = 0;
    public int RunMode = DaiKin.RUN_MODE_AUTO;
    public int FanScan = DaiKin.FAN_SCAN;
    public int FanSpeed = DaiKin.FAN_SPEED_MIN;
    public char[] Reserve = new char[4];
    public int Other;
    public String Alarm = "";
    public int Timer = 0;
    public boolean Locked = false;

    public void DaiKinStatus(){

    }
}
