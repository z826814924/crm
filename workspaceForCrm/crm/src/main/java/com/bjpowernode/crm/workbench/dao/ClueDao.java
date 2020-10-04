package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

public interface ClueDao {


   int delete(String clueId) ;


    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);
}
