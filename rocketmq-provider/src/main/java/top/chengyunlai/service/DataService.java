package top.chengyunlai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName
 * @Description
 * @Author:chengyunlai
 * @Date
 * @Version 1.0
 **/
@Service
public class DataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // @Transactional(rollbackFor = Exception.class)
    // public void saveData(){
    //     System.out.println("执行数据库的insert操作");
    // }

    @Transactional(rollbackFor = Exception.class)
    public void saveData(String data, String txId) {
        System.out.println("执行数据库insert操作。。。");
        System.out.println(data);
        System.out.println("保存txId......");
        jdbcTemplate.update("insert into tx_id (id) value (?)", txId);
        // 如果有问题
        int i = 1 / 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateData(String txId) {
        System.out.println("执行数据库update操作。。。");
        jdbcTemplate.update("insert into tx_id (id) value (?)", txId);
    }

    public boolean contains(String txId) {
        return jdbcTemplate.queryForObject("select count(1) from tx_id where id = ?", Integer.class, txId) > 0;
    }


}