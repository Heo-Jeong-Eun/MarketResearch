package com.cm.marketresearch.remote.volley;

import android.content.Context;
import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import com.cm.marketresearch.L;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VolleyService {

    public enum API {
        CODE,
        PEOPLE,
        RANK
    }

    private VolleyResult resultCallback = null;
    private Context mContext = null;

    private Response.Listener<JSONObject> successListener = response -> resultCallback.notifySuccess(null, response);

    private Response.ErrorListener errorListener = error -> resultCallback.notifyError(error);

    public VolleyService(VolleyResult resultCallback, Context context) {
        this.resultCallback = resultCallback;
        mContext = context;
    }

    public void getClientAPI(API api, String code) {
        //상권 코드 얻는 api
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://openapi.seoul.go.kr:8088/";

        Calendar calendar = Calendar.getInstance();

        switch (api) {
            case CODE:
                url = url + "674742516f696969313136515049574a/json/TbgisTrdarRelm/1/1000";
                break;
            case PEOPLE:
                url = url + "674742516f696969313136515049574a/json/VwsmTrdarFlpopQq/1/100/" + (calendar.get(Calendar.YEAR) -1) + "/" + code;
                break;
            case RANK:
                url = url + "674742516f696969313136515049574a/json/VwsmTrdarSelngQq/1/1000/" + (calendar.get(Calendar.YEAR) -1) + "/" + code;
                break;
        }

        L.e("::::getTbgisTrdarRelm URL : " + url);

        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(req);
    }
}
