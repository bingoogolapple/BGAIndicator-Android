package cn.bingoogolapple.bgaindicator.library;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bingoogolapple on 14/10/22.
 */
public class TabInfo implements Parcelable {
    public String title = null;

    public TabInfo() {
    }

    public TabInfo(String title) {
        this.title = title;
    }

    public TabInfo(Parcel p) {
        title = p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeString(title);
    }

    public static final Creator<TabInfo> CREATOR = new Creator<TabInfo>() {
        public TabInfo createFromParcel(Parcel p) {
            return new TabInfo(p);
        }

        public TabInfo[] newArray(int size) {
            return new TabInfo[size];
        }
    };
}