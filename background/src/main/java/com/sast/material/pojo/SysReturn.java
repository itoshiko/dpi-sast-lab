package com.sast.material.pojo;

import com.sast.material.pojo.enums.MaterialStatus;

import java.util.Date;

public class SysReturn {

    static final long serialVersionUID = 1L;

    private int returnId;
    private int materialId;
    private int count;
    private int borrowerId;
    private Date loanDate;
    private Date expectedReturnDate;
    private Date returnDate;
    private int numberReturned;
    private int numberDamaged;
    private int loanReviewerId;
    private int returnReviewerId;
    private MaterialStatus status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
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

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getNumberReturned() {
        return numberReturned;
    }

    public void setNumberReturned(int numberReturned) {
        this.numberReturned = numberReturned;
    }

    public int getNumberDamaged() {
        return numberDamaged;
    }

    public void setNumberDamaged(int numberDamaged) {
        this.numberDamaged = numberDamaged;
    }

    public int getLoanReviewerId() {
        return loanReviewerId;
    }

    public void setLoanReviewerId(int loanReviewerId) {
        this.loanReviewerId = loanReviewerId;
    }

    public int getReturnReviewerId() {
        return returnReviewerId;
    }

    public void setReturnReviewerId(int returnReviewerId) {
        this.returnReviewerId = returnReviewerId;
    }

    public MaterialStatus getStatus() {
        return status;
    }

    public void setStatus(MaterialStatus status) {
        this.status = status;
    }

    public SysReturn() {
    }

    public SysReturn(SysLoan loan) {
        this.materialId = loan.getMaterialId();
        this.count = loan.getCount();
        this.borrowerId = loan.getBorrowerId();
        this.loanDate = loan.getLoanDate();
        this.loanReviewerId = loan.getReviewerId();
        this.expectedReturnDate = loan.getExpectedReturnDate();
    }

    @Override
    public String toString() {
        return "SysReturn{" +
                "returnId=" + returnId +
                ", materialId=" + materialId +
                ", count=" + count +
                ", borrowerId=" + borrowerId +
                ", loanDate=" + loanDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", returnDate=" + returnDate +
                ", numberReturned=" + numberReturned +
                ", numberDamaged=" + numberDamaged +
                ", loanReviewerId=" + loanReviewerId +
                ", returnReviewerId=" + returnReviewerId +
                ", status=" + status +
                '}';
    }
}
