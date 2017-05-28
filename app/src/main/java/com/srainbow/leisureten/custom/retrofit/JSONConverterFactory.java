package com.srainbow.leisureten.custom.retrofit;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by SRainbow on 2017/5/24.
 */

public class JSONConverterFactory extends Converter.Factory {
    private JSONObject jsonObject;
    public static JSONConverterFactory create() {
        return create(new JSONObject());
    }
    private static JSONConverterFactory create(JSONObject jsonObject) {
        return new JSONConverterFactory(jsonObject);
    }
    private JSONConverterFactory(JSONObject jsonObject) {
        if (jsonObject == null)throw new NullPointerException("json == null");
        this.jsonObject = jsonObject;
    }
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new JSONResponseBodyConverter<>(jsonObject);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new JSONRequestBodyConverter<>(jsonObject);
    }
}
