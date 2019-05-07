package org.elastos.wallet.ela.ui.Assets.presenter;

import org.elastos.wallet.ela.base.BaseFragment;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.ObservableListener;
import org.elastos.wallet.ela.rxjavahelp.PresenterAbstract;
import org.elastos.wallet.ela.ui.Assets.listener.GetAllSubWalletsListner;
import org.elastos.wallet.ela.ui.Assets.listener.GetBalanceListner;
import org.elastos.wallet.ela.ui.Assets.listener.ISubWalletListener;

import org.elastos.wallet.core.SubWallet;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class CommonGetBalancePresenter extends PresenterAbstract {
    @Deprecated
    public void getBalance(String walletId, String chainID, BaseFragment baseFragment) {
        Observer observer = createObserver(GetBalanceListner.class, baseFragment);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().getBalance(walletId, chainID, SubWallet.BalanceType.Total);
            }
        });
        subscriberObservable(observer, observable);
    }

    public void getBalance(String walletId, String chainID, int BalanceType, BaseFragment baseFragment) {
        Observer observer = createObserver(GetBalanceListner.class, baseFragment);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseFragment.getMyWallet().getBalance(walletId, chainID, SubWallet.BalanceType.Total);
            }
        });
        subscriberObservable(observer, observable);
    }
}
