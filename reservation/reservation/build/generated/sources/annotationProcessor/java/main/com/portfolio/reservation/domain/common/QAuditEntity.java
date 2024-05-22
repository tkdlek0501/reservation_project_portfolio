package com.portfolio.reservation.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuditEntity is a Querydsl query type for AuditEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAuditEntity extends EntityPathBase<AuditEntity> {

    private static final long serialVersionUID = 1196174128L;

    public static final QAuditEntity auditEntity = new QAuditEntity("auditEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAuditEntity(String variable) {
        super(AuditEntity.class, forVariable(variable));
    }

    public QAuditEntity(Path<? extends AuditEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuditEntity(PathMetadata metadata) {
        super(AuditEntity.class, metadata);
    }

}

