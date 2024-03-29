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

package org.elastos.wallet.ela.ui.proposal.presenter.bean;


public class ProposalChangeOwnerDigestPayLoad {


    /**
     * CategoryData : testdata
     * OwnerPublicKey : ...
     * DraftHash : ...
     * TargetProposalHash : ...
     * NewRecipient : ...
     * NewOwnerPublicKey : ...
     * Signature : ...
     * NewOwnerSignature : ...
     * CRCouncilMemberDID : ...
     * CRCouncilMemberSignature : ...
     */

    private String CategoryData;
    private String OwnerPublicKey;
    private String DraftHash;
    private String TargetProposalHash;
    private String NewRecipient;
    private String NewOwnerPublicKey;
    private String Signature;
    private String NewOwnerSignature;
    private String CRCouncilMemberDID;
    private String CRCouncilMemberSignature;

    public String getCategoryData() {
        return CategoryData;
    }

    public void setCategoryData(String CategoryData) {
        this.CategoryData = CategoryData;
    }

    public String getOwnerPublicKey() {
        return OwnerPublicKey;
    }

    public void setOwnerPublicKey(String OwnerPublicKey) {
        this.OwnerPublicKey = OwnerPublicKey;
    }

    public String getDraftHash() {
        return DraftHash;
    }

    public void setDraftHash(String DraftHash) {
        this.DraftHash = DraftHash;
    }

    public String getTargetProposalHash() {
        return TargetProposalHash;
    }

    public void setTargetProposalHash(String TargetProposalHash) {
        this.TargetProposalHash = TargetProposalHash;
    }

    public String getNewRecipient() {
        return NewRecipient;
    }

    public void setNewRecipient(String NewRecipient) {
        this.NewRecipient = NewRecipient;
    }

    public String getNewOwnerPublicKey() {
        return NewOwnerPublicKey;
    }

    public void setNewOwnerPublicKey(String NewOwnerPublicKey) {
        this.NewOwnerPublicKey = NewOwnerPublicKey;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String Signature) {
        this.Signature = Signature;
    }

    public String getNewOwnerSignature() {
        return NewOwnerSignature;
    }

    public void setNewOwnerSignature(String NewOwnerSignature) {
        this.NewOwnerSignature = NewOwnerSignature;
    }

    public String getCRCouncilMemberDID() {
        return CRCouncilMemberDID;
    }

    public void setCRCouncilMemberDID(String CRCouncilMemberDID) {
        this.CRCouncilMemberDID = CRCouncilMemberDID;
    }

    public String getCRCouncilMemberSignature() {
        return CRCouncilMemberSignature;
    }

    public void setCRCouncilMemberSignature(String CRCouncilMemberSignature) {
        this.CRCouncilMemberSignature = CRCouncilMemberSignature;
    }
}
