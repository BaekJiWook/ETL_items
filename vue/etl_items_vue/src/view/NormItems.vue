<script setup>
import {ref, onMounted, computed} from "vue";
import axios from "axios";
import { format } from "date-fns";
import PaginationComponent from "@/components/PaginationComponent.vue";
import router from "@/router/router";

// 상태 관리
const items = ref([]);
const totalItems = ref(0);
const searchQuery = ref("");
const currentPage = ref(1);
const itemsPerPage = ref(10);
const loading = ref(false);
const error = ref(null);

// 날짜 값이 유효하지 않으면 '등록일 없음'으로 표시하는 함수
const formatDate = (date) => {
  if (!date) return '등록일 없음';
  const parsedDate = new Date(date);
  return isNaN(parsedDate.getTime()) ? '등록일 없음' : format(parsedDate, 'yyyy-MM-dd');
};

// 필터링된 데이터 계산
const filteredItems = computed(() => {
  if (!searchQuery.value.trim()) return items.value;
  return items.value.filter((item) =>
      item.prdctNm.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 현재 페이지 데이터 계산
const paginatedItems = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  return filteredItems.value.slice(start, start + itemsPerPage.value);
});

// 페이지 변경 핸들러
const changePage = (page) => {
  currentPage.value = page;
};

// id값을 받아 상세페이지로 이동
const goToDetail = (id, inspSeNu) => {
  const type = inspSeNu === 0 ? "norm" : "dng"; // 일반상품(0) / 위해상품(1) 구분
  router.push(`/${type}-items-detail/${id}`);
};


// 데이터 로드 함수
const loadData = async () => {
  loading.value = true;
  try {
    // const response = await axios.get("http://localhost:8082/api/normItems", {
    const response = await axios.get("http://52.79.139.134:8082/api/normItems", {
      params: {
        page: currentPage.value || 1,
        size: itemsPerPage.value || 10,
      },
    });

    // 응답 데이터를 items에 저장
    items.value = response.data.items.map(item => ({
      mstDataId : item.mstDataId,
      inspSeNu : item.inspSeNu,
      prdctNm: item.prdctNm,
      rptTypeNm: item.rptTypeNm,
      mnftrYmd: item.mnftrYmd,
      bzentyNm: item.bzentyData?.bzentyNm || "등록되지 않음",
      imgUrl: item.imgUrl
    }));

    // 전체 항목 수 설정
    totalItems.value = response.data.totalItems || 0;

    console.log(`데이터 로드 완료: 총 ${totalItems.value}개의 항목`);
  } catch (err) {
    error.value = err;
    console.error("데이터 로드 오류:", err);
  } finally {
    loading.value = false;
  }
};

// 초기 데이터 로드
onMounted(() => {
  loadData();
});

</script>

<template>
  <div>
    <!-- 검색 입력 필드 -->
    <div class="search-bar">
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" class="search-icon">
        <path fill="currentColor" d="M9.539 15.23q-2.398 0-4.065-1.666Q3.808 11.899 3.808 9.5t1.666-4.065T9.539 3.77t4.064 1.666T15.269 9.5q0 1.042-.369 2.017t-.97 1.668l5.909 5.907q.14.14.15.345q.009.203-.15.363q-.16.16-.354.16t-.354-.16l-5.908-5.908q-.75.639-1.725.989t-1.96.35m0-1q1.99 0 3.361-1.37q1.37-1.37 1.37-3.361T12.9 6.14T9.54 4.77q-1.991 0-3.361 1.37T4.808 9.5t1.37 3.36t3.36 1.37" />
      </svg>
      <input
          v-model="searchQuery"
          @input="currentPage = 1"
          type="text"
          placeholder="상품명을 검색하세요"
          class="search-input"
      />
    </div>

    <!-- 검색된 결과 목록 -->
    <ul class="items-all-list" v-if="paginatedItems.length > 0">
      <li class="items"
          v-for="(item, index) in paginatedItems"
          :key="index"
          @click="goToDetail(item.mstDataId, item.inspSeNu)"
      >
        <div class="header">
          <h3>{{ item.prdctNm }}</h3>
          <address>{{ item.bzentyNm }}</address>
          <span>&middot;</span>
          <time>{{ formatDate(item.mnftrYmd) }}</time>
        </div>
        <div class="bottom-info">
          <p class="type">{{ item.rptTypeNm }}</p>
          <!-- 이미지 -->
<!--          <img v-if="item.imgUrl" :src="`http://localhost:8082${item.imgUrl}`" alt="image" width="64" height="64" >-->
          <img v-if="item.imgUrl" :src="`http://52.79.139.134:8082${item.imgUrl}`" alt="image" width="64" height="64" >
        </div>
      </li>
    </ul>

    <!-- 로딩 상태 및 에러 처리 -->
    <p v-if="loading" class="loading_info">데이터를 불러오는 중입니다...</p>
    <p v-if="error" class="error_info">데이터를 가져오는 중 문제가 발생했습니다.</p>
    <p v-else-if="items.length === 0" class="loading_info">검색 결과가 없습니다.</p>

    <!-- 페이지 네이션 컴포넌트 -->
    <PaginationComponent
        :currentPage="currentPage"
        :totalItems="filteredItems.length"
        :itemsPerPage="itemsPerPage"
        :maxVisiblePages="10"
        @change-page="changePage"
    />
  </div>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;
  li {
    padding: 15px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  }
}

address, time, span {
  font-style: normal;
  display: inline;
  font-size: 12px;
  color: #666;
}

.bottom-info {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-top: 15px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 10px; /* 아이콘과 입력 필드 간격 */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #eeecec; /* 배경색 */
}

.search-icon {
  width: 20px;
  height: 20px;
  color: #888;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 16px;
  background-color: #eeecec; /* 배경색 */
}

</style>
