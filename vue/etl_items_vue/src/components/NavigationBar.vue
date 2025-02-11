<template>
  <nav>
    <div class="header-title">데이터 ETL</div>
      <hr class="custom-line">

    <h1>{{ title }}</h1>

    <!-- 일부 상세 페이지 등에서 보이는 닫기 버튼: etl-items 페이지로 이동 -->
    <router-link
        v-if="
              currentPath.startsWith('/dng-items-detail')
              || currentPath.startsWith('/norm-items-detail')
              || currentPath === '/item-post'
              || currentPath.startsWith('/item-post-updated')
             "
        to="/etl-items"
        class="btn-close"
    >
      <Icon
          icon="material-symbols:close"
          width="24"
          style="color: #1e1e1e;"
      />
    </router-link>

    <!-- 메인 페이지('/') 우측 영역: '데이터 이관' 버튼 -->
    <div
        class="right-button"
        v-if="currentPath === '/'"
    >
      <button
          @click="etlItems"
          :disabled="loading"
          :class="{ 'loading-button': loading }"
      >
        {{ loading ? '처리 중...' : '데이터 이관' }}
      </button>

    </div>

    <!-- etl-items에서만 보이는 뒤로가기 아이콘: 메인('/')으로 이동 -->
    <router-link
        v-if="currentPath === '/etl-items'"
        class="btn-close"
        to="/"
    >
      <Icon
          icon="ic:baseline-arrow-back"
          width="24"
          height="24"
          style="color: #1e1e1e;"
      />
    </router-link>

    <!-- etl-items 우측 영역: norm-items, dng-items 페이지로 이동하는 아이콘 -->
    <div
        class="right-button"
        v-if="currentPath === '/etl-items'"
    >
      <router-link to="/norm-items">
        <Icon
            icon="ant-design:safety-outlined"
            width="24"
            height="24"
        />
      </router-link>

      <router-link to="/dng-items">
        <Icon
            icon="hugeicons:danger"
            width="24"
            height="24"
        />
      </router-link>
      <router-link to="/item-post" >
        <Icon
            icon="mdi:pencil-outline"
            width="24"
            style="color: #1e1e1e;"
        />
      </router-link>
    </div>

    <!-- norm-items, dng-items에서만 보이는 뒤로가기 아이콘: etl-items로 이동 -->
    <router-link
        v-if="
          currentPath === '/dng-items'
          || currentPath === '/norm-items'
        "
        class="btn-close"
        to="/etl-items"
    >
      <Icon
          icon="ic:baseline-arrow-back"
          width="24"
          height="24"
          style="color: #1e1e1e;"
      />
    </router-link>
  </nav>
</template>


<script setup>
import { Icon } from '@iconify/vue'
import { useRoute,useRouter } from 'vue-router';
import { watch, ref } from 'vue';
import axios from 'axios'

// 라우터 인스턴스 가져오기
const router = useRouter()
const route = useRoute()
let currentPath = route.path;
let title = ref('');
// 로딩 상태를 관리하는 반응형 변수
const loading = ref(false)

watch (route, (newPath) => {
  currentPath = newPath.path;
  console.log(currentPath)

  if(currentPath === '/'){
    title.value = '전체상품'
  }else if (currentPath === '/etl-items'){
    title.value = '이관결과보기'
  }else if (currentPath === '/dng-items'){
    title.value = '위해상품'
  }else if (currentPath.startsWith('/dng-items-detail')){
    title.value = '위해상품상세'
  }else if (currentPath === '/norm-items'){
    title.value = '일반상품'
  }else if (currentPath.startsWith('/norm-items-detail')){
    title.value = '일반상품상세'
  }else if (currentPath === '/item-post'){
    title.value = '상품등록'
  }else if (currentPath.startsWith('/item-post-updated')){
    title.value = '상품수정'
  }
});

// ETL 프로세스를 호출하는 함수
const etlItems = async () => {
  loading.value = true
  try {
    // ETL 프로세스 호출 (POST 요청)
    // await axios.post('http://localhost:8082/api/etlItems')
    await axios.post('http://52.79.139.134:8082/api/etlItems')
    await new Promise(resolve => setTimeout(resolve, 2000))

    // ETL 완료 후 '/etl-items' 페이지로 이동
    await router.push('/etl-items')
  } catch (error) {
    // 오류 처리
    console.error('ETL 프로세스 실패:', error)
    alert('데이터 이관에 실패했습니다.')
  } finally {
    loading.value = false
  }
}


</script>

<style scoped lang="scss">
nav {
  // 레이아웃
  position: relative;
  width: 100%;
  min-height: 50px;  // 최소 높이
  padding: 10px;
  display: flex;
  flex-direction: column; // 세로 배치
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #ccc;

  /* 제목(타이틀) */
  .header-title {
    font-size: 16px;
    font-weight: bold;
    color: var(--text-color-dark);
    margin-bottom: 5px;
  }

  /*  가로줄 */
  .custom-line {
    width: calc(55vw - 40px); /* 🔹 가로 길이를 동적으로 설정 */
    max-width: 800px;
    height: 2px;
    background-color: #ccc;
    border: none;
    margin: 5px auto;
  }

  /* 닫기(뒤로가기) 아이콘 버튼 */
  .btn-close {
    position: absolute;
    left: 15px;
    text-decoration: none;
    color: var(--text-color-dark);
    display: flex;
    align-items: center;
  }

  /* 오른쪽 버튼/아이콘 영역 */
  .right-button {
    position: absolute;
    right: 15px;
    display: flex;
    align-items: center;
    gap: 10px; // 버튼/아이콘 사이 간격

    button {
      /* 필요한 경우 버튼 스타일 */
      background-color: #f5f5f5;
      border: 1px solid #ccc;
      border-radius: 4px;
      padding: 6px 12px;
      font-size: 14px;
      color: #333;
      cursor: pointer;

      &:hover {
        background-color: #e5e5e5;
      }
    }
    /* 🔴 로딩 상태일 때 버튼 색상 변경 */
    .loading-button {
      background-color: red;
      color: white;
      border-color: red;
    }
  }

}

</style>