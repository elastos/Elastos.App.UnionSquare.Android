package org.elastos.wallet.ela.utils;

import com.blankj.utilcode.util.CacheDiskUtils;

import org.elastos.wallet.ela.MyApplication;
import org.elastos.wallet.ela.db.RealmUtil;
import org.elastos.wallet.ela.db.table.Wallet;
import org.elastos.wallet.ela.ui.Assets.bean.IPEntity;
import org.elastos.wallet.ela.ui.crvote.bean.CRListBean;
import org.elastos.wallet.ela.ui.did.entity.DIDInfoEntity;
import org.elastos.wallet.ela.ui.mine.bean.MessageEntity;
import org.elastos.wallet.ela.ui.vote.bean.Area;
import org.elastos.wallet.ela.ui.vote.bean.VoteListBean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheUtil {
    private static File file = new File(MyApplication.getAppContext().getFilesDir().getParent() + "/keeps");

    public static ArrayList<Area> getArea() {
        return (ArrayList<Area>) CacheDiskUtils.getInstance(file)
                .getSerializable("area");
    }

    public static void setArea(List<Area> list) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            CacheDiskUtils.getInstance(file).remove("area");
            return;
        }
        CacheDiskUtils.getInstance(file).put("area", (Serializable) list, CacheDiskUtils.DAY * 360);
    }

    public static void clear() {
        CacheDiskUtils.getInstance(file).clear();
    }

    public static ArrayList<VoteListBean.DataBean.ResultBean.ProducersBean> getProducerList() {
        Wallet wallet = new RealmUtil().queryDefauleWallet();
        ArrayList<VoteListBean.DataBean.ResultBean.ProducersBean> list = (ArrayList<VoteListBean.DataBean.ResultBean.ProducersBean>) CacheDiskUtils.getInstance(file)
                .getSerializable("list" + wallet.getWalletId());
        return list == null ? new ArrayList<>() : list;
    }

    public static void setProducerList(List<VoteListBean.DataBean.ResultBean.ProducersBean> list) {
        if (list == null) {
            return;
        }
        Wallet wallet = new RealmUtil().queryDefauleWallet();
        if (list.size() == 0) {
            CacheDiskUtils.getInstance(file).remove("list" + wallet.getWalletId());
            return;
        }


        CacheDiskUtils.getInstance(file).put("list" + wallet.getWalletId(), (Serializable) list, CacheDiskUtils.DAY * 360);
    }

    public static ArrayList<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> getCRProducerList() {
        Wallet wallet = new RealmUtil().queryDefauleWallet();
        ArrayList<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> list = (ArrayList<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean>) CacheDiskUtils.getInstance(file)
                .getSerializable("CRlist1" + wallet.getWalletId());
        return list == null ? new ArrayList<>() : list;
    }

    public static void setCRProducerList(List<CRListBean.DataBean.ResultBean.CrcandidatesinfoBean> list) {
        if (list == null) {
            return;
        }
        Wallet wallet = new RealmUtil().queryDefauleWallet();
        if (list.size() == 0) {
            CacheDiskUtils.getInstance(file).remove("CRlist1" + wallet.getWalletId());
            return;
        }

        CacheDiskUtils.getInstance(file).put("CRlist1" + wallet.getWalletId(), (Serializable) list, CacheDiskUtils.DAY * 360);
    }


    public static ArrayList<DIDInfoEntity> getDIDInfoList() {
        ArrayList<DIDInfoEntity> list = (ArrayList<DIDInfoEntity>) CacheDiskUtils.getInstance(file)
                .getSerializable("DIDInfoList");
        return list == null ? new ArrayList<>() : list;
    }

    //Set<String> serverList = new HashSet<>();
    public static void setDIDInfoList(List<DIDInfoEntity> list) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            CacheDiskUtils.getInstance(file).remove("DIDInfoList");
            return;
        }
        CacheDiskUtils.getInstance(file).put("DIDInfoList", (Serializable) list, CacheDiskUtils.DAY * 360);
    }

    public static void remove(String key) {

        CacheDiskUtils.getInstance(file).remove(key);

    }

    public static ArrayList<IPEntity> getIps() {
        ArrayList<IPEntity> list = (ArrayList<IPEntity>) CacheDiskUtils.getInstance(file)
                .getSerializable("ips");
        return list == null ? new ArrayList<>() : list;
    }

    //Set<String> serverList = new HashSet<>();
    public static void setIps(List<IPEntity> list) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            CacheDiskUtils.getInstance(file).remove("ips");
            return;
        }
        CacheDiskUtils.getInstance(file).put("ips", (Serializable) list, CacheDiskUtils.DAY * 360);
    }


    private static void setMessage(List<MessageEntity> list, String key) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            CacheDiskUtils.getInstance(file).remove(key);
            return;
        }
        CacheDiskUtils.getInstance(file).put(key, (Serializable) list, CacheDiskUtils.DAY * 360);
    }

    private static ArrayList<MessageEntity> getMessage(String key) {
        ArrayList<MessageEntity> list = (ArrayList<MessageEntity>) CacheDiskUtils.getInstance(file)
                .getSerializable(key);
        return list == null ? new ArrayList<>() : list;
    }

    public static void setUnReadMessage(List<MessageEntity> list) {
        setMessage(list, "unReadMessage");
    }

    public static ArrayList<MessageEntity> getUnReadMessage() {

        return getMessage("unReadMessage");
    }

    public static void setReadMessage(List<MessageEntity> list) {
        setMessage(list, "readMessage");
    }

    public static ArrayList<MessageEntity> getReadMessage() {

        return getMessage("readMessage");
    }


}
