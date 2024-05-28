package com.portfolio.reservation.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -137616039L;

    public static final QUser user = new QUser("user");

    public final com.portfolio.reservation.domain.common.QBaseEntity _super = new com.portfolio.reservation.domain.common.QBaseEntity(this);

    public final EnumPath<AuthorityType> authority = createEnum("authority", AuthorityType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath refreshToken = createString("refreshToken");

    public final ListPath<com.portfolio.reservation.domain.reservation.Reservation, com.portfolio.reservation.domain.reservation.QReservation> reservations = this.<com.portfolio.reservation.domain.reservation.Reservation, com.portfolio.reservation.domain.reservation.QReservation>createList("reservations", com.portfolio.reservation.domain.reservation.Reservation.class, com.portfolio.reservation.domain.reservation.QReservation.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

