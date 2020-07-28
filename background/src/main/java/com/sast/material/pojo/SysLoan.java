package com.sast.material.pojo;

import java.io.Serializable;
import java.util.Date;

public class SysLoan implements Serializable {

    static final long serialVersionUID = 1L;

    private int loanId;
    private int materialId;
    private int count;
    private int borrowerId;
    private Date loanDate;
    private int reviewerId;
    private Date expectedReturnDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    @Override
    public String toString() {
        return "SysLoan{" +
                "loanId=" + loanId +
                ", materialId=" + materialId +
                ", count=" + count +
                ", borrowerId=" + borrowerId +
                ", loanDate=" + loanDate +
                ", reviewerId=" + reviewerId +
                ", expectedReturnDate=" + expectedReturnDate +
                '}';
    }
}
