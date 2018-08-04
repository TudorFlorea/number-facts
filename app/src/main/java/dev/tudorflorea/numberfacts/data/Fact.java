package dev.tudorflorea.numberfacts.data;

import dev.tudorflorea.numberfacts.utilities.JsonUtils;
import org.json.JSONObject;

/**
 * Created by Tudor on 12/2/2017.
 */

public class Fact {

    private static final String TEXT_FIELD = "text";

    private static final String NUMBER_FIELD = "number";

    private static final String FOUND_FIELD = "found";

    private static final String TYPE_FIELD = "type";

    private String mText;
    private int mNumber;
    private boolean mFound;
    private String mType;
    private String mTimestamp;

    public Fact() {

    }


    public Fact(Integer number, String text, boolean found, String type, String timestamp) {

        mNumber = number != null ? number : 0;
        mText = text;
        mFound = found;
        mType = type;
        mTimestamp = timestamp;

    }

    public void setFactFromJsonObject(JSONObject jsonObject) {
        if (jsonObject != null) {
            mText = JsonUtils.getStringFromJsonObject(jsonObject, TEXT_FIELD);
            mNumber = JsonUtils.getIntFromJsonObject(jsonObject, NUMBER_FIELD);
            mFound = JsonUtils.getBooleanFromJsonObject(jsonObject, FOUND_FIELD);
            mType = JsonUtils.getStringFromJsonObject(jsonObject, TYPE_FIELD);
        }
    }


    public String getText() {
        return mText;
    }

    public boolean isFound() {
        return mFound;
    }

    public int getNumber() {
        return mNumber;
    }

    public String getType() {
        return mType;
    }



}
