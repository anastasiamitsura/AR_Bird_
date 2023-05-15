package com.example.arbird;

import com.google.gson.annotations.SerializedName;

public class ResponseDoubleGis {
    public ResultDoubleGis getResult() {
        return result;
    }

    @SerializedName("result")
    public final ResultDoubleGis result;

    public ResponseDoubleGis(ResultDoubleGis result) {
        this.result = result;
    }
}
