package com.bitdubai.android_core.app.common.version_1.communication.server_system_broker.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Matias Furszyfer on 2016.05.12..
 */
public class ModuleObjectParameterWrapper implements Parcelable,Serializable{

    private Serializable object;
    private Class parameterType;

    public ModuleObjectParameterWrapper(Serializable object,Class parameterType) {
        this.object = object;
        this.parameterType = parameterType;
    }

    protected ModuleObjectParameterWrapper(Parcel in) {
        Serializable moduleObjectSerializable = in.readSerializable();
        this.object = moduleObjectSerializable;
    }

    public static final Parcelable.Creator<ModuleObjectParameterWrapper> CREATOR = new Parcelable.Creator<ModuleObjectParameterWrapper>() {
        @Override
        public ModuleObjectParameterWrapper createFromParcel(Parcel in) {
            return new ModuleObjectParameterWrapper(in);
        }

        @Override
        public ModuleObjectParameterWrapper[] newArray(int size) {
            return new ModuleObjectParameterWrapper[size];
        }
    };

    public Object getObject() {
        return object;
    }

    public Class getParameterType() {
        return parameterType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try{
            dest.writeSerializable(object);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String o = (object!=null)?object.toString():"null";
        return "FermatModuleObjectWrapper{" +
                "object=" +o+
                '}';
    }
}
