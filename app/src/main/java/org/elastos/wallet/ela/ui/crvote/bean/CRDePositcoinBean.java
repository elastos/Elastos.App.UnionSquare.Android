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

package org.elastos.wallet.ela.ui.crvote.bean;

import org.elastos.wallet.ela.rxjavahelp.BaseEntity;

public class CRDePositcoinBean extends BaseEntity {

    /**
     * code : 0
     * message : 查询成功^_^
     * data : {"error":null,"id":null,"jsonrpc":"2.0","result":{"available":"0","deducted":"0"}}
     * exceptionMsg : null
     */


    private DataBean data;
    private Object exceptionMsg;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(Object exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public static class DataBean {
        /**
         * error : null
         * id : null
         * jsonrpc : 2.0
         * result : {"available":"0","deducted":"0"}
         */

        private Object error;
        private Object id;
        private String jsonrpc;
        private ResultBean result;

        public Object getError() {
            return error;
        }

        public void setError(Object error) {
            this.error = error;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getJsonrpc() {
            return jsonrpc;
        }

        public void setJsonrpc(String jsonrpc) {
            this.jsonrpc = jsonrpc;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * available : 0
             * deducted : 0
             */

            private String available;
            private String deducted;

            public String getAvailable() {
                return available;
            }

            public void setAvailable(String available) {
                this.available = available;
            }

            public String getDeducted() {
                return deducted;
            }

            public void setDeducted(String deducted) {
                this.deducted = deducted;
            }
        }
    }
}
