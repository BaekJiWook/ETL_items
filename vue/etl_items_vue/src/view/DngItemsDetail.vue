<script setup>
import { ref, onMounted } from 'vue';
import { useRoute,useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute(); // 라우터에서 `:id` 가져오기
const router = useRouter();
const id = route.params.id; // URL의 `id`
const type = route.params.type; // URL의 `type` (norm 또는 dng)
const item = ref(null);
const loading = ref(true);
const error = ref(null);

const goToEdit = (id, inspSeNu) => {
  const type = inspSeNu === 0 ? "norm" : "dng"; // 일반상품(0) / 위해상품(1) 구분
  router.push(`/item-post-updated/${type}/${id}`);
};

const fetchData = async () => {
  loading.value = true;
  try {
    // const response = await axios.get(`http://localhost:8082/api/${type}Items/${id}`);
    const response = await axios.get(`http://52.79.139.134:8082/api/${type}Items/${id}`);
    const data = response.data; // 배열 데이터
    if (data.length > 0) {
      item.value = data[0]; // 첫 번째 객체만 저장
    } else {
      error.value = "데이터가 존재하지 않습니다.";
    }
  } catch (err) {
    error.value = "데이터를 불러올 수 없습니다.";
  } finally {
    loading.value = false;
  }
};
// 삭제 기능 추가
const deleteItem = async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  try {
    // await axios.delete(`http://localhost:8082/api/deleteItem/${id}`);
    await axios.delete(`http://52.79.139.134:8082/api/deleteItem/${id}`);
    alert("삭제가 완료되었습니다.");
    await router.push("/etl-items");
  } catch (err) {
    alert("삭제 중 오류가 발생했습니다.");
  }
};

onMounted(fetchData);
</script>

<template>
  <div class="detail-page">
    <div v-if="loading" class="loading">불러오는 중...</div>
    <div v-else-if="error" class="error">{{ error }}</div>

    <div v-else-if="item" class="content">
      <!-- 이미지 -->
      <figure v-if="item.imgUrl" class="product-image">
<!--        <img :src="`http://localhost:8082${item.imgUrl}`" alt="상품 이미지" />-->
        <img :src="`http://52.79.139.134:8082${item.imgUrl}`" alt="상품 이미지" />
      </figure>
      <!-- 내용 -->
      <div class="content" v-if="!loading && !error && item">
        <h2 class="product-name">{{ item.prdctNm }}</h2>
        <p class="sub-title">{{ item.bzentyNm }}</p>

        <ul class="info-list">
          <li>제품 유형명: <span>{{ item.prdctTypeNm }}</span></li>
          <li>제조일자: <span>{{ item.mnftrYmd || '정보 없음' }}</span></li>
          <li>업체주소: <span>{{ item.bzentyAddr }}{{item.bzentyDaddr|| '정보 없음'}}</span></li>
          <li>회수사유명: <span>{{ item.icptInspRsltCn || '정보 없음' }}</span></li>
          <li>보고기관명: <span>{{ item.rptInstNm || '정보 없음' }}</span></li>
          <li>보고 담당자명: <span>{{ item.rptPicNm || '정보 없음' }}</span></li>
          <li>보고 유형명: <span>{{ item.rptTypeNm }}</span></li>
        </ul>

        <div class="bottom-btn-group">
          <button class="btn-edit" @click="goToEdit(item.mstDataId, item.inspSeNu)">수정</button>
          <button class="btn-delete" @click="deleteItem" >삭제</button>
        </div>
      </div>

    </div>
    <!-- 로딩 상태 -->
    <div v-if="loading" class="loading">불러오는 중...</div>
    <!-- 에러 메시지 -->
    <div v-else-if="error" class="error">{{ error }}</div>

  </div>


</template>

<style scoped lang="scss">
figure {
  aspect-ratio: 16 / 9;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}
.detail-page {
  width: 100%;
  height: 100vh; /* 화면 전체 높이 */
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;

  /* 헤더 스타일 */
  .header {
    background-color: #eee;
    display: flex;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #ccc;

    .title {
      font-size: 18px;
      font-weight: bold;
      flex: 1;
      text-align: center;
    }
  }

  /* 본문 스타일 */
  .content {
    flex: 1;
    padding: 20px;
    overflow-y: auto;

    .product-name {
      font-size: 20px;
      font-weight: bold;
      margin-bottom: 10px;
      color: #333;
    }

    .sub-title {
      font-size: 14px;
      color: #888;
      margin-bottom: 20px;
    }

    .info-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        font-size: 16px;
        margin-bottom: 15px;
        color: #555;

        span {
          font-weight: bold;
          color: #333;
        }
      }
    }
  }
  .bottom-btn-group {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    display: flex;

    button, .btn-edit {
      width: 50%;
      border-radius: 0;
      padding-top: 14px;
      padding-bottom: 14px;
      margin: 0;
      cursor: pointer;
      text-align: center;
      color: #fff;
      text-decoration: none;
    }

    .btn-edit {
      background-color: var(--main-color-dark);
    }

    .btn-delete {
      background-color: var(--main-color-light);
    }

  }

  /* 로딩 및 에러 메시지 */
  .loading,
  .error {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    color: #888;
  }
}

</style>