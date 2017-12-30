package cn.exrick.dao;

import cn.exrick.bean.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Exrickx
 */
public interface PayDao extends JpaRepository<Pay,String> {

    List<Pay> getByStateIs(Integer state);

    List<Pay> getByStateIsNotAndStateIsNot(Integer state1,Integer state2);
}
