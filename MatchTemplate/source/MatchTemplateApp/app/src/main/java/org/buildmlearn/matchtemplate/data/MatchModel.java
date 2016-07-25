package org.buildmlearn.matchtemplate.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @brief Model class for Match The Following Template Editor data
 * <p>
 * Created by Anupam (opticod) on 24/7/16.
 */

public class MatchModel implements Parcelable {
    public final Parcelable.Creator<MatchModel> CREATOR = new Parcelable.Creator<MatchModel>() {
        @Override
        public MatchModel createFromParcel(Parcel parcel) {
            return new MatchModel(parcel);
        }

        @Override
        public MatchModel[] newArray(int size) {
            return new MatchModel[size];
        }
    };

    private String matchA;
    private String matchB;

    public MatchModel(String A, String B) {
        this.matchA = A;
        this.matchB = B;
    }

    public MatchModel() {

    }

    private MatchModel(Parcel in) {
        this.matchA = in.readString();
        this.matchB = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(matchA);
        dest.writeString(matchB);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMatchA() {
        return matchA;
    }

    public void setMatchA(String matchA) {
        this.matchA = matchA;
    }

    public String getMatchB() {
        return matchB;
    }

    public void setMatchB(String matchB) {
        this.matchB = matchB;
    }

}
