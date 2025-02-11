<script setup>
import axios from 'axios';
import { ref } from 'vue';
import { Icon } from '@iconify/vue';
import router from "@/router/router";

const previewImage = ref(null); // 미리보기 이미지 URL
const loading = ref(false); // 로딩 상태
const responseMessage = ref(""); // 응답 메시지

// 데이터 상태 관리
const etlItem = ref({
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

// 파일 객체 저장
let file = ref(null); // 파일 초기화

// 파일 선택 처리
const onFileChange = (e) => {
  const selectedFile = e.target.files[0];
  if (selectedFile) {
    file.value = selectedFile; // 파일 저장
    previewImage.value = URL.createObjectURL(selectedFile); // 미리보기 URL 생성
    etlItem.value.imgUrl = previewImage.value; // 미리보기 URL을 imgUrl로 설정
  } else {
    console.warn("파일이 선택되지 않았습니다.");
  }
};

// 데이터 삽입 API 호출
const insertData = async () => {
  loading.value = true;
  responseMessage.value = "";

  try {
    const formData = new FormData();

    // 파일 추가
    if (file.value) {
      formData.append("file", file.value);
    }

    // etlItem 데이터를 JSON 문자열로 추가
    formData.append("etlItem", JSON.stringify(etlItem.value));

    // API 요청
    // const response = await axios.post("http://localhost:8082/api/itemPost", formData, {
    const response = await axios.post("http://52.79.139.134:8082/api/itemPost", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    // 성공 응답 처리
    if (response.data.imgUrl) {
      etlItem.value.imgUrl = response.data.imgUrl; // 서버에서 받은 URL 설정
    }
    await router.push("/etl-items");
  } catch (error) {
    console.error("삽입 오류:", error.response?.data || error.message);
    responseMessage.value = error.response?.data?.message || "데이터 삽입 실패!";
  } finally {
    loading.value = false;
  }
};

</script>


<template>
  <div  v-if="loading">저장 중...</div>
  <div class="form-container">
  <form @submit.prevent="insertData" >
    <!-- 1.상품명 -->
    <div class="form-group">
      <label for="prdctNm">상품명</label>
      <input
          type="text"
          id="prdctNm"
          v-model="etlItem.prdctNm"
          placeholder="상품명을 입력하세요"
          required
      />
    </div>

    <!-- 2.제품 유형명 -->
    <div class="form-group">
      <label for="prdctTypeNm">제품 유형명</label>
      <input
          type="text"
          id="prdctTypeNm"
          v-model="etlItem.prdctTypeNm"
          placeholder="제품 유형명을 입력하세요"
          required
      />
    </div>

    <!-- 3.일반상품, 위해상품 선택 -->
    <div class="form-group">
      <div class="tab-group">
        <!-- 일반상품 라디오 버튼 -->
        <input
            type="radio"
            id="normItem"
            name="inspSeNu"
            value="0"
            v-model="etlItem.inspSeNu"
            required
            checked
        />
        <label for="normItem">일반상품</label>

        <!-- 위해상품 라디오 버튼 -->
        <input
            type="radio"
            id="dngItem"
            name="inspSeNu"
            value="1"
            v-model="etlItem.inspSeNu"
            required
        />
        <label for="dngItem">위해상품</label>
      </div>
    </div>

    <!-- 4.업체명 -->
    <div class="form-group">
      <label for="bzentyNm">업체명</label>
      <input
          type="text"
          id="bzentyNm"
          v-model="etlItem.bzentyNm"
          placeholder="업체명을 입력하세요"
          required
      />
    </div>

    <!-- 5.업체 주소 -->
    <div class="form-group">
      <label for="bzentyAddr">업체 주소</label>
      <input
          type="text"
          id="bzentyAddr"
          v-model="etlItem.bzentyAddr"
          placeholder="주소를 입력하세요"
          required
      />
      <input
          type="text"
          id="bzentyDaddr"
          v-model="etlItem.bzentyDaddr"
          placeholder="상세 주소를 입력하세요"
      />
    </div>

    <!-- 6.보고 유형명 -->
    <div class="form-group">
      <label for="rptTypeNm">보고 유형명</label>
      <input
          type="text"
          id="rptTypeNm"
          v-model="etlItem.rptTypeNm"
          placeholder="검사합격/회수명령"
          required
      />
    </div>

    <!-- 7.보고 기관명 -->
    <div class="form-group">
      <label for="rptInstNm">보고 기관명</label>
      <input
          type="text"
          id="rptInstNm"
          v-model="etlItem.rptInstNm"
          placeholder="보고 기관명을 입력하세요"
          required
      />
    </div>

    <!-- 8.보고 담당자명 -->
    <div class="form-group">
      <label for="rptPicNm">보고 담당자명</label>
      <input
          type="text"
          id="rptPicNm"
          v-model="etlItem.rptPicNm"
          placeholder="보고 담당자명을 입력하세요"
          required
      />
    </div>

    <!-- 9.사진(선택 입력) -->
    <div class="form-group">
      <label for="photo">
        <p class="title">사진(선택)</p>
        <figure>
          <Icon icon="mdi-light:camera" width="64" height="64"  style="color: #1e1e1e;" />
          <img :src="previewImage" alt="미리보기" width="64" height="64" v-if="previewImage"/>
          <img src="/box64.jpg" alt="미리보기" width="64" height="64" v-if="!previewImage"/>
        </figure>
      </label>
      <input
          @change="onFileChange"
          type="file"
          id="photo"
          accept="image/*">
      <!--  accept="image/*" : "모든 이미지 파일"을 허용 -->
    </div>

    <!-- 등록 버튼 -->
    <button type="submit" >등록</button>
  </form>
  </div>
  <!-- 응답 메시지 -->
  <p v-if="responseMessage">{{ responseMessage }}</p>
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
