package com.cm.marketresearch.remote.response;

import java.io.Serializable;

public class TbgisTrdarRelm implements Serializable {
    private String TRDAR_SE_CD;
    private String TRDAR_CD_NM;

    public TbgisTrdarRelm(String TRDAR_SE_CD, String TRDAR_CD_NM) {
        this.TRDAR_SE_CD = TRDAR_SE_CD;
        this.TRDAR_CD_NM = TRDAR_CD_NM;
    }

    public String getTRDAR_SE_CD() {
        return TRDAR_SE_CD;
    }

    public String getTRDAR_CD_NM() {
        return TRDAR_CD_NM;
    }

    @Override
    public String toString() {
        return "TbgisTrdarRelm{" + // 상권 영역 api 이름
                "TRDAR_CD='" + TRDAR_SE_CD + '\'' + // 상권 코드
                ", TRDAR_CD_NM='" + TRDAR_CD_NM + '\'' +  // 상권_코드_명
                '}';
    }
}
