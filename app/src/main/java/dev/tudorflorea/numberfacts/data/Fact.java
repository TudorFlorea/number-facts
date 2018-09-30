package dev.tudorflorea.numberfacts.data;

import android.os.Parcel;
import android.os.Parcelable;

import dev.tudorflorea.numberfacts.utilities.JsonUtils;
import org.json.JSONObject;


public class Fact implements Parcelable {

    private static final String TEXT_FIELD = "text";

    private static final String NUMBER_FIELD = "number";

    private static final String FOUND_FIELD = "found";

    private static final String TYPE_FIELD = "type";

    private long mDatabaseId;
    private String mText;
    private String mNumber;
    private boolean mFound;
    private String mType;
    private String mTimestamp;

    public Fact() {

    }


    public Fact(String number, String text, boolean found, String type, String timestamp) {

        mNumber = number;
        mText = text;
        mFound = found;
        mType = type;
        mTimestamp = timestamp;

    }

    public Fact(String number, String text, boolean found, String type, String timestamp, long databaseId) {

        mNumber = number;
        mText = text;
        mFound = found;
        mType = type;
        mTimestamp = timestamp;
        mDatabaseId = databaseId;

    }

    public void setFactFromJsonObject(JSONObject jsonObject) {
        if (jsonObject != null) {
            mText = JsonUtils.getStringFromJsonObject(jsonObject, TEXT_FIELD);
            mNumber = JsonUtils.getStringFromJsonObject(jsonObject, NUMBER_FIELD);
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

    public String getNumber() {
        return mNumber;
    }

    public String getType() {
        return mType;
    }

    public void setDatabaseId(long databaseId) {
        mDatabaseId = databaseId;
    }

    public long getDatabaseId() {
        return mDatabaseId;
    }




    protected Fact(Parcel in) {
        mDatabaseId = in.readLong();
        mText = in.readString();
        mNumber = in.readString();
        mFound = in.readByte() != 0x00;
        mType = in.readString();
        mTimestamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mDatabaseId);
        dest.writeString(mText);
        dest.writeString(mNumber);
        dest.writeByte((byte) (mFound ? 0x01 : 0x00));
        dest.writeString(mType);
        dest.writeString(mTimestamp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Fact> CREATOR = new Parcelable.Creator<Fact>() {
        @Override
        public Fact createFromParcel(Parcel in) {
            return new Fact(in);
        }

        @Override
        public Fact[] newArray(int size) {
            return new Fact[size];
        }
    };
}