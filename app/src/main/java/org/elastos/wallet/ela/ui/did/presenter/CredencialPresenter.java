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
