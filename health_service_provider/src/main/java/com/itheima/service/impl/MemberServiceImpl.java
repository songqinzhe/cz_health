package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional

public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    //新增会员
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            //明文改为密文
            //加密算法 .... java中的加密算法
            String md5Password = MD5Utils.md5(password);
            member.setPassword(md5Password);
        }
        memberDao.add(member);
    }

    //根据手机号查询会员
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }


    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {//2020.05
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            month = month + ".31";//2020年5月31日14:23:08
            Integer countBeforeDate = memberDao.findMemberCountBeforeDate(month);
            list.add(countBeforeDate);
        }
        return list;
    }
}
