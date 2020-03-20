package org.elastos.wallet.ela.ui.did.presenter;

import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.base.BaseActivity;
import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewPresenterAbstract;
import org.elastos.wallet.ela.rxjavahelp.ObservableListener;
import org.elastos.wallet.ela.ui.common.bean.CommmonBooleanEntity;
import org.elastos.wallet.ela.ui.common.bean.CommmonStringEntity;
import org.elastos.wallet.ela.utils.FileUtile;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class CredencialPresenter extends NewPresenterAbstract {

    public void keepFile(File filePath, String content, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "keepFile");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return new CommmonBooleanEntity(MyWallet.SUCCESSCODE, FileUtile.writeFile(filePath, content));
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

    public void keepFile(File filePath, String content, BaseActivity baseActivity) {
        Observer observer = createObserver(baseActivity, "keepFile");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return new CommmonBooleanEntity(MyWallet.SUCCESSCODE, FileUtile.writeFile(filePath, content));
            }
        });
        subscriberObservable(observer, observable, baseActivity);
    }

    public void readFile(File file, BaseFragment baseFragment) {
        Observer observer = createObserver(baseFragment, "readFile");
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return new CommmonStringEntity(MyWallet.SUCCESSCODE, FileUtile.readFile(file));
            }
        });
        subscriberObservable(observer, observable, baseFragment);
    }

}