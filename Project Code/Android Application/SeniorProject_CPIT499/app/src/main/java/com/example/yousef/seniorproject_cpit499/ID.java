package com.example.yousef.seniorproject_cpit499;

        import android.support.annotation.NonNull;

/**
 * Created by YOUSEF on 2018-04-04.
 */

public class ID {

    String ID;

    public <T extends ID> T getID(@NonNull final String ID){
        this.ID = ID;
        return (T) this;
    }
}
