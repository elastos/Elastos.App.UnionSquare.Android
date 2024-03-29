/*
 * Copyright (c) 2022 Gelaxy Team
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

package org.elastos.wallet.ela.ui.Assets.presenter;

import org.elastos.wallet.ela.ElaWallet.MyWallet;
import org.elastos.wallet.ela.base.BaseActivity;
import org.elastos.wallet.ela.rxjavahelp.BaseEntity;
import org.elastos.wallet.ela.rxjavahelp.NewPresenterAbstract;
import org.elastos.wallet.ela.rxjavahelp.ObservableListener;
import org.elastos.wallet.ela.ui.Assets.bean.IPEntity;
import org.elastos.wallet.ela.ui.common.bean.CommmonBooleanEntity;
import org.elastos.wallet.ela.utils.PingUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class IPPresenter extends NewPresenterAbstract {


    public void setFixedPeer(String walletId, String chainId, IPEntity entity, BaseActivity baseActivity) {
        Observer observer = createObserver(baseActivity, "setFixedPeer", entity);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                return baseActivity.getMyWallet().setFixedPeer(walletId, chainId, entity.getAddress(), entity.getPort());
            }
        });
        subscriberObservable(observer, observable, baseActivity);
    }

    public void ping(IPEntity entity, BaseActivity baseActivity) {
        Observer observer = createObserver(baseActivity, "ping", entity);
        Observable observable = createObservable(new ObservableListener() {
            @Override
            public BaseEntity subscribe() {
                boolean result = PingUtil.ping(entity.getAddress());
                return new CommmonBooleanEntity(MyWallet.SUCCESSCODE, result);

            }
        });
        subscriberObservable(observer, observable, baseActivity);
    }

}
