package com.basics.utils;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * @author hyk
 * snmp 协议监控
 * 2018/12/12
 */
@Slf4j
public class SnmpUtil {
    /**
     * ip
     */
    private static String ip;

    /**
     *
     */
    private static String community;


    public static Snmp snmp = null;

    public static void initSnmp(String protocol,String ip,String port) throws IOException {
        //1、初始化多线程消息转发类
        MessageDispatcher messageDispatcher = new MessageDispatcherImpl();
        //其中要增加三种处理模型。如果snmp初始化使用的是Snmp(TransportMapping<? extends Address> transportMapping) ,就不需要增加
//        messageDispatcher.addMessageProcessingModel(new MPv1());
        messageDispatcher.addMessageProcessingModel(new MPv2c());
        //当要支持snmpV3版本时，需要配置user
        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
//        USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(), localEngineID, 0);
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineID, 0);
        UsmUser user = new UsmUser(new OctetString("SNMPV3"), AuthSHA.ID, new OctetString("authPassword"),
                PrivAES128.ID, new OctetString("privPassword"));
        usm.addUser(user.getSecurityName(), user);
        messageDispatcher.addMessageProcessingModel(new MPv3(usm));
        //2、创建transportMapping
        String ipAddr = protocol + ":" + ip + "/" +port;
        UdpAddress updAddr = (UdpAddress) GenericAddress.parse(ipAddr);
//        TransportMapping<?> transportMapping = new DefaultUdpTransportMapping(updAddr);
        TransportMapping transportMapping = new DefaultUdpTransportMapping(updAddr);
        //3、正式创建snmp
        snmp = new Snmp(messageDispatcher, transportMapping);
        //开启监听
        snmp.listen();
    }

    @Value(value = "${snmp.ip}")
    public void setIp(String ip) {
        SnmpUtil.ip = ip;
    }

    @Value(value = "${snmp.community}")
    public void setCommunity(String community) {
        SnmpUtil.community = community;
    }

    public static void main(String[] args){
        try {
            SnmpUtil.initSnmp("tcp","47.105.181.242","161");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
