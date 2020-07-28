package com.sast;

import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysTag;
import com.sast.material.service.MaterialService;
import com.sast.user.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class BackgroundApplicationTests {

	@Resource
	SysUserService userService;
	@Resource
	MaterialService materialService;

	@Test
	void contextLoads() {
	}

	@Test
	void transactionTest(){
		userService.updateUsernameById(1, "success");
	}

	@Test
	void materialTest(){
		SysMaterial material = new SysMaterial();
		material.setId(6);
		material.setLoanable(true);
		material.setName("HC-05蓝牙模块");
		material.setClassification("00000XXX");
		material.setNeedReview(false);
		material.setPrice(20);
		material.setWarehousingDate(new Date());
		material.setTotal(100);
		material.setRemaining(70);
		material.setStorageLocation("NULL");
		ArrayList<SysTag> tags = new ArrayList<SysTag>();
		tags.add(new SysTag(3, "Wireless"));
		tags.add(new SysTag(4, "Bluetooth"));
		material.setTags(tags);
		System.out.println(material);
		materialService.updateMaterial(material);
	}
}
