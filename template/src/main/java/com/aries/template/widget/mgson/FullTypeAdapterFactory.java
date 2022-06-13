package com.aries.template.widget.mgson;

import com.aries.template.widget.mgson.types.IntegerNullAdapter;
import com.aries.template.widget.mgson.types.StringNullAdapter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/******
 *
 *
 * @author  ::: louis luo
 * Date ::: 2022/6/13 5:07 PM
 *
 */
public class FullTypeAdapterFactory<T> implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>)type.getRawType();
        // 对类型进行处理
        if (rawType == String.class)
            return (TypeAdapter<T>) new StringNullAdapter();
        if (rawType == Integer.class || rawType == int.class)
            return (TypeAdapter<T>) new IntegerNullAdapter();
        return null;
    }
}
