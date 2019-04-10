package com.traffic.pd.constant;

public class EventMessage {
    public static final int TYPE_GET_LOCATION = 0x4;
    public static final int SELECT_CAR = 0x5;
    private int type;

    private Object object;
    public EventMessage(int type, Object object){
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
