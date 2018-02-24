package com.slt.statistics.adapter.details_infos_list;


//another class to handle item's id and name
public class ObjectItem {

    public String valueOfLabel;
    public String label;

    // constructor
    public ObjectItem(String label, String val) {
        this.label = label;
        this.valueOfLabel = val;
    }

    public void setLabelAndVal(String label, String val) {
        this.label = label;
        this.valueOfLabel = val;
    }
}