package com.portfolio.reservation.exception.errorcode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // user
    ALREADY_EXISTS_USER("USR001", "이미 해당 아이디의 회원이 있습니다."),
    NOT_LOGIN_USER("USR002", "로그인 정보가 없습니다. 다시 로그인 해주세요."),
    NOT_MATCHED_PASSWORD("USR003", "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER("USR004", "회원이 조회되지 않습니다."),

    // store
    ALREADY_EXISTS_STORE("STR001", "이미 해당 이름의 매장이 있습니다."),
    NOT_FOUND_STORE("STR002", "매장이 조회되지 않습니다."),

    // system
    INVALID_ENUM_VALUE("SYS001", "변환할 수 없는 값이 들어왔습니다. : "),

    // schedule
    NOT_FOUND_SCHEDULE("SCD001", "스케줄이 조회되지 않습니다."),

    // operation
    EXCEED_DATEOPERATION_SIZE("OPR001", "기본 운영 방식은 3개까지 등록 가능합니다."),

    OVERLAP_DATEOPERATION("OPR002", "기존 운영 방식과 일자가 중복됩니다."),

    NOT_FOUND_DATEOPERATION("OPR003", "운영 날짜가 존재하지 않습니다."),

    EXCEED_OPERATION_DATE("OPR004", "기본 운영 방식은 시작일과 종료일의 차이가 30일이 넘으면 안됩니다."),

    OVERLAP_DATES_OPERATION("OPR005", "각 운영 방식의 일자가 중복됩니다."),

    OVERLAP_TIME_OPERATION("OPR006", "같은 날짜에 겹치는 예약 시간이 존재합니다."),

    NOT_SEARCH_TIME_TABLE("OPR007", "해당 기간 내 타임테이블이 조회되지 않습니다."),

    NOT_FOUND_DATETABLE("OPR008", "해당 일자에 설정된 예약이 없습니다."),

    NOT_ALLOWED_TIME("OPR009", "시작 시간은 종료 시간보다 이전 시간이어야 합니다."),


    ;

    private final String code;

    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
