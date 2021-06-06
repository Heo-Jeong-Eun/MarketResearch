package com.cm.marketresearch.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.cm.marketresearch.L;
import com.cm.marketresearch.R;
import com.cm.marketresearch.base.BaseActivity;
import com.cm.marketresearch.databinding.ActivitySearchBinding;
import com.cm.marketresearch.remote.response.RankItem;
import com.cm.marketresearch.remote.response.RankScoreItem;
import com.cm.marketresearch.remote.response.TbgisTrdarRelm;
import com.cm.marketresearch.remote.volley.VolleyResult;
import com.cm.marketresearch.remote.volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    private List<TbgisTrdarRelm> currentList = new ArrayList<>();
    private TbgisTrdarRelm selectedItem;

    private List<RankItem> rankList = new ArrayList<>();

    private SalesRankAdapter salesRankAdapter;
    private SalesRankAdapter preferenceRankAdapter;

    private ArrayList<Long> sumList = new ArrayList<>();

    @Override
    protected int layoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected void onViewCreated() {
        L.i(":::서치 진입");

        //매출순위,선호로 리사이클러뷰 셋팅
        initRecyclerView();

        //검색버튼클릭시.
        binding.btnSearchArea.setOnClickListener(view -> {
            if (selectedItem == null) return;

            binding.viewContent.setVisibility(View.VISIBLE);
            requestPeopleAmount();
            reqeustRank();
        });

        //디폴트 검색해주세요. 텍스트 클릭시
        binding.spinnerAreaDefault.setOnClickListener(view -> reqeustCode());
    }

    private void initRecyclerView() {
        salesRankAdapter = new SalesRankAdapter(this);
        binding.rvSalesRank.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSalesRank.setHasFixedSize(true);
        binding.rvSalesRank.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.rvSalesRank.setAdapter(salesRankAdapter);

        preferenceRankAdapter = new SalesRankAdapter(this);
        binding.rvPreferenceRank.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPreferenceRank.setHasFixedSize(true);
        binding.rvPreferenceRank.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.rvPreferenceRank.setAdapter(preferenceRankAdapter);
    }

    private void reqeustCode() {
        showProgressDialog("상권정보를 불러오는중입니다..");
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {
                try {
                    currentList.clear();
                    ArrayList<String> cdNm = new ArrayList<>();
                    JSONObject jsonObject = response.getJSONObject("TbgisTrdarRelm");

                    String list_total_count = jsonObject.getString("list_total_count");
                    JSONArray ja = jsonObject.getJSONArray("row");
                    cdNm.add(0, "");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        L.e(":::wtmJson " + item);
                        String TRDAR_SE_CD = item.getString("TRDAR_SE_CD");
                        String TRDAR_CD_NM = item.getString("TRDAR_CD_NM");
                        currentList.add(new TbgisTrdarRelm(TRDAR_SE_CD, TRDAR_CD_NM));
                        cdNm.add(TRDAR_CD_NM);
                    }
                    hideProgressDialog();
                    L.i(":::::::::::::list_total_count " + list_total_count);

                    new Thread(() -> {
                        runOnUiThread(() -> {
                            String[] guArrays = new String[cdNm.size()];

                            guArrays = cdNm.toArray(guArrays);
                            ArrayAdapter spinnerCity = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_spinner_item, guArrays);
                            binding.spinnerArea.setAdapter(spinnerCity);
                            binding.spinnerArea.setOnItemSelectedListener(mSpinnerSelectedListener);
                            binding.spinnerAreaDefault.setVisibility(View.GONE);
                        });
                    }).start();

                } catch (Exception e) {
                    hideProgressDialog();
                    L.e("::::::::::::::::e " + e);
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                L.e("::::::::::::::::e " + error);
                hideProgressDialog();
            }
        }, getApplicationContext());
        volleyService.getClientAPI(VolleyService.API.CODE, null);
    }

    private void requestPeopleAmount() {
        //유동인구 요청.
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("VwsmTrdarFlpopQq");
                    JSONArray ja = jsonObject.getJSONArray("row");

                    ArrayList<Integer> list = new ArrayList<>();
                    L.i(":::::[유동인구] 선택된 상권 코드 " + selectedItem.getTRDAR_SE_CD() + " 길이 ... " + ja.length());
                    long total_flpop = 0;
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        long TOT_FLPOP_CO = Math.round(item.getDouble("TOT_FLPOP_CO"));
                        String TRDAR_SE_CD = item.getString("TRDAR_SE_CD");
                        if (selectedItem.getTRDAR_SE_CD().equalsIgnoreCase(TRDAR_SE_CD)) {
                            total_flpop = total_flpop + TOT_FLPOP_CO;
                        }
                    }

                    binding.tvPeopleAmount.setText("유동인구 : " + total_flpop + " 명");
                    L.e(":::총 더한값 " + total_flpop);


                } catch (Exception e) {
                    hideProgressDialog();
                    L.e("::::::::::::::::e " + e);
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                L.e("::::::::::::::::e " + error);

            }
        }, getApplicationContext());
        volleyService.getClientAPI(VolleyService.API.PEOPLE, selectedItem.getTRDAR_SE_CD());
    }

    private void clear() {

    }

    private int saleRankScore = 9;
    private int preferenceRankScore = 5;

    private void reqeustRank() {
        //선호도 요청.

        showProgressDialog("데이터를 불러오는중입니다..");
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {
                try {
                    //검색마다 랭크를 초기화한다.
                    saleRankScore = 9;
                    preferenceRankScore = 5;

                    JSONObject jsonObject = response.getJSONObject("VwsmTrdarSelngQq");

                    String list_total_count = jsonObject.getString("list_total_count");
                    JSONArray ja = jsonObject.getJSONArray("row");

//                    L.i(":::::선택된 상권 코드 " + selectedItem.getTRDAR_SE_CD() + " 길이 ... " + ja.length());
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        String TRDAR_SE_CD = item.getString("TRDAR_SE_CD"); // 상권 구분 코드
                        String SVC_INDUTY_CD_NM = item.getString("SVC_INDUTY_CD_NM"); // 서비스 업종 코드명
                        String THSMON_SELNG_AMT = item.getString("THSMON_SELNG_AMT"); // 당월 매출 금액
                        String STOR_CO = item.getString("STOR_CO"); // 점포수 
//                        L.i(":::::TRDAR_SE_CD " + TRDAR_SE_CD);
//                        L.i(":::::STOR_CO " + STOR_CO);
                        if (selectedItem.getTRDAR_SE_CD().equalsIgnoreCase(TRDAR_SE_CD)) {
                            rankList.add(new RankItem(SVC_INDUTY_CD_NM, Long.parseLong(THSMON_SELNG_AMT), Long.parseLong(STOR_CO)));
                        }
                    }

                    L.i("::::::::::rankList " + rankList.size());

                    //매출액 순위
                    compositeDisposable.add(Observable.fromIterable(rankList)
                            .sorted((t1, t2) -> t1.getTHSMON_SELNG_AMT() >= t2.getTHSMON_SELNG_AMT() ? -1 : 1)  // 판매량에 따른 내림차순
                            .distinct(RankItem::getTRDAR_CD_NM) // 중복 제거를 위한 distinct
                            .take(5) // 5개 까지만 출력 내림차순이니 위로 5개만 끊어서
                            .map(result -> new RankScoreItem(result, saleRankScore--))
                            .toList() // 리스트형태로 방출
                            .subscribe(result -> {
                                //데이터업데이트
                                L.e("::::result " + result + " 사이즈 " + result.size());
                                salesRankAdapter.updateItems(result);
                            }, error -> {

                            }));

                    L.i("::::::::::rankList " + rankList.size());

                    //선호도 순위 필터링.
                    compositeDisposable.add(Observable.fromIterable(rankList)
                            .sorted((t1, t2) -> t1.getSTOR_CO() >= t2.getSTOR_CO() ? -1 : 1)  // 점포점 수에 따른 내림차순
                            .distinct(RankItem::getTRDAR_CD_NM) // 중복 제거를 위한 distinct
                            .take(5)
                            .map(result -> new RankScoreItem(result, preferenceRankScore--))
                            .toList() // 리스트형태로 방출
                            .subscribe(result -> {
                                L.e("::::result " + result + " 사이즈 " + result.size());
                                preferenceRankAdapter.updateItems(result);
                            }, error -> {

                            }));

                    //창업 업종 추천.
                    try {

                        //각각의 매출액과 선호도 리스트를 불러와 하나의 리스트로 합친다.
                        List<RankScoreItem> rankList = salesRankAdapter.getItemList();
                        List<RankScoreItem> preferenceList = preferenceRankAdapter.getItemList();
                        List<RankScoreItem> totalList = new ArrayList<>();
                        totalList.addAll(rankList);
                        totalList.addAll(preferenceList);
                        L.i(":::::::::::totalList " + totalList.size());

                        //하나의 리스트로 합친후. TOP3로 분류해준다.
                        Observable.just(totalList).map(list -> {
                            //업종이같은경우 숫자를 더해야하기때문에 hashMap을 사용한다
                            HashMap<String, Pair<String, Integer>> recommendMap = new HashMap<String, Pair<String, Integer>>();
                            for (RankScoreItem item : list) {
                                String key = item.getRankItem().getTRDAR_CD_NM();

                                //map에 업종이 있는경우
                                if (recommendMap.containsKey(key)) {
                                    //누적합산
                                    String name = recommendMap.get(key).first;
                                    int score = recommendMap.get(key).second + item.getScore();
                                    recommendMap.put(key, Pair.create(name, score));
                                } else {
                                    //업종이 map에 없는경우
                                    recommendMap.put(key, Pair.create(key, item.getScore()));
                                }
                            }
                            return recommendMap;
                            //top3로 분류한다
                        }).flatMap(map -> Observable.fromIterable(map.values()).sorted((t1, t2) -> t1.second >= t2.second ? -1 : 1).take(3).toList().toObservable()) // 내림차순
                                .subscribe(result -> {
                                    L.e("::::::::::: " + result);
                                    //각 분류된 top3 결과값을 표시해준다.
                                    for (int i = 0; i < result.size(); i++) {
                                        String keyName = result.get(i).first;
                                        if (i == 0) {
                                            binding.tvRank1.setText(keyName);
                                        } else if (i == 1) {
                                            binding.tvRank2.setText(keyName);
                                        } else if (i == 2) {
                                            binding.tvRank3.setText(keyName);
                                        }
                                    }

                                });

                    } catch (Exception e) {

                    }

                    hideProgressDialog();
                    L.i(":::::::::::::list_total_count " + list_total_count);

                } catch (Exception e) {
                    hideProgressDialog();
                    L.e("::::::::::::::::e " + e);
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                L.e("::::::::::::::::e " + error);
                hideProgressDialog();
            }
        }, getApplicationContext());
//        if(selectedItem.getTRDAR_SE_CD().equalsIgnoreCase("R")){
//            volleyService.getClientAPI(VolleyService.API.R_RANK, selectedItem.getTRDAR_SE_CD());
//        }else{
//            volleyService.getClientAPI(VolleyService.API.RANK, selectedItem.getTRDAR_SE_CD());
//        }
        volleyService.getClientAPI(VolleyService.API.RANK, selectedItem.getTRDAR_SE_CD());

    }

    private AdapterView.OnItemSelectedListener mSpinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (currentList.size() < 0) return;
            if (i == 0) return;
            selectedItem = currentList.get(i - 1);
            L.e(":::::클릭한 아이탬 " + selectedItem);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
}
