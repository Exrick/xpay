package cn.exrick.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Exrickx
 */
@Entity
@Table(name = "tb_thanks")
public class Thanks implements Serializable{

    /**
     * 唯一标识
     */
    @Id
    @Column
    private String id;
}
