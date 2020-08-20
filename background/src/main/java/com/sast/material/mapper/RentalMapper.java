package com.sast.material.mapper;

import com.sast.material.pojo.SysLoan;
import com.sast.material.pojo.SysReturn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface RentalMapper {

    public void loanMaterial(SysLoan loanInfo);

    public void returnMaterial(SysReturn returnInfo);

    public SysLoan selectLoanById(@Param("loanId") int loanId);

    public SysReturn selectReturnById(@Param("returnId") int returnId);

    public ArrayList<SysLoan> selectNotReturnedByMaterialId(@Param("mid") int mid);

    public ArrayList<SysReturn> selectReturnedByMaterialId(@Param("mid") int mid);

    public ArrayList<SysLoan> selectNotReturnedByUserId(@Param("uid") int uid);

    public ArrayList<SysReturn> selectReturnedByUserId(@Param("uid") int uid);

    public ArrayList<SysLoan> selectAllNotReturned();

    public ArrayList<SysReturn> selectAllReturned();

    public void deleteLoanRecord(int lid);

    public void deleteReturnRecord(int rid);

    public void deleteRecordByUserId(@Param("id") int uid);

    public void updateLoan(SysLoan sysLoan);

    public void updateReturn(SysReturn sysReturn);

    public String selectLoanPending(@Param("lid") int id);

    public HashMap<String, Object> selectReturnPending(@Param("rid") int id);

    public ArrayList<SysLoan> allPendingLoan();

    public ArrayList<SysReturn> allPendingReturn();

    public void addPendingLoan(@Param("lid") int lid, @Param("remark") String remark);

    public void addPendingReturn(@Param("lid") int lid, @Param("rid") int rid, @Param("remark") String remark);

    public void deletePendingLoan(@Param("lid") int lid);

    public void deletePendingReturn(@Param("rid") int rid);

}
