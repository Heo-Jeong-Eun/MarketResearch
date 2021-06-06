package com.cm.marketresearch.remote.response;

public class RankScoreItem {
    private RankItem rankItem;
    private int score;

    public RankScoreItem(RankItem rankItem, int score) {
        this.rankItem = rankItem;
        this.score = score;
    }

    public RankItem getRankItem() {
        return rankItem;
    }

    public int getScore() {
        return score;
    }

    @Override
    // 각 변수에 데이터가 어떤 것이 들어간 건지 String으로 변환
    public String toString() {
        return "RankScoreItem{" +
                "rankItem=" + rankItem +
                ", score=" + score +
                '}';
    }
}
