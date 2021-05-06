/*
 * Copyright (c) 2019 Elastos Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.elastos.wallet.ela.ui.committee.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.elastos.wallet.ela.rxjavahelp.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class CtDetailBean extends BaseEntity {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        private String id;//第几jie
        private String type;
        private String cid;
        private String status;
        private String did;
        private String didName;
        private String avatar;
        private String address;
        private String introduction;
        private int location;
        private String dpospublickey;
        private long birthday;
        private String email;
        private String wechat;
        private String weibo;
        private String facebook;
        private String microsoft;
        private long startDate;
        private long endDate;
        private double impeachmentThroughVotes;
        private double impeachmentVotes;
        private double impeachmentRatio;
        private String depositAmount;
        private List<Term> term;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", cid='" + cid + '\'' +
                    ", status='" + status + '\'' +
                    ", did='" + did + '\'' +
                    ", didName='" + didName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", address='" + address + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", location=" + location +
                    ", dpospublickey='" + dpospublickey + '\'' +
                    ", birthday=" + birthday +
                    ", email='" + email + '\'' +
                    ", wechat='" + wechat + '\'' +
                    ", weibo='" + weibo + '\'' +
                    ", facebook='" + facebook + '\'' +
                    ", microsoft='" + microsoft + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", impeachmentThroughVotes=" + impeachmentThroughVotes +
                    ", impeachmentVotes=" + impeachmentVotes +
                    ", impeachmentRatio=" + impeachmentRatio +
                    ", depositAmount='" + depositAmount + '\'' +
                    ", term=" + term +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getWeibo() {
            return weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getMicrosoft() {
            return microsoft;
        }

        public void setMicrosoft(String microsoft) {
            this.microsoft = microsoft;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public String getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(String depositAmount) {
            this.depositAmount = depositAmount;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getDidName() {
            return didName;
        }

        public void setDidName(String didName) {
            this.didName = didName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getDpospublickey() {
            return dpospublickey;
        }

        public void setDpospublickey(String dpospublickey) {
            this.dpospublickey = dpospublickey;
        }

        public double getImpeachmentThroughVotes() {
            return impeachmentThroughVotes;
        }

        public void setImpeachmentThroughVotes(double impeachmentThroughVotes) {
            this.impeachmentThroughVotes = impeachmentThroughVotes;
        }

        public double getImpeachmentVotes() {
            return impeachmentVotes;
        }

        public void setImpeachmentVotes(double impeachmentVotes) {
            this.impeachmentVotes = impeachmentVotes;
        }

        public double getImpeachmentRatio() {
            return impeachmentRatio;
        }

        public void setImpeachmentRatio(long impeachmentRatio) {
            this.impeachmentRatio = impeachmentRatio;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public List<Term> getTerm() {
            return term;
        }

        public void setTerm(List<Term> term) {
            this.term = term;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.type);
            dest.writeString(this.cid);
            dest.writeString(this.status);
            dest.writeString(this.did);
            dest.writeString(this.didName);
            dest.writeString(this.avatar);
            dest.writeString(this.address);
            dest.writeString(this.introduction);
            dest.writeInt(this.location);
            dest.writeString(this.dpospublickey);
            dest.writeLong(this.birthday);
            dest.writeString(this.email);
            dest.writeString(this.wechat);
            dest.writeString(this.weibo);
            dest.writeString(this.facebook);
            dest.writeString(this.microsoft);
            dest.writeLong(this.startDate);
            dest.writeLong(this.endDate);
            dest.writeDouble(this.impeachmentThroughVotes);
            dest.writeDouble(this.impeachmentVotes);
            dest.writeDouble(this.impeachmentRatio);
            dest.writeString(this.depositAmount);
            dest.writeList(this.term);
        }

        public void readFromParcel(Parcel source) {
            this.id = source.readString();
            this.type = source.readString();
            this.cid = source.readString();
            this.status = source.readString();
            this.did = source.readString();
            this.didName = source.readString();
            this.avatar = source.readString();
            this.address = source.readString();
            this.introduction = source.readString();
            this.location = source.readInt();
            this.dpospublickey = source.readString();
            this.birthday = source.readLong();
            this.email = source.readString();
            this.wechat = source.readString();
            this.weibo = source.readString();
            this.facebook = source.readString();
            this.microsoft = source.readString();
            this.startDate = source.readLong();
            this.endDate = source.readLong();
            this.impeachmentThroughVotes = source.readDouble();
            this.impeachmentVotes = source.readDouble();
            this.impeachmentRatio = source.readDouble();
            this.depositAmount = source.readString();
            this.term = new ArrayList<Term>();
            source.readList(this.term, Term.class.getClassLoader());
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.type = in.readString();
            this.cid = in.readString();
            this.status = in.readString();
            this.did = in.readString();
            this.didName = in.readString();
            this.avatar = in.readString();
            this.address = in.readString();
            this.introduction = in.readString();
            this.location = in.readInt();
            this.dpospublickey = in.readString();
            this.birthday = in.readLong();
            this.email = in.readString();
            this.wechat = in.readString();
            this.weibo = in.readString();
            this.facebook = in.readString();
            this.microsoft = in.readString();
            this.startDate = in.readLong();
            this.endDate = in.readLong();
            this.impeachmentThroughVotes = in.readDouble();
            this.impeachmentVotes = in.readDouble();
            this.impeachmentRatio = in.readDouble();
            this.depositAmount = in.readString();
            this.term = new ArrayList<Term>();
            in.readList(this.term, Term.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    public static class Term {
        private long id;
        private String title;
        private String didName;
        private String status;
        private String voteResult;
        private long createdAt;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDidName() {
            return didName;
        }

        public void setDidName(String didName) {
            this.didName = didName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVoteResult() {
            return voteResult;
        }

        public void setVoteResult(String voteResult) {
            this.voteResult = voteResult;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
    }
}
