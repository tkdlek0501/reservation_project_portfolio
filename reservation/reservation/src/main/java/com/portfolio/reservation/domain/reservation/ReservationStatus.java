package com.portfolio.reservation.domain.reservation;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ReservationStatus {

    RESERVE_CONFIRM("예약 확정"),
    CHANGE_REQUEST("예약 변경 요청"),
    CHANGE_CONFIRM("예약 변경 확인"),
    CHANGE_REFUSE("예약 변경 거절"),
//    CANCEL_REQUEST("예약 취소 요청"),
    CANCEL_CONFIRM("예약 취소 확인"),
//    CANCEL_REFUSE("예약 취소 거절"),
    STORE_CANCEL("매장(관리자) 예약 취소"),
    CLIENT_NOSHOW("고객 노쇼"),
    RESERVE_COMPLETE("예약 완료");

    private String value;

    ReservationStatus(String value){
        this.value = value;
    }

    /**
     * 예약 인원을 차지하는 상태들을 반환합니다.
     */
    public static List<ReservationStatus> getOccupiedReservations() {
        return List.of(
                RESERVE_CONFIRM, CHANGE_REQUEST, CHANGE_CONFIRM, CHANGE_REFUSE, RESERVE_COMPLETE);
    }

    /**
     * 변경이 가능한 상태 조회
     */
    public static List<ReservationStatus> getPossibleModify() {
        return List.of(RESERVE_CONFIRM, CHANGE_CONFIRM, CHANGE_REFUSE);
    }
}
