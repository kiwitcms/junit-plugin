package org.kiwitcms.java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.kiwitcms.java.model.Build;
import org.kiwitcms.java.model.Priority;
import org.kiwitcms.java.model.Product;
import org.kiwitcms.java.model.Version;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KiwiProductJsonRpcClient extends BaseRpcClient {

    private static final String PRODUCT_FILTER = "Product.filter";
    private static final String CREATE_PRODUCT_METHOD = "Product.create";
    private static final String BUILD_FILTER = "Build.filter";
    private static final String CREATE_BUILD_METHOD = "Build.create";
    private static final String CREATE_VERSION_METHOD = "Version.create";
    private static final String VERSION_FILTER = "Version.filter";
    private static final String PRIORITY_FILTER = "Priority.filter";
    private static final String CATEGORY_FILTER = "Category.filter";

    Integer getProductId(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(PRODUCT_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return null;
        } else {
            try {
                Product[] product = new ObjectMapper().readValue(jsonArray.toJSONString(), Product[].class);
                return product[0].getId();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    Product createNewProduct(String name) {
        Map<String, Object> params = new HashMap<>();

        // TODO: move classification filtering in emitter
        // get the first possible classification
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams("Classification.filter", Arrays.asList((Object) params));
        Object classificationId = ((JSONObject) jsonArray.get(0)).get("id");

        params.put("name", name);
        params.put("classification_id", classificationId);

        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_PRODUCT_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Product.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    Build[] getBuilds(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(BUILD_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return new Build[0];
        } else {
            try {
                Build[] builds = new ObjectMapper().readValue(jsonArray.toJSONString(), Build[].class);
                return builds;
            } catch (IOException e) {
                e.printStackTrace();
                return new Build[0];
            }
        }
    }

    Build createBuild(String name, int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("product", productId);
        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_BUILD_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Build.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    Version[] getVersions(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(VERSION_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return new Version[0];
        } else {
            try {
                Version[] versions = new ObjectMapper().readValue(jsonArray.toJSONString(), Version[].class);
                return versions;
            } catch (IOException e) {
                e.printStackTrace();
                return new Version[0];
            }
        }
    }

    Version createProductVersion(String version, int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("value", version);
        params.put("product", productId);

        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_VERSION_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Version.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    Priority[] getPriority(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(PRIORITY_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return new Priority[0];
        } else {
            try {
                return new ObjectMapper().readValue(jsonArray.toJSONString(), Priority[].class);
            } catch (IOException e) {
                e.printStackTrace();
                return new Priority[0];
            }
        }
    }

    // TODO: Create Category class
    JSONArray getCategory(Map<String, Object> filter) {
        return (JSONArray) executeViaPositionalParams(CATEGORY_FILTER, Arrays.asList((Object) filter));
    }
}
