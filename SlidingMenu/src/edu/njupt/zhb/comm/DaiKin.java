package edu.njupt.zhb.comm;

/**
 * Created by Administrator on 2014/5/29.
 */
public class DaiKin {
    public static final int TEMP_MAX = 32;
    public static final int TEMP_MIN = 16;
    public static final int FAN_SPEED_MIN = 1;
    public static final int FAN_SPEED_MAX = 5;
    public static final int FAN_SCAN = 7;
    public static final int FAN_FIX = 1;

    public static final int RUN_MODE_FAN = 0x60;
    public static final int RUN_MODE_HEAT = 0x61;
    public static final int RUN_MODE_COLD = 0x62;
    public static final int RUN_MODE_AUTO = 0x63;
    public static final int RUN_MODE_DRY = 0x67;

    public static final String CMD_GET_DATA = "rdRoom,";
    public static final String CMD_SET_DATA = "wrRoom,";
    public static final String CMD_SCENE = "Scene,";


    /*****************************************************************
     * Write to AtuServer's String Format
     * tt,pp,rr,ff, Hex format
     *  tt, SetTemp, 10 ~ 20
     *  pp, PowerOnOff, 01 or 00
     *  rr, RunMode, 60 ~ 67
     *  ff, Fan, 11 ~ 57
     * @param status
     * @return tt,pp,rr,ff,
     */
    public static String BuildDataString(DaiKinStatus status){
        String strData = String.format("%02X,", status.SetTemp & 0xFF);
      //  strData+=String.format("%02X,", status.RoomTemp & 0xFF)+"00,00,00,00,";
        if(status.Power == 1){
            strData += "01,";
        }else{
            strData += "00,";
        }
        strData += String.format("%02X,",status.RunMode & 0xFF);
        int fan = (status.FanSpeed << 4) & 0xF0;
        fan |= status.FanScan & 0x0F;
        fan &= 0xFF;
        strData += String.format("%02X,",fan);
        return strData;
    }

}
