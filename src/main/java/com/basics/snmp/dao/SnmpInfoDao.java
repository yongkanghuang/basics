package com.basics.snmp.dao;

import com.basics.snmp.entity.SnmpInfo;

/**
 * @Description //TODO
 * @Author hyk
 * @Date 2018/12/21 13:38
 **/
public interface SnmpInfoDao {

    /**
     * 保存snmp
     * @param snmpInfo
     */
    void saveSnmpInfo(SnmpInfo snmpInfo);
}
