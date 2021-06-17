package org.helsinki.vismapay.gson.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.helsinki.vismapay.model.Product;

import java.lang.reflect.Type;

public class ProductTypeSerializer implements JsonSerializer<Product.Type> {

	public JsonElement serialize(Product.Type src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getValue());
	}
}
