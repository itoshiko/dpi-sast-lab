package com.sast.material.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sast.material.pojo.SysLoan;
import com.sast.material.pojo.SysReturn;
import com.sast.material.service.RentalService;
import com.sast.user.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class RentalController {

    @Resource
    RentalService rentalService;
    @Resource
    SysUserService userService;
    @Resource
    ObjectMapper mapper;

    @GetMapping("/materials/loan")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String loanMaterial(@RequestBody HashMap<String, String> map) throws JsonProcessingException {
        try {
            int uid = Integer.parseInt(map.get("borrowerId"));
            String username = userService.selectById(uid).getUsername();
            return mapper.writeValueAsString(rentalService.loanMaterial(map, username));
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> returnInfo = new HashMap<>();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return mapper.writeValueAsString(returnInfo);
        }
    }

    @GetMapping("/materials/return")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String returnMaterial(@RequestBody HashMap<String, String> map) throws JsonProcessingException {
        try {
            int loanId = Integer.parseInt(map.get("loadId"));
            SysLoan loan = rentalService.selectLoanById(loanId);
            int uid = loan.getBorrowerId();
            String username = userService.selectById(uid).getUsername();
            return mapper.writeValueAsString(rentalService.returnMaterial(map, username));
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> returnInfo = new HashMap<>();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return mapper.writeValueAsString(returnInfo);
        }
    }


    @GetMapping("/materials/list-not-returned")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String listNotReturned(@RequestBody HashMap<String, String> map){
        ArrayList<SysLoan> result;
        if(map.get("uid") != null && map.get("uid").length() != 0){
            try {
                result = rentalService.selectNotReturnedByUserId(Integer.parseInt(map.get("uid")));
                return mapper.writeValueAsString(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
        else if(map.get("mid") != null && map.get("mid").length() != 0){
            try {
                result = rentalService.selectNotReturnedByMaterialId(Integer.parseInt(map.get("mid")));
                return mapper.writeValueAsString(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
        else{
            try {
                result = rentalService.selectAllNotReturned();
                return mapper.writeValueAsString(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
    }


    @GetMapping("/materials/list-returned")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String listReturned(@RequestBody HashMap<String, String> map){
        ArrayList<SysReturn> result;
        if(map.get("uid") != null && map.get("uid").length() != 0){
            try {
                result = rentalService.selectReturnedByUserId(Integer.parseInt(map.get("uid")));
                return mapper.writeValueAsString(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
        else if(map.get("mid") != null && map.get("mid").length() != 0){
            try {
                result = rentalService.selectReturnedByMaterialId(Integer.parseInt(map.get("mid")));
                return mapper.writeValueAsString(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
        else{
            try {
                result = rentalService.selectAllReturned();
                return mapper.writeValueAsString(result);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }
    }


    @GetMapping("/materials/review-loan")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String reviewLoan(@RequestBody HashMap<String, String> map) throws JsonProcessingException {
        try{
            return mapper.writeValueAsString(rentalService.loanReview(map));
        } catch (Exception e){
            e.printStackTrace();
            HashMap<String, String> returnInfo = new HashMap<>();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return mapper.writeValueAsString(returnInfo);
        }
    }


    @GetMapping("/materials/review-return")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String reviewReturn(@RequestBody HashMap<String, String> map) throws JsonProcessingException {
        try{
            return mapper.writeValueAsString(rentalService.returnReview(map));
        } catch (Exception e){
            e.printStackTrace();
            HashMap<String, String> returnInfo = new HashMap<>();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return mapper.writeValueAsString(returnInfo);
        }
    }


    @GetMapping("/materials/list-loan-pending")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String listLoanPending(){
        try {
            return mapper.writeValueAsString(rentalService.allPendingLoan());
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    @GetMapping("/materials/list-return-pending")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String listReturnPending(){
        try {
            return mapper.writeValueAsString(rentalService.allPendingReturn());
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    @GetMapping("/materials/loan-pending-detail")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String getLoanPending(@RequestParam("lid") int lid){
        try {
            SysLoan loan = rentalService.selectLoanById(lid);
            String remark = rentalService.getLoanRemark(lid);
            ObjectNode root = mapper.valueToTree(loan);
            root.put("remark", remark);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @GetMapping("/materials/return-pending-detail")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String getReturnPending(@RequestParam("rid") int rid){
        try {
            SysReturn returnInfo = rentalService.selectReturnById(rid);
            String remark = rentalService.getReturnRemark(rid);
            ObjectNode root = mapper.valueToTree(returnInfo);
            root.put("remark", remark);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
