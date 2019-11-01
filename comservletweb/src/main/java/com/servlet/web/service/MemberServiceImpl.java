package com.servlet.web.service;

import com.servlet.web.dao.Member;
import com.servlet.web.service.impl.MemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public Member add(Member member){
        Member newMember = new Member();
//        INSERT SQL ...
        newMember.setName("世豪");
        return newMember;
    }

    @Override
    public Member get(Long id){
        Member member = new Member();
//        SELECT * FROM member WHERE id = ${id}
        member.setName("牛肉");
        return member;
    }

    @Override
    public List<Member> getAll(){
        List<Member> list = new ArrayList<>();
//        SELECT * FROM member LIMIT (0, 10)
        Member m = new Member();
        m.setName("红");
        list.add(m);
        return list;
    }
}
