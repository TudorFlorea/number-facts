package dev.tudorflorea.numberfacts.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayFactBuilder implements Parcelable {

    private Fact mFact;
    private boolean mHasFact;
    private boolean mHasQuery;
    private String mNumber;
    private int mDay;
    private int mMonth;
    private int mQueryType;

    public static final int QUERY_RANDOM_TRIVIA = 1;
    public static final int QUERY_RANDOM_MATH = 2;
    public static final int QUERY_RANDOM_YEAR = 3;
    public static final int QUERY_RANDOM_DATE = 4;
    public static final int QUERY_TRIVIA_NUMBER = 5;
    public static final int QUERY_MATH_NUMBER = 6;
    public static final int QUERY_YEAR_NUMBER = 7;
    public static final int QUERY_DATE_NUMBER = 8;


    private DisplayFactBuilder() {

    }

    public static DisplayFactBuilder withFact(Fact fact) {
        DisplayFactBuilder builder =  new DisplayFactBuilder();
        builder.setQueryType(DisplayFactBuilder.QUERY_RANDOM_TRIVIA);
        builder.setFact(fact);
        builder.setHasFact(true);
        return builder;
    }

    public static DisplayFactBuilder queryRandom(int queryType) {
        DisplayFactBuilder builder = new DisplayFactBuilder();
        builder.setQueryType(queryType);
        builder.setHasQuery(true);
        return builder;
    }

    public static DisplayFactBuilder queryNumber(String number, int queryType) {
        DisplayFactBuilder builder = new DisplayFactBuilder();
        builder.setQueryType(queryType);
        builder.setNumber(number);
        builder.setHasQuery(true);
        return builder;
    }

    public static DisplayFactBuilder queryDate(int day, int month, int queryType) {
        DisplayFactBuilder builder = new DisplayFactBuilder();
        builder.setQueryType(queryType);
        builder.setDate(day, month);
        builder.setHasQuery(true);
        return builder;
    }

    private void setFact(Fact fact) {
        mFact = fact;
    }

    public Fact getFact() {
        return mFact;
    }

    private void setQueryType(int queryType) {
        mQueryType = queryType;
    }

    public int getQueryType() {
        return mQueryType;
    }

    private void setNumber(String number) {
        mNumber = number;
    }

    public String getNumber() {
        return mNumber;
    }

    public int getDay() {
        return mDay;
    }

    public int getMonth() {
        return mMonth;
    }

    private void setDate(int day, int month) {
        mDay = day;
        mMonth = month;
    }

    private void setHasFact(boolean hasFact) {
        mHasFact = hasFact;
    }

    private void setHasQuery(boolean hasQuery) {
        mHasQuery = hasQuery;
    }


    public boolean hasFact() {
        return mHasFact;
    }

    public boolean hasQuery() {
        return mHasQuery;
    }

    protected DisplayFactBuilder(Parcel in) {
        mFact = (Fact) in.readValue(Fact.class.getClassLoader());
        mHasFact = in.readByte() != 0x00;
        mHasQuery = in.readByte() != 0x00;
        mNumber = in.readString();
        mDay = in.readInt();
        mMonth = in.readInt();
        mQueryType = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mFact);
        dest.writeByte((byte) (mHasFact ? 0x01 : 0x00));
        dest.writeByte((byte) (mHasQuery ? 0x01 : 0x00));
        dest.writeString(mNumber);
        dest.writeInt(mDay);
        dest.writeInt(mMonth);
        dest.writeInt(mQueryType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DisplayFactBuilder> CREATOR = new Parcelable.Creator<DisplayFactBuilder>() {
        @Override
        public DisplayFactBuilder createFromParcel(Parcel in) {
            return new DisplayFactBuilder(in);
        }

        @Override
        public DisplayFactBuilder[] newArray(int size) {
            return new DisplayFactBuilder[size];
        }
    };
}