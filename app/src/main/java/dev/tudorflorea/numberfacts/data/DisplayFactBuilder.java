package dev.tudorflorea.numberfacts.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DisplayFactBuilder implements Parcelable {

    private Fact mFact;
    private boolean mHasFact;
    private boolean mHasQueryRandom;
    private boolean mHasQueryNumber;
    private boolean mHasQueryDate;
    private int mNumber;
    private int mDay;
    private int mMonth;

    private DisplayFactBuilder() {

    }

    public static DisplayFactBuilder withFact(Fact fact) {
        DisplayFactBuilder builder =  new DisplayFactBuilder();
        builder.setFact(fact);
        builder.setHasFact(true);
        return builder;
    }

    public static DisplayFactBuilder queryRandom() {
        DisplayFactBuilder builder = new DisplayFactBuilder();
        builder.setHasQueryRandom(true);
        return builder;
    }

    public static DisplayFactBuilder queryNumber(int number) {
        DisplayFactBuilder builder = new DisplayFactBuilder();
        builder.setNumber(number);
        builder.setHasQueryNumber(true);
        return builder;
    }

    public static DisplayFactBuilder queryDate(int day, int month) {
        DisplayFactBuilder builder = new DisplayFactBuilder();
        builder.setDate(day, month);
        builder.setHasQueryDate(true);
        return builder;
    }

    private void setFact(Fact fact) {
        mFact = fact;
    }

    public Fact getFact() {
        return mFact;
    }

    private void setNumber(int number) {
        mNumber = number;
    }

    public int getNumber() {
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

    private void setHasQueryRandom(boolean hasQueryRandom) {
        mHasQueryRandom = hasQueryRandom;
    }

    private void setHasQueryNumber(boolean hasQueryNumber) {
        mHasQueryNumber = hasQueryNumber;
    }

    private void  setHasQueryDate(boolean hasQueryDate) {
        mHasQueryDate = hasQueryDate;
    }

    public boolean hasFact() {
        return mHasFact;
    }

    public boolean hasQueryRandom() {
        return mHasQueryRandom;
    }

    public boolean hasQueryNumber() {
        return mHasQueryNumber;
    }

    public boolean hasQueryDate() {
        return mHasQueryDate;
    }




    protected DisplayFactBuilder(Parcel in) {
        mFact = (Fact) in.readValue(Fact.class.getClassLoader());
        mHasFact = in.readByte() != 0x00;
        mHasQueryRandom = in.readByte() != 0x00;
        mHasQueryNumber = in.readByte() != 0x00;
        mHasQueryDate = in.readByte() != 0x00;
        mNumber = in.readInt();
        mDay = in.readInt();
        mMonth = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mFact);
        dest.writeByte((byte) (mHasFact ? 0x01 : 0x00));
        dest.writeByte((byte) (mHasQueryRandom ? 0x01 : 0x00));
        dest.writeByte((byte) (mHasQueryNumber ? 0x01 : 0x00));
        dest.writeByte((byte) (mHasQueryDate ? 0x01 : 0x00));
        dest.writeInt(mNumber);
        dest.writeInt(mDay);
        dest.writeInt(mMonth);
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
