package com.example.controlpcpro.bean;

import com.example.controlpcpro.fragment.Frag02;

/**
 * Created by Administrator on 2017/5/22.
 */

public class Info {

    private Frag02.Status EnumStatus;

    private Frag02.INTStatus intStatus;

    public Info(){
        EnumStatus=Frag02.Status.NOCELLECTED;
        intStatus = Frag02.INTStatus.NOTESTED;
    }

    public Frag02.Status getEnumStatus() {
        return EnumStatus;
    }

    public Frag02.INTStatus getIntStatus(){return intStatus;}

    public void setEnumStatus(Frag02.Status enumStatus) {
        EnumStatus = enumStatus;
    }

    private String status;

    private String order;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIntStatus(Frag02.INTStatus mintStatus){this.intStatus = mintStatus;}

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
