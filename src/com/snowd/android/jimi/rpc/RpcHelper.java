package com.snowd.android.jimi.rpc;

import android.util.Log;
import com.snowd.android.jimi.common.HttpHelper;
import com.snowd.android.jimi.model.ResponseData;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by xuelong.wenxl on 13-12-20.
 */
public class RpcHelper {

    public static final int SC_ERR_NETWORK = HttpStatus.SC_REQUEST_TIMEOUT;
    public static final int SC_ERR_INTERNAL= HttpStatus.SC_INTERNAL_SERVER_ERROR;
    public static final int SC_SUCCESS= HttpStatus.SC_OK;

    public interface Callback {
        public void onPostResult(RpcResult result);
        public RpcResult onPrepareResult();
    }

    public interface DataProcesser<T> {
        public T process(String input) throws Exception;
    }

    public static RpcResult get(final String url, DataProcesser p){
        RpcResult result = new RpcResult();
        try {
            String resp = HttpHelper.get(url);
            //注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
//			json = json.replaceAll("\\x0a|\\x0d","");
            if (p != null) {
                result.data = p.process(resp);
            } else {
                result.data = resp;
            }
        } catch (IOException e) {
            result.code = SC_ERR_NETWORK;
            e.printStackTrace();
        } catch (Exception e) {
            result.code = SC_ERR_INTERNAL;
            e.printStackTrace();
        }
        result.code = SC_SUCCESS;
        return result;
    }


    public static class RpcResult<E> {
        public int code;
        public E data;
    }


}
