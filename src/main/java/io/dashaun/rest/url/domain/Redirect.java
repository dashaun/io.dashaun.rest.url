package io.dashaun.rest.url.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Redirect implements Serializable {

    @Id
    private String id;

    @Column
    private String longUrl;

    @Column
    private String domain;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    @Column
    private Long accessCount;

}
