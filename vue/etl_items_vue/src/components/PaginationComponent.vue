<template>
  <div class="pagination" v-if="totalPages > 1">
    <button
        @click="$emit('change-page', currentPage - 1)"
        :disabled="currentPage === 1"
    >
      이전
    </button>
    <button
        v-for="page in visiblePages"
        :key="page"
        @click="$emit('change-page', page)"
        :class="{ active: currentPage === page }"
    >
      {{ page }}
    </button>
    <button
        @click="$emit('change-page', currentPage + 1)"
        :disabled="currentPage === totalPages"
    >
      다음
    </button>
  </div>
</template>

<script setup>
import { defineProps, computed } from "vue";

// Props 정의
const props = defineProps({
  currentPage: {
    type: Number,
    required: true,
  },
  totalItems: {
    type: Number,
    required: true,
  },
  itemsPerPage: {
    type: Number,
    required: true,
  },
  maxVisiblePages: {
    type: Number,
    default: 10,
  },
});

// 총 페이지 수 계산
const totalPages = computed(() => Math.ceil(props.totalItems / props.itemsPerPage));

// 표시할 페이지 번호 계산
const visiblePages = computed(() => {
  const pages = [];
  const half = Math.floor(props.maxVisiblePages / 2);
  const start = Math.max(1, props.currentPage - half);
  const end = Math.min(totalPages.value, start + props.maxVisiblePages - 1);

  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  return pages;
});

</script>


<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin: 20px 0;

  button {
    padding: 5px 10px;
    margin: 0 5px;
    border: 1px solid #ccc;
    background-color: #f9f9f9;
    cursor: pointer;
    border-radius: 4px;

    &.active {
      background-color: #007bff;
      color: white;
      font-weight: bold;
    }

    &:disabled {
      cursor: not-allowed;
      opacity: 0.5;
    }
  }
}
</style>
