package com.servlet.web.service.impl;

import com.servlet.web.dao.Member;

import java.util.List;

public interface MemberService {
    Member add(Member member);
    Member get(Long id);
    List<Member> getAll();
}
