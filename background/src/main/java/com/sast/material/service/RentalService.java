package com.sast.material.service;

import com.sast.material.mapper.RentalMapper;
import com.sast.material.pojo.SysLoan;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysReturn;
import com.sast.material.pojo.enums.MaterialStatus;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.DateUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class RentalService {

    @Resource
    MaterialService materialService;
    @Resource
    SysUserService userService;
    @Resource
    RentalMapper rentalMapper;

    @PreAuthorize("principal.username.equals(#username)")
    public HashMap<String, String> loanMaterial(HashMap<String, String> request, String username) {
        HashMap<String, String> returnInfo = new HashMap<>();
        int id = Integer.parseInt(request.get("materialId"));
        int count = Integer.parseInt(request.get("count"));
        SysMaterial material = materialService.selectById(id);
        if (material == null) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid material id");
            return returnInfo;
        }
        if (material.getRemaining() < count) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "inventory shortage");
            return returnInfo;
        }
        if (!material.isCanLoan()) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "no loans allowed");
            return returnInfo;
        }
        if (userService.selectById(Integer.parseInt(request.get("borrowerId"))) == null) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid user id");
            return returnInfo;
        }
        SysLoan loan = new SysLoan();
        loan.setMaterialId(id);
        loan.setCount(count);
        loan.setBorrowerId(Integer.parseInt(request.get("borrowerId")));
        loan.setLoanDate(new Date());
        if (request.get("expectedReturnDate") != null && (request.get("expectedReturnDate")).length() > 0) {
            loan.setExpectedReturnDate(DateUtil.formatDate(request.get("expectedReturnDate")));
        }
        rentalMapper.loanMaterial(loan);
        if (material.isNeedReview()) {
            if (request.get("remark") != null || request.get("remark").length() == 0) {
                addPendingLoan(loan.getLoanId(), request.get("remark"));
            } else {
                addPendingLoan(loan.getLoanId(), "nothing to say");
            }
            material.setRemaining(material.getRemaining() - count);
            materialService.updateMaterial(material);
            returnInfo.put("success", "true");
            returnInfo.put("errInfo", "review pending");
            return returnInfo;
        } else {
            material.setRemaining(material.getRemaining() - count);
            materialService.updateMaterial(material);
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    @PreAuthorize("principal.username.equals(#username)")
    public HashMap<String, String> returnMaterial(HashMap<String, String> request, String username) {
        HashMap<String, String> returnInfo = new HashMap<>();
        int loanId = Integer.parseInt(request.get("loadId"));
        SysLoan loan = selectLoanById(loanId);
        if (loan == null) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid loan id");
            return returnInfo;
        }
        SysReturn sysReturn = new SysReturn(loan);
        if (request.get("numberReturned") == null || request.get("numberReturned").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "missing information");
            return returnInfo;
        }
        if (request.get("numberDamaged") == null || request.get("numberDamaged").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "missing information");
            return returnInfo;
        }
        if (request.get("status") == null || request.get("status").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "need status");
            return returnInfo;
        }
        sysReturn.setReturnDate(new Date());
        sysReturn.setNumberReturned(Integer.parseInt(request.get("numberReturned")));
        sysReturn.setNumberDamaged(Integer.parseInt(request.get("numberDamaged")));
        sysReturn.setStatus(MaterialStatus.valueOf(request.get("status")));
        rentalMapper.returnMaterial(sysReturn);
        if (request.get("remark") != null || request.get("remark").length() == 0) {
            addPendingReturn(loanId, sysReturn.getReturnId(), request.get("remark"));
        } else {
            addPendingReturn(loanId, sysReturn.getReturnId(), "nothing to say");
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "review pending");
        return returnInfo;
    }

    public SysLoan selectLoanById(int loadId) {
        return rentalMapper.selectLoanById(loadId);
    }

    public SysReturn selectReturnById(int returnId) {
        return rentalMapper.selectReturnById(returnId);
    }

    public ArrayList<SysLoan> selectNotReturnedByMaterialId(int mid) {
        return rentalMapper.selectNotReturnedByMaterialId(mid);
    }

    public ArrayList<SysReturn> selectReturnedByMaterialId(int mid) {
        return rentalMapper.selectReturnedByMaterialId(mid);
    }

    public ArrayList<SysLoan> selectNotReturnedByUserId(int uid) {
        return rentalMapper.selectNotReturnedByUserId(uid);
    }

    public ArrayList<SysLoan> allPendingLoan() {
        return rentalMapper.allPendingLoan();
    }

    public ArrayList<SysReturn> allPendingReturn() {
        return rentalMapper.allPendingReturn();
    }

    public ArrayList<SysReturn> selectReturnedByUserId(int uid) {
        return rentalMapper.selectReturnedByUserId(uid);
    }

    public ArrayList<SysLoan> selectAllNotReturned() {
        return rentalMapper.selectAllNotReturned();
    }

    public ArrayList<SysReturn> selectAllReturned() {
        return rentalMapper.selectAllReturned();
    }

    public void deleteRecordByUserId(int id) {
        rentalMapper.deleteRecordByUserId(id);
    }

    public String getLoanRemark(int lid) {
        return rentalMapper.selectLoanPending(lid);
    }

    public String getReturnRemark(int lid) {
        return (String)rentalMapper.selectReturnPending(lid).get("remark");
    }

    public void addPendingLoan(int lid, String remark) {
        rentalMapper.addPendingLoan(lid, remark);
    }

    public void addPendingReturn(int lid, int rid, String remark) {
        rentalMapper.addPendingReturn(lid, rid, remark);
    }

    public HashMap<String, String> loanReview(HashMap<String, String> request) {
        HashMap<String, String> returnInfo = new HashMap<>();
        int loanId = Integer.parseInt(request.get("loanId"));
        SysLoan sysLoan = rentalMapper.selectLoanById(loanId);
        if (sysLoan == null || rentalMapper.selectLoanPending(loanId) == null) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid loan id");
            return returnInfo;
        }
        if (request.get("approve") == null || request.get("approve").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "need result");
            return returnInfo;
        }
        try {
            if (request.get("approve").equals("true")) {
                sysLoan.setReviewerId(Integer.parseInt(request.get("reviewerId")));
                rentalMapper.updateLoan(sysLoan);
                rentalMapper.deletePendingLoan(loanId);
            } else if (request.get("approve").equals("false")) {
                SysMaterial material = materialService.selectById(sysLoan.getMaterialId());
                material.setRemaining(material.getRemaining() + sysLoan.getCount());
                materialService.updateMaterial(material);
                rentalMapper.deletePendingLoan(loanId);
                rentalMapper.deleteLoanRecord(loanId);
            } else {
                returnInfo.put("success", "false");
                returnInfo.put("errInfo", "invalid result");
                return returnInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    public HashMap<String, String> returnReview(HashMap<String, String> request) {
        HashMap<String, String> returnInfo = new HashMap<>();
        int returnId = Integer.parseInt(request.get("returnId"));
        SysReturn sysReturn = rentalMapper.selectReturnById(returnId);
        HashMap<String, Object> pendingInfo = rentalMapper.selectReturnPending(returnId);
        if (sysReturn == null || pendingInfo == null) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid return id");
            return returnInfo;
        }
        if (request.get("approve") == null || request.get("approve").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "need result");
            return returnInfo;
        }
        if (request.get("approve").equals("true")) {
            if (request.get("status") != null && request.get("status").length() != 0) {
                sysReturn.setStatus(MaterialStatus.valueOf(request.get("status")));
            }
            if (request.get("numberReturned") != null && request.get("numberReturned").length() != 0) {
                sysReturn.setNumberReturned(Integer.parseInt(request.get("numberReturned")));
            }
            if (request.get("numberDamaged") != null && request.get("numberDamaged").length() != 0) {
                sysReturn.setNumberDamaged(Integer.parseInt(request.get("numberDamaged")));
            }
            try {
                sysReturn.setReturnReviewerId(Integer.parseInt(request.get("returnReviewerId")));
                rentalMapper.updateReturn(sysReturn);
                rentalMapper.deletePendingReturn(returnId);
                rentalMapper.deleteLoanRecord((int) pendingInfo.get("lid"));
                SysMaterial material = materialService.selectById(sysReturn.getMaterialId());
                material.setRemaining(material.getRemaining() + sysReturn.getNumberReturned());
                material.setTotal(material.getTotal() - sysReturn.getNumberDamaged());
                materialService.updateMaterial(material);
            } catch (Exception e) {
                e.printStackTrace();
                returnInfo.put("success", "false");
                returnInfo.put("errInfo", "unexpected error");
                return returnInfo;
            }
        } else if (request.get("approve").equals("false")) {
            try {
                rentalMapper.deletePendingReturn(returnId);
                rentalMapper.deleteReturnRecord(returnId);
            } catch (Exception e) {
                returnInfo.put("success", "false");
                returnInfo.put("errInfo", "unexpected error");
                return returnInfo;
            }
        } else {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid result");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }
}
