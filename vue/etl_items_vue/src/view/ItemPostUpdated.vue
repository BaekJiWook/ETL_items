<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const id = route.params.id;
const type = route.params.type;
const loading = ref(false);
const error = ref(null);

const previewImage = ref(null);
const file = ref(null);
const etlItem = ref({
  mstDataId:"",
  prdctNm: "",
  prdctTypeNm: "",
  inspSeNu: 0,
  bzentyNm: "",
  bzentyAddr: "",
  bzentyDaddr: "",
  rptTypeNm: "",
  rptInstNm: "",
  rptPicNm: "",
  imgUrl: "",
});

const onFileChange = (e) => {
  const selectedFile = e.target.files[0];
  if (selectedFile) {
    file.value = selectedFile;
    previewImage.value = URL.createObjectURL(selectedFile);

    // 🔹 imgUrl을 blob: URL로 저장하지 않음 (기존 값 유지)
    etlItem.value.imgUrl = "";  // 새 파일을 업로드하면 기존 URL을 백엔드에서 덮어씀
  }
};

const fetchItem = async () => {
  loading.value = true;
  try {
    // const response = await axios.get(`http://localhost:8082/api/${type}Items/${id}`);
    const response = await axios.get(`http://52.79.139.134:8082/api/${type}Items/${id}`);
    const data = Array.isArray(response.data) ? response.data[0] : response.data;
    if (data) {
      // 🔹 `inspSeNu`가 문자열로 전달되었을 경우 숫자로 변환
      data.inspSeNu = Number(data.inspSeNu);
      etlItem.value = data;
    } else {
      error.value = "데이터를 찾을 수 없습니다.";
    }
  } catch (err) {
    error.value = "데이터를 불러올 수 없습니다.";
  } finally {
    loading.value = false;
  }
};

const updateItem = async () => {
  loading.value = true;
  try {
    const formData = new FormData();

    // 🔹 JSON 데이터를 Blob으로 변환하여 추가
    formData.append("etlItemsVo", new Blob([JSON.stringify(etlItem.value)], { type: "application/json" }));

    // 🔹 파일이 선택된 경우 FormData에 추가
    if (file.value) {
      formData.append("file", file.value);
    }

    // 🔹 서버에 PUT 요청 (multipart/form-data 형식)
    // await axios.put(`http://localhost:8082/api/${type}Items/${id}`, formData, {
    await axios.put(`http://52.79.139.134:8082/api/${type}Items/${id}`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    alert("수정이 완료되었습니다.");
    await router.push("/etl-items");
  } catch (err) {
    alert("수정 중 오류가 발생했습니다.");
    console.error(err);
  } finally {
    loading.value = false;
  }
};



onMounted(fetchItem);
</script>

<template>
  <div v-if="loading">저장 중...</div>
  <div class="form-container">
    <h2>{{ type === 'norm' ? '일반상품' : '위해상품' }} 정보 수정</h2>
    <form @submit.prevent="updateItem">
      <!-- 상품명 -->
      <div class="form-group">
        <label for="prdctNm">상품명</label>
        <input type="text" id="prdctNm" v-model="etlItem.prdctNm" placeholder="상품명을 입력하세요" required />
      </div>

      <!-- 제품 유형명 -->
      <div class="form-group">
        <label for="prdctTypeNm">제품 유형명</label>
        <input type="text" id="prdctTypeNm" v-model="etlItem.prdctTypeNm" placeholder="제품 유형명을 입력하세요" required />
      </div>

      <!-- 일반상품, 위해상품 선택 -->
      <div class="form-group">
        <div class="tab-group">
          <!-- 일반상품 (inspSeNu = 0) -->
          <input type="radio" id="normItem" name="inspSeNu" :value="0" v-model.number="etlItem.inspSeNu" required />
          <label for="normItem">일반상품</label>

          <!-- 위해상품 (inspSeNu = 1) -->
          <input type="radio" id="dngItem" name="inspSeNu" :value="1" v-model.number="etlItem.inspSeNu" required />
          <label for="dngItem">위해상품</label>
        </div>
      </div>

      <!-- 업체명 -->
      <div class="form-group">
        <label for="bzentyNm">업체명</label>
        <input type="text" id="bzentyNm" v-model="etlItem.bzentyNm" placeholder="업체명을 입력하세요" required />
      </div>

      <!-- 업체 주소 -->
      <div class="form-group">
        <label for="bzentyAddr">업체 주소</label>
        <input type="text" id="bzentyAddr" v-model="etlItem.bzentyAddr" placeholder="주소를 입력하세요" required />
        <input type="text" id="bzentyDaddr" v-model="etlItem.bzentyDaddr" placeholder="상세 주소를 입력하세요" />
      </div>

      <!-- 보고 유형명 -->
      <div class="form-group">
        <label for="rptTypeNm">보고 유형명</label>
        <input type="text" id="rptTypeNm" v-model="etlItem.rptTypeNm" placeholder="검사합격/회수명령" required />
      </div>

      <!-- 보고 기관명 -->
      <div class="form-group">
        <label for="rptInstNm">보고 기관명</label>
        <input type="text" id="rptInstNm" v-model="etlItem.rptInstNm" placeholder="보고 기관명을 입력하세요" required />
      </div>

      <!-- 보고 담당자명 -->
      <div class="form-group">
        <label for="rptPicNm">보고 담당자명</label>
        <input type="text" id="rptPicNm" v-model="etlItem.rptPicNm" placeholder="보고 담당자명을 입력하세요" required />
      </div>

      <!-- 사진 업로드 -->
      <div class="form-group">
        <label for="photo">
          <p class="title">사진(선택)</p>
          <figure>
            <Icon icon="mdi-light:camera" width="64" height="64" style="color: #1e1e1e;" />
            <img :src="previewImage" alt="미리보기" width="64" height="64" v-if="previewImage" />
            <img src="/box64.jpg" alt="미리보기" width="64" height="64" v-if="!previewImage" />
          </figure>
        </label>
        <input @change="onFileChange" type="file" id="photo" accept="image/*" />
      </div>

      <!-- 등록 버튼 -->
      <button type="submit">수정하기</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>


</template>


<style scoped lang="scss">
// 이미선언 해놓은거 가져옴
@use "../style/form.scss";

.form-container {
  margin-top: 20px;
  padding-bottom: 50px;

  .tab-group {
    display: flex;
    gap: 15px;
    label {
      flex: 1;
      border: 1px solid var(--main-color-dark);
      border-radius: 8px;
      text-align: center;
      padding: 12px;
    }
  }
  .error {
    color: red;
  }

  /* 라디오 버튼 숨김 */
  input[type="radio"] {
    display: none;
  }

  /* 선택된 라디오 버튼에 스타일 적용 */
  input[type="radio"]:checked + label {
    background-color: #3e2723; /* 선택된 배경색 */
    color: white; /* 선택된 텍스트 색상 */
    border-color: #3e2723;
  }

  /* 호버 효과 */
  .tab-group label:hover {
    background-color: #e0e0e0;
  }

  #pay { margin-top: 8px;}

  // for=photo를 가진 form-group의 후손 input의 보더 스타일 제거
  .form-group:has(label[for=photo]) input {
    border: none;
  }

  //file 아이콘
  label[for=photo] {
    figure {
      display: flex;
      align-items: center;
      img {
        border: 1px solid red;
        margin-left: 30px;
      }
    }
  }
  input[type="file"] {
    display: none;
    // 파일선택이라는 부분도 안보이게(아이콘으로 대체)
  }
}

.btn-submit {
  background: var(--main-color-light);
}

.form-group:has(label[for=photo]) {
  padding-bottom: 25px;
  border-bottom: 5px solid #ccc;
}
</style>
