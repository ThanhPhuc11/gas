package vn.gas.thq.util.dialog;

import java.io.Serializable;

public class DialogListModel implements Serializable {
    public String id;
    public String name;
    public String other;

    public DialogListModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DialogListModel(String id, String name, String other) {
        this.id = id;
        this.name = name;
        this.other = other;
    }


    //
    public String station_input_id;
    public String station_output_id;
}


