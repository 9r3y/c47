package com.y3r9.c47.easy;

import com.y3r9.c47.easy.dao.UserMapper;
import com.y3r9.c47.easy.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by zyq on 2014/6/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-*.xml"})
public class UserDaoTest {

    @Autowired
    private UserMapper dao;

    @BeforeClass
    public static void beforeClass() {

    }

    @Before
    public void before() {
    }

    @Test
    public void test() {
        List<User> list = dao.selectUser();
        System.out.println("feag");
    }
}
