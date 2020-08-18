package com.hazz.aipick.mvp.model.bean;

public class SimulateSummaryBean {

    /**
     * initial_value : 1000
     * current_value : 2000
     * total_gain : 3000
     */

    private String initial_value;
    private String current_value;
    private String total_gain;

    public String getInitial_value() {
        return initial_value;
    }

    public void setInitial_value(String initial_value) {
        this.initial_value = initial_value;
    }

    public String getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }

    public String getTotal_gain() {
        return total_gain;
    }

    public void setTotal_gain(String total_gain) {
        this.total_gain = total_gain;
    }
}
