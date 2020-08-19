package com.sast.material.service;

import com.sast.material.mapper.RentalMapper;
import com.sast.material.pojo.SysLoan;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysReturn;
import com.sast.material.pojo.enums.MaterialStatus;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.DateUtil;
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

    public HashMap<String, String> loanMaterial(HashMap<String, String> request) {
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
        material.setRemaining(material.getRemaining() - count);
        materialService.updateMaterial(material);
        if (material.isNeedReview()) {
            if (request.get("remark") != null || request.get("remark").length() == 0) {
                addPendingLoan(loan.getLoanId(), request.get("remark"));
            } else {
                addPendingLoan(loan.getLoanId(), "nothing to say");
            }
            returnInfo.put("success", "true");
            returnInfo.put("errInfo", "review pending");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    public HashMap<String, String> returnMaterial(HashMap<String, String> request) {
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
            returnInfo.put("errInfo", "no loans allowed");
            return returnInfo;
        }
        if (request.get("returnDate") == null || request.get("returnDate").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid user id");
            return returnInfo;
        }
        if (request.get("status") == null || request.get("status").length() == 0) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid user id");
            return returnInfo;
        }
        sysReturn.setReturnDate(DateUtil.formatDate(request.get("returnDate")));
        sysReturn.setNumberReturned(Integer.parseInt(request.get("numberReturned")));
        sysReturn.setNumberDamaged(Integer.parseInt(request.get("numberDamaged")));
        sysReturn.setStatus(MaterialStatus.valueOf(request.get("status")));
        rentalMapper.returnMaterial(sysReturn);
        if (request.get("remark") != null || request.get("remark").length() == 0) {
            addPendingReturn(sysReturn.getReturnId(), request.get("remark"));
        } else {
            addPendingReturn(sysReturn.getReturnId(), "nothing to say");
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "review pending");
        return returnInfo;
    }

    public SysLoan selectLoanById(int loadId) {
        return rentalMapper.selectLoanById(loadId);
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

    public void addPendingLoan(int lid, String remark) {
        rentalMapper.addPendingLoan(lid, remark);
    }

    public void addPendingReturn(int rid, String remark) {
        rentalMapper.addPendingReturn(rid, remark);
    }

    public HashMap<String, String> reviewReturn(HashMap<String, String> request) {
        HashMap<String, String> returnInfo = new HashMap<>();
        int returnId = Integer.parseInt(request.get("returnId"));
        SysReturn sysReturn = rentalMapper.selectReturnById(returnId);
        if (sysReturn == null) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid return id");
            return returnInfo;
        }
        if (request.get("status") != null && request.get("status").length() != 0) {
            sysReturn.setStatus(MaterialStatus.valueOf(request.get("status")));
        }
        if (request.get("numberReturned") != null && request.get("numberReturned").length() != 0) {
            sysReturn.setNumberReturned(Integer.parseInt(request.get("numberReturned")));
        }
        if (request.get("numberDamaged") != null && request.get("numberDamaged").length() != 0) {
            sysReturn.setNumberDamaged(Integer.parseInt(request.get("numberDamaged")));
        }
        try{
            sysReturn.setReturnReviewerId(Integer.parseInt(request.get("returnReviewerId")));
            rentalMapper.updateReturn(sysReturn);
            rentalMapper.deletePendingReturn(returnId);
            SysMaterial material = materialService.selectById(sysReturn.getMaterialId());
            material.setRemaining(material.getRemaining() + sysReturn.getNumberReturned());
            materialService.updateMaterial(material);
        } catch(Exception e){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }


}
