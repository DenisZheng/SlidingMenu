package edu.njupt.zhb.comm;

import java.net.InetAddress;

/**
 * Created by Administrator on 2014/4/8.
 */

public interface AtuUdpInterface{
    //                           远端回应ip地址   远端回应信息
    public void ReceiveCallback(String ip, String strEcho);
}
