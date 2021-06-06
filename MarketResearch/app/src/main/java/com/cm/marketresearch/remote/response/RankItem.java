package com.cm.marketresearch.remote.response;

public class RankItem {
    private String TRDAR_CD_NM;
    private long THSMON_SELNG_AMT;
    private long STOR_CO;

    public RankItem(String TRDAR_CD_NM, long THSMON_SELNG_AMT, long STOR_CO) {
        this.TRDAR_CD_NM = TRDAR_CD_NM;
        this.THSMON_SELNG_AMT = THSMON_SELNG_AMT;
        this.STOR_CO = STOR_CO;
    }

    public String getTRDAR_CD_NM() {
        return TRDAR_CD_NM;
    }

    public long getTHSMON_SELNG_AMT() {
        return THSMON_SELNG_AMT;
    }

    public long getSTOR_CO() {
        return STOR_CO;
    }

    @Override
    public String toString() {
        return "RankItem{" +
                "TRDAR_CD_NM='" + TRDAR_CD_NM + '\'' + // 상권_코드_명
                ", THSMON_SELNG_AMT=" + THSMON_SELNG_AMT + // 당월_매출_금액
                ", STOR_CO=" + STOR_CO + // 점포수
                '}';
    }
}




