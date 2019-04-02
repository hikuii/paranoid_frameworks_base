/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.telephony;

import android.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Defines available network information which includes corresponding subscription id,
 * network plmns and corresponding priority to be used for network selection by Alternative Network
 * Service.
 */
public final class AvailableNetworkInfo implements Parcelable {

    /*
     * Defines number of priority level high.
     */
    public static final int PRIORITY_HIGH = 1;

    /*
     * Defines number of priority level medium.
     */
    public static final int PRIORITY_MED = 2;

    /*
     * Defines number of priority level low.
     */
    public static final int PRIORITY_LOW = 3;

    /**
     * subscription Id of the available network. This value must be one of the entry retrieved from
     * {@link SubscriptionManager#getOpportunisticSubscriptions}
     */
    private int mSubId;

    /**
     * Priority for the subscription id.
     * Priorities are in the range of 1 to 3 where 1
     * has the highest priority.
     */
    private int mPriority;

    /**
     * Describes the List of PLMN ids (MCC-MNC) associated with mSubId.
     * If this entry is left empty, then the platform software will not scan the network
     * to revalidate the input else platform will scan and verify specified PLMNs are available.
     */
    private ArrayList<String> mMccMncs;

    /**
     * Returns the frequency bands associated with the {@link #getMccMncs() MCC/MNCs}.
     * Opportunistic network service will use these bands to scan.
     *
     * When no specific bands are specified (empty array or null) CBRS band (B48) will be
     * used for network scan.
     *
     * See {@link AccessNetworkConstants} for details.
     */
    private ArrayList<Integer> mBands;

    /**
     * Return subscription Id of the available network.
     * This value must be one of the entry retrieved from
     * {@link SubscriptionManager#getOpportunisticSubscriptions}
     * @return subscription id
     */
    public int getSubId() {
        return mSubId;
    }

    /**
     * Return priority for the subscription id. Valid value will be within
     * [{@link AvailableNetworkInfo#PRIORITY_HIGH}, {@link AvailableNetworkInfo#PRIORITY_LOW}]
     * @return priority level
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * Return List of PLMN ids (MCC-MNC) associated with the sub ID.
     * If this entry is left empty, then the platform software will not scan the network
     * to revalidate the input.
     * @return list of PLMN ids
     */
    public @NonNull List<String> getMccMncs() {
        return (List<String>) mMccMncs.clone();
    }

    /**
     * Returns the frequency bands that need to be scanned by opportunistic network service
     *
     * The returned value is defined in either of {@link AccessNetworkConstants.GeranBand},
     * {@link AccessNetworkConstants.UtranBand} and {@link AccessNetworkConstants.EutranBand}
     */
    public @NonNull List<Integer> getBands() {
        return (List<Integer>) mBands.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSubId);
        dest.writeInt(mPriority);
        dest.writeStringList(mMccMncs);
        dest.writeList(mBands);
    }

    private AvailableNetworkInfo(Parcel in) {
        mSubId = in.readInt();
        mPriority = in.readInt();
        mMccMncs = new ArrayList<>();
        in.readStringList(mMccMncs);
        mBands = new ArrayList<>();
        in.readList(mBands, Integer.class.getClassLoader());
    }

    public AvailableNetworkInfo(int subId, int priority, @NonNull List<String> mccMncs,
            @NonNull List<Integer> bands) {
        mSubId = subId;
        mPriority = priority;
        mMccMncs = new ArrayList<String>(mccMncs);
        mBands = new ArrayList<Integer>(bands);
    }

    @Override
    public boolean equals(Object o) {
        AvailableNetworkInfo ani;

        try {
            ani = (AvailableNetworkInfo) o;
        } catch (ClassCastException ex) {
            return false;
        }

        if (o == null) {
            return false;
        }

        return (mSubId == ani.mSubId
            && mPriority == ani.mPriority
            && (((mMccMncs != null)
            && mMccMncs.equals(ani.mMccMncs)))
            && mBands.equals(ani.mBands));
    }

    @Override
    public int hashCode() {
        return Objects.hash(mSubId, mPriority, mMccMncs, mBands);
    }

    public static final @android.annotation.NonNull Parcelable.Creator<AvailableNetworkInfo> CREATOR =
            new Creator<AvailableNetworkInfo>() {
                @Override
                public AvailableNetworkInfo createFromParcel(Parcel in) {
                    return new AvailableNetworkInfo(in);
                }

                @Override
                public AvailableNetworkInfo[] newArray(int size) {
                    return new AvailableNetworkInfo[size];
                }
            };

    @Override
    public String toString() {
        return ("AvailableNetworkInfo:"
            + " mSubId: " + mSubId
            + " mPriority: " + mPriority
            + " mMccMncs: " + Arrays.toString(mMccMncs.toArray())
            + " mBands: " + Arrays.toString(mBands.toArray()));
    }
}
